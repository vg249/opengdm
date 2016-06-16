package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;


/**
 * Created by Phil on 5/13/2016.
 */
public class Authenticator {


    public static boolean authenticate(GobiiCropType gobiiCropType) throws Exception {

        // clear the current context so that we start from scratch populating server configs
        //ClientContext.getInstance(null, false).resetConfiguration();

        // in a real client, the user would supply domain and port at startup
        // the URL we are constructing here should look like what the
        // end-user will specify (for example, http://biotech.cornell.edu:8080/gobii-rice)
        // we will assume that the port number is also specified in the URL
        // the url _must_ include the protocol and port and context path -- everything
        // as if you were navigating to that path in a web browser
        ConfigSettings configSettings = new ConfigSettings();
        Integer port = configSettings.getCropConfig(gobiiCropType).getServicePort();
        String gobiiUrl = configSettings.getCropConfig(gobiiCropType).getServiceDomain()
                + ":"
                + port.toString()
                + "/"
                + configSettings.getCropConfig(gobiiCropType).getServiceAppRoot();


        ClientContext.getInstance(null, false).setCurrentClientCrop(gobiiCropType);
        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());


        return ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword());
    }

    public static boolean authenticate() throws Exception {

        // in a real client, the user would supply domain and port at startup

        // clear the current context so that we start from scratch populating server configs
        ClientContext.resetConfiguration();

        ConfigSettings configSettings = new ConfigSettings();
        Integer port = configSettings.getCropConfig(configSettings.getCurrentGobiiCropType()).getServicePort();
        String gobiiUrl = "http://"
                + configSettings.getCropConfig(configSettings.getCurrentGobiiCropType()).getServiceDomain()
                + ":"
                + port.toString()
                + "/"
                + configSettings.getCropConfig(configSettings.getCurrentGobiiCropType()).getServiceAppRoot();

        GobiiCropType gobiiCropTypeDefault = ClientContext.getInstance(gobiiUrl,true).getDefaultCropType();
        return Authenticator.authenticate(gobiiCropTypeDefault);
    }

    // not implemented yet
    public static boolean deAuthenticate() throws Exception {
        return true;
    }
}
