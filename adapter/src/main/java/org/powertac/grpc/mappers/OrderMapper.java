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


import de.pascalwhoop.powertac.grpc.PBOrder;
import de.pascalwhoop.powertac.grpc.PBOrderbook;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Broker;
import org.powertac.common.Order;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

/**
 * Maps the Order and PBOrder classes
 */
@Mapper(uses = {
    InstantMapper.class,
    TimeslotMapper.class,
    OrderMapper.BuilderFactory.class,
    BrokerMapper.class
},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = ALWAYS)
public interface OrderMapper extends AbstractPbPtacMapper<PBOrder, Order>
{
  OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

  @Override
  @Mappings({})
  PBOrder.Builder map(Order in);

  @Override
  @Mappings({})
  Order map(PBOrder in);

  class BuilderFactory extends AbstractBuilderFactory<PBOrder, Order>
  {
    PBOrder.Builder builder()
    {
      return PBOrder.newBuilder();
    }

    @ObjectFactory
    Order build(PBOrder in)
    {
      Broker b = BrokerMapper.INSTANCE.map(in.getBroker());
      //TODO missing the ID!
      Order o = new Order(b, in.getTimeslot(), in.getMWh(), in.getLimitPrice() );
      return builderSetId(in, o);
    }
  }

}
