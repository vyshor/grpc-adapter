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

import de.pascalwhoop.powertac.grpc.PBMarketPosition;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Broker;
import org.powertac.common.MarketPosition;
import org.powertac.grpc.mappers.MarketPositionMapper.BuilderFactory;


@Mapper(uses = {BuilderFactory.class, BrokerMapper.class, TimeslotMapper.class},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MarketPositionMapper
{

  MarketPositionMapper INSTANCE = Mappers.getMapper(MarketPositionMapper.class);


  @Mappings({})
  PBMarketPosition.Builder map(MarketPosition ptacObject);

  @Mappings({})
  MarketPosition map(PBMarketPosition pbObject);


  class BuilderFactory
  {
    PBMarketPosition.Builder builder()
    {
      return PBMarketPosition.newBuilder();
    }

    @ObjectFactory
    MarketPosition builder(PBMarketPosition in)
    {
      Broker broker = BrokerMapper.INSTANCE.map(in.getBroker());
      return new MarketPosition(broker, in.getTimeslot(), in.getOverallBalance());
    }
  }
}
