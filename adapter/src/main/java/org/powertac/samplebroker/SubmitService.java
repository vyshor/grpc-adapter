package org.powertac.samplebroker;

import com.google.protobuf.Message;
import de.pascalwhoop.powertac.grpc.Empty;
import de.pascalwhoop.powertac.grpc.PBOrder;
import de.pascalwhoop.powertac.grpc.PBTariffRevoke;
import de.pascalwhoop.powertac.grpc.PBTariffSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.powertac.common.TariffSpecification;
import org.powertac.common.repo.TariffRepo;
import org.powertac.grpc.GrpcServiceChannel;
import org.powertac.grpc.mappers.AbstractPbPtacMapper;
import org.powertac.grpc.mappers.OrderMapper;
import org.powertac.grpc.mappers.TariffRevokeMapper;
import org.powertac.grpc.mappers.TariffSpecificationMapper;
import org.powertac.samplebroker.core.MessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This service takes the messages passed from the python part of the agent and passes it along to the server. It's multithreaded because the iterators are blocking.
 */
@Service
public class SubmitService {
    static private Logger log = LogManager.getLogger(ContextManagerService.class);
    @Autowired
    GrpcServiceChannel comm;
    @Autowired
    MessageDispatcher dispatcher;
    @Autowired
    TariffRepo tariffRepo;

    List<TariffSpecification> tariffSpecificationList = new ArrayList<>();

    boolean disconnect = false;
    LinkedList<Thread> threads = new LinkedList<>();

    public void connectReceiving() {
        this.ensureAllDisconnected();

        //new orders
        Thread t1 = new Thread(() -> {
            Iterator<PBOrder> iter = comm.submitStub.submitOrder(Empty.newBuilder().build());
            hookReceiver(OrderMapper.INSTANCE, iter);
        });
        t1.start();
        //tariff spec
        Thread t2 = new Thread(() -> {
            Iterator<PBTariffSpecification> iter = comm.submitStub.submitTariffSpec(Empty.newBuilder().build());
            hookReceiver(TariffSpecificationMapper.INSTANCE, iter);
        });
        t2.start();
        //revokes
        Thread t3 = new Thread(() -> {
            Iterator<PBTariffRevoke> iter = comm.submitStub.submitTariffRevoke(Empty.newBuilder().build());
            hookReceiver(TariffRevokeMapper.INSTANCE, iter);
        });
        t3.start();
    }

    private void ensureAllDisconnected() {
        if (threads.size() > 0){
            log.error("something went wrong, disconnecting all submit threads");
        }
        for (Thread t : threads) {
            t.interrupt();
        }
    }

    private <P extends Message, T> void hookReceiver(AbstractPbPtacMapper<P, T> mapper, Iterator<P> iter) {
        try {
            while (iter.hasNext()) {
                P nextPBMsg = iter.next();
                T msg = mapper.map(nextPBMsg);
                // Not clean code, but required, otherwise there is no reference to TariffSpecification upon customer subscription
                if (msg instanceof TariffSpecification) {
//                    System.out.println("Converting to TariffSpec");
                    TariffSpecification tariffSpecification = (TariffSpecification) msg;
                    tariffRepo.addSpecification(tariffSpecification);
                    dispatcher.sendMessage(tariffSpecification);
                } else {
                    dispatcher.sendMessage(msg);
                }
//                dispatcher.sendMessage(msg);

                if (this.checkInterrupted()) break;
            }
        } catch (RuntimeException ex) {
            System.out.println("Exception in connecting receiver");
            System.out.println(ex.toString());
            log.error("exception in connecting receiver");
            log.error(ex);
        }
        //if one dies, all must die
        this.cleanupAll();
    }

    /**
     * tells all others to die too.
     */
    private void cleanupAll() {
        for (Thread t : threads) {
            t.interrupt();
            threads.remove(t);
        }
    }

    private boolean checkInterrupted() {
        return Thread.currentThread().isInterrupted();
    }


}
