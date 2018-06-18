package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBTariffRevoke;
import de.pascalwhoop.powertac.grpc.PBTariffRevoke;
import de.pascalwhoop.powertac.grpc.PBTariffSpecification;
import org.joda.time.Instant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Broker;
import org.powertac.common.TariffSpecification;
import org.powertac.common.msg.TariffRevoke;
import org.powertac.common.repo.BrokerRepo;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {TariffRevokeMapper.BuilderFactory.class,BrokerMapper.class, InstantMapper.class, TariffSpecificationMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TariffRevokeMapper extends AbstractPbPtacMapper<PBTariffRevoke, TariffRevoke>
{

    TariffRevokeMapper INSTANCE = Mappers.getMapper(TariffRevokeMapper.class);


    @Mappings({
    })
    PBTariffRevoke.Builder map(TariffRevoke ptacObject);

    @Mappings({})
    TariffRevoke map(PBTariffRevoke pbObject);


    class BuilderFactory
    {
        @Autowired
        BrokerRepo brokerRepo;

        PBTariffRevoke.Builder builder()
        {
            return PBTariffRevoke.newBuilder();
        }

        @ObjectFactory
        TariffRevoke builder(PBTariffRevoke in){
            TariffSpecification mockSpec = TariffSpecificationMapper.INSTANCE.map(PBTariffSpecification.newBuilder().setId(in.getTariffId()).build());
            return new TariffRevoke(new Broker(in.getBroker()), mockSpec);
        }
    }
}
