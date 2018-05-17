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


import de.pascalwhoop.powertac.grpc.PBBalancingTransaction;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.BalancingTransaction;
import org.powertac.common.Broker;
import org.powertac.grpc.mappers.BalancingTransactionMapper.BuilderFactory;
import org.powertac.samplebroker.core.BrokerMain;


@Mapper(uses = BuilderFactory.class,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BalancingTransactionMapper extends AbstractPbPtacMapper<PBBalancingTransaction, BalancingTransaction> {

    BalancingTransactionMapper INSTANCE = Mappers.getMapper(BalancingTransactionMapper.class);

    @Mappings ({
        @Mapping(source = "broker.username", target = "broker"),
        @Mapping(source = "postedTimeslotIndex", target = "postedTimeslot")
    })
    PBBalancingTransaction.Builder map(BalancingTransaction in);

    @InheritInverseConfiguration
    BalancingTransaction map(PBBalancingTransaction in);





    class BuilderFactory extends AbstractBuilderFactory<PBBalancingTransaction, BalancingTransaction> {
        PBBalancingTransaction.Builder builder() {
            return PBBalancingTransaction.newBuilder();
        }

        @ObjectFactory
        public BalancingTransaction createBalancingTransaction(PBBalancingTransaction in){
            Broker broker  = BrokerMapper.INSTANCE.map(in.getBroker());
            BalancingTransaction out =  new BalancingTransaction(broker, in.getPostedTimeslot(), in.getKWh(), in.getCharge());
            return builderSetId(in, out);

        }
    }
}
