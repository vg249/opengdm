package org.gobiiproject.gobiidao.resultset.core;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * Created by Phil on 4/7/2016.
 */
public class StoredProcExec {

    @PersistenceContext
    protected EntityManager em;

    public void doWithConnection(Work work) {

        Session session = (Session) em.getDelegate();
        session.doWork(work);
    }

} // doWithConnection()
