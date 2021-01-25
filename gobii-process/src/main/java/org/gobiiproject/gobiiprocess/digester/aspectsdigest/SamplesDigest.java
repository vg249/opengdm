package org.gobiiproject.gobiiprocess.digester.aspectsdigest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiidomain.services.gdmv3.Utils;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.DnaRunTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.*;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.modelmapper.AspectMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.IntegerUtils;
import org.gobiiproject.gobiiprocess.spring.GobiiProcessContextSingleton;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiisampletrackingdao.LoaderTemplateDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;

import java.io.File;
import java.util.*;

public class SamplesDigest extends AspectDigest {

    final HashSet<String> propertyFields = new HashSet<>(Arrays.asList(
        "dnaRunProperties",
        "dnaSampleProperties",
        "germplasmProperties"));

    final Map<String, CvGroupTerm> propertyFieldsCvGroupMap = Map.of(
        "dnaRunProperties", CvGroupTerm.CVGROUP_DNARUN_PROP,
        "dnaSampleProperties", CvGroupTerm.CVGROUP_DNASAMPLE_PROP,
        "germplasmProperties", CvGroupTerm.CVGROUP_GERMPLASM_PROP);

    private ProjectDao projectDao;
    private ExperimentDao experimentDao;
    private CvDao cvDao;

    SamplesDigest(LoaderInstruction loaderInstruction) {
        super(loaderInstruction);
        this.projectDao = GobiiProcessContextSingleton.getInstance().getBean(ProjectDao.class);
        this.experimentDao =
            GobiiProcessContextSingleton.getInstance().getBean(ExperimentDao.class);
        this.cvDao = GobiiProcessContextSingleton.getInstance().getBean(CvDao.class);
    }

    public DigesterResult digest(LoaderInstruction loaderInstruction) {

        File dnaRunInputFile = new File(loaderInstruction.getInputFile()) ;
        String[] fileColumns = Utils.getHeaders(dnaRunInputFile);
        Map<String, Object> aspects = getAspects(fileColumns);

        for(String table : aspects.keySet()) {

        }

        return null;
    }

    /**
     * Get aspects mapped for the given dnarun file headers
     * @param fileHeaders
     * @return
     */
    private Map<String, Object> getAspects(String[] fileHeaders) {

        // Set Marker Aspects
        Map<String, Object> aspects = new HashMap<>();

        Map<String, Object> dnaRunTemplateMap;
        DnaRunTemplateDTO dnaRunTemplate;
        DnaRunTable dnaRunTable = new DnaRunTable();
        DnaSampleTable dnaSampleTable = new DnaSampleTable();
        GermplasmTable germplasmTable = new GermplasmTable();

        LoaderTemplateDao loaderTemplateDao =
            GobiiProcessContextSingleton
                .getInstance()
                .getBean(LoaderTemplateDao.class);

        // new status to set for new dna runs
        Cv newStatus = cvDao.getNewStatus();

        // Read Dnarun load template
        LoaderTemplate loaderTemplate =
            loaderTemplateDao.getById(loaderInstruction.getTemplateId());

        DnaRunUploadRequestDTO uploadRequest =
            (DnaRunUploadRequestDTO) loaderInstruction.getUserRequest();

        try {
            dnaRunTemplateMap = mapper.treeToValue(
                loaderTemplate.getTemplate(), HashMap.class);
            dnaRunTemplate = mapper.treeToValue(
                loaderTemplate.getTemplate(),
                DnaRunTemplateDTO.class);
        }
        catch (JsonProcessingException jE) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Invalid dnarun template object.");
        }

        //Get API fields Entity Mapping
        Map<String, List<String>> fileColumnsApiFieldsMap =
            Utils.getFileColumnsApiFieldsMap(dnaRunTemplateMap, propertyFields);

        // Map for Aspect values to each api field.
        Map<String, Object> aspectValues = new HashMap<>();


        // To memoize cv for each property group for each table.
        Map<String, Map<String, Cv>> propertiesCvMaps = new HashMap<>();

        // Set Aspect for each file column
        for(int i = 0; i < fileHeaders.length; i++) {
            String fileHeader = fileHeaders[i];
            ColumnAspect columnAspect = new ColumnAspect(1, i);

            for(String apiFieldName : fileColumnsApiFieldsMap.get(fileHeader)) {

                String propertyGroupName = null;
                if(apiFieldName.contains(propertyGroupSeparator)) {
                    propertyGroupName =
                        apiFieldName.substring(0, apiFieldName.indexOf(propertyGroupSeparator));
                }

                // Check for dna run properties fields
                if (propertyFields.contains(propertyGroupName)) {
                    String propertyName =
                        apiFieldName.replace(propertyGroupName+propertyGroupSeparator, "");
                    setPropertyAspect(
                        aspectValues,
                        columnAspect,
                        propertiesCvMaps,
                        propertyName,
                        propertyGroupName,
                        propertyFieldsCvGroupMap);
                } else {
                    aspectValues.put(apiFieldName, columnAspect);
                }
            }
        }

        // Set Project Id in DnaRun and DnaSample Table Aspects
        if(!IntegerUtils.isNullOrZero(uploadRequest.getProjectId())) {
            Project project = projectDao.getProject(uploadRequest.getProjectId());
            if (project == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid Project");
            }
            dnaRunTable.setProjectId(project.getProjectId().toString());
            dnaSampleTable.setProjectId(project.getProjectId().toString());
        }

        // Set Experiment Id in DnaRun Table Aspects
        if(!IntegerUtils.isNullOrZero(uploadRequest.getExperimentId())) {
            Experiment experiment =
                experimentDao.getExperiment(uploadRequest.getExperimentId());
            if (experiment == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid Experiment");
            }
            dnaRunTable.setExperimentId(experiment.getExperimentId().toString());
        }

        boolean dnaRunMapped =
            AspectMapper.mapTemplateToAspects(dnaRunTemplate, dnaRunTable, aspectValues);

        boolean dnaSampleMapped =
            AspectMapper.mapTemplateToAspects(dnaRunTemplate, dnaSampleTable, aspectValues);

        boolean germplasmMapped =
            AspectMapper.mapTemplateToAspects(dnaRunTemplate, germplasmTable, aspectValues);

        if(dnaRunMapped) {
            String dnaRunTableName = Utils.getTableName(DnaRunTable.class);
            dnaRunTable.setStatus(newStatus.getCvId().toString());
            aspects.put(dnaRunTableName, dnaRunTable);
        }

        if(dnaSampleMapped) {
            String dnaSampleTableName = Utils.getTableName(DnaSampleTable.class);
            dnaSampleTable.setStatus(newStatus.getCvId().toString());
            aspects.put(dnaSampleTableName, dnaSampleTable);
        }

        if(germplasmMapped) {
            String germplasmTableName = Utils.getTableName(GermplasmTable.class);
            germplasmTable.setStatus(newStatus.getCvId().toString());
            aspects.put(germplasmTableName, germplasmTable);
        }

        return aspects;

    }


}
