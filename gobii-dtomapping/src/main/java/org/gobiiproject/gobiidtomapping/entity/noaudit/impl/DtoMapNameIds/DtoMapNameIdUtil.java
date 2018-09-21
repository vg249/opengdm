package org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIds;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by VCalaminos on 9/20/2018.
 */
public class DtoMapNameIdUtil {


    public static Integer getIdFromResultSet(List<NameIdDTO> nameIdDTOList, ResultSet resultSet, String columnName, String columnId) throws SQLException {

        return DtoMapNameIdUtil.getIdFromResultSet(nameIdDTOList, resultSet, columnName, columnId, GobiiFilterType.NAMES_BY_NAME_LIST);
    }

    public static Integer getIdFromResultSet(List<NameIdDTO> nameIdDTOList, ResultSet resultSet, String columnName, String columnId, GobiiFilterType gobiiFilterType) throws SQLException {

        Collections.sort(nameIdDTOList);
        Integer index;

        List<NameIdDTO> nameList = new ArrayList<>();

        while (resultSet.next()) {

            index = searchIndex(nameIdDTOList, resultSet.getString(columnName));

            if (index > -1) {

                NameIdDTO nameIdDTO  = nameIdDTOList.get(index);
                nameIdDTO.setId(resultSet.getInt(columnId));
                nameIdDTOList.set(index, nameIdDTO);

                if (gobiiFilterType == GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_ABSENT) {
                    nameIdDTOList.remove(nameIdDTOList.get(index));

                } else if (gobiiFilterType == GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_EXISTS) {
                    nameList.add(nameIdDTO);
                }

            }

        }

        if (gobiiFilterType == GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_EXISTS) {

            nameIdDTOList.removeAll(nameIdDTOList);
            nameIdDTOList.addAll(nameList);
        }

        return resultSet.getFetchSize();

    }


    public static int searchIndex(List<NameIdDTO> nameIdDTOList, String name) {
        int start = 0;
        int end = nameIdDTOList.size() - 1;
        int mid;

        while (start <= end) {
            mid = (start + end) / 2;

            if (nameIdDTOList.get(mid).getName().compareTo(name) < 0){
                start = mid + 1;
            } else if (nameIdDTOList.get(mid).getName().compareTo(name) > 0) {
                end = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }


}
