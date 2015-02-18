/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.scenegraph;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import naftoreiclag.villagefive.SAM;

public class AxesMaker
{
    public static final Material xm;;
    public static final Material ym;
    public static final Material zm;
    
    static
    {
        xm = new Material(SAM.a, "Common/MatDefs/Misc/Unshaded.j3md");
        ym = xm.clone();
        zm = xm.clone();
        
        xm.setColor("Color", ColorRGBA.Red);
        ym.setColor("Color", ColorRGBA.Green);
        zm.setColor("Color", ColorRGBA.Blue);
    }
        
    public static Node make()
    {
        Node ret = new Node();
        
        Arrow x = new Arrow(Vector3f.UNIT_X);
        Geometry xg = new Geometry("arrow", x);
        xg.setMaterial(xm);
        ret.attachChild(xg);
        
        Arrow y = new Arrow(Vector3f.UNIT_Y);
        Geometry yg = new Geometry("arrow", y);
        yg.setMaterial(ym);
        ret.attachChild(yg);
        
        Arrow z = new Arrow(Vector3f.UNIT_Z);
        Geometry zg = new Geometry("arrow", z);
        zg.setMaterial(zm);
        ret.attachChild(zg);
        
        return ret;
    }
}
