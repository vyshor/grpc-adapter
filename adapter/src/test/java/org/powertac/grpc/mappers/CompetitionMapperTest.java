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

import de.pascalwhoop.powertac.grpc.PBCompetition;
import org.junit.Before;
import org.junit.Test;
import org.powertac.common.Competition;
import org.powertac.common.XMLMessageConverter;
import org.powertac.grpc.TestObjectGenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompetitionMapperTest extends AbstractMapperTest<PBCompetition, Competition, CompetitionMapper> implements MapperTestInterface
{


  @Before
  @Override
  public void before()
  {
    super.before();
    ptac = TestObjectGenerator.competition;
    mapper = CompetitionMapper.INSTANCE;
  }

  @Override
  public void testToPB()
  {
    PBCompetition out = mapper.map(ptac).build();
    assertEquals(out.getBrokersCount(), ptac.getBrokers().size());
    assertEquals(out.getCustomersCount(), ptac.getCustomers().size());
    assertEquals(out.getDownRegulationDiscount(), ptac.getDownRegulationDiscount(), 0.0001);

  }

  @Override
  public void testToPtac()
  {

  }

  @Test
  public void testSizeCompare() throws IOException
  {

    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream is = classloader.getResourceAsStream("test_competition.xml");
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    String xml = s.hasNext() ? s.next() : "";

    XMLMessageConverter conv = new XMLMessageConverter();
    conv.afterPropertiesSet();

    //String xml2 = conv.toXML(TestObjectGenerator.competition);
    //assertEquals(xml, xml2);

    Competition comp = (Competition) conv.fromXML(xml);
    PBCompetition pbComp = mapper.map(comp).build();
    System.out.println(pbComp.getSerializedSize());
    System.out.println(xml.getBytes().length);
    assertTrue(pbComp.getSerializedSize()<xml.getBytes().length);
  }

}