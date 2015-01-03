/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.serializable.PlotSerial;
import naftoreiclag.villagefive.world.World;
import org.json.simple.JSONObject;

public class SaveLoad
{
    public static void save(OverworldAppState data)
    {
        JSONObject obj = new JSONObject();
        obj.put("name", "foo");
        System.out.print(obj);
    }
    public static void save(PlotSerial data)
    {
        JSONObject obj = new JSONObject();
        obj.put("plots", data);
        System.out.print(obj);
    }
    public static void save(World data)
    {
        JSONObject obj = new JSONObject();
        obj.put("worlds", data);
        System.out.print(obj);
    }
}
