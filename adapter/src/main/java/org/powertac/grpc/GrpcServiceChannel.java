/*
 *  Copyright 2009-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an
 *  "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied. See the License for the specific language
 *  governing permissions and limitations under the License.
 */

package org.powertac.grpc;

import de.pascalwhoop.powertac.grpc.*;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.powertac.common.config.ConfigurableValue;
import org.powertac.samplebroker.SubmitService;
import org.powertac.samplebroker.interfaces.BrokerContext;
import org.powertac.samplebroker.interfaces.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrpcServiceChannel implements Initializable

{
    static private Logger log = LogManager.getLogger(GrpcServiceChannel.class);

    @Autowired
    public GRPCTypeConverter converter;
    ManagedChannel channel;
    @ConfigurableValue(valueType = "String", description = "Host DNS/IP to connect to with the GRPC client")
    private String host = "localhost";
    @ConfigurableValue(valueType = "Integer", description = "Port to connect to with the GRPC client")
    private Integer port = 50053;
    @ConfigurableValue(valueType = "Integer", description = "GRPC conection retry limit")
    private Integer retryLimit = 60;

    private final Object lock = new Object();
    private Thread serverThread;

    public SubmitServiceGrpc.SubmitServiceBlockingStub submitStub;
    public ContextManagerServiceGrpc.ContextManagerServiceBlockingStub contextStub;
    public MarketManagerServiceGrpc.MarketManagerServiceBlockingStub marketStub;
    public PortfolioManagerServiceGrpc.PortfolioManagerServiceBlockingStub portfolioStub;
    public GameServiceGrpc.GameServiceBlockingStub gameStub;
    private ConnectionServiceGrpc.ConnectionServiceBlockingStub connStub;
    public ExtraSpyMessageManagerServiceGrpc.ExtraSpyMessageManagerServiceBlockingStub spyStub;

    //receiving components need to be manually triggered at boot
    @Autowired
    SubmitService submitService;

    @Override
    public void initialize(BrokerContext broker) {
        try {
            startServer();
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startServer() {
        serverThread = new Thread(() -> {
            try {
                ManagedChannelBuilder builder = ManagedChannelBuilder.forAddress(this.host, this.port);
                channel = builder
                        .usePlaintext(true)
                        .build();


                log.info("Channel opening to Python GRPC Server");
                log.info("#####################################");

                submitStub = SubmitServiceGrpc.newBlockingStub(channel);
                contextStub = ContextManagerServiceGrpc.newBlockingStub(channel);
                marketStub = MarketManagerServiceGrpc.newBlockingStub(channel);
                portfolioStub = PortfolioManagerServiceGrpc.newBlockingStub(channel);
                connStub = ConnectionServiceGrpc.newBlockingStub(channel);
                gameStub = GameServiceGrpc.newBlockingStub(channel);
                spyStub = ExtraSpyMessageManagerServiceGrpc.newBlockingStub(channel);

                //to connect with the server
                runPing();

                connectReceiving(channel);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    private void runPing() {
        int retries = 0;
        while (retries < retryLimit) {
            try {
                System.out.println("connecting to grpc...");
                connStub.pingpong(Empty.newBuilder().build());
                System.out.println("GRPC CONNECTED!");
                synchronized (lock) {
                    lock.notifyAll();
                }
                break;
            } catch (StatusRuntimeException ex) {
                log.warn("GRPC partner not available");
                retries++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void connectReceiving(ManagedChannel channel) {
        if (channel.getState(true).equals(ConnectivityState.READY)) {
            submitService.connectReceiving();
        }
        channel.notifyWhenStateChanged(ConnectivityState.READY, () -> submitService.connectReceiving());
    }


}
