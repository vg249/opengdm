package org.gobiiproject.gobiimodel.config;

import org.simpleframework.xml.Element;
import lombok.Data;

@Data
public class RabbitMqConfig {

    /**
     * If this is not empty, the loader instructions will be sent to the queue instead of a file.
     */
    @Element(required = false)
    private String host = "localhost";

}
