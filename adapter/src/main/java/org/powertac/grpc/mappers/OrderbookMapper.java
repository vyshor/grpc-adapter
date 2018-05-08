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

import com.google.protobuf.Message;
import de.pascalwhoop.powertac.grpc.PBOrderbook;
import de.pascalwhoop.powertac.grpc.PBOrderbookOrder;
import org.joda.time.Instant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Orderbook;
import org.springframework.messaging.support.MessageBuilder;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@Mapper(uses = {
    OrderbookMapper.BuilderFactory.class,
    InstantMapper.class,
    TimeslotMapper.class,
    OrderbookOrderMapper.class

},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = ALWAYS)
public interface OrderbookMapper extends AbstractPbPtacMapper<PBOrderbook, Orderbook>
{

  OrderbookMapper INSTANCE = Mappers.getMapper(OrderbookMapper.class);

  @Mappings({})
  PBOrderbook.Builder map(Orderbook in);

  @Mappings({})
  Orderbook map(PBOrderbook in, @MappingTarget Orderbook out);

  class BuilderFactory
  {
    PBOrderbook.Builder builder(){
      return PBOrderbook.newBuilder();
    }

    @ObjectFactory
    Orderbook build(PBOrderbook in)
    {
      return new Orderbook(in.getTimeslot(), in.getClearingPrice(), new Instant(in.getDateExecuted()));
    }

  }
}
