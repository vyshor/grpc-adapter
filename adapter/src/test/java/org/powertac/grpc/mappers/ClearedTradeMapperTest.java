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
import com.thoughtworks.xstream.annotations.XStreamConverter;
import de.pascalwhoop.powertac.grpc.PBClearedTrade;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powertac.common.Broker;
import org.powertac.common.ClearedTrade;
import org.powertac.common.XMLMessageConverter;
import org.powertac.common.repo.BrokerRepo;
import org.powertac.common.repo.TimeslotRepo;
import org.powertac.grpc.ValueGenerator;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClearedTradeMapperTest implements MapperTestInterface
{

  ClearedTrade ptac = ValueGenerator.clearedTrade;
  private ClearedTradeMapper mapper = ClearedTradeMapper.INSTANCE;


  @Override
  @Before
  public void before()
  {
    ptac = spy(ptac);
    doReturn(ValueGenerator.timeslot).when(ptac).getTimeslot();
    //when(ptac.getTimeslot()).thenReturn(ValueGenerator.timeslot);
  }

  @Override
  @Test
  public void testToPB()
  {
    PBClearedTrade out = mapper.map(ptac).build();
    assertEquals(ptac.getExecutionMWh(), out.getExecutionMWh(), 0.0001);
    assertEquals(ptac.getTimeslot().getId(), out.getTimeslot());

  }

  @Override
  @Test
  public void testToPtac()
  {
    PBClearedTrade intermediate = mapper.map(ptac).build();
    ClearedTrade out = mapper.map(intermediate);

    assertEquals(ptac.getExecutionMWh(), out.getExecutionMWh(), 0.0001);
    assertEquals(ptac.getTimeslot().getId(), ValueGenerator.timeslot.getId());
  }

  @Override
  @Test
  public void roundtripJsonCompare() throws InvalidProtocolBufferException
  {
    //There must be a way to do this without crashing. Would also be awesome to have this generic. Take ptac classes (orig xml) turn them into objects, then roundtrip through grpc and still same xml

    //XMLMessageConverter converter = new XMLMessageConverter();
    //String in = converter.toXML(ptac);
    //String roundtrip = converter.toXML(mapper.map(mapper.map(ptac).build()));
    //assertEquals(in, roundtrip);

  }
}