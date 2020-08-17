package org.gobiiproject.gobiisampletrackingdao;

import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobiimodel.entity.Protocol;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ProtocolDaoImpl implements ProtocolDao {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Protocol getProtocolById(Integer protocolId) {
        return em.find(Protocol.class, protocolId);
    }

    @Override
    public List<Protocol> getProtocols(Integer pageSize,
                                       Integer pageOffset,
                                       Integer platformId) {

        List<Protocol> protocols;
        List<Predicate> predicates = new ArrayList<>();

        try {

            Objects.requireNonNull(pageSize, "pageSize : Required non null");
            Objects.requireNonNull(pageOffset, "pageOffset: Required non null");

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Protocol> criteriaQuery = criteriaBuilder.createQuery(Protocol.class);

            Root<Protocol> root = criteriaQuery.from(Protocol.class);

            root.fetch("platform");

            if(platformId != null) {
                predicates.add(criteriaBuilder.equal(root.get("platform").get("platformId"),
                    platformId));
            }

            criteriaQuery.where(predicates.toArray(new Predicate[]{}));

            protocols = em.createQuery(criteriaQuery)
                .setMaxResults(pageSize)
                .setFirstResult(pageOffset)
                .getResultList();

            return protocols;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }
    }

    @Override
    public Protocol createProtocol(Protocol protocolToBeCreated) {
        return new Protocol();
    }

    @Override
    public Protocol patchProtocol(Protocol protcolToBePatched) {
        return new Protocol();
    }

    @Override
    public void deleteProtocol(Protocol protocol) {
    }

}
