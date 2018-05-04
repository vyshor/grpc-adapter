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
import de.pascalwhoop.powertac.grpc.PBCustomerInfo;
import org.junit.Before;
import org.junit.Test;
import org.powertac.common.ClearedTrade;
import org.powertac.common.CustomerInfo;
import org.powertac.grpc.ValueGenerator;

import static org.junit.Assert.*;

public class CustomerInfoMapperTest implements MapperTestInterface
{

  CustomerInfo ptac = ValueGenerator.customerInfo;
  private CustomerInfoMapper mapper = CustomerInfoMapper.INSTANCE;

  @Before
  public void before()
  {

  }

  @Test
  public void testToPB()
  {
    PBCustomerInfo out = mapper.map(ptac).build();
    assertEquals(out.getCustomerClassValue(), ptac.getCustomerClass().ordinal());
    assertEquals(out.getName(), ptac.getName());

  }

  @Test
  public void testToPtac()
  {
    CustomerInfo out = mapper.map(mapper.map(ptac).build());
    assertEquals(out.getCustomerClass().ordinal(), ptac.getCustomerClass().ordinal());
  }

  @Test
  public void roundtripJsonCompare() throws InvalidProtocolBufferException
  {

  }

}