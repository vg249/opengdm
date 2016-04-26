package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public interface RsCvDao {

    ResultSet getCvTermsByGroup(String groupName) throws GobiiDaoException;

	ResultSet getCvGroups() throws GobiiDaoException;


}
