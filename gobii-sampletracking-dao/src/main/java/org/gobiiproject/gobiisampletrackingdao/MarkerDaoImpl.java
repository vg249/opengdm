package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.entity.QueryField;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class MarkerDaoImpl implements MarkerDao {

    Logger LOGGER = LoggerFactory.getLogger(MarkerDao.class);

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;

    @Override
    @Transactional
    public List<Marker> getMarkers(Integer pageSize, Integer rowOffset,
                                   Integer markerId, Integer datasetId) {

        List<Marker> markers = new ArrayList<>();

        String queryString = "SELECT marker.* " +
                " FROM marker AS marker " +
                " WHERE (:datasetId IS NULL OR marker.dataset_marker_idx->CAST(:datasetId AS TEXT) IS NOT NULL) " +
                " AND (:markerId IS NULL OR marker.marker_id = :markerId) " +
                " LIMIT :pageSize OFFSET :rowOffset ";

        try {

            Session session = em.unwrap(Session.class);

            if(pageSize == null) {
                pageSize = defaultPageSize;
            }

            if(rowOffset == null) {
                rowOffset = 0;
            }


            markers = session.createNativeQuery(queryString)
                    .addEntity(Marker.class)
                    .setParameter("pageSize", pageSize, IntegerType.INSTANCE)
                    .setParameter("rowOffset", rowOffset, IntegerType.INSTANCE)
                    .setParameter("markerId", markerId, IntegerType.INSTANCE)
                    .setParameter("datasetId", datasetId, IntegerType.INSTANCE)
                    .list();

            return markers;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }



    }

    @Override
    @Transactional
    public List<Marker> getMarkersByDatasetId(Integer datasetId,
                                   Integer pageSize,
                                   Integer rowOffset) {
        return getMarkers(pageSize, rowOffset, null, datasetId);
    }

    /**
     * Returns List of Marker Entity with scalar fields Satrt and Stop from MarkerLinkageGroup Entity mapped
     * to markerStart and markerStop transient fields in the Marker Entity.
     * A given marker id may have more than one rows with different start stops.
     * Used only for BrAPI /variants Web service.
     * Avoid using it for Genotype Extraction or other use cases where markerId is expected to be unique.
     * @param pageSize -Page size to be fetched
     * @param rowOffset - Row offset after which the pages need to be fetched
     * @param markerId - Marker Id for which marker need to be fetched.
     * @param datasetId - To fetch the markers belonging to the dataset Id.
     * @return Lits of Marker Entity which match the filter criteria
     */
    @Override
    @Transactional
    public List<Marker> getMarkersWithStartAndStop(Integer pageSize, Integer rowOffset,
                                   Integer markerId, Integer datasetId) {

        List<Marker> markers = new ArrayList<>();

        String queryString = "SELECT {marker.*}, markerli.start AS start, markerli.stop AS stop " +
                " FROM marker AS marker " +
                " LEFT JOIN marker_linkage_group AS markerli " +
                " USING(marker_id) " +
                " WHERE (marker.dataset_marker_idx->CAST(:datasetId AS TEXT) IS NOT NULL OR :datasetId IS NULL) " +
                " AND (marker.marker_id = :markerId OR :markerId IS NULL) " +
                " LIMIT :pageSize OFFSET :rowOffset ";

        try {

            if(pageSize == null) {
                pageSize = defaultPageSize;
            }

            if(rowOffset == null) {
                rowOffset = 0;
            }

            Session session = em.unwrap(Session.class);

            List<Object[]> resultTuples = session.createNativeQuery(queryString)
                    .addEntity("marker", Marker.class)
                    .addScalar("start", IntegerType.INSTANCE)
                    .addScalar("stop", IntegerType.INSTANCE)
                    .setParameter("pageSize", pageSize, IntegerType.INSTANCE)
                    .setParameter("rowOffset", rowOffset, IntegerType.INSTANCE)
                    .setParameter("datasetId", datasetId, IntegerType.INSTANCE)
                    .setParameter("markerId", markerId, IntegerType.INSTANCE)
                    .list();


            for(Object[] tuple : resultTuples) {
                Marker marker = (Marker) tuple[0];
                marker.setMarkerStart((Integer) tuple[1]);
                marker.setMarkerStop((Integer) tuple[2]);
                markers.add(marker);
            }

            return markers;


        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }


    }

    @Transactional
    @Override
    public List<Marker> getMarkersByMarkerIds(int[] markerIds) {
        List<Marker> markers = new ArrayList<>();
        return markers;
    }

}
