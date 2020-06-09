package org.gobiiproject.gobidomain.services.brapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.brapi.SamplesDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.modelmapper.CvMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Transactional
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
    public PagedResult<SamplesDTO> getSamples(Integer pageSize, Integer pageNum,
                                            Integer sampleDbId, Integer germplasmDbId,
                                            String observationUnitDbId) {

        PagedResult<SamplesDTO> returnVal = new PagedResult<>();

        List<SamplesDTO> samplesDTOs = new ArrayList<>();

        try {

            Objects.requireNonNull(pageNum, "pageNum : Required non null");
            Objects.requireNonNull(pageSize, "pageSize : Required non null");

            Integer rowOffset = 0;

            if(pageNum != null && pageSize != null) {
                rowOffset = pageNum*pageSize;
            }

            List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(
                    pageSize, rowOffset,
                    null, sampleDbId,
                    germplasmDbId, observationUnitDbId);

            List<Cv> cvList = cvDao.getCvListByCvGroup(
                    CvGroupTerm.CVGROUP_DNASAMPLE_PROP.getCvGroupName(), null);

            for (DnaSample dnaSample : dnaSamples) {

                if (dnaSample != null) {

                    SamplesDTO samplesDTO = new SamplesDTO();

                    ModelMapper.mapEntityToDto(dnaSample, samplesDTO);

                    if (dnaSample.getProperties() != null && dnaSample.getProperties().size() > 0) {

                        samplesDTO.setAdditionalInfo(CvMapper.mapCvIdToCvTerms(
                                cvList, dnaSample.getProperties()));

                        if (samplesDTO.getAdditionalInfo().containsKey("sample_type")) {
                            samplesDTO.setTissueType(samplesDTO.getAdditionalInfo().get("sample_type"));
                            samplesDTO.getAdditionalInfo().remove("sample_type");
                        }
                    }

                    samplesDTOs.add(samplesDTO);
                }
            }

            returnVal.setCurrentPageSize(samplesDTOs.size());
            returnVal.setCurrentPageNum(pageNum);
            returnVal.setResult(samplesDTOs);

            return returnVal;
        }
        catch(Exception e) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.NONE,
                    e.getMessage());
        }
    }


}
