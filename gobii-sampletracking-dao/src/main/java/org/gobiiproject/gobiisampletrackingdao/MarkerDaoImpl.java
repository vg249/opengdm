package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.vladmihalcea.hibernate.type.array.StringArrayType;

import org.apache.commons.collections.CollectionUtils;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.IntegerUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class MarkerDaoImpl implements MarkerDao {

    Logger LOGGER = LoggerFactory.getLogger(MarkerDaoImpl.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    public List<Marker> getMarkers(Integer pageSize, Integer rowOffset, Integer markerId, Integer datasetId)
            throws GobiiException {

        List<Marker> markers;

        List<Predicate> predicates = new ArrayList<>();

        try {

            Objects.requireNonNull(pageSize, "pageSize : Required non null");
            Objects.requireNonNull(pageSize, "rowOffset : Required non null");

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Marker> criteriaQuery = criteriaBuilder.createQuery(Marker.class);

            Root<Marker> markerRoot = criteriaQuery.from(Marker.class);
            markerRoot.fetch("platform", JoinType.LEFT);
            markerRoot.fetch("reference", JoinType.LEFT);

            if (markerId != null) {
                predicates.add(criteriaBuilder.equal(markerRoot.get("markerId"), markerId));
            }

            if (datasetId != null) {

                Expression<Boolean> datasetIdExists = criteriaBuilder.function("JSONB_EXISTS", Boolean.class,
                        markerRoot.get("datasetMarkerIdx"), criteriaBuilder.literal(datasetId.toString()));

                predicates.add(criteriaBuilder.isTrue(datasetIdExists));

            }

            criteriaQuery.select(markerRoot);
            criteriaQuery.where(predicates.toArray(new Predicate[] {}));

            markers = em.createQuery(criteriaQuery).setFirstResult(rowOffset).setMaxResults(pageSize).getResultList();

            return markers;

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

    }

    @Override
    public Marker getMarkerById(Integer markerId) {

        try {

            Objects.requireNonNull(markerId, "markerId : Required non null");

            // To overload the getMarkers. There should be only one marker for a marker id
            // So, to check any discrepancy with that from database side
            Integer pageSize = 2;
            Integer rowOffset = 0;

            List<Marker> markersById = this.getMarkers(pageSize, rowOffset, markerId, null);

            if (markersById.size() > 1) {

                LOGGER.error("More than one duplicate entries found.");

                throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.NONE,
                        "More than one marker entity exists for the same Id");

            } else if (markersById.size() == 0) {
                throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Marker Entity for given id does not exist");
            }

            return markersById.get(0);

        } catch (GobiiException ge) {
            throw ge;
        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }

    }

    @Override
    public List<Marker> getMarkersByMarkerIdCursor(Integer pageSize, Integer markerIdCursor, Integer markerId,
            Integer datasetId) {

        List<Marker> markers;
        List<Predicate> predicates = new ArrayList<>();

        try {

            Objects.requireNonNull(pageSize, "pageSize : Required non null");

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Marker> criteriaQuery = criteriaBuilder.createQuery(Marker.class);

            Root<Marker> markerRoot = criteriaQuery.from(Marker.class);

            markerRoot.fetch("platform", JoinType.LEFT);
            markerRoot.fetch("reference", JoinType.LEFT);

            if (markerId != null) {
                predicates.add(criteriaBuilder.equal(markerRoot.get("markerId"), markerId));
            }

            if (markerIdCursor != null) {
                predicates.add(criteriaBuilder.gt(markerRoot.get("markerId"), markerIdCursor));
            }

            if (datasetId != null) {

                Expression<Boolean> datasetIdExists = criteriaBuilder.function("JSONB_EXISTS", Boolean.class,
                        markerRoot.get("datasetMarkerIdx"), criteriaBuilder.literal(datasetId.toString()));

                predicates.add(criteriaBuilder.isTrue(datasetIdExists));

            }

            criteriaQuery.select(markerRoot);
            criteriaQuery.orderBy(criteriaBuilder.asc(markerRoot.get("markerId")));
            criteriaQuery.where(predicates.toArray(new Predicate[] {}));

            markers = em.createQuery(criteriaQuery).setMaxResults(pageSize).getResultList();

            return markers;

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }
    }

    @Override
    public List<Marker> getMarkersByDatasetId(Integer datasetId, Integer pageSize, Integer rowOffset) {
        return getMarkers(pageSize, rowOffset, null, datasetId);
    }

    /**
     * Returns List of Marker Entities for given markerIds
     * 
     * @param markerIds - List of Marker Ids
     * @return Lis of Marker Entities
     */
    @Override
    public List<Marker>
    getMarkers(Set<Integer> markerIds, Set<String> markerNames,
               Set<String> datasetIds, Integer pageSize,
               Integer markerIdCursor) throws GobiiException {

        List<Marker> markers;

        List<Predicate> predicates = new ArrayList<>();

        String[] datasetIdsArray = new String[] {};

        try {

            CriteriaBuilder cb = em.getCriteriaBuilder();

            // Initialize criteria with Marker Entity as Result
            CriteriaQuery<Marker> criteria = cb.createQuery(Marker.class);

            // Set Root entity and selected entities
            Root<Marker> root = criteria.from(Marker.class);
            criteria.select(root);
            root.fetch("platform");

            if(!CollectionUtils.isEmpty(markerIds)) {
                predicates.add(root.get("markerId").in(markerIds));
            }

            if(!CollectionUtils.isEmpty(markerNames)) {
                predicates.add(root.get("markerName").in(markerNames));
            }

            if(!CollectionUtils.isEmpty(datasetIds)) {

                datasetIdsArray = datasetIds.toArray(new String[0]);

                ParameterExpression datasetIdsExp = cb.parameter(
                        String[].class,
                        "datasetIds");

                Expression<Boolean> datasetIdExists = cb.function("JSONB_EXISTS_ANY", Boolean.class,
                        root.get("datasetMarkerIdx"), datasetIdsExp);

                predicates.add(cb.isTrue(datasetIdExists));

            }

            if(!IntegerUtils.isNullOrZero(markerIdCursor)) {
                predicates.add(cb.gt(root.get("markerId"), markerIdCursor));
            }

            criteria.where(predicates.toArray(new Predicate[]{}));
            criteria.orderBy(cb.asc(root.get("markerId")));

            TypedQuery query = em.createQuery(criteria);

            if(!CollectionUtils.isEmpty(datasetIds)) {
                query
                        .unwrap(org.hibernate.query.Query.class)
                        .setParameter(
                                "datasetIds", datasetIdsArray,
                                StringArrayType.INSTANCE);
            }

            if(!IntegerUtils.isNullOrZero(pageSize)) {
                query.setMaxResults(pageSize);
            }

            markers = query
                    .getResultList();

            return markers;

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }
    }

    /**
     * Returns List of Marker Entities for given markerIds
     * @param markerIds - List of Marker Ids
     * @return Lis of Marker Entities
     */
    @Override
    public List<Marker> getMarkersByMarkerIds(Set<Integer> markerIds) {
        return this.getMarkers(markerIds, null, null, null, null);
    }

    /**
     * Retruns List of Marker Entities for given Marker Names
     * @param markerNames - Marker
     * @return
     */
    @Override
    public List<Marker> getMarkersByMarkerNames(Set<String> markerNames) {
        return this.getMarkers(null, markerNames, null, null, null);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Marker> getMarkersByPlatformMarkerNameTuples(List<List<String>> markerTuples) {
        try {
            Session session = em.unwrap(Session.class);
            //TODO: find a way to to solve this, article in StackOverflow says the only way to do tuple where in
            //is via Native Query since not all dbs support the syntax (but Postgres does) and there is no way to do this
            //in JPA
            List<String> tupleList = new ArrayList<>();
            for(List<String> item: markerTuples) {
                tupleList.add(String.format("('%s', '%s')", item.get(0).replace("'", "\\'"), item.get(1).replace("'", "\\'")));
            };
            String values = String.join(",", tupleList);
            List<Object[]> objects = session.createNativeQuery(
                "SELECT {marker.*}, {platform.*}  FROM marker LEFT JOIN platform ON marker.platform_id = platform.platform_id " + 
                " WHERE (platform.name, marker.name) IN (" + values + ")"
            )
            .addEntity("marker", Marker.class)
            .addJoin("platform", "marker.platform")
            .list();
            List<Marker> markers  = new ArrayList<>();
            for (Object[] tuple: objects) {
                Marker marker = (Marker) tuple[0];
                //Platform platform = (Platform) tuple[1];
                //marker.setPlatform(platform);
                markers.add(marker);
            }


            //EntityGraph<?> graph = this.em.getEntityGraph("graph.marker");
            //q.setHint("javax.persistence.fetchgraph", graph);
            //q.setParameter("tuplelist", tupleList.toString());
           


            return markers;

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }
    }

}
