package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.gobiiproject.gobiidao.cache.PageFrameState;
import org.gobiiproject.gobiidao.cache.PageFramesTrackingCache;
import org.gobiiproject.gobiidao.cache.PageState;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.paged.PagerSql;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class DtoListFromSqlPaged<T> implements Work {

    @Autowired
    PageFramesTrackingCache pageFramesTrackingCache;

    private Class<T> dtoType;
    private ListStatementPaged listStatementPaged;
    private Integer pageSize;
    private Integer pageNo;
    private String pgQueryId;

    private String idColAlias = "pg_item_id";
    private String nameColAlias = "pg_name_id";
    private String pageNumberColAlias = "page_number";

    public DtoListFromSqlPaged(Class<T> dtoType,
                               ListStatementPaged listStatementPaged,
                               Integer pageSize,
                               Integer pageNo,
                               String pgQueryId) {
        this.dtoType = dtoType;
        this.listStatementPaged = listStatementPaged;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.pgQueryId = pgQueryId;
    }


    List<T> dtoList = new ArrayList<>();

    public List<T> getDtoList() {
        return dtoList;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {


        Map sqlParams =  new HashMap<String, Object>() {
            {
                put(PagerSql.PARAM_NAME_PAGE_SIZE, pageSize);
                put(PagerSql.PARAM_NAME_NAME_COL_ALIAS, nameColAlias);
                put(PagerSql.PARAM_NAME_ID_COL_ALIAS, idColAlias);
                put(PagerSql.PARAM_NAME_PAGE_NUMBER_COL_ALIAS, pageNumberColAlias);
            }
        };


        PageFrameState pageFrameState = this.pageFramesTrackingCache.getPageFrames(this.pgQueryId);
        if ( pageFrameState == null ) {

            PreparedStatement preparedStatement = listStatementPaged.makePreparedStatementForPageFrames(
                    dbConnection,
                    sqlParams
            );

            ResultSet resultSet = preparedStatement.executeQuery();
            pageFrameState = new PageFrameState();
            while (resultSet.next()) {
                Integer idColVal = resultSet.getInt(this.idColAlias);
                String nameColVal = resultSet.getString(this.nameColAlias);
                Integer pageNumber = resultSet.getInt(this.pageNumberColAlias);

                PageState currentPageState = new PageState(pageNumber,nameColVal, idColVal);
                pageFrameState.getPages().add(currentPageState);
            }
        }


//
//        PreparedStatement preparedStatement = listStatementPaged.makePreparedStatement(dbConnection,
//                this.jdbcParameters,
//                this.sqlParameters);
//
//        ResultSet resultSet = preparedStatement.executeQuery();
//
//        this.dtoList = new ArrayList<>();
//        while (resultSet.next()) {
//            try {
//                T dto = dtoType.newInstance();
//                ResultColumnApplicator.applyColumnValues(resultSet, dto);
//                dtoList.add(dto);
//            } catch (IllegalAccessException e) {
//                throw new SQLException(e);
//            } catch (InstantiationException e) {
//                throw new SQLException(e);
//            }
//        }

    } // execute()
}
