package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.QueryParameterBean;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NativerQueryRunner {

     Logger LOGGER = LoggerFactory.getLogger(DatasetDao.class);

     public NativerQueryRunner() {}

     public NativerQueryRunner(EntityManager em) { this.em = em;}

    @PersistenceContext
    protected EntityManager em;

    public static NativerQueryRunner getInstance() {
        return new NativerQueryRunner();
    }

    @Transactional
    public List<Object[]> run(String queryString,
                              List<QueryParameterBean> parameterList,
                              HashMap<String, Class> entityMap,
                              List<String> scalarAliasList) {

            try {

                Session session = em.unwrap(Session.class);

                List<Object[]> resultTuplesList = new ArrayList<>();

                NativeQuery nativeQuery = session
                        .createNativeQuery(queryString);

                for (QueryParameterBean queryParameter : parameterList) {
                    nativeQuery.setParameter(queryParameter.getParameterName(),
                            queryParameter.getParameterValue(), queryParameter.getParamterType());
                }

                for (String entityAlias : entityMap.keySet()) {
                    nativeQuery.addEntity(entityAlias, entityMap.get(entityAlias));
                }

                for (String scalarAlias : scalarAliasList) {
                    nativeQuery.addScalar(scalarAlias);
                }


                resultTuplesList = nativeQuery.list();


                return resultTuplesList;
            }
            catch(Exception e) {
                LOGGER.error(e.getMessage(), e);

                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.UNKNOWN,
                        e.getMessage() + " Cause Message: " + e.getCause().getMessage());

            }


    }
}
