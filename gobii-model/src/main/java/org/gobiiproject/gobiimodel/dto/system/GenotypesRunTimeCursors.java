package org.gobiiproject.gobiimodel.dto.system;

import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.entity.DnaRun;

import java.util.*;

public class GenotypesRunTimeCursors {


    public Integer markerIdCursor = null;

    public Integer dnaRunIdCursor = null;

    public Integer startDatasetId = null;

    public Integer pageOffset = 0;

    public Integer nextPageOffset = 0;

    public Integer dnaRunOffset = 0;

    public Integer markerPageSize = 0;

    public Integer nextDnaRunOffset = 0;

    public Integer columnOffset = 0;



    public Map<String, List<String>> markerHdf5IndexMap = new HashMap<>();

    public Map<String, List<String>> dnarunHdf5IndexMap = new HashMap<>();

    public SortedMap<Integer, Integer> dnarunHdf5OrderMap = new TreeMap<>();

}
