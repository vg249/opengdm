package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobidomain.CvIdCvTermMapper;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.brapi.SamplesDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.BrapiDefaults;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class SamplesServiceImpl implements SamplesService {


    @Autowired
    private DnaSampleDao dnaSampleDao;

    @Autowired
    private CvDao cvDao;

    /**
     * Gets the list of dna sample for given search criteria
     * @param pageNum
     * @param pageSize
     * @param sampleDbId
     * @param germplasmDbId
     * @param observationUnitDbId
     * @return List of Genotype Calls
     */
    @Override
    public List<SamplesDTO> getSamples(Integer pageNum, Integer pageSize,
                                       Integer sampleDbId, Integer germplasmDbId,
                                       String observationUnitDbId) {
        try {

            List<SamplesDTO> returnVal = new ArrayList<>();

            Integer rowOffset = 0;

            if(pageSize == null) {
                pageSize = Integer.parseInt(BrapiDefaults.pageSize);
            }

            if(pageNum != null && pageSize != null) {
                rowOffset = pageNum*pageSize;
            }

            List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(
                    pageSize, rowOffset,
                    null, sampleDbId,
                    germplasmDbId, observationUnitDbId);

            List<Cv> cvList = cvDao.getCvListByCvGroup(
                    CvGroup.CVGROUP_DNASAMPLE_PROP.getCvGroupName(), null);

            for (DnaSample dnaSample : dnaSamples) {

                if (dnaSample != null) {

                    SamplesDTO samplesDTO = new SamplesDTO();

                    ModelMapper.mapEntityToDto(dnaSample, samplesDTO);

                    if (dnaSample.getProperties() != null && dnaSample.getProperties().size() > 0) {

                        samplesDTO.setAdditionalInfo(CvIdCvTermMapper.mapCvIdToCvTerms(
                                cvList, dnaSample.getProperties()));

                        if (samplesDTO.getAdditionalInfo().containsKey("sample_type")) {
                            samplesDTO.setTissueType(samplesDTO.getAdditionalInfo().get("sample_type"));
                            samplesDTO.getAdditionalInfo().remove("sample_type");
                        }
                    }

                    returnVal.add(samplesDTO);
                }
            }

            return returnVal;
        }
        catch(Exception e) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.NONE,
                    e.getMessage());
        }
    }


}
