package org.gobiiproject.gobiimodel.dto.brapi.envelope;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.gobiiproject.gobiimodel.dto.brapi.AccessFlowDTO;

import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BrApiResultServerInfo<ServerInfoDTO> {

    private List<ServerInfoDTO> calls;

    private List<AccessFlowDTO> accessFlows;


}
