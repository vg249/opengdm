/**
 * ContactDao.java
 * 
 * Contact DAO object for contact data.
 * @author RNDuldulao, Jr.
 * @since 2020-03-23
 */

package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Contact;

public interface ContactDao {

    List<Contact> getContacts(Integer page, Integer pageSize, Integer organizationId);
    Contact getContact(Integer contactId) throws Exception;
    Contact getContactByUsername(String username) throws Exception;

 }