/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.json;

// I hate java

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public interface JSONThingy extends JSONAware
{
    // The JsonIndexes are used only for preserving object references when saving/loading with JSON.
    // Specifically, it refers to the index of this object in a JSONArray and therefore does not require saving.
    
    // public abstract <ConcreteJSONThingy extends JSONThingy> ConcreteJSONThingy fromJson(JSONObject data);
    public abstract long getJsonIndex();
    public abstract void setJsonIndex(long index);
    public abstract void dopeJsonObject(JSONObject obj);
}
