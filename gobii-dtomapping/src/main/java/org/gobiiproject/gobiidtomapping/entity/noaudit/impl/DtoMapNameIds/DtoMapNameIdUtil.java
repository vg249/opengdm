package org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIds;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by VCalaminos on 9/20/2018.
 */
public class DtoMapNameIdUtil {


    static Integer getIdFromResultSet(List<NameIdDTO> nameIdDTOList, ResultSet resultSet, String columnName, String columnId) throws SQLException {

        Collections.sort(nameIdDTOList);
        Integer index;

        while (resultSet.next()) {

            index = searchIndex(nameIdDTOList, resultSet.getString(columnName));

            if (index > -1) {

                NameIdDTO nameIdDTO  = nameIdDTOList.get(index);
                nameIdDTO.setId(resultSet.getInt(columnId));
                nameIdDTOList.set(index, nameIdDTO);


            }

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
