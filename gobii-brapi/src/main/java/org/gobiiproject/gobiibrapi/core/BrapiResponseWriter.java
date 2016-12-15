package org.gobiiproject.gobiibrapi.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Pagination;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseWriter<T_LIST_ITEM, T_LIST_META> {


    Class<T_LIST_ITEM> listItemType;
    Class<T_LIST_META> listMetaType;

    private ObjectMapper objectMapper = new ObjectMapper();

    public BrapiResponseWriter(Class<T_LIST_ITEM> listItemType, Class<T_LIST_META> listMetaType) {
        this.listItemType = listItemType;
        this.listMetaType = listMetaType;
        //objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
        //objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        //objectMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);

    }
//    return("{\n" +
//            "  \"metadata\" : {\n" +
//            "    \"pagination\" : {\n" +
//            "      \"pageSize\" : 0,\n" +
//            "      \"currentPage\" : 0,\n" +
//            "      \"totalCount\" : 0,\n" +
//            "      \"totalPages\" : 0\n" +
//            "    },\n" +
//            "    \"status\" : [ ],\n" +
//            "    \"datafiles\" : [ ]\n" +
//            "  },\n" +
//            "  \"result\" : {\n" +
//            "    \"data\" : [ {\n" +
//            "      \"call\" : \"calls\",\n" +
//            "      \"datatypes\" : [ \"json\" ],\n" +
//            "      \"methods\" : [ \"GET\" ]\n" +
//            "    }]\n" +
//            "  }\n" +
//            "}");
//
//}


    private final String BRAPI_KEY_METADATA = "metadata";
    private final String BRAPI_KEY_METADATA_PAGINATION = "pagination";
    private final String BRAPI_KEY_METADATA_STATUS = "status";
    private final String BRAPI_KEY_METADATA_DATAFILES = "datafiles";
    private final String BRAPI_KEY_RESULT = "result";
    private final String BRAPI_KEY_RESULT_DATA = "data";


    public String makeBrapiResponse(List<T_LIST_ITEM> listItems,
                                    T_LIST_META listMetadData,
                                    Pagination pagination,
                                    List<Map<String, String>> statusItems,
                                    List<String> dataFiles
    ) throws Exception {

        JsonObject jsonObjectRoot = new JsonObject();
        // jsonObjectRoot.add(BRAPI_KEY_METADATA,new JsonObject());


        Gson gson = new GsonBuilder().create();

        BrapiMetaData brapiMetaData = new BrapiMetaData();
        if (pagination != null) {
            brapiMetaData.setPagination(pagination);
        }
        //jsonObjectRoot.get(BRAPI_KEY_METADATA).getAsJsonObject().add(BRAPI_KEY_METADATA_PAGINATION, );

        if (statusItems != null) {
            brapiMetaData.setStatus(statusItems);
        }

        if (dataFiles != null) {
            brapiMetaData.setDatafiles(dataFiles);
        }

        String metaDataAsString = this.objectMapper.writeValueAsString(brapiMetaData);
        jsonObjectRoot.addProperty(BRAPI_KEY_METADATA, gson.toJson(metaDataAsString));

        BrapiResult<T_LIST_ITEM> brapiResult = new BrapiResult<>(listItemType);
        brapiResult.setData(listItems);
        if (listMetadData == null) {

            String resultAsString = objectMapper.writeValueAsString(brapiResult);
            jsonObjectRoot.addProperty(BRAPI_KEY_RESULT, gson.toJson(resultAsString));
        }


        String returnVal = jsonObjectRoot.toString().replace("\\","").replace("\"\"","");
        return returnVal;

    }
}
