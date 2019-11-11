package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class MarkerDaoImpl implements MarkerDao {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleDao.class);

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;
    final int defaultPageNum = 0;


    @Override
    @Transactional
    public List<Marker> getMarkers(Integer pageNum, Integer pageSize,
                                   Integer markerId, Integer datasetId) {

        List<Marker> markers;


        try {


            Session session = em.unwrap(Session.class);

            Criteria markerCriteria = session.createCriteria(Marker.class);


            if(markerId != null) {
                markerCriteria.add(Restrictions.eq("markerId", markerId));
            }

            if(datasetId != null) {
                markerCriteria.add(Restrictions.sqlRestriction(
                        "{alias}.dataset_marker_idx ?? ?", datasetId.toString(),
                        StringType.INSTANCE));
            }



            if(pageSize != null && pageNum != null) {
                markerCriteria
                        .setMaxResults(pageSize)
                        .setFirstResult(pageSize * pageNum);
            }
            else {
                //If either page size or page number is not provided, use default maximum limit which is 1000
                //and fetch the first page
                markerCriteria
                        .setMaxResults(defaultPageSize)
                        .setFirstResult(defaultPageNum);
            }

            markers = markerCriteria.list();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

        return markers;

    }

    @Override
    @Transactional
    public List<Tuple> getMarkerWithStartStopTuples(Integer pageNum, Integer pageSize,
                                                    Integer markerId, Integer datasetId) {


        List<Tuple> markerStartStops = new ArrayList<>();

        try {

            Session session = em.unwrap(Session.class);

            String queryString = "SELECT marker.* from marker as marker " +
                    " LEFT JOIN marker_linkage_group AS markerli " +
                    " ON marker.marker_id = markerli.marker_id " +
                    " WHERE marker.dataset_marker_idx -> ? IS NOT NULL OR ? = '' ";


            Query markerStartStopsQuery = session.createNativeQuery(queryString, Marker.class);

            String datasetIdParam = "";

            if(datasetId != null) {
               datasetIdParam = datasetId.toString();
            }

            markerStartStopsQuery.setParameter(1, datasetIdParam);
            markerStartStopsQuery.setParameter(2, datasetIdParam);


            List<Marker> markers = markerStartStopsQuery.getResultList();

            System.out.println(markers.size());

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

        return markerStartStops;

    }

}
