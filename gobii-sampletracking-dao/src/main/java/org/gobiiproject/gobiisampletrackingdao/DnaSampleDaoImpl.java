package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.spworkers.SpWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class DnaSampleDaoImpl implements DnaSampleDao {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleDao.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    public List<DnaSample> getDnaSamples() {

        List<DnaSample> dnaSamples = new ArrayList<>();

        try {

            dnaSamples = em
                    .createQuery("from DnaSample")
                    .getResultList();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());
        }



        return dnaSamples;

    }

}
