package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Angel on 4/27/2016.
 */
public interface RsRoleDao {


    ResultSet getContactRoleNames() throws GobiiDaoException;
    
}
