package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.LinkageGroup;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LinkageGroupDaoImpl implements LinkageGroupDao {

    Logger LOGGER = LoggerFactory.getLogger(LinkageGroupDaoImpl.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public List<LinkageGroup> getLinkageGroups(Integer pageSize, Integer rowOffset,
                                        Integer linkageGroupId, Integer mapsetId) {

        Objects.requireNonNull(pageSize);
        Objects.requireNonNull(rowOffset);

        List<LinkageGroup> linkageGroups = new ArrayList<>();

        List<Predicate> predicates = new ArrayList<>();

        try {

            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<LinkageGroup> criteriaQuery = cb.createQuery(LinkageGroup.class);

            Root<LinkageGroup> linkageGroup = criteriaQuery.from(LinkageGroup.class);
            criteriaQuery.select(linkageGroup);

            Join<Object, Object> mapset = (Join<Object, Object>) linkageGroup.fetch("mapset");

            if(linkageGroupId != null) {
                predicates.add(cb.equal(linkageGroup.get("linkageGroupId"), linkageGroupId));
            }

            if(mapsetId != null) {
                predicates.add(cb.equal(mapset.get("mapsetId"), mapsetId));
            }

            criteriaQuery.where(predicates.toArray(new Predicate[]{}));

            linkageGroups = em.createQuery(criteriaQuery)
                    .setFirstResult(rowOffset)
                    .setMaxResults(pageSize)
                    .getResultList();

            return linkageGroups;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

    }

}
