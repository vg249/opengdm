package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapDatasetBrapi;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetBrapiDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class DatasetBrapiServiceImplTest {

    @InjectMocks
    private DatasetBrapiServiceImpl datasetBrapiService;

    @Mock
    private DtoMapDatasetBrapi dtoMapDatasetBrapi;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private AnalysisBrapiDTO createMockAnalysisDTO(Integer analysisId) {

        AnalysisBrapiDTO analysisBrapiDTO = new AnalysisBrapiDTO();

        analysisBrapiDTO.setAnalysisDbId(analysisId);
        analysisBrapiDTO.setAnalysisName("test-analysis-"+analysisId.toString());
        analysisBrapiDTO.setType("calling");

        return analysisBrapiDTO;
    }

    private DataSetBrapiDTO createMockDatasetDTO() {

        DataSetBrapiDTO dataSetBrapiDTO = new DataSetBrapiDTO();

        AnalysisBrapiDTO analysisBrapiDTO = createMockAnalysisDTO(1);
        AnalysisBrapiDTO analysisBrapiDTO1 = createMockAnalysisDTO(2);

        dataSetBrapiDTO.setVariantSetDbId(36);
        dataSetBrapiDTO.setVariantSetName("test-variantSet");
        dataSetBrapiDTO.setStudyDbId(1);
        dataSetBrapiDTO.setCallingAnalysisId(analysisBrapiDTO.getAnalysisDbId());
        dataSetBrapiDTO.setStudyName("test-study");

        List<AnalysisBrapiDTO> analysisBrapiDTOList = new ArrayList<>();
        analysisBrapiDTOList.add(analysisBrapiDTO1);

        dataSetBrapiDTO.setAnalyses(analysisBrapiDTOList);

        return dataSetBrapiDTO;
    }

    @Test
    public void getVariantSets() throws Exception {

        List<DataSetBrapiDTO> variantSetsMock = new ArrayList<>();

        DataSetBrapiDTO dataSetBrapiDTO = createMockDatasetDTO();

        variantSetsMock.add(dataSetBrapiDTO);

        when (
                dtoMapDatasetBrapi.getList(
                        any(Integer.TYPE), any(Integer.TYPE), any(DataSetBrapiDTO.class)
                )
        ).thenReturn(variantSetsMock);

        List<DataSetBrapiDTO> variantSetList = datasetBrapiService.getDatasets(any(Integer.TYPE), any(Integer.TYPE), any(DataSetBrapiDTO.class));

        assertEquals(variantSetsMock.size(), variantSetList.size());
        verify(dtoMapDatasetBrapi, times(1)).getList(any(Integer.TYPE), any(Integer.TYPE), any(DataSetBrapiDTO.class));
    }

    @Test
    public void getVariantSetById() throws Exception {

        DataSetBrapiDTO variantSetDTOMock = createMockDatasetDTO();

        when (
                dtoMapDatasetBrapi.get(variantSetDTOMock.getVariantSetDbId())
        ).thenReturn(variantSetDTOMock);

        DataSetBrapiDTO variantSetResult = datasetBrapiService.getDatasetById(variantSetDTOMock.getVariantSetDbId());

        assertEquals(variantSetDTOMock.getVariantSetDbId(), variantSetResult.getVariantSetDbId());
        assertEquals(variantSetDTOMock.getVariantSetName(), variantSetResult.getVariantSetName());
        verify(dtoMapDatasetBrapi, times(1)).get(variantSetDTOMock.getVariantSetDbId());
    }
}
