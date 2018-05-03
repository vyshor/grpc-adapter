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

import com.google.protobuf.InvalidProtocolBufferException;
import de.pascalwhoop.powertac.grpc.PBBalancingTransaction;
import org.junit.Before;
import org.junit.Test;
import org.powertac.common.BalancingTransaction;
import org.powertac.common.Broker;
import org.powertac.common.repo.BrokerRepo;
import org.powertac.grpc.ValueGenerator;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

public class BalancingTransactionMapperTest implements MapperTestInterface
{

  @Before
  public void before(){
    BrokerMapperImpl inst = (BrokerMapperImpl) BrokerMapper.INSTANCE;
    BrokerMapper.BuilderFactory bf = (BrokerMapper.BuilderFactory) ReflectionTestUtils.getField(inst, "builderFactory");
    BrokerRepo repo = new BrokerRepo();
    ReflectionTestUtils.setField(bf, "repo", repo);
    repo.add(ValueGenerator.broker);
  }

  @Test
  public void testToPB()
  {
    BalancingTransaction in = ValueGenerator.balancingTransaction;
    PBBalancingTransaction out = BalancingTransactionMapper.INSTANCE.map(in).build();
    assertEquals(in.getCharge(), out.getCharge(), 0.0001);
    assertEquals(in.getBroker().getUsername(), out.getBroker());
  }


  @Test
  public void testToPtac()
  {

    PBBalancingTransaction in = BalancingTransactionMapper.INSTANCE.map(ValueGenerator.balancingTransaction).build();
    BalancingTransaction out = BalancingTransactionMapper.INSTANCE.map(in);
    assertEquals(in.getCharge(), out.getCharge(), 0.0001);
    assertEquals(in.getBroker(), out.getBroker().getUsername());
  }

  @Override
  @Test
  public void roundtripJsonCompare() throws InvalidProtocolBufferException
  {

  }


}