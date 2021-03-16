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

package org.powertac.grpc;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import de.pascalwhoop.powertac.grpc.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.Instant;
import org.powertac.common.*;
import org.powertac.common.enumerations.PowerType;
import org.powertac.common.msg.*;
import org.powertac.common.repo.BrokerRepo;
import org.powertac.common.repo.TariffRepo;
import org.powertac.common.repo.TimeslotRepo;
import org.powertac.common.xml.BrokerConverter;
import org.powertac.grpc.mappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Helper class that converts all types forth and back between PB versions and powerTAC originals
 * 
 * All "old" ways to convert (i.e not using a "Mapper" based on MapStruct) should slowly be phased out, whenever a
 * method is needed!
 * They don't work! 
 */
@Service
public class GRPCTypeConverter
{

  @Autowired
  BrokerRepo brokerRepo;
  @Autowired
  TariffRepo tariffRepo;
  @Autowired
  TimeslotRepo timeslotRepo;
  Logger log = LogManager.getLogger(GRPCTypeConverter.class);

  public GRPCTypeConverter()
  {
  }

  public Timeslot convert(PBTimeslot p)
  {
    return new Timeslot(p.getSerialNumber(), convert(p.getStartInstant()));
  }

  public PBTimeslot convert(Timeslot t)
  {
    return PBTimeslot.newBuilder()
        .setSerialNumber(t.getSerialNumber())
        .setStartInstant(t.getStartInstant().getMillis())
        .build();
  }


  public PBBankTransaction convert(BankTransaction in)
  {
    return PBBankTransaction.newBuilder()
        .setId(in.getId())
        .setAmount(in.getAmount())
        .setPostedTimeslot(in.getPostedTimeslot().getSerialNumber())
        .build();
  }

  public BankTransaction convert(PBBankTransaction pbbtx)
  {
    return new BankTransaction(
        brokerRepo.findByUsername(pbbtx.getBroker()),
        pbbtx.getAmount(),
        pbbtx.getPostedTimeslot());
  }

  public Broker convert(PBBroker pbb)
  {
    return new Broker(pbb.getUsername(), pbb.getLocal(), pbb.getWholesale());
  }

  public PBBroker convert(Broker b)
  {
    return PBBroker.newBuilder()
        .setId(b.getId())
        .setCash(b.getCashBalance())
        .setKey(b.getKey())
        .setUsername(b.getUsername())
        .setPassword(b.getPassword())
        .setIdPrefix(b.getIdPrefix())
        .setWholesale(b.isWholesale())
        .setQueueName(b.toQueueName())
        .setLocal(b.isLocal())
        .setCash(b.getCashBalance())
        //.putAllMktPositions(TODO not able to get those)
        .build();
  }

  public Instant convert(long i)
  {
    return new Instant(i);
  }

  public PBCashPosition convert(CashPosition cp)
  {
    return PBCashPosition.newBuilder()
        .setId(cp.getId())
        .setBroker(new BrokerConverter().toString(cp.getBroker()))
        .setPostedTimeslot(convert(cp.getPostedTimeslot()).getSerialNumber())
        .setBalance(cp.getBalance())
        .build();
  }

  public PBDistributionReport convert(DistributionReport dr)
  {
    return PBDistributionReport.newBuilder()
        .setId(dr.getId())
        .setTimeslot(dr.getTimeslot())
        .setTotalConsumption(dr.getTotalConsumption())
        .setTotalProduction(dr.getTotalProduction())
        .build();
  }

  public PBCompetition convert(Competition comp)
  {
      return CompetitionMapper.INSTANCE.map(comp).build();
  }

  public PBCustomerInfo convert(CustomerInfo ci)
  {
    return PBCustomerInfo.newBuilder()
        .setId(ci.getId())
        .setName(ci.getName())
        .setPopulation(ci.getPopulation())
        .setPowerType(convert(ci.getPowerType()))
        .setControllableKW(ci.getControllableKW())
        .setCustomerClass(PBCustomerClass.forNumber(ci.getCustomerClass().ordinal()))
        .build();
  }

  public List<PBCustomerInfo> convert(List<CustomerInfo> cil)
  {
    LinkedList<PBCustomerInfo> l = new LinkedList<>();
    for (CustomerInfo c : cil) {
      l.add(convert(c));
    }
    return l;
  }

  public PBPowerType convert(PowerType pt)
  {
    return PBPowerType.newBuilder()
        .setLabel(pt.toString())
        .build();
  }

