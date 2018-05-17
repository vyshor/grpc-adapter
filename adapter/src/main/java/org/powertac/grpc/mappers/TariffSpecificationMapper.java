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
public abstract class TariffSpecificationMapper implements AbstractPbPtacMapper<PBTariffSpecification, TariffSpecification> {

    public static TariffSpecificationMapper INSTANCE = Mappers.getMapper(TariffSpecificationMapper.class);


    @Mappings({
            @Mapping(source = "rates", target = "ratesList"),
            @Mapping(source = "regulationRates" , target = "regulationRatesList"),
            @Mapping(source = "supersedes", target = "supersedesList", ignore = true)
    })
    @Override
    public abstract PBTariffSpecification.Builder map(TariffSpecification ptacObject);

    @AfterMapping
    void addPrimitiveSupersedesList (TariffSpecification in, @MappingTarget PBTariffSpecification.Builder out){
        if(in.getSupersedes() == null){
            return;
        }
        for (Long s: in.getSupersedes()){
            out.addSupersedes(s);
        }
    }

    @Mappings({
            @Mapping(expression = "java( in.getExpiration()== 0? null : new Instant(in.getExpiration()) )", target = "expiration"),
            //@Mapping(source = "ratesList", target = "rates"),
            //@Mapping(source = "regulationRatesList", target = "regulationRates"),
            @Mapping(source = "supersedesList", target = "supersedes")
    })
    public abstract TariffSpecification map(PBTariffSpecification in);
    //public abstract TariffSpecification map(PBTariffSpecification in, @MappingTarget TariffSpecification out);


    @AfterMapping
    void cleanExpiration(PBTariffSpecification in, @MappingTarget TariffSpecification out){
        if (in.getExpiration() == 0){
            out.withExpiration(null);
        }
    }

    public PBTariffSpecification map(PBTariffSpecification.Builder tariffSpecification){
        return tariffSpecification.build();
    }

    // ---------------------------------------------------------------
    //helper builder factory class
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
            //doing manually because I cannot target the same list by two sources
            for(PBRate r: rates){
               out.addRate(rm.map(r));
            }
            List<PBRegulationRate> rrates = in.getRegulationRatesList();
            for (PBRegulationRate r: rrates){
               out.addRate(rrm.map(r));
            }
            //hacky way to make out.supersedes not null
            out.addSupersedes(0l);
            out.getSupersedes().clear();
            return builderSetId(in, out);
        }
    }
}
