package org.gobiiproject.gobiisampletrackingdao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gobiiproject.gobiimodel.entity.Platform;

public class PlatformDaoImpl implements PlatformDao {

    @PersistenceContext
    EntityManager em;
    
    @Override
    public Platform createPlatform(Platform platform) {
        em.persist(platform);
        em.flush();
        em.refresh(platform, this.getHints());
        return platform;
    }

    private Map<String, Object> getHints() {
        EntityGraph<?> graph = em.getEntityGraph("graph.platform");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);
        return hints;    
    }
    
}