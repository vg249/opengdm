/**
 * ContactDaoImpl.java
 * 
 * Default ContactDao implementation
 * @author RNDuldulao, Jr.
 * @since 2020-03-23
 */

package org.gobiiproject.gobiisampletrackingdao;

import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.VendorProtocol;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class VendorProtocolDaoImpl implements VendorProtocolDao {

    @PersistenceContext
    protected EntityManager em;


    @Override
    public List<VendorProtocol> getVendorProtocols(Integer page, Integer pageSize) {
        List<VendorProtocol> vendorProtocols = new ArrayList<>();

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<VendorProtocol> criteriaQuery =
                    criteriaBuilder.createQuery(VendorProtocol.class);

            Root<VendorProtocol> vendorProtocolRoot =
                    criteriaQuery.from(VendorProtocol.class);

            criteriaQuery.select(vendorProtocolRoot);
            criteriaQuery.orderBy(
                    criteriaBuilder.asc(
                            vendorProtocolRoot.get("vendorProtocolId")));

            vendorProtocols = em
                    .createQuery(criteriaQuery)
                    .setFirstResult(page * pageSize).setMaxResults(pageSize)
                    .getResultList();

            return vendorProtocols;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }
    }
     
 }