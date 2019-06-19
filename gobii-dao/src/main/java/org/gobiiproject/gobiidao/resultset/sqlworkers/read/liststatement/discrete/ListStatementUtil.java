package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DnaSampleDTO;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by VCalaminos on 9/21/2018.
 */
public class ListStatementUtil {


    public static String generateParsedNameList(List<NameIdDTO> nameIdDTOList) {

        StringBuilder stringBuilder = new StringBuilder();

        for (NameIdDTO nameIdDTO : nameIdDTOList) {

            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }

            stringBuilder.append("'")
                    .append(nameIdDTO.getName())
                    .append("'");

        }

        return stringBuilder.toString();

    }


    public static String generateParsedNameListForDnaSamples(List<NameIdDTO> nameIdDTOList) {

        StringBuilder stringBuilder = new StringBuilder();

        for (NameIdDTO nameIdDTO : nameIdDTOList) {

            //LinkedHashMap queryObject = (LinkedHashMap) nameIdDTO.getQueryObject();

            String paramName = "dnaSampleNum";

            if (nameIdDTO.getParameters().containsKey(paramName)) {

                if (nameIdDTO.getParameters().get(paramName) == null || ! StringUtils.isNumeric(nameIdDTO.getParameters().get(paramName).toString())) {
                    throw new GobiiDaoException("Required NameId parameter value is missing or not numeric: " + paramName);
                }

                if (stringBuilder.length() > 0) {
                    stringBuilder.append(" or ");
                }

                stringBuilder.append("(name='")
                        .append(nameIdDTO.getName())
                        .append("' and num='")
                        .append(nameIdDTO.getParameters().get(paramName).toString())
                        .append("')");

            } else {

                if (stringBuilder.length() > 0) {
                    stringBuilder.append(" or ");
                }

                stringBuilder.append("(name='")
                        .append(nameIdDTO.getName())
                        .append("')");

            }
        }

        return stringBuilder.toString();

    }


}
