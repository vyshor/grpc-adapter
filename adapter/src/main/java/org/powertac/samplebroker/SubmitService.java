package org.powertac.samplebroker;

import com.google.protobuf.Message;
import de.pascalwhoop.powertac.grpc.Empty;
import de.pascalwhoop.powertac.grpc.PBOrder;
import de.pascalwhoop.powertac.grpc.PBTariffRevoke;
import de.pascalwhoop.powertac.grpc.PBTariffSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.powertac.grpc.GrpcServiceChannel;
import org.powertac.grpc.mappers.AbstractPbPtacMapper;
import org.powertac.grpc.mappers.OrderMapper;
import org.powertac.grpc.mappers.TariffRevokeMapper;
import org.powertac.grpc.mappers.TariffSpecificationMapper;
import org.powertac.samplebroker.core.MessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;

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
                dispatcher.sendMessage(msg);

                if (this.checkInterrupted()) break;
            }
        } catch (RuntimeException ex) {
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
