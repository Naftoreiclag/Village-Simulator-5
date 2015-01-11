/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.S;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

// Hold information about a particular person
public class Resident implements JSONAware
{
    public long SID;
    
    String name;
    List<Long> propertySIDs = new ArrayList<Long>();
    long entitySID;

    public Resident()
    {
        SID = World.nextSid();
    }
    
    public Resident(JSONObject data)
    {
        this.SID = (Long) data.get("SID");
        this.name = (String) data.get("name");
        this.entitySID = (Long) data.get("entitySID");
        this.propertySIDs = (List<Long>) data.get("propertySIDs");
    }
    
    public String toJSONString()
    {
        JSONObject obj = new JSONObject();
        
        obj.put("SID", SID);
        obj.put("name", name);
        obj.put("entitySID", entitySID);
        obj.put("propertySIDs", propertySIDs);
        
        return obj.toJSONString();
    }

}
