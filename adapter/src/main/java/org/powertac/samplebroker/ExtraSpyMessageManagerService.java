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

package org.powertac.samplebroker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.powertac.common.Order;
import org.powertac.grpc.GrpcServiceChannel;
import org.powertac.samplebroker.interfaces.BrokerContext;
import org.powertac.samplebroker.interfaces.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtraSpyMessageManagerService implements Initializable
{
  static private Logger log = LogManager.getLogger(ContextManagerService.class);
  @Autowired
  GrpcServiceChannel comm;

  @Override
  public void initialize(BrokerContext broker)
  {

  }

  //orders from other brokers
  public synchronized void handleMessage(Order msg){
    log.info("received spy messages for order");
    comm.spyStub.handlePBOrder(comm.converter.convert(msg));
  }


}
