package org.gobiiproject.gobiisampletrackingdao;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Map<String, Object> getHints(EntityManager em, String graphName) {
        EntityGraph<?> graph = em.getEntityGraph(graphName);
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);
        return hints;
    }
}
