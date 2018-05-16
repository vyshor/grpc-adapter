package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBRate;
import de.pascalwhoop.powertac.grpc.PBRegulationRate;
import de.pascalwhoop.powertac.grpc.PBResponseTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Rate;
import org.powertac.common.RegulationRate;

@Mapper(uses = {RegulationRateMapper.BuilderFactory.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RegulationRateMapper extends AbstractPbPtacMapper<PBRegulationRate, RegulationRate> {

    RegulationRateMapper INSTANCE = Mappers.getMapper(RegulationRateMapper.class);

    @Mappings({})
    PBRegulationRate.Builder map(RegulationRate in);

    default PBRegulationRate map(PBRegulationRate.Builder in){
        return in.build();
    }

    @Mappings({})
    RegulationRate map(PBRegulationRate in);

    @ValueMapping(source = "UNRECOGNIZED", target = "MINUTES")
    RegulationRate.ResponseTime map (PBResponseTime in);

    class BuilderFactory {
        PBRegulationRate.Builder builder() {
            return PBRegulationRate.newBuilder();
        }
    }
}
