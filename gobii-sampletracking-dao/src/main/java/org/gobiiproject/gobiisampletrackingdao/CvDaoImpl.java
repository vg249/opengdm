package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.spworkers.SpWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class CvDaoImpl implements CvDao {

    Logger LOGGER = LoggerFactory.getLogger(SpWorker.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    public List<Cv> getCvListByCvGroup(String cvGroupName) {

        List<Cv> cvList = new ArrayList<>();

        try {

            cvList = em
                    .createNativeQuery(
                            "SELECT DISTINCT cv.* FROM cv " +
                                    "INNER JOIN cvgroup " +
                                    "ON (cv.cvgroup_id = cvgroup.cvgroup_id " +
                                    "AND cvgroup.name = ?)", Cv.class)
                    .setParameter(1, cvGroupName)
                    .getResultList();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }

        return cvList;
    }
}
