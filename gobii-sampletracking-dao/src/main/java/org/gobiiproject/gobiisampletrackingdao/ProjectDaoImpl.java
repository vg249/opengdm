package org.gobiiproject.gobiisampletrackingdao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        // TODO Auto-generated method stub
        LOGGER.debug("DAO getting projects");
        List<Project> projects = new ArrayList<>();

        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);

            Root<Project> projectRoot = criteriaQuery.from(Project.class);
            projectRoot.fetch("contact");
            criteriaQuery.select(projectRoot);

            projects = em.createQuery(criteriaQuery)
                .setFirstResult(pageNum * pageSize)
                .setMaxResults(pageSize)
                .getResultList(); 
            LOGGER.debug("Projects List " + projects.size());
            return projects;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiDaoException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.UNKNOWN,
                e.getMessage() +  " Cause Message: " + e.getCause().getMessage()
             );
        }
        
    }

    @Transactional
	@Override
	public Project createProject(String contactId, String projectName, String projectDescription,
			List<CvPropertyDTO> properties) throws Exception {

        Contact contact = this.getContact(Integer.parseInt(contactId));
        if (contact == null) throw new GobiiDaoException("Contact Not Found");
        LOGGER.debug("Contact " + contact.getFirstName());
    
        //Get the Cv for status, new row


        List<Cv> cvList = cvDao.getCvs( "new",
                CvGroup.CVGROUP_STATUS.getCvGroupName(), GobiiCvGroupType.GROUP_TYPE_SYSTEM);
        
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
                props.put(
                    dto.getPropertyId().toString(),
                    dto.getPropertyValue()
                );
            });
        }

        project.setProperties(props);
        
        em.persist(project);
        em.flush();
        
		return project;
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

    
}