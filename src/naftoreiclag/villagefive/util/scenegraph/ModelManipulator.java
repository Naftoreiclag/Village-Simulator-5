/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.scenegraph;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;

public class ModelManipulator
{
    public static AssetManager assetManager;

    public static Node loadNode(String modelName)
    {
        if(modelName == null)
        {
            Node node = new Node();
            return node;
        }
        
        Node node = (Node) assetManager.loadModel(modelName);
        return node;
    }
    
    public static Control[] getControls(Node model)
    {
        int numControls = model.getNumControls();
        
        Control[] controls = new Control[numControls];
        for(int i = 0; i < numControls; ++ i)
        {
            controls[i] = model.getControl(i);
        }
        
        return controls;
    }
}
