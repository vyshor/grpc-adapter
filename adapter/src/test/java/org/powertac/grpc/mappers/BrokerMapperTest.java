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
import de.pascalwhoop.powertac.grpc.PBBroker;
import org.junit.Before;
import org.junit.Test;
import org.powertac.common.Broker;
import org.powertac.common.repo.BrokerRepo;
import org.powertac.grpc.TestObjectGenerator;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

public class BrokerMapperTest implements MapperTestInterface
{
  Broker ptac = TestObjectGenerator.broker;
  private BrokerMapper mapper = BrokerMapper.INSTANCE;

  @Override
  @Before
  public void before()
  {
    //prepare repo
    BrokerMapperImpl inst = (BrokerMapperImpl) mapper;
    BrokerMapper.BuilderFactory bf = (BrokerMapper.BuilderFactory) ReflectionTestUtils.getField(inst, "builderFactory");
    BrokerRepo repo = new BrokerRepo();
    ReflectionTestUtils.setField(bf, "repo", repo);

    //prepare instance
    ptac.updateCash(45);
    repo.add(ptac);
  }

  @Override
  @Test
  public void testToPB()
  {
    PBBroker out = mapper.map(ptac).build();
    assertEquals(out.getCash(), ptac.getCashBalance(), 0.0001);
    assertEquals(out.getUsername(), ptac.getUsername());
  }

  @Override
  @Test
  public void testToPtac()
  {
    //broker doesnt get grpc-> ptac
    // issues with wrong format of get/set anyways
  }

  @Override
  @Test
  public void roundtripJsonCompare() throws InvalidProtocolBufferException
  {
    //doesn't happen because toPtac doesn't happen
  }
}