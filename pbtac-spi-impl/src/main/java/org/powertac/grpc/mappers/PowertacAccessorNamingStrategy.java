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
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.beans.Introspector;

/**
 * class that helps MapStruct find all the right accessors. There are some patterns used in our project, one being the 'withX' builder pattern
 */
public class PowertacAccessorNamingStrategy extends DefaultAccessorNamingStrategy
{
  public static final String PROTOBUF_GENERATED_MESSAGE_V3 = "com.google.protobuf.GeneratedMessageV3";
  // this is due to the getter in protobufs being called 'get<repeatedFieldName>List'
  public static final String PROTOBUF_LIST_SUFFIX = "List";

  //@Override
  //public boolean isGetterMethod(ExecutableElement method)
  //{
  //  if (super.isGetterMethod(method)) {
  //    return true;
  //  }
  //  String methodName = method.getSimpleName().toString();
  //  return !methodName.startsWith("with") && method.getReturnType().getKind() != TypeKind.VOID;
  //}

  //@Override
  //public boolean isSetterMethod(ExecutableElement method)
  //{
  //  if (super.isSetterMethod(method)) {
  //    return true;
  //  }
  //  String methodName = method.getSimpleName().toString();
  //  return isUpdateWith(method) || (methodName.startsWith("with") && methodName.length() > 4);
  //}

  @Override
  public String getPropertyName(ExecutableElement getterOrSetterMethod)
  {
    String propertyName;
    String methodName = getterOrSetterMethod.getSimpleName().toString();
    if (methodName.startsWith("update")) {
      propertyName = methodName.substring(6);
    }
    else if (methodName.startsWith("with")) {
      propertyName = methodName.substring(4);
    }
    else {
      propertyName = super.getPropertyName(getterOrSetterMethod);
    }
    return Introspector.decapitalize(propertyName);
  }

  /**
   * written for the Broker "updateCash" which technically is not a setter but a adder. Since we start with empty objects though, it's ok because 0+x is the same as setting it to x
   *
   * @param method
   * @return
   */
  public boolean isUpdateWith(ExecutableElement method)
  {
    String methodName = method.getSimpleName().toString();
    return methodName.startsWith("update") && methodName.length() > 6;
  }


  @Override
  /**
   * mostly specific for Protobuf
   */
  public String getElementName(ExecutableElement adderMethod)
  {

    String methodName = super.getElementName(adderMethod);
    Element receiver = adderMethod.getEnclosingElement();
    if (receiver != null && receiver.getKind() == ElementKind.CLASS) {
      TypeElement type = (TypeElement) receiver;
      TypeMirror superType = type.getSuperclass();
      if (superType != null && PROTOBUF_GENERATED_MESSAGE_V3.equals(superType.toString())) {
        methodName += PROTOBUF_LIST_SUFFIX;
      }
    }
    return methodName;
  }
}
