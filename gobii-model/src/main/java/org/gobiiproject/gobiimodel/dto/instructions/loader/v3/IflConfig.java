package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import java.util.List;
import java.util.Map;

public class IflConfig {
    private Map<String, List<String>> loadOrder;

    public Map<String, List<String>> getLoadOrder() {
        return loadOrder;
    }

    public void setLoadOrder(Map<String, List<String>> loadOrder) {
        this.loadOrder = loadOrder;
    }
}