  //public PowerType convert(PBPowerType pbpt)
  //{
  //  return PowerType.valueOf(pbpt.getLabel());
  //}

  public PBProperties convert(Properties serverProps)
  {
    Enumeration<?> props = serverProps.propertyNames();
    PBProperties.Builder builder = PBProperties.newBuilder();
    while (props.hasMoreElements()) {
      String key = (String) props.nextElement();
      builder.putValues(key, serverProps.getProperty(key));
    }
    return builder.build();

  }

  public PBMarketBootstrapData convert(MarketBootstrapData in)
  {
      return MarketBootstrapDataMapper.INSTANCE.map(in).build();
  }

  public PBBalancingTransaction convert(BalancingTransaction tx)
  {
    return BalancingTransactionMapper.INSTANCE.map(tx).build();

//    return PBBalancingTransaction.newBuilder()
//        .setId(tx.getId())
//        .setKWh(tx.getKWh())
//        .setCharge(tx.getCharge())
//        .setBroker(tx.getBroker().getUsername())
//        .setPostedTimeslot(tx.getPostedTimeslotIndex())
//        .build();
  }

  public PBClearedTrade convert(ClearedTrade ct)
  {
    return ClearedTradeMapper.INSTANCE.map(ct).build();
   // return PBClearedTrade.newBuilder()
   //     .setId(ct.getId())
   //     .setDateExecuted(ct.getDateExecuted().getMillis())
   //     .setExecutionMWh(ct.getExecutionMWh())
   //     .setExecutionPrice(ct.getExecutionPrice())
   //     .build();
  }

  public PBDistributionTransaction convert(DistributionTransaction in)
  {
    return PBDistributionTransaction.newBuilder()
        .setId(in.getId())
        .setBroker(in.getBroker().getUsername())
        .setCharge(in.getCharge())
        .setKWh(in.getKWh())
        .setNLarge(in.getNLarge())
        .setNSmall(in.getNSmall())
        .build();
  }

  public PBCapacityTransaction convert(CapacityTransaction in)
  {
    return PBCapacityTransaction.newBuilder()
        .setId(in.getId())
        .setBroker(in.getBroker().getUsername())
        .setCharge(in.getCharge())
        .setKWh(in.getKWh())
        .setPeakTimeslot(in.getPeakTimeslot())
        .setThreshold(in.getThreshold())
        .build();
  }

  public PBMarketPosition convert(MarketPosition in)
  {
    return MarketPositionMapper.INSTANCE.map(in).build();
//    return PBMarketPosition.newBuilder()
//        .setId(in.getId())
//        .setBroker(in.getBroker().getUsername())
//        .setOverallBalance(in.getOverallBalance())
//        .setTimeslot(in.getTimeslotIndex())
//        .build();

  }

  public PBMarketTransaction convert(MarketTransaction in)
  {
    return MarketTransactionMapper.INSTANCE.map(in).build();
//    return PBMarketTransaction.newBuilder()
//        .setId(in.getId())
//        .setBroker(in.getBroker().getUsername())
//        .setMWh(in.getMWh())
//        .setPrice(in.getPrice())
//        .setTimeslot(in.getTimeslotIndex())
//        .build();

  }

  public PBOrderbook convert(Orderbook in)
  {
    return OrderbookMapper.INSTANCE.map(in).build();
//    return PBOrderbook.newBuilder()
//        .setId(in.getId())
//        .addAllAsks(convert(in.getAsks()))
//        .addAllBids(convert(in.getBids()))
//        .setTimeslot(in.getTimeslotIndex())
//        .setClearingPrice(in.getClearingPrice())
//        .setDateExecuted(in.getDateExecuted().getMillis())
//        .build();
  }

  private Iterable<? extends PBOrderbookOrder> convert(SortedSet<OrderbookOrder> asks)
  {
    LinkedList<PBOrderbookOrder> list = new LinkedList<>();
    for (OrderbookOrder ask : asks) {
      list.add(convert(ask));
    }
    return list;
  }

  private PBOrderbookOrder convert(OrderbookOrder in)
  {
    return PBOrderbookOrder.newBuilder()
        .setId(in.getId())
        .setLimitPrice(in.getLimitPrice())
        .setMWh(in.getMWh())
        .build();
  }

