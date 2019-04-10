package com.kute.webflux.config.converter;

import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import org.bson.BsonTimestamp;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * created by bailong001 on 2019/04/10 12:15
 * <p>
 * mongo 扩展converter，虽然已经内置了很多converter，具体见 MongoCustomConversions
 */
@Component
public class MongoTypeToJavaTypeExtentionConverter implements ConditionalGenericConverter {

    /**
     * 有条件的convert
     *
     * @param sourceType
     * @param targetType
     * @return
     */
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return true;
    }

    /**
     * convert pair
     *
     * @return
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Sets.newHashSet(
                new ConvertiblePair(org.bson.BsonTimestamp.class, java.sql.Timestamp.class),
                new ConvertiblePair(java.lang.String.class, java.sql.Timestamp.class),
                new ConvertiblePair(java.util.Date.class, java.sql.Timestamp.class)
//                new ConvertiblePair(java.sql.Timestamp.class, org.bson.BsonTimestamp.class)
        );
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }

        if (sourceType.getType() == BsonTimestamp.class) {
            return new Timestamp(((BsonTimestamp) source).getValue());
        } else if(sourceType.getType() == String.class && Longs.tryParse((String)source) != null) {
            return new Timestamp(Longs.tryParse((String)source));
        } else if (sourceType.getType() == java.util.Date.class) {
            return new Timestamp(((Date)source).getTime());
        }
        return source;
    }

}
