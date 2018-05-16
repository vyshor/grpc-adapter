package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBRate;
import de.pascalwhoop.powertac.grpc.PBRateCore;
import de.pascalwhoop.powertac.grpc.PBRegulationRate;
import de.pascalwhoop.powertac.grpc.PBTariffSpecification;
import org.joda.time.Instant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Broker;
import org.powertac.common.TariffSpecification;
import org.powertac.common.enumerations.PowerType;
import org.powertac.common.repo.BrokerRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(uses = {TariffSpecificationMapper.BuilderFactory.class, InstantMapper.class, RateMapper.class, RegulationRateMapper.class, BrokerMapper.class, RateCoreMapper.class, PowerTypeMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        imports = {Instant.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TariffSpecificationMapper extends AbstractPbPtacMapper<PBTariffSpecification, TariffSpecification> {

    TariffSpecificationMapper INSTANCE = Mappers.getMapper(TariffSpecificationMapper.class);


    @Mappings({
            @Mapping(source = "rates", target = "ratesList"),
            @Mapping(source = "regulationRates" , target = "regulationRatesList")
    })
    @Override
    PBTariffSpecification.Builder map(TariffSpecification ptacObject);

    @Mappings({
            @Mapping(expression = "java( in.getExpiration()== 0? null : new Instant(in.getExpiration()) )", target = "expiration")
    })
    TariffSpecification map(PBTariffSpecification in, @MappingTarget TariffSpecification out);

    //@AfterMapping
    //PBTariffSpecification map(PBTariffSpecification.Builder in);

    default PBTariffSpecification map(PBTariffSpecification.Builder tariffSpecification){
        return tariffSpecification.build();
    }

    class BuilderFactory extends AbstractBuilderFactory<PBTariffSpecification, TariffSpecification>{

        @Autowired
        BrokerRepo repo;

        RateMapper rm = RateMapper.INSTANCE;
        RegulationRateMapper rrm = RegulationRateMapper.INSTANCE;

        PBTariffSpecification.Builder builder() {
            return PBTariffSpecification.newBuilder();
        }

        @ObjectFactory
        TariffSpecification builder(PBTariffSpecification in) {
            Broker broker = repo.findByUsername(in.getBroker());
            PowerType pt = PowerTypeMapper.INSTANCE.map(in.getPowerType());
            TariffSpecification out = new TariffSpecification(broker, pt);
            List<PBRate> rates = in.getRatesList();
            for(PBRate r: rates){
               out.addRate(rm.map(r));
            }
            List<PBRegulationRate> rrates = in.getRegulationRatesList();
            for (PBRegulationRate r: rrates){
               out.addRate(rrm.map(r));
            }
            return builderSetId(in, out);
        }
    }
}
