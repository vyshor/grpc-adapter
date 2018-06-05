package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBSimEnd;
import org.joda.time.Instant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.msg.SimEnd;
import org.springframework.stereotype.Component;

@Mapper(uses = {SimEndMapper.BuilderFactory.class, InstantMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SimEndMapper {

    SimEndMapper INSTANCE = Mappers.getMapper(SimEndMapper.class);

    @Mappings({ })
    PBSimEnd.Builder map(SimEnd ptacObject);

    @Mappings({})
    SimEnd map(PBSimEnd pbObject);


    @Component
    class BuilderFactory {
        PBSimEnd.Builder builder() {
            return PBSimEnd.newBuilder();
        }
    }
}
