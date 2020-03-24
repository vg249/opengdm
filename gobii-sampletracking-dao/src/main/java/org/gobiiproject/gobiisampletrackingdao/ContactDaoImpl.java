/**
 * ContactDaoImpl.java
 * 
 * Default ContactDao implementation
 * @author RNDuldulao, Jr.
 * @since 2020-03-23
 */

package org.gobiiproject.gobiisampletrackingdao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.gobiiproject.gobiimodel.entity.Contact;

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
     
 }