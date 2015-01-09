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
    // Turn your list into a JSONArray, also generating the indexes.
    public static <SomeJSONThingy extends JSONThingy> JSONArray encodeList(List<SomeJSONThingy> list)
    {
        JSONArray conv = new JSONArray();
        
        for(int i = 0; i < list.size(); ++ i)
        {
            SomeJSONThingy thing = list.get(i);
            
            thing.setJsonIndex(i);
            conv.add(thing);
        }
        
        return conv;
    }
    
    // Turn your list into a JSONArray, but instead of storing the actual object data, store the pointers to the data.
    // In other words, make a JSONArray of indexes of some other JSONArray (that you define) that has the actual object data.
    public static <SomeJSONThingy extends JSONThingy> JSONArray encodePntrList(List<SomeJSONThingy> list)
    {
        JSONArray conv = new JSONArray();
        
        for(int i = 0; i < list.size(); ++ i)
        {
            conv.add(list.get(i).getJsonIndex());
        }
        
        return conv;
    }
    
    // Optional form that makes the code look a little cleaner.
    public static <ConcreteJSONThingy extends JSONThingy> List<ConcreteJSONThingy> readList(JSONObject data, String key, Class<ConcreteJSONThingy> expectedType)
    {
        return readList((JSONArray) data.get(key), expectedType);
    }
    
    // Read a List from a JSONArray, given the expectedType.
    public static <ConcreteJSONThingy extends JSONThingy> List<ConcreteJSONThingy> readList(JSONArray array, Class<ConcreteJSONThingy> expectedType)
    {
        List<ConcreteJSONThingy> newList = new ArrayList<ConcreteJSONThingy>();
        
        for(int i = 0; i < array.size(); ++ i)
        {
            ConcreteJSONThingy thing = null;
            try
            {
                thing = expectedType.getConstructor(JSONObject.class).newInstance(array.get(i));
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
