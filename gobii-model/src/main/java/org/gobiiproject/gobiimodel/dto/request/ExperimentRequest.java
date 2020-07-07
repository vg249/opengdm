/**
 * ExperimentRequest.java
 * 
 * HTTP Request payload class.
 * @author Rodolfo N. Duldulao, Jr.
 */

package org.gobiiproject.gobiimodel.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Deprecated
public class ExperimentRequest {

    @Positive
    @JsonSerialize(using=ToStringSerializer.class)
    private Integer projectId;

    @NotBlank
    private String experimentName;

    @Positive
    @JsonSerialize(using=ToStringSerializer.class)
    private Integer vendorProtocolId;
}