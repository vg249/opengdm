package org.gobiiproject.gobiiprocess.digester.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructionHashmap<String, String, String> {

    private Map<String,Map<String,String>> mkMap;

    public InstructionHashmap(){
        mkMap = new HashMap<String,Map<String,String>>();
    }

    public String put(String k1, String k2, String v) {
        Map<String,String> k2Map = null;
        if(mkMap.containsKey(k1)) {
            k2Map = mkMap.get(k1);
        }else {
            k2Map = new HashMap<String,String>();
            mkMap.put(k1, k2Map);
        }
        return k2Map.put(k2, v);
    }

    public boolean containsKey(String k1, String k2) {
        if(mkMap.containsKey(k1)) {
            Map<String,String> k2Map = mkMap.get(k1);
            return k2Map.containsKey(k2);
        }
        return false;
    }

    public boolean containsKey(String k1) {
        return mkMap.containsKey(k1);
    }

        public String get(String k1, String k2) {
        if(mkMap.containsKey(k1)) {
            Map<String,String> k2Map = mkMap.get(k1);
            return k2Map.get(k2);
        }
        return null;
    }

    public Map<String,String> get(String k1) {
        return mkMap.get(k1);
    }


    public String getBySubKey(String k2) {
        for(Map<String,String> m : mkMap.values()) {
            if(m.containsKey(k2))
                return m.get(k2);
        }
        return null;
    }

    public String remove(String k1, String k2) {
        if(mkMap.containsKey(k1)) {
            Map<String,String> k2Map = mkMap.get(k1);
            return k2Map.remove(k2);
        }
        return null;
    }


    private Map<String,String> remove(String k1) {
        return mkMap.remove(k1);
    }

    public int size() {
        int size = 0;
        for(Map<String,String> m : mkMap.values()) {
            size++;
            size += m.size();
        }
        return size;
    }


    public List<String> getAllItems(){
        List<String> items = new ArrayList<String>();
        for(Map<String,String> m : mkMap.values()) {
            for(String v : m.values()) {
                items.add(v);
            }
        }
        return items;
    }


    public void clear() {
        for(Map<String,String> m : mkMap.values())
            m.clear();

        mkMap.clear();
    }

}
