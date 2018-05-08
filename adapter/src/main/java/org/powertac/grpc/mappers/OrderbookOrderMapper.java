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
import de.pascalwhoop.powertac.grpc.PBOrderbookOrder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;
import org.powertac.common.OrderbookOrder;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@Mapper(uses = {
    OrderbookOrderMapper.BuilderFactory.class,
    InstantMapper.class,
},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = ALWAYS)
public interface OrderbookOrderMapper extends AbstractPbPtacMapper<PBOrderbookOrder, OrderbookOrder>
{
  OrderbookOrderMapper INSTANCE = Mappers.getMapper(OrderbookOrderMapper.class);

  @Mappings({})
  @Override
  PBOrderbookOrder.Builder map(OrderbookOrder in);

  @Mappings({})
  @Override
  OrderbookOrder map(PBOrderbookOrder in);

  class BuilderFactory
  {
    PBOrderbookOrder.Builder builder(){
      return PBOrderbookOrder.newBuilder();
    }

    @ObjectFactory
    OrderbookOrder builder(PBOrderbookOrder in)
    {
      return new OrderbookOrder(in.getMWh(), in.getLimitPrice());
    }

  }
}
