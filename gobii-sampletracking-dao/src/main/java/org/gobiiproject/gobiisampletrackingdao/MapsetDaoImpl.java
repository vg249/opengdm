package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
public class MapsetDaoImpl implements MapsetDao {

    Logger LOGGER = LoggerFactory.getLogger(MapsetDaoImpl.class);


    @PersistenceContext
    protected EntityManager em;

    public List<Mapset> getMapsetsWithCountsByExperimentId(
            Integer pageSize, Integer rowOffset,
            Integer experimentId) throws GobiiException {

        List<Mapset> mapsetsWithCounts = new ArrayList<>();

        String queryString = "SELECT DISTINCT {mapset.*}, COUNT(linkage_group_id) AS linkage_group_count, " +
                " COUNT(DISTINCT marker_id) AS marker_count " +
                "FROM experiment " +
                "INNER JOIN dataset USING(experiment_id) " +
                "INNER JOIN marker ON(jsonb_exists(marker.dataset_marker_idx, CAST(dataset.dataset_id AS TEXT))) " +
                "INNER JOIN marker_linkage_group USING(marker_id) " +
                "INNER JOIN linkage_group USING(linkage_group_id) " +
                "INNER JOIN mapset ON(mapset.mapset_id = linkage_group.map_id) " +
                "WHERE (:experimentId IS NULL OR experiment.experiment_id = :experimentId)" +
                "GROUP BY mapset.mapset_id;";


        try {

            Session session = em.unwrap(Session.class);

            session.enableFetchProfile("mapset-typecv");

            List<Object[]> resultTuples = session.createNativeQuery(queryString)
                    .addEntity("mapset", Mapset.class)
                    .addScalar("linkage_group_count", IntegerType.INSTANCE)
                    .addScalar("marker_count", IntegerType.INSTANCE)
                    .setParameter("experimentId", experimentId, IntegerType.INSTANCE)
                    .setFetchSize(pageSize)
                    .setFirstResult(rowOffset)
                    .list();


            for(Object[] tuple : resultTuples) {

                Mapset mapset = (Mapset) tuple[0];
                mapset.setLinkageGroupCount((Integer) tuple[1]);
                mapset.setMarkerCount((Integer) tuple[1]);

                mapsetsWithCounts.add(mapset);

            }

            return mapsetsWithCounts;

        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }

    }
}
