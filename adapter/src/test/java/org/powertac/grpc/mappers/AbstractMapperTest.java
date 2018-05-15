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

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.junit.Test;
import org.powertac.common.IdGenerator;
import org.powertac.common.XMLMessageConverter;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public abstract class AbstractMapperTest<P extends Message, T, M extends AbstractPbPtacMapper<P, T>> {

    public void before() {
        IdGenerator.recycle();
    }

    XMLMessageConverter converter = new XMLMessageConverter();
    T ptac;
    M mapper;

    AbstractMapperTest() {
        converter.afterPropertiesSet();
    }


    public T copyByXml(T obj) {
        String xml = converter.toXML(obj);
        return (T) converter.fromXML(xml);
    }


    @Test
    public void roundtripJsonCompare() throws InvalidProtocolBufferException {
        //create xml from original object
        String inXml = converter.toXML(ptac);
        // map once to protobuf and back
        P pbObject = (P) mapper.map(ptac).build();
        T roundtripObject = mapper.map(pbObject);
        //create new xml from roundtrip object
        //expect both xml strings to be identical (because the data shouldn't change)
        String outXml = converter.toXML(roundtripObject);

        //remove all IDs, because they are always changing due to the nature of the ID generator
        Pattern ids = Pattern.compile("id=\"[0-9]+\"");
        inXml = ids.matcher(inXml).replaceAll("id=\"X\"");
        outXml = ids.matcher(outXml).replaceAll("id=\"X\"");
        assertEquals(inXml, outXml);

    }
}
