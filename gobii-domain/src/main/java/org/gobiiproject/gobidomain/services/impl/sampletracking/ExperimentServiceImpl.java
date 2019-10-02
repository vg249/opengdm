package org.gobiiproject.gobidomain.services.impl.sampletracking;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ExperimentDTO;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExperimentServiceImpl implements ExperimentService<ExperimentDTO> {

    Logger LOGGER = LoggerFactory.getLogger(ExperimentServiceImpl.class);

    @Autowired
    private ContactService contactService;


    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private CvDao cvDao;


    @Override
    public ExperimentDTO createExperiment(ExperimentDTO newExperimentDTO) throws GobiiDomainException {

        ExperimentDTO returnVal = new ExperimentDTO();

        try {

            Experiment experiment = new Experiment();

            ModelMapper.mapDtoToEntity(newExperimentDTO, experiment);



        }
        catch(GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE.getMessage());
            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch(Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }


    public String saveExperimentDataFile(String cropType, String fileName, byte[] dataFileBytes) {

        BufferedReader bufferedReader;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String rawUserFilesPath = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.RAW_USER_FILES);

            String experimentFileFolderName = UUID.randomUUID().toString().replace("-","");

            String experimentDataFilePath = (
                    LineUtils.terminateDirectoryPath(rawUserFilesPath) +
                            experimentFileFolderName + "/" + fileName);



            return  experimentDataFilePath;


        }
        catch(Exception e) {

            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Unable to save experiment data file.");
        }

        return "";
    }

    @Override
    public ExperimentDTO replaceExperiment(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDomainException {
        return experimentDTO;
    }

    @Override
    public List<ExperimentDTO> getExperiments() throws GobiiDomainException {
        List<ExperimentDTO> returnVal = new ArrayList<>();


        return returnVal;
    }

    @Override
    public ExperimentDTO getExperimentById(Integer experimentId) throws GobiiDomainException {

        ExperimentDTO returnVal = new ExperimentDTO();


        return returnVal;

    }

    @Override
    public List<ExperimentDTO> getExperimentsByProjectIdForLoadedDatasets(Integer projectId) throws GobiiDomainException {
        List<ExperimentDTO> returnVal = null;

        return returnVal;
    }

}
