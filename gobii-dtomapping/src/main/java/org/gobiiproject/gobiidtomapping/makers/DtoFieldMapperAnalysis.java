package org.gobiiproject.gobiidtomapping.makers;

import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoFieldMapperAnalysis {

    public static AnalysisDTO make(ResultSet resultSet) throws SQLException {

        AnalysisDTO returnVal = new AnalysisDTO();


        returnVal.setAnalysisId(resultSet.getInt("analysis_id"));
        returnVal.setAnalysisName(resultSet.getString("name"));
        returnVal.setAnalysisDescription(resultSet.getString("description"));
        returnVal.setAnlaysisTypeId(resultSet.getInt("type_id"));
        returnVal.setProgram(resultSet.getString("program"));
        returnVal.setProgramVersion(resultSet.getString("programversion"));
        returnVal.setAlgorithm(resultSet.getString("algorithm"));
        returnVal.setSourceName(resultSet.getString("sourcename"));
        returnVal.setSourceVersion(resultSet.getString("sourceversion"));
        returnVal.setSourceUri(resultSet.getString("sourceuri"));
        returnVal.setReferenceId(resultSet.getInt("reference_id"));
        returnVal.setTimeExecuted(resultSet.getDate("timeexecuted"));
        returnVal.setStatus(resultSet.getInt("status"));

//        resultSet.getString("parameters"));

        return returnVal;

    }
}
