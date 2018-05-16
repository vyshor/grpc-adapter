package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBTariffTransaction;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powertac.common.TariffTransaction;
import org.powertac.common.repo.BrokerRepo;
import org.powertac.common.repo.TimeslotRepo;
import org.powertac.common.spring.SpringApplicationContext;
import org.powertac.grpc.TestObjectGenerator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
public class TariffTransactionMapperTest extends AbstractMapperTest<PBTariffTransaction, TariffTransaction, TariffTransactionMapper> {

    @Before
    public void before(){
        super.before();
        ptac = TestObjectGenerator.tariffTransaction;
        mapper = TariffTransactionMapper.INSTANCE;

        TimeslotRepo timeslotRepo = (TimeslotRepo) SpringApplicationContext.getBean("timeslotRepo");
        doReturn(TestObjectGenerator.timeslot).when(timeslotRepo).findBySerialNumber(1);

        //mocking broker repositories
        BrokerRepo brokerRepo = new BrokerRepo();
        brokerRepo.add(TestObjectGenerator.broker);

        //mocking it in the tariffTransactionMapper
        TariffTransactionMapper ttm = TariffTransactionMapper.INSTANCE;
        TariffTransactionMapper.BuilderFactory bf = (TariffTransactionMapper.BuilderFactory) ReflectionTestUtils.getField(ttm, "builderFactory");
        bf.repo = brokerRepo;
        //mocking it in the tariffSpecMapper
        TariffSpecificationMapper tsm = TariffSpecificationMapper.INSTANCE;
        TariffSpecificationMapper.BuilderFactory bf2 = (TariffSpecificationMapper.BuilderFactory) ReflectionTestUtils.getField(tsm, "builderFactory");
        bf2.repo = brokerRepo;

    }
}