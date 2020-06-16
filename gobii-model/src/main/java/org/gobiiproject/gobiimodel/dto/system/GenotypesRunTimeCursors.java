package org.gobiiproject.gobiimodel.dto.system;

import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenotypesRunTimeCursors {


    public Integer markerIdCursor = null;

    public Integer dnaRunIdCursor = null;

    public Integer startDatasetId = null;

    public Map<String, List<String>> markerHdf5IndexMap;

    public Map<String, List<String>> dnarunHdf5IndexMap;


}
