/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import naftoreiclag.villagefive.util.json.AbstractJSONThingy;
import naftoreiclag.villagefive.util.json.JSONThingy;
import org.json.simple.JSONObject;

// Hold information about a particular person
public class Resident extends AbstractJSONThingy
{
    String name;
    

    public Resident() {}
    
    public Resident(JSONObject data)
    {
        this.name = (String) data.get("name");
    }
    
    @Override
    public JSONThingy createFromJson(JSONObject data)
    {
        return new Resident(data);
    }

    public void populateJson(JSONObject obj)
    {
        obj.put("name", name);
    }

}
