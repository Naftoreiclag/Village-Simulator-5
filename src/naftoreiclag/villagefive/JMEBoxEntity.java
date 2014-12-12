/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.export.JmeExporter;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.xml.XMLExporter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.io.File;
import java.io.IOException;

public class JMEBoxEntity extends Entity
{
    @Override
    public void meow()
    {
        System.out.println("box");
    }
    
    @Override
    public void assertNode(Node newNode)
    {
        super.assertNode(newNode);
        
        Box box = new Box(Vector3f.ZERO, 1.0f, 1.0f, 1.0f);
        Geometry geom = new Geometry("Box", box);

        Material mat = world.assetManager.loadMaterial("Materials/matRetry.j3m");
        
        
        
        
        exportMat(mat, "oregono");
        
        
        
        Node dummy = world.loadNode("Models/Box.mesh.j3o");
        
        Material lol = ((Geometry) dummy.getChild("Box")).getMaterial().clone();
        exportMat(lol, "wat");
        
        
        
        geom.setMaterial(mat);

        newNode.attachChild(geom);
    }
    
    

    @Override
    public String getModelName()
    {
        return null;
    }

    @Override
    public void tick(float tpf)
    {
    }

    private void exportMat(Material lol, String diff)
    {
        JmeExporter exporter = XMLExporter.getInstance();
        File file = new File("test_dni/" + diff + ".j3m");
        try
        {
            exporter.save(lol, file);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

}
