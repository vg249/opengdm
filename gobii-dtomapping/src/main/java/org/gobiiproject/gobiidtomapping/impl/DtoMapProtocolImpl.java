package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsProtocolDao;
import org.gobiiproject.gobiidao.resultset.access.RsVendorProtocolDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapProtocol;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ProtocolDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.Map;
/**
 * Created by VCalaminos on 2016-12-14.
 */
public class DtoMapProtocolImpl implements DtoMapProtocol {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapProtocolImpl.class);

    @Autowired
    private RsProtocolDao rsProtocolDao;

    @Autowired
    private RsVendorProtocolDao rsVendorProtocolDao;

    @Override
    public ProtocolDTO createProtocol(ProtocolDTO protocolDTO) throws GobiiDtoMappingException {

        ProtocolDTO returnVal = protocolDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            Integer protocolId = rsProtocolDao.createProtocol(parameters);
            returnVal.setProtocolId(protocolId);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw e;
        }

        return returnVal;
    }

    @Override
    public ProtocolDTO replaceProtocol(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDtoMappingException {

        ProtocolDTO returnVal = protocolDTO;

        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("protocolId", protocolId);
        rsProtocolDao.updateProtocol(parameters);

        return returnVal;
    }

    @Transactional
    @Override
    public ProtocolDTO getProtocolDetails(Integer protocolId) throws Exception {

        ProtocolDTO returnVal = new ProtocolDTO();

        try {

            ResultSet resultSet = rsProtocolDao.getProtocolDetailsByProtocolId(protocolId);

            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw  e;
        }

        return returnVal;
    }

}
