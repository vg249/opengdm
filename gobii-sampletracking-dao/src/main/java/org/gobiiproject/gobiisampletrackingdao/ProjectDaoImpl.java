/**
 * ProjetDaoImpl.java
 * 
 * ProjectDao Default Implementation.  DAO for Project and Project Properties (CV)
 * @author Rodolfo N. Duldulao, Jr.
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
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
import org.gobiiproject.gobiimodel.modelmapper.CvMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProjectDaoImpl implements ProjectDao {

    @Autowired
    private CvDao cvDao;

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;

    @Transactional
    @Override
    public List<Project> getProjects(Integer pageNum, Integer pageSize) {
        log.debug("DAO getting projects");
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
            log.debug("Projects List " + projects.size());
            return projects;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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

        // Get the Cv for status, new row
        List<Cv> cvList = cvDao.getCvs("new", CvGroup.CVGROUP_STATUS.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM);

        Cv cv = null;
        if (!cvList.isEmpty()) {
            cv = cvList.get(0);
        }
        log.debug("Cv " + cv.getTerm());
        Project project = new Project();

        project.setContact(contact);
        project.setProjectName(projectName);
        project.setProjectDescription(projectDescription);
        project.setStatus(cv);

        java.util.Map<String, String> props = CvMapper.mapCvIdToCvTerms(properties);
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
        return em.find(Contact.class, id);
    }

    public Cv getCv(Integer id) throws Exception {
        return em.find(Cv.class, id);
    }

    @Transactional
    @Override
    public Project patchProject(Integer projectId, Map<String, String> attributes,
            List<CvPropertyDTO> propertiesList, String updatedBy) throws Exception {
        Project project = em.find(Project.class, projectId, getContactHints());
        if (project == null) {
            return null;
        }
        //audit items first
        Contact editor = this.getContactByUsername(updatedBy);
        project.setModifiedBy(null);
        if (editor != null)
            project.setModifiedBy(editor.getContactId());
        project.setModifiedDate(new java.util.Date());
        
        //update project fields
        if (attributes != null && attributes.size() > 0) {
            this.updateAttributes(project, attributes);
        }
      
        //update project properties
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
        //em.refresh(project);

        return project;

    }

    private void updateProperties(Project project, List<CvPropertyDTO> propertiesList) {
        java.util.Map<String, String> currentProperties = project.getProperties();
        java.util.Map<String, String> incomingProperties = CvMapper.mapCvIdToCvTerms(propertiesList);

        currentProperties.putAll(incomingProperties);
        currentProperties.values().removeAll(Collections.singleton(null)); //remove nulled values
        
        project.setProperties(currentProperties);
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
        if (project.getContact() != null && project.getPiContactId().toString().equals(value)) return;
        Contact contact = this.getContact(Integer.parseInt(value));
        
        if (contact != null) {
            project.setContact(contact);
        }
    }
    
    @Override
    public List<Cv> getCvProperties(Integer page, Integer pageSize) {
        return cvDao.getCvs(null, CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null, page, pageSize);
    }

    @Override
    public Project getProject(Integer projectId) {
        Project project = em.find(Project.class, projectId, getContactHints());
        return project;
    }

    private Map<String, Object> getContactHints() {
        EntityGraph<?> graph = this.em.getEntityGraph("project.contact");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);
        return hints;
    }
}