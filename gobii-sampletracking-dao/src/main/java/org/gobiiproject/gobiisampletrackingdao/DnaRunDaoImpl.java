package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class DnaRunDaoImpl implements DnaRunDao {

    Logger LOGGER = LoggerFactory.getLogger(MarkerDao.class);

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;

    /**
     * Lists the DnaRun Entities which matches the given criterias.
     * @param pageSize - Si
     * @param rowOffset
     * @param dnaRunId
     * @param datasetId
     * @return
     */
    @Override
    @Transactional
    public List<DnaRun> getDnaRuns(Integer pageSize, Integer rowOffset,
                                   Integer dnaRunId, Integer datasetId) {

        List<DnaRun> dnaRuns = new ArrayList<>();

        String queryString = "SELECT {dnarun.*} " +
                " FROM dnarun AS dnarun " +
                " WHERE (:datasetId IS NULL OR danrun.dataset_dnarun_idx->CAST(:datasetId AS TEXT) IS NOT NULL) " +
                " AND (:dnaRunId IS NULL OR dnarun.dnarun_id = :dnaRunId) " +
                " LIMIT :pageSize OFFSET :rowOffset ";

        try {

            Session session = em.unwrap(Session.class);

            if(pageSize == null) {
                pageSize = defaultPageSize;
            }

            dnaRuns = session.createNativeQuery(queryString)
                    .addEntity("dnarun", DnaRun.class)
                    .setParameter("pageSize", pageSize, IntegerType.INSTANCE)
                    .setParameter("rowOffset", rowOffset, IntegerType.INSTANCE)
                    .setParameter("dnaRunId", dnaRunId, IntegerType.INSTANCE)
                    .setParameter("datasetId", datasetId, IntegerType.INSTANCE)
                    .list();

            return dnaRuns;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }



    }


}
