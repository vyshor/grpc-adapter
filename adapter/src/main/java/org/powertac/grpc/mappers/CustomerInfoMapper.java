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

import de.pascalwhoop.powertac.grpc.PBCustomerClass;
import de.pascalwhoop.powertac.grpc.PBCustomerInfo;
import de.pascalwhoop.powertac.grpc.PBPowerType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.CustomerInfo;
import org.powertac.common.enumerations.PowerType;
import org.powertac.grpc.mappers.CustomerInfoMapper.BuilderFactory;


@Mapper(uses = {BuilderFactory.class, PowerTypeMapper.class},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class CustomerInfoMapper
  implements AbstractPbPtacMapper<PBCustomerInfo, CustomerInfo>
{

  public static CustomerInfoMapper INSTANCE = Mappers.getMapper(CustomerInfoMapper.class);


  @Mappings({ })
  public abstract PBCustomerInfo.Builder map(CustomerInfo ptacObject);

  public PBCustomerInfo build(CustomerInfo in){
    return map(in).build();
  }

  @Mappings({})
  public abstract CustomerInfo map(PBCustomerInfo pbObject, @MappingTarget CustomerInfo in);

  @ValueMappings({
      @ValueMapping(source = "SMALL", target = "SMALL"),
      @ValueMapping(source = "LARGE", target = "LARGE"),
      @ValueMapping(source = "UNRECOGNIZED", target = "SMALL")
  })
  public abstract CustomerInfo.CustomerClass mapCcEnum(PBCustomerClass in);

//  default PBCustomerInfo build(PBCustomerInfo.Builder builder){
//    return builder.build();
//  }


  class BuilderFactory extends AbstractBuilderFactory<PBCustomerInfo, CustomerInfo>
  {
    PBCustomerInfo.Builder builder()
    {
      return PBCustomerInfo.newBuilder();
    }

    @ObjectFactory
    CustomerInfo builder(PBCustomerInfo in){
      CustomerInfo out =  new CustomerInfo(in.getName(), in.getPopulation());
      return builderSetId(in, out);
    }
  }
}
