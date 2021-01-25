package org.gobiiproject.gobiiprocess.digester.aspectsdigest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.docx4j.openpackaging.io.Load;
import org.gobiiproject.gobiidomain.services.gdmv3.Utils;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.ColumnAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.JsonAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiiprocess.spring.GobiiProcessContextSingleton;
import org.gobiiproject.gobiisampletrackingdao.CvDao;

import java.util.*;

public abstract class AspectDigest {

    final ObjectMapper mapper = new ObjectMapper();

    final String propertyGroupSeparator = ".";

    LoaderInstruction loaderInstruction;

    AspectDigest(LoaderInstruction loaderInstruction) {
        this.loaderInstruction = loaderInstruction;
    }

    abstract public DigesterResult digest(LoaderInstruction loaderInstruction);

    /**
     * @param templateMap
     * @param propertyFieldNames
     * @return
     */
    protected Map<String, List<String>> getFileColumnsApiFieldsMap(
        Map<String, Object> templateMap,
        HashSet<String> propertyFieldNames) {

        if(propertyFieldNames == null) {
            propertyFieldNames = new HashSet<>();
        }
        Map<String, List<String>> fileColumnsApiFieldsMap = new HashMap<>();
        List<String> fileField;

        for(String apiField : templateMap.keySet()) {
            if(propertyFieldNames.contains(apiField)) {
                Map<String, List<String>> properties =
                    (HashMap<String, List<String>>) templateMap.get(apiField);
                for(String property : properties.keySet()) {
                    fileField = properties.get(property);
                    if(fileField.size() > 0) {
                        if(!fileColumnsApiFieldsMap.containsKey(fileField.get(0))) {
                            fileColumnsApiFieldsMap.put(fileField.get(0), new ArrayList<>());
                        }
                        fileColumnsApiFieldsMap.get(fileField.get(0)).add(apiField+"."+property);
                    }
                }
            }
            else {
                fileField = (List<String>) templateMap.get(apiField);
                if (fileField.size() > 0) {
                    if(!fileColumnsApiFieldsMap.containsKey(fileField.get(0))) {
                        fileColumnsApiFieldsMap.put(fileField.get(0), new ArrayList<>());
                    }
                    fileColumnsApiFieldsMap.get(fileField.get(0)).add(apiField);
                }
            }
        }
        return fileColumnsApiFieldsMap;
    }

    protected void setPropertyAspect(Map<String, Object> aspectValues,
                                   ColumnAspect columnAspect,
                                   Map<String, Map<String, Cv>> propertiesCvMaps,
                                   String propertyName,
                                   String propertyGroup,
                                   Map<String, CvGroupTerm> propertyFieldsCvGroupMap
    ) throws GobiiException {

        JsonAspect jsonAspect;
        Map<String, Cv> cvMap;

        if (!aspectValues.containsKey(propertyGroup)) {
            // Initialize and set json aspect for properties field.
            jsonAspect = new JsonAspect();
            aspectValues.put(propertyGroup, jsonAspect);
        }
        else {
            jsonAspect = ((JsonAspect)aspectValues.get(propertyGroup));
        }

        if(!propertiesCvMaps.containsKey(propertyGroup)) {
            cvMap = getCvMapByTerm(propertyFieldsCvGroupMap.get(propertyGroup));
            propertiesCvMaps.put(propertyGroup, cvMap);
        }
        else {
            cvMap = propertiesCvMaps.get(propertyGroup);
        }
        if (cvMap.containsKey(propertyName)) {
            String propertyId = cvMap
                .get(propertyName)
                .getCvId()
                .toString();
            jsonAspect.getJsonMap().put(propertyId, columnAspect);
        }

    }

    private Map<String, Cv> getCvMapByTerm(CvGroupTerm cvGroupTerm) throws GobiiException {
        CvDao cvDao = GobiiProcessContextSingleton.getInstance().getBean(CvDao.class);
        List<Cv> dnaRunPropertiesCvList = cvDao.getCvListByCvGroup(
            cvGroupTerm.getCvGroupName(),
            null);
        return Utils.mapCvNames(dnaRunPropertiesCvList);
    }

}
