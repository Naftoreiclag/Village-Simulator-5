/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class Inventory implements JSONAware {

    public Map<Integer, InvItem> items = new HashMap<Integer, InvItem>();
    
    public String toJSONString() {
        JSONObject object = new JSONObject();
        for(Map.Entry<Integer, InvItem> entry : items.entrySet()) {
            object.put(entry.getKey(), entry.getValue());
        }
        
        return object.toJSONString();
    }
    
    public int findDeepestItem() {
        int max = -1;
        for(Map.Entry<Integer, InvItem> entry : items.entrySet()) {
            if(entry.getKey() > max) {
                max = entry.getKey();
            }
        }
        
        return max;
    }
    
    public int findFirstOpenSlot() {
        int deep = findDeepestItem();
        
        for(int i = 0; i < deep; ++ i) {
            if(!items.containsKey(i)) {
                return i;
            }
        }
        
        return deep + 1;
    }

    public void addItem(InvItem i) {
        int firstSlot = findFirstOpenSlot();
        
        items.put(firstSlot, i);
    }
}
