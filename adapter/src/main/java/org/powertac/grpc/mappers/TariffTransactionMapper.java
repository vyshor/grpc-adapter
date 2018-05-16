package org.powertac.grpc.mappers;

import de.pascalwhoop.powertac.grpc.PBTariffTransaction;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Broker;
import org.powertac.common.CustomerInfo;
import org.powertac.common.TariffSpecification;
import org.powertac.common.TariffTransaction;
import org.powertac.common.repo.BrokerRepo;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {TariffTransactionMapper.BuilderFactory.class,
        TimeslotMapper.class,
        PowerTypeMapper.class,
        InstantMapper.class,
        BrokerMapper.class,
        CustomerInfoMapper.class,
        TariffSpecificationMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TariffTransactionMapper extends AbstractPbPtacMapper<PBTariffTransaction, TariffTransaction> {

    TariffTransactionMapper INSTANCE = Mappers.getMapper(TariffTransactionMapper.class);


    @Mappings({})
    @Override
    PBTariffTransaction.Builder map(TariffTransaction ptacObject);

    @Mappings({})
    @Override
    TariffTransaction map(PBTariffTransaction pbObject);

    //PBCustomerInfo map(CustomerInfo ptacObject);


    class BuilderFactory extends AbstractBuilderFactory<PBTariffTransaction, TariffTransaction> {

        @Autowired
        BrokerRepo repo;

        PBTariffTransaction.Builder builder() {
            return PBTariffTransaction.newBuilder();
        }

        @ObjectFactory
        TariffTransaction builder(PBTariffTransaction in) {
            Broker broker = repo.findByUsername(in.getBroker());
            TariffTransaction.Type type = TariffTransaction.Type.valueOf(in.getTxType().name());
            TariffSpecification spec = TariffSpecificationMapper.INSTANCE.map(in.getTariffSpec());
            CustomerInfo info = CustomerInfoMapper.INSTANCE.map(in.getCustomerInfo());

            TariffTransaction tariffTransaction = new TariffTransaction(broker, in.getPostedTimeslot(), type, spec, info, in.getCustomerCount(), in.getKWh(), in.getCharge(), in.getRegulation());
            return builderSetId(in, tariffTransaction);
        }
    }
}
