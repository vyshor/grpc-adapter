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

import com.google.protobuf.Message;
import de.pascalwhoop.powertac.grpc.PBBalanceReport;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.msg.BalanceReport;

@Mapper(
    uses = {BalanceReportMapper.BuilderFactory.class},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface BalanceReportMapper extends AbstractPbPtacMapper<PBBalanceReport, BalanceReport>
{
  BalanceReportMapper INSTANCE = Mappers.getMapper(BalanceReportMapper.class);

  @Mappings({})
  @Override
  PBBalanceReport.Builder map(BalanceReport in);

  @Mappings({})
  BalanceReport map(PBBalanceReport in, @MappingTarget BalanceReport out);

  class BuilderFactory{
    PBBalanceReport.Builder builder() {
      return PBBalanceReport.newBuilder();
    }

    @ObjectFactory
    BalanceReport builder(PBBalanceReport in){
      return new BalanceReport(in.getTimeslotIndex(), in.getNetImbalance());
    }

  }

}
