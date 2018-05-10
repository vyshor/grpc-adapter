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
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.powertac.common.Competition;
import org.powertac.common.XMLMessageConverter;
import org.powertac.grpc.TestObjectGenerator;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompetitionMapperTest extends AbstractMapperTest<PBCompetition, Competition, CompetitionMapper> implements MapperTestInterface
{

  XMLMessageConverter conv = new XMLMessageConverter();

  @Before
  @Override
  public void before()
  {
    super.before();
    ptac = TestObjectGenerator.competition;
    mapper = CompetitionMapper.INSTANCE;

    conv.afterPropertiesSet();
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
  public void testSizeCompare()
  {

    String xml = getTestCompetitionXmlString();


    //String xml2 = conv.toXML(TestObjectGenerator.competition);
    //assertEquals(xml, xml2);

    Competition comp = (Competition) conv.fromXML(xml);
    PBCompetition pbComp = mapper.map(comp).build();
    System.out.println(pbComp.getSerializedSize());
    System.out.println(xml.getBytes().length);
    assertTrue(pbComp.getSerializedSize() < xml.getBytes().length);
  }

  private String getTestCompetitionXmlString()
  {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream is = classloader.getResourceAsStream("test_competition.xml");
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

  //@Test
  public void testSerialisationSpeed()
  {

    String xml = getTestCompetitionXmlString();
    Instant start = Instant.now();
    Competition comp = (Competition) conv.fromXML(xml);
    for (int i = 0; i < 1000; i++) {
      conv.toXML(comp);
    }
    Instant stop = Instant.now();
    long xStreamDiff = stop.minus(start.getMillis()).getMillis();


    start = Instant.now();
    for (int i = 0; i < 1000; i++) {
      mapper.map(comp).build();
      //comp = mapper.map(pbcomp);
    }
    stop = Instant.now();
    long grpcDiff = stop.minus(start.getMillis()).getMillis();

    System.out.println("XStream time for 1000 serialisations: " + xStreamDiff);
    System.out.println("Grpc time for 1000 serialisations   : " + grpcDiff);


  }


  //@Test
  public void testDeserialisation()
  {

    String xml = getTestCompetitionXmlString();
    Instant start = Instant.now();
    Competition comp = (Competition) conv.fromXML(xml);
    for (int i = 0; i < 1000; i++) {
      conv.toXML(comp);
    }
    Instant stop = Instant.now();
    long xStreamDiff = stop.minus(start.getMillis()).getMillis();


    start = Instant.now();
    PBCompetition pbcomp = mapper.map(comp).build();
    for (int i = 0; i < 1000; i++) {
      mapper.map(pbcomp);
    }
    stop = Instant.now();
    long grpcDiff = stop.minus(start.getMillis()).getMillis();

    System.out.println("XStream time for 1000 deserialisations: " + xStreamDiff);
    System.out.println("Grpc time for 1000 deserialisations   : " + grpcDiff);
  }

}