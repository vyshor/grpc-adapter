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

import com.sun.org.apache.xpath.internal.SourceTree;
import de.pascalwhoop.powertac.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.powertac.common.config.ConfigurableValue;
import org.powertac.samplebroker.ContextManagerService;
import org.powertac.samplebroker.interfaces.BrokerContext;
import org.powertac.samplebroker.interfaces.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GrpcServiceChannel implements Initializable

{
  @Autowired
  public GRPCTypeConverter converter;
  ManagedChannel channel;
  @ConfigurableValue(valueType= "String", description="Host DNS/IP to connect to with the GRPC client")
  private String host = "localhost";
  @ConfigurableValue(valueType= "Integer", description="Port to connect to with the GRPC client")
  private Integer port = 50051;
  @ConfigurableValue(valueType= "Integer", description="GRPC conection retry limit")
  private Integer retryLimit = 0;

  public ContextManagerServiceGrpc.ContextManagerServiceBlockingStub     contextStub;
  public MarketManagerServiceGrpc.MarketManagerServiceBlockingStub       marketStub;
  public PortfolioManagerServiceGrpc.PortfolioManagerServiceBlockingStub portfolioStub;
  public  GameServiceGrpc.GameServiceBlockingStub                        gameStub;
  private ConnectionServiceGrpc.ConnectionServiceBlockingStub            connStub;
  static private                                                         Logger log = LogManager.getLogger(ContextManagerService.class);

  @Override
  public void initialize(BrokerContext broker)
  {
    int trial = 1;
    boolean isEnabled = false;
    while (!isEnabled && trial != retryLimit) {
      try{
        channel = ManagedChannelBuilder.forAddress(this.host, this.port).usePlaintext(true).build();
        log.info("Channel opening to Python GRPC Server");
        log.info("#####################################");

        contextStub = ContextManagerServiceGrpc.newBlockingStub(channel);
        marketStub = MarketManagerServiceGrpc.newBlockingStub(channel);
        portfolioStub = PortfolioManagerServiceGrpc.newBlockingStub(channel);
        connStub = ConnectionServiceGrpc.newBlockingStub(channel);
        gameStub = GameServiceGrpc.newBlockingStub(channel);

        connStub.pingpong(Empty.newBuilder().build());
        isEnabled = true;
      }
      catch (Exception e){
        System.out.println("GRPC connection refused " + trial++);
        try {
          Thread.sleep(1000);
        }
        catch (InterruptedException e1) {
          e1.printStackTrace();
        }
      }
    }
  }


}
