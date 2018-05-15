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

import de.pascalwhoop.powertac.grpc.PBOrderbook;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powertac.common.Orderbook;
import org.powertac.common.repo.TimeslotRepo;
import org.powertac.common.spring.SpringApplicationContext;
import org.powertac.grpc.TestObjectGenerator;
import org.powertac.samplebroker.core.BrokerMessageReceiver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
public class OrderbookMapperTest extends AbstractMapperTest<PBOrderbook, Orderbook, OrderbookMapper> implements MapperTestInterface {

    @Before
    @Override
    public void before() {
        super.before();
        ptac = TestObjectGenerator.orderbook;
        mapper = OrderbookMapper.INSTANCE;
        //doesn't work because xstream needs the proper object
        //ptac = Mockito.spy(ptac);
        //doReturn(TestObjectGenerator.timeslot).when(ptac).getTimeslot();


        TimeslotRepo repo = (TimeslotRepo) SpringApplicationContext.getBean("timeslotRepo");
        doReturn(TestObjectGenerator.timeslot).when(repo).findBySerialNumber(1);


    }

    @Override
    public void testToPB() {

    }

    @Override
    public void testToPtac() {

    }
}