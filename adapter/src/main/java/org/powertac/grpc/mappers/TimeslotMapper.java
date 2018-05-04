/*
 *  Copyright 2009-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an
 *  "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied. See the License for the specific language
 *  governing permissions and limitations under the License.
 */

package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBTimeslot;
import org.joda.time.Instant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Timeslot;
import org.powertac.common.repo.TimeslotRepo;
import org.powertac.grpc.mappers.TimeslotMapper.BuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Mapper(uses = {BuilderFactory.class, InstantMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TimeslotMapper {

    TimeslotMapper INSTANCE = Mappers.getMapper(TimeslotMapper.class);


    @Mappings({ })
    PBTimeslot.Builder map(Timeslot ptacObject);

    @Mappings({})
    Timeslot map(PBTimeslot pbObject);

    default int mapToInt(Timeslot ptacObject){
        return ptacObject.getSerialNumber();
    }



    @Component
    class BuilderFactory {

        @Autowired
        TimeslotRepo repo;

        @ObjectFactory
        Timeslot timeslotFromId(int id){
            return repo.findBySerialNumber(id);
        }

        @ObjectFactory //avoidable with empty constructors
        Timeslot builder(PBTimeslot in){
            return new Timeslot(in.getSerialNumber(), new Instant(in.getStartInstant()));
        }

        PBTimeslot.Builder builder() {
            return PBTimeslot.newBuilder();
        }
    }
}
