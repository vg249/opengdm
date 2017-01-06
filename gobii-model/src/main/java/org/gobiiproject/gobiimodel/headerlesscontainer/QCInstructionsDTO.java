package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.instructions.GobiiQCComplete;

/**
 * Created by araquel on 1/6/2017.
 */
public class QCInstructionsDTO extends DTOBase {

    public QCInstructionsDTO() {
    }

    GobiiQCComplete gobiiQCComplete = new GobiiQCComplete();

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {

    }

    public GobiiQCComplete getGobiiQCComplete() {
        return gobiiQCComplete;
    }

    public void setGobiiQCComplete(GobiiQCComplete gobiiQCComplete) {
        this.gobiiQCComplete = gobiiQCComplete;
    }


}
