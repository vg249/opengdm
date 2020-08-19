package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Protocol;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

public class ProtocolDTO extends DTOBaseAuditable {

    public ProtocolDTO() {
        super(GobiiEntityNameType.PROTOCOL);
    }

    @GobiiEntityMap(paramName = "protocolId", entity = Protocol.class, ignoreOnDtoToEntity = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer protocolId;

    @GobiiEntityMap(paramName = "name", entity = Protocol.class)
    private String protocolName;

    @GobiiEntityMap(paramName = "description", entity = Protocol.class)
    private String protocolDescription;

    @GobiiEntityMap(paramName = "platform.platformId", entity = Protocol.class, deep = true)
    private Integer platformId;

    @GobiiEntityMap(paramName = "status.term", entity = Protocol.class, deep = true)
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
