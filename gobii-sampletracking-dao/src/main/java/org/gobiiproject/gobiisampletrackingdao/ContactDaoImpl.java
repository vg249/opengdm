/**
 * ContactDaoImpl.java
 * 
 * Default ContactDao implementation
 * @author RNDuldulao, Jr.
 * @since 2020-03-23
 */

package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContactDaoImpl implements ContactDao {
    @PersistenceContext
    protected EntityManager em;

    /**
     * Get Contact data
     */
    @Override
    public Contact getContact(Integer id) throws Exception {
        return em.find(Contact.class, id);
    }

    @Override
    public Contact getContactByUsername(String username) throws Exception {
        try {
            TypedQuery<Contact> tq = em.createQuery("FROM Contact WHERE username=?1", Contact.class);
            Contact result = tq.setParameter(1, username).getSingleResult();
            return result;
        } catch (NoResultException nre) {
            return null;
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public List<Contact> getContacts(Integer page, Integer pageSize, Integer organizationId) {
        List<Contact> contacts = new ArrayList<>();
        Integer orgId = (Optional.ofNullable(organizationId)).orElse(0);

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);

            Root<Contact> contactRoot = criteriaQuery.from(Contact.class);
            contactRoot.fetch("organization", JoinType.LEFT);
            criteriaQuery.select(contactRoot);
            if (orgId > 0) {
                criteriaQuery.where(
                    criteriaBuilder.equal(
                        contactRoot.get("organization"),
                        orgId
                    )
                );
            }
            criteriaQuery.orderBy(criteriaBuilder.asc(contactRoot.get("contactId")));

            contacts = em.createQuery(criteriaQuery).setFirstResult(page * pageSize).setMaxResults(pageSize)
                    .getResultList();
            return contacts;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }
    }
     
 }