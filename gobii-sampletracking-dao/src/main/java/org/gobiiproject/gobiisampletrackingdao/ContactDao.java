/**
 * ContactDao.java
 * 
 * Contact DAO object for contact data.
 * @author RNDuldulao, Jr.
 * @since 2020-03-23
 */

package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Contact;

public interface ContactDao {

    Contact getContact(Integer contactId) throws Exception;
    Contact getContactByUsername(String username) throws Exception;

 }