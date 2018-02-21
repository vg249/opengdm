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
import java.util.stream.Collectors;

/**
 */
public class ResultSetFromSqlPaged<T> implements Work {

    @Autowired
    PageFramesTrackingCache pageFramesTrackingCache;

    private ListStatementPaged listStatementPaged;
    private Integer pageSize;
    private Integer pageNo;
    private String pgQueryId;
    private PageFrameState pageFrameState;

    public ResultSetFromSqlPaged(ListStatementPaged listStatementPaged,
                                 Integer pageSize,
                                 Integer pageNo,
                                 String pgQueryId) {
        this.listStatementPaged = listStatementPaged;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.pgQueryId = pgQueryId;
    }


    private ResultSet resultSet;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public PageFrameState getPageFrameState() {
        return pageFrameState;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {


        this.pageFrameState = this.pageFramesTrackingCache.getPageFrames(this.pgQueryId);
        if ( this.pageFrameState == null ) {

            PreparedStatement preparedStatement = listStatementPaged.makePreparedStatementForPageFrames(
                    dbConnection,
                    pageSize
            );

            ResultSet resultSet = preparedStatement.executeQuery();
            this.pageFrameState = new PageFrameState(this.pageSize);
            while (resultSet.next()) {
                Integer idColVal = resultSet.getInt(listStatementPaged.getIdColName());
                String nameColVal = resultSet.getString(listStatementPaged.getNameColName());
                Integer pageNumber = resultSet.getInt(listStatementPaged.getPageNumberColName());

                PageState currentPageState = new PageState(pageNumber,nameColVal, idColVal);
                this.pageFrameState.getPages().add(currentPageState);
            }

            this.pageFramesTrackingCache.setPageFrames(this.pgQueryId,pageFrameState);
        }

        String nameColVal = null;
        Integer idColVal = null;

        if( this.pageNo > 1 ) {

            List<PageState> pageStatesForPage
                    = this.pageFrameState.pages.stream()
                    .filter( ps -> ps.getPageNumber()
                            .equals(this.pageNo))
                    .collect(Collectors.toList());



            if( pageStatesForPage.size() == 1) {
                nameColVal = pageStatesForPage.get(0).getNameValue();
                idColVal = pageStatesForPage.get(0).getIdValue();
            } else {
                // Cannot decide whether it's better to throw in the case where we got a non existent page number
                Integer idxOfLast = this.pageFrameState.getPages().size() -1 ;
                nameColVal = this.pageFrameState.getPages().get(idxOfLast).getNameValue();
                idColVal = this.pageFrameState.getPages().get(idxOfLast).getIdValue();
            }
        }


        PreparedStatement preparedStatement = listStatementPaged.makePreparedStatementForAPage(dbConnection,
                this.pageSize,
                nameColVal,
                idColVal);

        this.resultSet = preparedStatement.executeQuery();


    } // execute()
}
