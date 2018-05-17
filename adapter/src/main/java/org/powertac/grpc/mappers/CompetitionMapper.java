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
import de.pascalwhoop.powertac.grpc.PBCustomerInfo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Competition;
import org.powertac.common.CustomerInfo;
import org.powertac.grpc.mappers.CompetitionMapper.BuilderFactory;


@Mapper(uses = {BuilderFactory.class, InstantMapper.class, BrokerMapper.class, CustomerInfoMapper.class, PowerTypeMapper.class},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CompetitionMapper extends AbstractPbPtacMapper<PBCompetition, Competition>
{

  CompetitionMapper INSTANCE = Mappers.getMapper(CompetitionMapper.class);


  @Mappings({
      @Mapping(source = "brokers", target = "brokersList"),
      @Mapping(source = "customers", target = "customersList")
  })
  @Override
  PBCompetition.Builder map(Competition ptacObject);

  @Mappings({
      @Mapping(source = "brokersList", target = "brokers"),
      @Mapping(source = "customersList", target = "customers")
  })

  @Override
  Competition map(PBCompetition pbObject);


  class BuilderFactory extends AbstractBuilderFactory<PBCompetition, Competition>
  {
    PBCompetition.Builder builder()
    {
      return PBCompetition.newBuilder();
    }

    @ObjectFactory
    Competition builder(PBCompetition in)
    {
      Competition out =  Competition.newInstance(in.getName());
      return builderSetId(in, out);
    }
  }
}
