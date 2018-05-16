package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBRate;
import de.pascalwhoop.powertac.grpc.PBRateCore;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Rate;

@Mapper(uses = {RateMapper.BuilderFactory.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RateMapper extends AbstractPbPtacMapper<PBRate, Rate> {

    RateMapper INSTANCE = Mappers.getMapper(RateMapper.class);

    @Mappings({})
    PBRate.Builder map(Rate in);

    default PBRate map(PBRate.Builder in){
        return in.build();
    }

    @Mappings({})
    Rate map(PBRate in);

    class BuilderFactory {
        PBRate.Builder builder() {
            return PBRate.newBuilder();
        }
    }
}
