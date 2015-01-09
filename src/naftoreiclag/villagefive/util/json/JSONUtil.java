/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.json;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// For writing and reading JSONArrays with ease!
// Keeps track of those pesky indexes for you!
public class JSONUtil
{
    public static <SomeJSONThingy extends JSONThingy> JSONArray convertToArray(List<SomeJSONThingy> j)
    {
        JSONArray conv = new JSONArray();
        
        for(int i = 0; i < j.size(); ++ i)
        {
            SomeJSONThingy thing = j.get(i);
            
            thing.setJsonIndex(i);
            conv.add(thing);
        }
        
        return conv;
    }
    
    public static <ConcreteJSONThingy extends JSONThingy> List<ConcreteJSONThingy> decodeArray(JSONArray array, Class<ConcreteJSONThingy> type)
    {
        List<ConcreteJSONThingy> newList = new ArrayList<ConcreteJSONThingy>();
        
        for(int i = 0; i < array.size(); ++ i)
        {
            ConcreteJSONThingy thing = null;
            try
            {
                thing = type.getConstructor(JSONObject.class).newInstance(array.get(i));
            }
            catch(Exception ex)
            {
                Logger.getLogger(JSONThingy.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            newList.add(thing);
            
        }
        
        return newList;
    }
}
