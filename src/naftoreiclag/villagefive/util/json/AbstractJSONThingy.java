/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.json;

import org.json.simple.JSONObject;

public abstract class AbstractJSONThingy implements JSONThingy
{
    private long jsonIndex = 0;

    public long getJsonIndex()
    {
        return jsonIndex;
    }

    public void setJsonIndex(long index)
    {
        this.jsonIndex = index;
    }

    public String toJSONString()
    {
        JSONObject dummy = new JSONObject();
        this.populateJson(dummy);
        return dummy.toJSONString();
    }
    // what the
    public abstract JSONThingy createFromJson(JSONObject data);
}
