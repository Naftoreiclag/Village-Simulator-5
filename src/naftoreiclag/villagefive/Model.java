/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

// Simple way to interface with an complex spatial instance

import com.jme3.scene.Node;
import java.util.List;

public class Model
{
    public final String name;
    public final Node body;
    
    public Model(String name, Node body)
    {
        this.name = name;
        this.body = body;
    }
    
    public List<String> listAnimations()
    {
        return null;
    }
    
    public void playAnimation(String animation)
    {
        
    }
}
