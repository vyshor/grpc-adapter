package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBCustomerBootstrapData;
import org.junit.Before;
import org.powertac.common.XMLMessageConverter;
import org.powertac.common.msg.CustomerBootstrapData;
import org.powertac.common.repo.BrokerRepo;
import org.powertac.common.repo.CustomerRepo;
import org.powertac.common.repo.CustomerRepo;
import org.powertac.common.repo.TimeslotRepo;
import org.powertac.common.spring.SpringApplicationContext;
import org.powertac.grpc.TestObjectGenerator;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

public class CustomerBootstrapDataMapperTest extends AbstractMapperTest<PBCustomerBootstrapData, CustomerBootstrapData, CustomerBootstrapDataMapper> {

  XMLMessageConverter conv = new XMLMessageConverter();

  @Before
  @Override
  public void before()
  {
    super.before();
    ptac = TestObjectGenerator.customerBootstrapData;
    mapper = CustomerBootstrapDataMapper.INSTANCE;
    conv.afterPropertiesSet();

    //mocking customer repo
    //mocking broker repositories
    CustomerRepo customerRepo = new CustomerRepo();
    customerRepo.add(TestObjectGenerator.customerInfo);

    //mocking it in the tariffSpecMapper
    CustomerBootstrapDataMapper.BuilderFactory bf2 = (CustomerBootstrapDataMapper.BuilderFactory) ReflectionTestUtils.getField(mapper, "builderFactory");
    bf2.customerRepo = customerRepo;
  }

}