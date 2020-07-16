package org.gobiiproject.gobidomain.services.gdmv3;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class ContactServiceImplTest {

    @Mock
    private ContactDao contactDao;

    @InjectMocks
    private ContactServiceImpl contactServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetContactsOk() throws Exception {
        List<Contact> mockList = new ArrayList<>();
        mockList.add(new Contact());
        when(contactDao.getContacts(0, 1000, null)).thenReturn(mockList);

        PagedResult<ContactDTO> result = contactServiceImpl.getContacts(0, 1000, null);
        assertTrue( result.getCurrentPageNum() == 0);
        assertTrue( result.getCurrentPageSize() == 1);
        assertTrue( result.getResult().size() == 1);
    }

    @Test( expected = GobiiException.class)
    public void testGetContactsNotOk1() throws Exception {
        when(contactDao.getContacts(0, 1000, null)).thenThrow(new GobiiException("test"));

        contactServiceImpl.getContacts(0, 1000, null);
    }

    @Test( expected = GobiiDomainException.class)
    public void testGetContactsNotOk2() throws Exception {
        contactServiceImpl.getContacts(null, null, null);
    }

    @Test
    public void testAddContactOk() throws Exception {
        when(contactDao.getContactByUsername("test")).thenReturn(null);
        when(contactDao.addContact(any(Contact.class))).thenReturn(new Contact());
        ArgumentCaptor<Contact> arg = ArgumentCaptor.forClass(Contact.class);

        contactServiceImpl.addContact("test", "test1", "testlastname", "test@email");
        verify(contactDao).addContact(arg.capture());
        assertTrue( arg.getValue().getFirstName().equals("test1"));
        assertTrue( arg.getValue().getLastName().equals("testlastname"));
        assertTrue( arg.getValue().getUsername().equals("test"));
        assertTrue( arg.getValue().getEmail().equals("test@email"));
        
    }

    @Test
    public void testAddContactAndContactAlreadyExistsOk() throws Exception {
        Contact mockContact = new Contact();
        when(contactDao.getContactByUsername("test")).thenReturn(mockContact);

        verify(contactDao, times(0)).addContact(any(Contact.class));
      
    }
    

}