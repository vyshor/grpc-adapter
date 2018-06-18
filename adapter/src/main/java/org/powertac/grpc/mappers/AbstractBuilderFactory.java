package org.powertac.grpc.mappers;

import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.powertac.common.IdGenerator;
import org.powertac.grpc.GrpcServiceChannel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

/**
 * This abstract class serves for any builderFactory in the MapStruct environment to properly set the id property of the powerTac fields.
 * The reason for this is that the ID cannot be set using the setters of the entities and must therefore be injected through reflection
 * @param <P>
 * @param <T>
 */
public abstract class AbstractBuilderFactory<P extends Message, T> {
    static private Logger log = LogManager.getLogger(AbstractBuilderFactory.class);

    T builderSetId(P in, T out){
        Descriptors.FieldDescriptor idDescriptor = null;
        for ( Descriptors.FieldDescriptor fd : in.getAllFields().keySet()){
           if(fd.toProto().getName().equals("id")){
              idDescriptor = fd;
              break;
           }
        }
        if(idDescriptor == null){
            warnAboutNotHavingIdField();
            return out;
        }

        try {
            long id = (long) in.getField(idDescriptor);
            id = ensureId(id);
            //FieldUtils.writeDeclaredField(out, "id", id);
            Field idField = getIdField(out.getClass());
            if (idField == null){
                warnAboutNotHavingIdField();
                return out;
            }
            idField.setAccessible(true);
            idField.setLong(out, id);
        } catch (Exception e) {
            try {
                log.error("error while converting id field through reflection for " + JsonFormat.printer().print(in));
            } catch (InvalidProtocolBufferException e1) {
                log.error("error while converting id field through reflection and cannot print protobuf object");
            }
        }
        return out;
    }

    /**
     * if the id is 0
     * @param id
     * @return
     */
    private long ensureId(long id) {
        return id == 0 ? IdGenerator.createId() : id;
    }

    private void warnAboutNotHavingIdField() {
        log.warn("tried to set an id field on an object that does not have an id field");
    }

    Field getIdField(Class in){
        Field fields[] = in.getDeclaredFields();
        if(fields.length == 0){
            return null;
        }else{
           for(Field f : fields){
               if(f.getName().equals("id")){
                   return f;
               }
           }
           //recursive call to walk up the inheritance chain
           return getIdField(in.getSuperclass());
        }
    }

}
