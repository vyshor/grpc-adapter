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

import de.pascalwhoop.powertac.grpc.PBBalanceReport;
import org.junit.Before;
import org.powertac.common.msg.BalanceReport;
import org.powertac.grpc.TestObjectGenerator;

import static org.junit.Assert.*;

public class BalanceReportMapperTest extends AbstractMapperTest<PBBalanceReport, BalanceReport, BalanceReportMapper> implements MapperTestInterface
{

  @Override
  @Before
  public void before(){
    ptac = TestObjectGenerator.balanceReport;
    mapper = BalanceReportMapper.INSTANCE;
  }

  @Override
  public void testToPB()
  {

  }

  @Override
  public void testToPtac()
  {

  }
}