  public PBWeatherForecast convert(WeatherForecast in)
  {
    return basicConversionToPB(in, PBWeatherForecast.newBuilder())
        .setId(in.getId())
        .setCurrentTimeslot(in.getTimeslotIndex())
        .addAllPredictions(listConvert(in.getPredictions(), WeatherForecastPrediction.class, PBWeatherForecastPrediction.class))
        .build();

  }

  public PBWeatherForecastPrediction convert(WeatherForecastPrediction in){
    return basicConversionToPB(in, PBWeatherForecastPrediction.newBuilder())
        .build();


  }

 // private Iterable<? extends PBWeatherForecastPrediction> convert(List<WeatherForecastPrediction> predictions)
 // {
 //   LinkedList<PBWeatherForecastPrediction> list = new LinkedList<>();
 //   for (WeatherForecastPrediction prediction :
 //       predictions) {
 //     PBWeatherForecastPrediction pbPred = basicConversionToPB(prediction, PBWeatherForecastPrediction.newBuilder())
 //         .build();
 //     list.add(pbPred);
 //   }
 //   return list;
 // }

  public PBWeatherReport convert(WeatherReport in)
  {
    return basicConversionToPB(in, PBWeatherReport.newBuilder())
        .setCurrentTimeslot(convert(in.getCurrentTimeslot()))
        .build();
  }

  public PBBalanceReport convert(BalanceReport in)
  {
    return BalanceReportMapper.INSTANCE.map(in).build();
    //return basicConversionToPB(in, PBBalanceReport.newBuilder()).build();
  }

  public PBCustomerBootstrapData convert(CustomerBootstrapData in)
  {
      return CustomerBootstrapDataMapper.INSTANCE.map(in).build();
  }

  public PBRate convert(Rate in){
    return basicConversionToPB(in, PBRate.newBuilder())
        .build();
  }

  public PBSimPause convert(SimPause in)
  {
    return basicConversionToPB(in, PBSimPause.newBuilder())
        .build();
  }
  public PBSimResume convert(SimResume in)
  {
    return basicConversionToPB(in, PBSimResume.newBuilder())
        .setStart(in.getStart().getMillis())
        .build();
  }
  public PBTimeslotComplete convert(TimeslotComplete in)
  {
    return basicConversionToPB(in, PBTimeslotComplete.newBuilder())
        .build();
  }

  public PBTimeslotUpdate convert(TimeslotUpdate in)
  {
    return TimeslotUpdateMapper.INSTANCE.map(in).build();
  }

  public PBTariffSpecification convert(TariffSpecification in)
  {
    return TariffSpecificationMapper.INSTANCE.map(in).build();
  }

  public PBTariffRevoke convert(TariffRevoke in){
    return TariffRevokeMapper.INSTANCE.map(in).build();
  }

  public PBTariffTransaction convert(TariffTransaction in){return TariffTransactionMapper.INSTANCE.map(in).build();}

  /**
   * Generates a list of
   * @param inputList
   * @param outputClass
   * @param <I>
   * @param <O>
   * @return
   */
  protected <I,O extends GeneratedMessageV3> Iterable<O> listConvert(List<I> inputList, Class<I> inputClass, Class<O> outputClass)
  {
    LinkedList<O> list = new LinkedList<>();
    for (I inputItem :
        inputList) {

      //TODO casting is ugly. Can we avoid it?
      O outputItem = (O) reflectionConvertCall(inputItem, inputClass, outputClass);
      list.add(outputItem);
    }
    return list;
  }


  protected <I, O> O reflectionConvertCall(Object input, Class<I> i, Class<O> o)
  {
    Class[] argClasses = {i};
    Object[] args = {input};
    try {
      Method method = this.getClass().getMethod("convert", argClasses);
      return (O) method.invoke(this, args);
    }
    catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      log.error(e);
    }
    return null;
  }

  public PBOrder convert(Order msg)
  {
    return OrderMapper.INSTANCE.map(msg).build();
  }


  public PBSimEnd convert(SimEnd msg) {
      return SimEndMapper.INSTANCE.map(msg).build();
  }


