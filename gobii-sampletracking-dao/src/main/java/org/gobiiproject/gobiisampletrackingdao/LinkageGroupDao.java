package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.LinkageGroup;

import java.util.List;

public interface LinkageGroupDao {

    List<LinkageGroup> getLinkageGroups(Integer pageSize, Integer rowOffset,
                                        Integer linkageGroupId, Integer mapsetId);

}
