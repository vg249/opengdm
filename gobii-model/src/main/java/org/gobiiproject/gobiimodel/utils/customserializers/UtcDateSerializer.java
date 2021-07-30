package org.gobiiproject.gobiimodel.utils.customserializers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Helper class for Jackson Json serializer for Date objects.
 * Sets time zone as UTC and
 * Serializes Timestamp as Date String in "yyyy-MM-dd'T'HH:mm:ssZ" format.
 */
@SuppressWarnings("serial")
public class UtcDateSerializer extends StdSerializer<Date> {

    private SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'");

    public UtcDateSerializer() {
        this(null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public UtcDateSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize(Date dateValue, JsonGenerator generator,
                          SerializerProvider provider) throws IOException, JsonProcessingException {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        generator.writeString(formatter.format(dateValue));
    }

}
