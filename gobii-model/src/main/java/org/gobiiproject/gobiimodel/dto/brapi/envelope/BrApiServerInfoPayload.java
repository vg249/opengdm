package org.gobiiproject.gobiimodel.dto.brapi.envelope;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.gobiiproject.gobiimodel.dto.brapi.AccessFlowDTO;

import java.util.List;

@SuppressWarnings("rawtypes")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrApiServerInfoPayload<ServerInfoDTO>  extends BrApiMasterPayload {

    private BrApiResultServerInfo<ServerInfoDTO> result =
            new BrApiResultServerInfo<>();

    public BrApiServerInfoPayload(List<ServerInfoDTO> calls, List<AccessFlowDTO> accessFlows) {
        this.result.setCalls(calls);
        this.result.setAccessFlows(accessFlows);
    }

    public BrApiServerInfoPayload(List<ServerInfoDTO> calls,
                                  List<AccessFlowDTO> accessFlows,
                                  Integer pageSize,
                                  Integer currentPage) {
        this.result.setCalls(calls);
        this.result.setAccessFlows(accessFlows);
        this.getMetadata().getPagination().setPageSize(pageSize);
        this.getMetadata().getPagination().setCurrentPage(currentPage);
    }

    public BrApiResultServerInfo<ServerInfoDTO> getResult() {
        return this.result;
    }

    public void setResult(BrApiResultServerInfo<ServerInfoDTO> result) {
        this.result = result;
    }

}
