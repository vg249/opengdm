/**
 * ProjetDaoImpl.java
 * 
 * ProjectDao Default Implementation.  DAO for Project and Project Properties (CV)
 * @author Rodolfo N. Duldulao, Jr.
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.entity.pgsql.ProjectProperties;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectDaoImpl implements ProjectDao {

    Logger LOGGER = LoggerFactory.getLogger(ProjectDaoImpl.class);

    @Autowired
    private CvDao cvDao;

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;

    @Transactional
    @Override
    public List<Project> getProjects(Integer pageNum, Integer pageSize) {
        LOGGER.debug("DAO getting projects");
        List<Project> projects = new ArrayList<>();

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);

            Root<Project> projectRoot = criteriaQuery.from(Project.class);
            projectRoot.fetch("contact");
            criteriaQuery.select(projectRoot);
            criteriaQuery.orderBy(criteriaBuilder.asc(projectRoot.get("projectId")));

            projects = em.createQuery(criteriaQuery).setFirstResult(pageNum * pageSize).setMaxResults(pageSize)
                    .getResultList();
            LOGGER.debug("Projects List " + projects.size());
            return projects;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

    }

    @Transactional
    @Override
    public Project createProject(String contactId, String projectName, String projectDescription,
            List<CvPropertyDTO> properties, String createByUserName) throws Exception {

        Contact contact = this.getContact(Integer.parseInt(contactId));
        if (contact == null)
            throw new GobiiDaoException("Contact Not Found");
        LOGGER.debug("Contact " + contact.getFirstName());

        // Get the Cv for status, new row

        List<Cv> cvList = cvDao.getCvs("new", CvGroup.CVGROUP_STATUS.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM);

        Cv cv = null;
        if (!cvList.isEmpty()) {
            cv = cvList.get(0);
        }
        LOGGER.debug("Cv " + cv.getTerm());
        Project project = new Project();

        project.setContact(contact);
        project.setProjectName(projectName);
        project.setProjectDescription(projectDescription);
        project.setStatus(cv);

        ProjectProperties props = new ProjectProperties();
        if (properties != null) {
            properties.forEach(dto -> {
                //TODO: no checking if Cv exists
                props.put(dto.getPropertyId().toString(), dto.getPropertyValue());
            });
        }

        project.setProperties(props);
        // audit items
        Contact creator = this.getContactByUsername(createByUserName);
        if (creator != null)
            project.setCreatedBy(creator.getContactId());
        project.setCreatedDate(new java.util.Date());

        em.persist(project);
        em.flush();

        return project;
    }

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

    /**
     * Get Contact data
     */
    public Contact getContact(Integer id) throws Exception {
        return em.getReference(Contact.class, id);
    }

    public Cv getCv(Integer id) throws Exception {
        return em.find(Cv.class, id);
    }

    @Transactional
    @Override
    public Project patchProject(Integer projectId, Map<String, String> attributes,
            List<Map<String, String>> propertiesList, String updatedBy) throws Exception {
        
        Project project = em.find(Project.class, projectId);
        if (project == null) {
            return null;
        }

        //audit items first
        Contact editor = this.getContactByUsername(updatedBy);
        project.setModifiedBy(null);
        if (editor != null)
            project.setModifiedBy(editor.getContactId());
        project.setModifiedDate(new java.util.Date());
        
        if (attributes != null && attributes.size() > 0) {
            this.updateAttributes(project, attributes);
        }
      
        if (propertiesList != null && propertiesList.size() > 0) {
            this.updateProperties(project, propertiesList);
        }

        //set new status

        List<Cv> cvList = cvDao.getCvs("modified", CvGroup.CVGROUP_STATUS.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM);

        Cv cv = null;
        if (!cvList.isEmpty()) {
            cv = cvList.get(0);
        }
        project.setStatus(cv);

        em.persist(project);
        em.flush();

        return project;

    }

    private void updateProperties(Project project, List<Map<String, String>> propertiesList) {
        ProjectProperties properties = project.getProperties();
        for (Map<String, String> prop: propertiesList) {
            String key = prop.get("propertyId");
            String value = prop.get("propertyValue");
            if (properties.containsKey(key) && value == null) {
                //remove the property
                properties.remove(key);
            } else if (properties.containsKey(key) && value != null) {
                properties.put(key, value);
            } else if (!properties.containsKey(key)) {
                properties.put(key, value); //TODO: no checking if Cv exists?
            }
        }
        project.setProperties(properties);
    }

    private void updateAttributes(Project project, Map<String, String> attributes)
            throws NumberFormatException, Exception {
        for (String key: attributes.keySet()) {
            String value = attributes.get(key);
            switch(key) {
                case "piContactId":
                    this.updateContact(project, value);
                    break;
                case "projectName":
                    this.updateProjectName(project, value);
                    break;
                case "projectDescription":
                    this.updateProjectDescription(project, value);
                    break;
            }
        }
    }

    private void updateProjectDescription(Project project, String value) {
        project.setProjectDescription(value);
    }

    private void updateProjectName(Project project, String value) throws Exception {
        if (value == null || value.trim() == "") throw new Exception("projectName is required");
        project.setProjectName(value);
    }

    private void updateContact(Project project, String value) throws NumberFormatException, Exception {
        Contact contact = this.getContact(Integer.parseInt(value));
        project.setContact(contact);
    }

    
    @Override
    public List<Cv> getProjectProperties(Integer page, Integer pageSize) {
        return cvDao.getCvs(null, CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null, page, pageSize);
    }
 
}