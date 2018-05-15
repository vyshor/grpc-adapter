package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBTimeslot;
import de.pascalwhoop.powertac.grpc.PBTimeslotUpdate;
import org.joda.time.Instant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Timeslot;
import org.powertac.common.msg.TimeslotUpdate;
import org.powertac.common.repo.TimeslotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(uses = {TimeslotUpdateMapper.BuilderFactory.class, InstantMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TimeslotUpdateMapper {

    TimeslotUpdateMapper INSTANCE = Mappers.getMapper(TimeslotUpdateMapper.class);

    @Mappings({ })
    PBTimeslotUpdate.Builder map(TimeslotUpdate ptacObject);

    @Mappings({})
    TimeslotUpdate map(PBTimeslotUpdate pbObject);


    @Component
    class BuilderFactory {

        @ObjectFactory //avoidable with empty constructors
        TimeslotUpdate builder(PBTimeslotUpdate in){
            return new TimeslotUpdate(new Instant(in.getPostedTime()), in.getFirstEnabled(), in.getLastEnabled());
        }

        PBTimeslotUpdate.Builder builder() {
            return PBTimeslotUpdate.newBuilder();
        }
    }
}
