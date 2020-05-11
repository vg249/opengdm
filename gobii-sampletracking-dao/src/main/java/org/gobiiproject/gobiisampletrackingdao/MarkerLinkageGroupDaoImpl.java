package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MarkerLinkageGroupDaoImpl implements MarkerLinkageGroupDao {

    Logger LOGGER = LoggerFactory.getLogger(MarkerLinkageGroupDaoImpl.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public List<MarkerLinkageGroup>
    getMarkerLinkageGroups(Integer pageSize, Integer rowOffset,
                           Integer mapsetId, String mapsetName,
                           Integer linkageGroupId, String linkageGroupName,
                           Integer markerId, String markerName,
                           BigDecimal minPosition, BigDecimal maxPosition,
                           Integer datasetId) throws GobiiException
    {
        List<MarkerLinkageGroup> markerLinkageGroups = new ArrayList<>();

        List<Predicate> predicates = new ArrayList<>();

        Objects.requireNonNull(pageSize, "Page Size is required");
        Objects.requireNonNull(rowOffset, "Row Offset is required");

        try {

            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<MarkerLinkageGroup> criteriaQuery =
                cb.createQuery(MarkerLinkageGroup.class);

            Root<MarkerLinkageGroup> markerLinkageGroup =
                criteriaQuery.from(MarkerLinkageGroup.class);

            criteriaQuery.select(markerLinkageGroup);

            Join<Object, Object> marker =
                (Join<Object, Object>) markerLinkageGroup.fetch("marker");

            //Join LinkageGroup to root
            Join<Object, Object> linkageGroup = (Join<Object, Object>) (
                    markerLinkageGroup.fetch("linkageGroup"));
            Join<Object, Object> mapset =
                (Join<Object, Object>) linkageGroup.fetch("mapset");

            //Associated Tables that needs to be fetched along with root table

            if(mapsetId != null) {
                predicates.add(cb.equal(mapset.get("mapsetId"), mapsetId));
            }
            if(mapsetName != null) {
                predicates.add(cb.equal(mapset.get("mapsetName"), mapsetName));
            }

            if(linkageGroupId != null) {
                predicates.add(cb.equal(
                    linkageGroup.get("linkageGroupId"), linkageGroupId));
            }
            if(linkageGroupName != null) {
                predicates.add(cb.equal(
                    linkageGroup.get("linkageGroupName"), linkageGroupName));
            }

            if(markerId != null) {
                predicates.add(cb.equal(marker.get("markerId"), markerId));
            }
            if(markerName != null) {
                predicates.add(cb.equal(marker.get("markerName"), markerName));
            }

            if(minPosition != null) {
                predicates.add(
                    cb.ge(markerLinkageGroup.get("start"), minPosition));
            }
            if(maxPosition != null) {
                predicates.add(
                    cb.le(markerLinkageGroup.get("stop"), maxPosition));
            }

            if(datasetId != null) {
                Expression<Boolean> datasetIdExists =
                    cb.function(
                        "JSONB_EXISTS", Boolean.class,
                        marker.get("datasetMarkerIdx"),
                        cb.literal(datasetId.toString()));
                predicates.add(cb.isTrue(datasetIdExists));
            }

            criteriaQuery.where(predicates.toArray(new Predicate[]{}));

            markerLinkageGroups = em.createQuery(criteriaQuery)
                    .setFirstResult(rowOffset)
                    .setMaxResults(pageSize)
                    .getResultList();

            return markerLinkageGroups;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: "
                        + e.getCause().getMessage());
        }

    }

}
