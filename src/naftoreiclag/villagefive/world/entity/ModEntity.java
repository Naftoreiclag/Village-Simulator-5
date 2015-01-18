/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import naftoreiclag.villagefive.PluginResourceManager;
import naftoreiclag.villagefive.PluginEntity;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;
import naftoreiclag.villagefive.world.body.EntityBody;
import org.dyn4j.geometry.Circle;
import org.json.simple.parser.ParseException;

public class ModEntity extends Entity
{
    public final PluginEntity data;

    ModEntity(PluginEntity entity)
    {
        this.data = entity;
    }

    Entity duplicate()
    {
        return new ModEntity(data);
    }
    
    @Override
    public String getTypeName()
    {
        return data.name;
    }

    @Override
    public void createNode()
    {
        node = new Node();
        try
        {
            System.out.println(this.data.model);
            Spatial geo = PluginResourceManager.loadModel("std:" + this.data.model);
            
            System.out.println(geo);
            
            node.attachChild(geo);
        }
        catch(IOException ex)
        {
            Logger.getLogger(ModEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(ParseException ex)
        {
            Logger.getLogger(ModEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createBody()
    {
        Vec2 location = this.getLocation();
        body = new EntityBody(this);
        body.addFixture(new Circle(1), 5);
        body.setMass();
       
        this.setLocation(location);
    }

}
