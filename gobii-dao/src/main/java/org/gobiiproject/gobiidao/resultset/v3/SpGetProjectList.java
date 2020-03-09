/**
 * RsGetProjectList.java
 * 
 * V3 API Get project list 
 */
package org.gobiiproject.gobiidao.resultset.v3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.jdbc.Work;

public class SpGetProjectList implements Work {
    private final static Map<String, Object> DEFAULT_PARAMETERS;
    
    static {
        HashMap<String, Object> defaultParams = new HashMap<String, Object>();
        defaultParams.put("pageNum", 0);
        defaultParams.put("pageSize", 1000);
        DEFAULT_PARAMETERS = defaultParams;
    }
    private Map<String, Object> parameters = null;

    public SpGetProjectList(Map<String, Object> parameters) {
        this.parameters = DEFAULT_PARAMETERS;

        if (parameters != null) {
            this.parameters.putAll(parameters);
        }

    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        // TODO: Add the left join for Markers
        // TODO: Use separate queries?
        Integer offset = (Integer) this.parameters.get("pageNum");
        Integer limit = (Integer) this.parameters.get("pageSize");
        String sql = 
            "SELECT project.*, N1.experimentCount, N2.datasetCount, N4.dnaRunsCount " + 
            "FROM project " + 
            "LEFT JOIN " + 
            "(SELECT project_id, COUNT(experiment_id) as experimentCount " +
            " FROM experiment GROUP BY project_id ) N1"+
            " ON N1.project_id=project.project_id " +
            "LEFT JOIN " +
            "(SELECT project.project_id, COUNT(dataset.dataset_id) as datasetCount " +
            " FROM project, experiment, dataset" +
            " WHERE project.project_id=experiment.project_id" +
            "   AND dataset.experiment_id=experiment.experiment_id " +
            " GROUP BY project.project_id) N2 " +
            " ON N2.project_id = project.project_id " +
            "LEFT JOIN " +    //TODO: add MArker left join here
            "(SELECT project.project_id, COUNT(dnarun.dnarun_id) as dnaRunsCount " +
            " FROM project, experiment, dnarun" +
            " WHERE project.project_id=experiment.project_id " +
            "   AND experiment.experiment_id=dnarun.experiment_id " +
            " GROUP BY project.project_id ) N4 " +
            " ON N4.project_id=project.project_id " + 
            " OFFSET ? " + 
            " LIMIT ? ";
        
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, offset);
        ps.setInt(2, limit);

        resultSet = ps.executeQuery();
        
    }
}