package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.GobiiCropDbConfig;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.*;
import java.util.Properties;

/**
 * Created by jdl232 on 6/21/2017.
 */
public class SimplePostgresConnector {

    public SimplePostgresConnector(GobiiCropDbConfig config){
        this.dbConn=getDataSource(config);
    }

    private Connection dbConn=null;
    public static Connection getDataSource(GobiiCropDbConfig config){
        Connection conn = null;
        try{
            conn=DriverManager.getConnection(config.getConnectionString(),config.getUserName(),config.getPassword());
        }catch(SQLException e){
            ErrorLogger.logError("SimplePostgresConnector","Failed creating postgres connection",e);
        }
        return conn;
    }

    /**
     * Use if you want a query that returns true on any result, or false on empty result.
     * @param query Query to execute, as a string
     * @return True or False
     * @throws SQLException Often
     */
    public boolean boolQuery(String query) throws SQLException{
        Statement s = dbConn.createStatement();
        boolean ret = s.execute(query);
        s.close();
        return ret;
    }

    /**
     * Calls boolQuery on 'Select 1 from {TABLE} where {ENTITY} = {NAME} LIMIT 1
     * returns true if there were any results
     * @param table table name
     * @param entity column name in the table
     * @param name object's identifier in the column named in entity
     * @return True or False
     * @throws SQLException Often
     */
    public boolean hasEntry(String table, String entity, String name) throws SQLException{
        String statement="SELECT 1 from "+table+" WHERE "+entity+" = '"+ name + "' LIMIT 1";
        return boolQuery(statement);
    }
    public boolean hasMarker(String markerName) throws SQLException{
        return hasEntry("marker","name",markerName);
    }

    public boolean hasCVEntry(String cvGroupName, String cvName) throws SQLException{
        String statement="SELECT 1 from cv join cvgroup b on cv.cvgroup_id = b.cvgroup_id where b.name = '"+cvGroupName+"' and cv.term = '"+cvName+"' LIMIT 1";
        return boolQuery(statement);
    }
    public boolean hasGermplasmType(String germplasmType) throws SQLException{
        return hasCVEntry("germplasm_type",germplasmType);
    }
    public boolean hasGermplasmSpecies(String germplasmSpecies) throws SQLException{
        return hasCVEntry("germplasm_species",germplasmSpecies);
    }

    public boolean close(){
        try {
            dbConn.close();
        } catch (SQLException e) {
            ErrorLogger.logError("SimplePostgresConnector","Error closing",e);
            return false;
        }
        dbConn=null;
        return true;
    }

}
