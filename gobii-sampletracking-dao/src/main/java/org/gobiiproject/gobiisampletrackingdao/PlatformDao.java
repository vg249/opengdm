package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Platform;

public interface PlatformDao {

	Platform createPlatform(Platform platform);

	List<Platform> getPlatforms(Integer offset, Integer pageSize, Integer platformTypeId);

	Platform getPlatform(Integer platformId);

	Platform updatePlatform(Platform platform);

	void deletePlatform(Platform platform);
    
}