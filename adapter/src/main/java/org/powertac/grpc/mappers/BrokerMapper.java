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

import de.pascalwhoop.powertac.grpc.PBBroker;
import org.mapstruct.*;

import org.mapstruct.factory.Mappers;
import org.powertac.common.Broker;
import org.powertac.common.repo.BrokerRepo;
import org.powertac.grpc.mappers.BrokerMapper.BuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Mapper(uses = BuilderFactory.class,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BrokerMapper {

    BrokerMapper INSTANCE = Mappers.getMapper(BrokerMapper.class);

    @Mappings({})
    Broker map(String username);

    @Mappings({
        @Mapping(source = "cashBalance", target = "cash")
    })
    PBBroker.Builder map(Broker in);

    default String mapToString(Broker in){
        return in.getUsername();
    }




    @Component
    class BuilderFactory {
        @Autowired
        BrokerRepo repo;

        PBBroker.Builder builder() {
            return PBBroker.newBuilder();
        }

        @ObjectFactory
        Broker brokerFromUsername(String username){
            return new Broker(username);
        }

        @ObjectFactory
        Broker brokerfromPBBroker(PBBroker in){
            return new Broker(in.getUsername());
        }
    }
}