//    public  PBPowerType convert(PowerType pt){
//        //TODO using reflection here, dirty trick, there must be a better way to get this info
//        try {
//            Field f = pt.getClass().getDeclaredField("label");
//            f.setAccessible(true);
//            int type = ((TypeLabel)f.get(pt)).ordinal();
//            return PBPowerType.forNumber(type);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

  //Copy of TypeLabel from private field in PowerType.java. MUST bee same
  private enum TypeLabel
  {
    CONSUMPTION,
    PRODUCTION,
    STORAGE,
    INTERRUPTIBLE_CONSUMPTION,
    THERMAL_STORAGE_CONSUMPTION,
    SOLAR_PRODUCTION, WIND_PRODUCTION,
    RUN_OF_RIVER_PRODUCTION,
    PUMPED_STORAGE_PRODUCTION,
    CHP_PRODUCTION,
    FOSSIL_PRODUCTION,
    BATTERY_STORAGE,
    ELECTRIC_VEHICLE
  }

  private List<Double> arrayToList(double[] doubles)
  {
    LinkedList<Double> list = new LinkedList<>();
    for (double aDouble : doubles) {
      list.add(aDouble);
    }
    return list;
  }

  private List<Integer> arrayToList(int[] vals)
  {
    LinkedList<Integer> list = new LinkedList<>();
    for (int v : vals) {
      list.add(v);
    }
    return list;
  }

  /**
   * provides a base conversion helper that converts any basic types of an object into that of a PB version Complete
   * the object conversion afterwards, as this only covers the basics
   *
   * @return
   */
  protected <T extends GeneratedMessageV3.Builder<T>> T basicConversionToPB(Object in, T builder)
  {
    Map<String, String> props = null;
    try {
      props = BeanUtils.describe(in);
    }
    catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      log.error("error with bean util describe");
      log.error(in.getClass().getName());
    }

    for (Map.Entry<String, String> next : props.entrySet()) {
      Descriptors.FieldDescriptor fieldByName = builder.getDescriptorForType().findFieldByName(next.getKey());
      //parsing all different types from string
      String value = next.getValue();
      if (fieldByName == null) continue;
      if ((in instanceof WeatherForecast) && (next.getKey().equals("currentTimeslot"))) continue;
      try {
        if(fieldByName.isRepeated()){
          //skipping repeated fields right away
          break;
        }
        switch (fieldByName.getType()) {
          case DOUBLE:
            builder.setField(fieldByName, Double.parseDouble(value));
            break;
          case FLOAT:
            builder.setField(fieldByName, Float.parseFloat(value));
            break;
          case INT64:
            builder.setField(fieldByName, Long.parseLong(value));
            break;
          case UINT64:
            builder.setField(fieldByName, Long.parseLong(value));
            break;
          case INT32:
            builder.setField(fieldByName, Integer.parseInt(value));
            break;
          case FIXED64:
            builder.setField(fieldByName, Long.parseLong(value));
            break;
          case FIXED32:
            builder.setField(fieldByName, Integer.parseInt(value));
            break;
          case BOOL:
            builder.setField(fieldByName, Boolean.parseBoolean(value));
            break;
          case STRING:
            builder.setField(fieldByName, value);
            break;
          case GROUP:
            //TODO is message
            break;
          case MESSAGE:
            //TODO is message
            break;
          case BYTES:
            builder.setField(fieldByName, value.getBytes());
            break;
          case UINT32:
            builder.setField(fieldByName, Integer.parseInt(value));
            break;
          case ENUM:
            //TODO check
            builder.setField(fieldByName, Integer.parseInt(value));
            break;
          case SFIXED32:
            builder.setField(fieldByName, Integer.parseInt(value));
            break;
          case SFIXED64:
            builder.setField(fieldByName, Long.parseLong(value));
            break;
          case SINT32:
            builder.setField(fieldByName, Integer.parseInt(value));
            break;
          case SINT64:
            builder.setField(fieldByName, Long.parseLong(value));
            break;
        }
      }
      catch (Exception e) {
        log.error(String.format("Conversion error for %1$s -- %2$s -- %3$s", value, in.getClass().getName(), fieldByName));
        log.error(e);
      }

    }

    return builder;
  }


  // protected   <T> T basicConversionFromPB(Class<T> type, GeneratedMessageV3 in, T out ) {
  //     Map<Descriptors.FieldDescriptor, Object> fields = in.getAllFields();
  //     for (Map.Entry<Descriptors.FieldDescriptor, Object> next : fields.entrySet()) {
  //         String propertyName = next.getKey().getFullName();
  //         BeanUtils.
  //     }
  // }

  protected <T> T copyProperties(Class<T> outType, GeneratedMessageV3 in, T out)
  {
    try {
      BeanUtils.copyProperties(in, out);
    }
    catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return out;
  }
}

