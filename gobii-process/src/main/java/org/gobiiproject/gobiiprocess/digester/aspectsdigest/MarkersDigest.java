package org.gobiiproject.gobiiprocess.digester.aspectsdigest;

import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiidomain.services.gdmv3.Utils;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.ColumnAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.JsonAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.utils.email.DigesterProcessMessage;
import org.gobiiproject.gobiiprocess.spring.GobiiProcessContextSingleton;
import org.gobiiproject.gobiisampletrackingdao.CvDao;

import java.util.*;

public class MarkersDigest  extends AspectDigest {

    public DigesterResult digest(LoaderInstruction loaderInstruction) {
        HashSet<String> propertyFields = new HashSet<String>(){{add("markerProperties");}};
        return null;
    }


    private void mapAspectsFromTemplates(String[] fileColumns,
                                         Set<String> propertyFields) {

        // Get CvDao
        CvDao cvDao = GobiiProcessContextSingleton.getInstance().getBean(CvDao.class);


    }

}
