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

import de.pascalwhoop.powertac.grpc.PBCustomerInfo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.CustomerInfo;
import org.powertac.grpc.mappers.CustomerInfoMapper.BuilderFactory;


@Mapper(uses = BuilderFactory.class,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerInfoMapper
{

  CustomerInfoMapper INSTANCE = Mappers.getMapper(CustomerInfoMapper.class);


  @Mappings({})
  PBCustomerInfo.Builder map(CustomerInfo ptacObject);

  @Mappings({})
  CustomerInfo map(PBCustomerInfo pbObject);

//  default PBCustomerInfo build(PBCustomerInfo.Builder builder){
//    return builder.build();
//  }


  class BuilderFactory
  {
    PBCustomerInfo.Builder builder()
    {
      return PBCustomerInfo.newBuilder();
    }

    @ObjectFactory
    CustomerInfo builder(PBCustomerInfo in){
      return new CustomerInfo(in.getName(), in.getPopulation());
    }
  }
}
