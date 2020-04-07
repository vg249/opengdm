/**
 * ExperimentPatchequest.java
 * 
 * HTTP Patch Request payload class.
 * @author Rodolfo N. Duldulao, Jr.
 */

package org.gobiiproject.gobiimodel.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExperimentPatchRequest {
    @JsonSerialize(using=ToStringSerializer.class)
    private Integer projectId = null;

    private String experimentName = null;

    @JsonSerialize(using=ToStringSerializer.class)
    private Integer vendorProtocolId = null;
}