package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.dto.children.PropNameId;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiSampleListType;

@Data
public class GobiiDataSetExtract {

    //Type of file to export (or meta data without separate data entries
    private GobiiFileType gobiiFileType = null;
    //Combine data sets into a single output file (Unused/unsupported)
    private boolean accolate = false;
    //Descriptive name of the data set. Used in reporting
    private JobProgressStatusType gobiiJobStatus;
    //Internal ID of the data set. Used for internal lookups.
    private String extractDestinationDirectory = null;

    private GobiiExtractFilterType gobiiExtractFilterType;
    private List<String> markerList = new ArrayList<>();
    private List<String> sampleList = new ArrayList<>();
    private String listFileName;
    private PropNameId gobiiDatasetType = new PropNameId();
    private PropNameId principleInvestigator = new PropNameId();
    private PropNameId project = new PropNameId();
    private PropNameId dataSet = new PropNameId();
    private List<PropNameId> platforms = new ArrayList<>();
    private GobiiSampleListType gobiiSampleListType;
    private List<PropNameId> markerGroups = new ArrayList<>();
    private List<File> extractedFiles = new ArrayList<>();

}