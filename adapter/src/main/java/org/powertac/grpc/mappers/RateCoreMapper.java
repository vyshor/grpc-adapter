package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBRate;
import de.pascalwhoop.powertac.grpc.PBRateCore;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.RateCore;


@Mapper(uses = {RateCoreMapper.BuilderFactory.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RateCoreMapper extends AbstractPbPtacMapper<PBRateCore, RateCore>{

    RateCoreMapper INSTANCE = Mappers.getMapper(RateCoreMapper.class);

    @Override
    @Mappings ({})
    PBRateCore.Builder map(RateCore in);

    default PBRateCore map(PBRateCore.Builder in){
        return in.build();
    }

    @Override
    @Mappings ({})
    RateCore map(PBRateCore in);

    class BuilderFactory {
        PBRateCore.Builder builder() {
            return PBRateCore.newBuilder();
        }
    }
}
