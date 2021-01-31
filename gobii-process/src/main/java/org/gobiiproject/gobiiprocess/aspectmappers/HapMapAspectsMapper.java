package org.gobiiproject.gobiiprocess.aspectmappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.gobii.masticator.aspects.TableAspect;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.Table;
import org.gobiiproject.gobiiprocess.digester.utils.FileUtils;

import java.io.*;
import java.util.*;

/**
 * Maps table aspect for hapmap file
 */
@Slf4j
public class HapMapAspectsMapper {

    private MatrixLoaderInstruction matrixLoaderInstruction;

    private final String headerIdentifier = "rs#";

    private final int maxLinesToLookForHeader = 1000;

    private final String[] hapMapRequiredColumns = {
        "rs#",
        "alleles",
        "chrom",
        "pos",
        "strand",
        "assembly#",
        "center",
        "protLSID",
        "assayLSID",
        "panelLSID",
        "QCcode"
    };

    private final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     *
     * @param loaderInstruction   Matrix loader instructions with file type and data format
     *                            details.
     * @return  List of Maps for table name and their respective aspects
     */
    public List<Map<String, Table>> mapHapMapAspects(
        LoaderInstruction loaderInstruction
    ) throws GobiiException {


        return new ArrayList<>();
    }

    private Map<String, Object> getHapMapAspectValuesMap(String[] fileColumns) {
        return new HashMap<>();
    }



}
