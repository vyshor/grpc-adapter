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

import de.pascalwhoop.powertac.grpc.PBClearedTrade;
import org.joda.time.Instant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.ClearedTrade;
import org.powertac.common.repo.TimeslotRepo;
import org.powertac.grpc.mappers.ClearedTradeMapper.BuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(uses = {BuilderFactory.class, InstantMapper.class},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ClearedTradeMapper
{

  ClearedTradeMapper INSTANCE = Mappers.getMapper(ClearedTradeMapper.class);


  @Mappings({
      @Mapping(source = "timeslot.serialNumber", target = "timeslot")
  })
  PBClearedTrade.Builder map(ClearedTrade ptacObject);

  @Mappings({})
  ClearedTrade map(PBClearedTrade pbObject);


  class BuilderFactory
  {

    PBClearedTrade.Builder builder()
    {
      return PBClearedTrade.newBuilder();
    }

    @ObjectFactory
    ClearedTrade builder(PBClearedTrade in){
      return new ClearedTrade(in.getTimeslot(), in.getExecutionMWh(), in.getExecutionPrice(), new Instant(in.getDateExecuted()));

    }
  }
}
