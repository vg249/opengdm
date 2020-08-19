package org.gobiiproject.gobiimodel.dto.gdmv3;

import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

public class ProtocolDTO extends DTOBaseAuditable {

    public ProtocolDTO() {
        super(GobiiEntityNameType.PROTOCOL);
    }

    private Integer protocolId;

    private String protocolName;

    private String protocolDescription;

    private Integer platformId;

    private String status;


    @Override
    public Integer getId() {
        return protocolId;
    }

    @Override
    public void setId(Integer id) {
        this.protocolId = id;
    }

    public Integer getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Integer protocolId) {
        this.protocolId = protocolId;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolDescription() {
        return protocolDescription;
    }

    public void setProtocolDescription(String protocolDescription) {
        this.protocolDescription = protocolDescription;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
