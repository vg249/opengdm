package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerStartStopDTO;
import org.gobiiproject.gobiimodel.entity.Marker;

import javax.persistence.Tuple;
import java.util.List;

public interface MarkerDao {

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param markerId
     * @param datasetId
     * @return
     */
    List<Marker> getMarkers(Integer pageNum, Integer pageSize,
                            Integer markerId, Integer datasetId);

    /**
     * Gets the DTO of Marker Entity with Start and Stop from MarkerLinkageGroup Entities
     * with MarkerLinkageGroups Left Joined to the Markers
     *
     * For InnerJoin use the getMarkerLinkageGroups method in MarkerLinkageGroupDao,
     * as ManyToOne association is defined in the MarkerLinkageGroup Entity
     * @return List of Tuple(MarkerEntity, MarkerLinkageGroupEntity)
     */
    List<MarkerStartStopDTO> getMarkerStartStopDTOs(Integer pageNum, Integer pageSize,
                                                    Integer markerId, Integer datasetId);

}
