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

import de.pascalwhoop.powertac.grpc.PBCompetition;
import org.mapstruct.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Competition;
import org.powertac.grpc.mappers.CompetitionMapper.BuilderFactory;

import org.powertac.grpc.mappers.CompetitionMapper.BuilderFactory;


@Mapper(uses = {BuilderFactory.class, InstantMapper.class, BrokerMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CompetitionMapper {

    CompetitionMapper INSTANCE = Mappers.getMapper(CompetitionMapper.class);


    @Mappings({})
    PBCompetition.Builder map(Competition ptacObject);

    @Mappings({})
    Competition map(PBCompetition pbObject);




    class BuilderFactory {
        PBCompetition.Builder builder() {
            return PBCompetition.newBuilder();
        }

        @ObjectFactory
        Competition builder(PBCompetition in){
            return  Competition.newInstance(in.getName());
        }
    }
}
