package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBBrokerAccept;
import org.joda.time.Instant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.msg.BrokerAccept;

@Mapper(uses = {BrokerAcceptMapper.BuilderFactory.class})
public interface BrokerAcceptMapper
{

    BrokerAcceptMapper INSTANCE = Mappers.getMapper(BrokerAcceptMapper.class);

    @Mappings({ })
    PBBrokerAccept.Builder map(BrokerAccept ptacObject);

    @Mappings({})
    BrokerAccept map(PBBrokerAccept pbObject);


    class BuilderFactory
    {

        PBBrokerAccept.Builder builder()
        {
            return PBBrokerAccept.newBuilder();
        }

        @ObjectFactory
        BrokerAccept builder(PBBrokerAccept in){
            return new BrokerAccept(in.getPrefix(), in.getKey());
        }
    }
}
