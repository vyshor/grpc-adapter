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


import org.mapstruct.ap.spi.DefaultAccessorNamingStrategy;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class ProtobufAccessorNamingStrategy extends DefaultAccessorNamingStrategy
{

  public static final String PROTOBUF_GENERATED_MESSAGE_V3 = "com.google.protobuf.GeneratedMessageV3";
  //public static final String LIST_SUFFIX = "List";
  public static final String LIST_SUFFIX = "";

  @Override
  public String getElementName(ExecutableElement adderMethod)
  {

    String methodName = super.getElementName(adderMethod);
    Element receiver = adderMethod.getEnclosingElement();
    if (receiver != null && receiver.getKind() == ElementKind.CLASS) {
      TypeElement type = (TypeElement) receiver;
      TypeMirror superType = type.getSuperclass();
      if (superType != null && PROTOBUF_GENERATED_MESSAGE_V3.equals(superType.toString())) {
        methodName += LIST_SUFFIX;
      }
    }
    return methodName;
  }

}