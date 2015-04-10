/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import java.util.HashMap;
import java.util.Map;
import naftoreiclag.villagefive.inventory.InventoryRenderAppState;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class Inventory implements JSONAware {
    public InventoryRenderAppState state;
    public Map<Integer, InvItem> items = new HashMap<Integer, InvItem>();
    
    private void callListeners(int slot) {
        if(state != null) {
            state.onUpdate(this, slot);
        }
    }
    
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
        i.inv = this;
        items.put(firstSlot, i);
        callListeners(firstSlot);
    }

    public InvItem getItem(int slotI) {
        if(items.containsKey(slotI)) {
            return items.get(slotI);
        } else {
            return null;
        }
    }
    
    public void deleteItem(InvItem i) {
        
        // Avoid concurrent modification!
        
        int removeMe = -1;
        for(Map.Entry<Integer, InvItem> entry : items.entrySet()) {
            if(entry.getValue() == i) {
                removeMe = entry.getKey();
            }
        }
        
        if(removeMe != -1) {
            items.remove(removeMe);
            callListeners(removeMe);
            
        }
    }
}
