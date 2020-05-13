package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapsetDaoImpl implements MapsetDao {

    Logger LOGGER = LoggerFactory.getLogger(MapsetDaoImpl.class);

    String mapsByExperimentIdListQueryString = "SELECT DISTINCT {mapset.*}, {typecv.*}, " +
            "COUNT(DISTINCT linkage_group_id) AS linkage_group_count, " +
            " COUNT(marker_id) AS marker_count " +
            "FROM experiment " +
            "INNER JOIN dataset USING(experiment_id) " +
            "INNER JOIN marker ON(jsonb_exists(marker.dataset_marker_idx, CAST(dataset.dataset_id AS TEXT))) " +
            "INNER JOIN marker_linkage_group USING(marker_id) " +
            "INNER JOIN linkage_group USING(linkage_group_id) " +
            "INNER JOIN mapset ON(mapset.mapset_id = linkage_group.map_id) " +
            "LEFT JOIN cv typecv ON(mapset.type_id = typecv.cv_id) " +
            "WHERE (:experimentId IS NULL OR experiment.experiment_id = :experimentId) " +
            "AND (:mapsetId IS NULL OR mapset.mapset_Id = :mapsetId) " +
            "GROUP BY mapset.mapset_id, typecv.cv_id " +
            "LIMIT :pageSize OFFSET :rowOffset";

    String mapsListQueryString = "SELECT DISTINCT {mapset.*}, {typecv.*}, " +
            "COUNT(DISTINCT linkage_group_id) AS linkage_group_count, " +
            "COUNT(marker_id) AS marker_count " +
            "FROM mapset " +
            "LEFT OUTER JOIN cv typecv ON(mapset.type_id = typecv.cv_id) " +
            "LEFT OUTER JOIN linkage_group ON(mapset.mapset_id = linkage_group.map_id) " +
            "LEFT OUTER JOIN marker_linkage_group USING(linkage_group_id) " +
            "WHERE (:mapsetId IS NULL OR mapset.mapset_Id = :mapsetId) " +
            "GROUP BY mapset.mapset_id, typecv.cv_id " +
            "LIMIT :pageSize OFFSET :rowOffset";

    @PersistenceContext
    protected EntityManager em;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Mapset> getMapsetsWithCounts(
            Integer pageSize, Integer rowOffset, Integer mapsetId,
            Integer experimentId) throws GobiiException {

        List<Mapset> mapsetsWithCounts = new ArrayList<>();

        try {

            Session session = em.unwrap(Session.class);

            String queryString = mapsListQueryString;

            if(experimentId != null) {
                queryString = mapsByExperimentIdListQueryString;
            }

            NativeQuery query = session.createNativeQuery(queryString)
                    .addEntity("mapset", Mapset.class)
                    .addJoin("typecv", "mapset.type")
                    .addScalar("linkage_group_count", IntegerType.INSTANCE)
                    .addScalar("marker_count", IntegerType.INSTANCE)
                    .setParameter("mapsetId", mapsetId, IntegerType.INSTANCE)
                    .setParameter("pageSize", pageSize, IntegerType.INSTANCE)
                    .setParameter("rowOffset", rowOffset, IntegerType.INSTANCE);

            if(experimentId != null) {
                query.setParameter("experimentId", experimentId);
            }

            List<Object[]> resultTuples = query.list();

            for(Object[] tuple : resultTuples) {

                Mapset mapset = (Mapset) tuple[0];

                mapset.setLinkageGroupCount((Integer) tuple[1]);
                mapset.setMarkerCount((Integer) tuple[2]);

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


  @Override
   public Mapset getMapsetWithCountsById(Integer mapsetId) {
      try {

          List<Mapset> mapsetsById = this.getMapsetsWithCounts(null, null,
                  mapsetId, null);

          if (mapsetsById.size() > 1) {

              LOGGER.error("More than one duplicate entries found.");

              throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                      GobiiValidationStatusType.NONE,
                      "More than one Mapset entity exists for the same Id");

          } else if (mapsetsById.size() == 0) {
              throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                      GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                      "Mapset Entity for given id does not exist");
          }

          return mapsetsById.get(0);

      }
      catch(GobiiException ge) {
          throw ge;
      }
      catch (Exception e) {

          LOGGER.error(e.getMessage(), e);

          throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                  GobiiValidationStatusType.UNKNOWN,
                  e.getMessage() + " Cause Message: " + e.getCause().getMessage());

      }

  }
}
