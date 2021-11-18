package org.gobiiproject.gobiidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;
import org.gobiiproject.gobiidomain.services.gdmv3.exceptions.InvalidException;
import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.FileDTO;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class DnaRunServiceImplTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private DnaRunServiceImpl dnaRunServiceImpl;

    @Mock
    private ExperimentDao experimentDao;

    @Mock
    private ProjectDao projectDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNoInputFilesThrowsException() throws Exception {
        DnaRunUploadRequestDTO dnaRunUploadRequest = new DnaRunUploadRequestDTO();
        List<FileDTO> emptyFileList = new ArrayList<FileDTO>();
        dnaRunUploadRequest.setInputFiles(emptyFileList);

        exception.expect(InvalidException.class);
        exception.expectMessage("request: no input files");
        dnaRunServiceImpl.loadDnaRuns(dnaRunUploadRequest, "test");
    }

    @Test
    public void inputFileMustHaveFilePath() throws Exception {
        DnaRunUploadRequestDTO dnaRunUploadRequest = new DnaRunUploadRequestDTO();
        List<FileDTO> fileList = new ArrayList<FileDTO>();
        fileList.add(new FileDTO());
        dnaRunUploadRequest.setInputFiles(fileList);

        exception.expect(InvalidException.class);
        exception.expectMessage("empty server file path");
        dnaRunServiceImpl.loadDnaRuns(dnaRunUploadRequest, "test");
    }

    @Test
    public void inputFileMustHaveValidFilePath() throws Exception {
        DnaRunUploadRequestDTO dnaRunUploadRequest = new DnaRunUploadRequestDTO();

        List<FileDTO> fileList = new ArrayList<FileDTO>();
        FileDTO fileWithPath = new FileDTO();
        fileWithPath.setServerFilePath("/invalid/Server/File/Path");
        fileList.add(fileWithPath);
        dnaRunUploadRequest.setInputFiles(fileList);

        exception.expect(InvalidException.class);
        exception.expectMessage("input file path does not exist");
        dnaRunServiceImpl.loadDnaRuns(dnaRunUploadRequest, "test");
    }

}
