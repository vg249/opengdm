package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class DnaSampleDaoImpl implements DnaSampleDao {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleDao.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    @Transactional
    public List<DnaSample> getDnaSamples(Integer pageNum, Integer pageSize, Integer projectId,
                                         Integer dnaSampleId, Integer germplasmId,
                                         String germplasmExternalCode) {

        List<DnaSample> dnaSamples = new ArrayList<>();

        try {

            Session session = em.unwrap(Session.class);

            Criteria dnaSampleCriteria = session.createCriteria(DnaSample.class);

            Criteria germplasmCriteria = dnaSampleCriteria.createCriteria("germplasm");

            if(dnaSampleId != null) {
                dnaSampleCriteria.add(Restrictions.eq("dnaSampleId", dnaSampleId));
            }

            if(projectId != null) {
                dnaSampleCriteria.add(Restrictions.eq("projectId", projectId));
            }

            if(germplasmId != null) {
                germplasmCriteria.add(Restrictions.eq("germplasmId", germplasmId));
            }


            if(germplasmExternalCode != null) {
                germplasmCriteria.add(Restrictions.eq("externalCode", germplasmExternalCode));
            }

            dnaSampleCriteria
                    .setMaxResults(pageSize)
                    .setFirstResult(pageSize*pageNum);

            dnaSamples = dnaSampleCriteria.list();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());
        }



        return dnaSamples;

    }

    @Override
    @Transactional
    public List<DnaSample> getDnaSamples(Integer pageNum, Integer pageSize) {

        return getDnaSamples(pageNum, pageSize,
                null, null,
                null, (String)null);

    }

    @Override
    @Transactional
    public List<DnaSample> getDnaSamplesByDnaSampleId(Integer pageNum, Integer pageSize,
                                          Integer dnaSampleId) {

        return getDnaSamples(pageNum, pageSize,
                null, dnaSampleId,
                null, (String)null);

    }

    @Override
    @Transactional
    public List<DnaSample> getDnaSamplesByProjectId(Integer pageNum, Integer pageSize, Integer projectId) {

        return getDnaSamples(pageNum, pageSize,
                projectId, null,
                null, (String)null);

    }

    @Override
    @Transactional
    public List<DnaSample> getDnaSamplesByGermplasmId(Integer pageNum, Integer pageSize, Integer germplasmId) {

        return getDnaSamples(pageNum, pageSize,
                null, null,
                germplasmId, (String) null);

    }

    @Override
    @Transactional
    public List<DnaSample> getDnaSamplesByGermplasmExternalCode(Integer pageNum, Integer pageSize,
                                                                String germplasmExternalCode) {

        return getDnaSamples(pageNum, pageSize,
                null, null,
                null, germplasmExternalCode);

    }

}
