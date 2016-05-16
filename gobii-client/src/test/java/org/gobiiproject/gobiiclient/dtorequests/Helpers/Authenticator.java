package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;


/**
 * Created by Phil on 5/13/2016.
 */
public class Authenticator {

    public static boolean authenticate() throws Exception {
        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        return ClientContext.getInstance().login(userDetail.getUserName(), userDetail.getPassword());
    }

    // not implemented yet
    public static boolean deAuthenticate() throws  Exception {
        return true;
    }
}
