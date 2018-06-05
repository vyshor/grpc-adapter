package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBCustomerBootstrapData;
import de.pascalwhoop.powertac.grpc.PBCustomerBootstrapDataOrBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.Instant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.CustomerInfo;
import org.powertac.common.enumerations.PowerType;
import org.powertac.common.msg.CustomerBootstrapData;
import org.powertac.common.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(uses = {CustomerBootstrapDataMapper.BuilderFactory.class, PowerTypeMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerBootstrapDataMapper extends AbstractPbPtacMapper<PBCustomerBootstrapData, CustomerBootstrapData> {

    CustomerBootstrapDataMapper INSTANCE = Mappers.getMapper(CustomerBootstrapDataMapper.class);

    @Mappings({
            //@Mapping(source = "netUsage", target="netUsageList")
    })
    PBCustomerBootstrapData.Builder map(CustomerBootstrapData ptacObject);

    @Mappings({})
    CustomerBootstrapData map(PBCustomerBootstrapData pbObject);


    @Component
    class BuilderFactory extends AbstractBuilderFactory<PBCustomerBootstrapData, CustomerBootstrapData>{

        @Autowired
        CustomerRepo customerRepo;

        @ObjectFactory
        PBCustomerBootstrapData.Builder build(CustomerBootstrapData in) {
            PBCustomerBootstrapData.Builder builder =  PBCustomerBootstrapData.newBuilder();
            //mapping manually because this is array[double] to List[Double] and mapstruct was having issues doing this right
            for (Double d : in.getNetUsage()){
                builder.addNetUsage(d);
            }
            return builder;
        }

        @ObjectFactory
        CustomerBootstrapData build(PBCustomerBootstrapData in){
            CustomerInfo info = customerRepo.findByName(in.getCustomerName()).get(0);
            PowerType pt = PowerType.valueOf(in.getPowerType().getLabel());
            double[] arr = in.getNetUsageList().stream().mapToDouble(d -> d).toArray(); //identity function, Java unboxes automatically to get the double value            double[] usageArr = ArrayUtils.toPrimitive(netUsageList);
            CustomerBootstrapData out = new CustomerBootstrapData(info,pt, arr);

            return builderSetId(in, out);
        }
    }
}
