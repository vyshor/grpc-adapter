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
import de.pascalwhoop.powertac.grpc.PBMarketTransaction;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Broker;
import org.powertac.common.MarketTransaction;
import org.powertac.grpc.mappers.MarketTransactionMapper.BuilderFactory;


@Mapper(uses = {BuilderFactory.class, BrokerMapper.class},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MarketTransactionMapper
{

  MarketTransactionMapper INSTANCE = Mappers.getMapper(MarketTransactionMapper.class);


  @Mappings({
      @Mapping(source = "timeslotIndex", target = "timeslot"),
      @Mapping(source = "postedTimeslotIndex", target = "postedTimeslot")
  })
  PBMarketTransaction.Builder map(MarketTransaction ptacObject);

  @Mappings({
  })
  MarketTransaction map(PBMarketTransaction pbObject);


  class BuilderFactory
  {
    PBMarketTransaction.Builder builder()
    {
      return PBMarketTransaction.newBuilder();
    }

    @ObjectFactory
    MarketTransaction builder(PBMarketTransaction in){
      Broker broker = BrokerMapper.INSTANCE.map(in.getBroker());
      return new MarketTransaction(broker, in.getPostedTimeslot(), in.getTimeslot(), in.getMWh(), in.getPrice());
    }
  }
}
