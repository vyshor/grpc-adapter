//package org.powertac.grpc.mappers;
//
//import de.pascalwhoop.powertac.grpc.PBTariffSpecification;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.powertac.common.TariffSpecification;
//import org.powertac.common.repo.BrokerRepo;
//import org.powertac.common.repo.TimeslotRepo;
//import org.powertac.common.spring.SpringApplicationContext;
//import org.powertac.grpc.TestObjectGenerator;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.doReturn;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:test-config.xml"})
//public class TariffSpecificationMapperTest extends AbstractMapperTest<PBTariffSpecification, TariffSpecification, TariffSpecificationMapper> {
//
//    @Before
//    public void before(){
//        super.before();
//        ptac = TestObjectGenerator.tariffSpecification;
//        mapper = TariffSpecificationMapper.INSTANCE;
//
//        TimeslotRepo timeslotRepo = (TimeslotRepo) SpringApplicationContext.getBean("timeslotRepo");
//        doReturn(TestObjectGenerator.timeslot).when(timeslotRepo).findBySerialNumber(1);
//
//        //mocking broker repositories
//        BrokerRepo brokerRepo = new BrokerRepo();
//        brokerRepo.add(TestObjectGenerator.broker);
//
//        //mocking it in the tariffSpecMapper
//        TariffSpecificationMapper.BuilderFactory bf2 = (TariffSpecificationMapper.BuilderFactory) ReflectionTestUtils.getField(mapper, "builderFactory");
//        bf2.repo = brokerRepo;
//
//    }
//}