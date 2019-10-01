package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.spworkers.SpDef;
import org.gobiiproject.gobiisampletrackingdao.spworkers.SpWorker;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

public class ExperimentDaoImpl implements ExperimentDao {


    Logger LOGGER = LoggerFactory.getLogger(ExperimentDaoImpl.class);

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    protected SpWorker spWorker;


    @Override
    @Transactional
    public Integer createExperiment(Experiment newExperiment) {

        Integer returnVal = 0;

        try {

            SpDef spDef = new SpDef("{call createexperiment(?,?,?,?,?,?,?,?,?,?,?)}")
                    .addParamDef(1, String.class, newExperiment.getExperimentName())
                    .addParamDef(2, String.class, newExperiment.getExperimentCode())
                    .addParamDef(3, Integer.class, newExperiment.getProjectId())
                    .addParamDef(4, Integer.class, newExperiment.getVendorProtocolId())
                    .addParamDef(5, Integer.class, newExperiment.getManifestId())
                    .addParamDef(6, String.class, newExperiment.getDataFile())
                    .addParamDef(7, Integer.class, newExperiment.getCreatedBy())
                    .addParamDef(8, Date.class, newExperiment.getCreatedDate())
                    .addParamDef(9, Integer.class, newExperiment.getModifiedBy())
                    .addParamDef(10, Date.class, newExperiment.getModifiedDate())
                    .addParamDef(11, Integer.class, newExperiment.getExperimentStatus());

            spWorker.run(spDef);

            returnVal = spWorker.getResult();

        }
        catch (ConstraintViolationException constraintViolation) {

            String errorMsg;

            GobiiValidationStatusType statusType = GobiiValidationStatusType.BAD_REQUEST;

            // Postgresql error code for Unique Constraint Violation is 23505
            if(constraintViolation.getSQLException() != null) {

                if(constraintViolation.getSQLException().getSQLState().equals("23505")) {

                    statusType = GobiiValidationStatusType.ENTITY_ALREADY_EXISTS;

                    errorMsg = "Experiment already exists";

                }
                else {

                    errorMsg = "Invalid request or Missing required fields.";

                }

            }
            else {
                errorMsg = constraintViolation.getMessage();
            }
            throw (new GobiiDaoException(
                    GobiiStatusLevel.ERROR,
                    statusType,
                    errorMsg)
            );
        }
        catch(Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());
        }

        return returnVal;

    }
}
