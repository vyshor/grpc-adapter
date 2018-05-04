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

import de.pascalwhoop.powertac.grpc.PBPowerType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.enumerations.PowerType;
import org.powertac.grpc.mappers.PowerTypeMapper.BuilderFactory;


@Mapper(uses = BuilderFactory.class,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PowerTypeMapper
{

  PowerTypeMapper INSTANCE = Mappers.getMapper(PowerTypeMapper.class);


  @Mappings({})
  PBPowerType.Builder map(PowerType ptacObject);

  @Mappings({})
  PowerType map(PBPowerType pbObject);


  class BuilderFactory
  {
    PBPowerType.Builder builder()
    {
      return PBPowerType.newBuilder();
    }


    @ObjectFactory
    PowerType builder(PBPowerType in){
      return PowerType.valueOf(in.getLabel());
    }
  }
}
