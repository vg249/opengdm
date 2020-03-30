package org.gobiiproject.gobiidtomapping.instructions;


import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterProcedureDTO;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapDigesterProcedure {

    DigesterProcedureDTO createInstruction(String cropType, DigesterProcedureDTO digesterProcedureDTO) throws GobiiDtoMappingException, GobiiDaoException;
}
