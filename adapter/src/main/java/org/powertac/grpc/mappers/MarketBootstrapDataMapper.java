package org.powertac.grpc.mappers;


import de.pascalwhoop.powertac.grpc.PBMarketBootstrapData;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.powertac.common.Broker;
import org.powertac.common.msg.MarketBootstrapData;

@Mapper(uses = {MarketBootstrapDataMapper.BuilderFactory.class},
		collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface MarketBootstrapDataMapper
{

	MarketBootstrapDataMapper INSTANCE = Mappers.getMapper(MarketBootstrapDataMapper.class);


	@Mappings({})
	PBMarketBootstrapData.Builder map(MarketBootstrapData ptacObject);

	@Mappings({})
	MarketBootstrapData map(PBMarketBootstrapData pbObject);


	class BuilderFactory
	{
		PBMarketBootstrapData.Builder builder()
		{
			return PBMarketBootstrapData.newBuilder();
		}

		@ObjectFactory
		MarketBootstrapData builder(PBMarketBootstrapData in)
		{
			double[] mWh = in.getMwhList().stream().mapToDouble(d -> d).toArray(); //identity function, Java unboxes automatically to get the double value
			double[] price = in.getMarketPriceList().stream().mapToDouble(d -> d).toArray();
			return new MarketBootstrapData(mWh, price);
		}
	}
}
