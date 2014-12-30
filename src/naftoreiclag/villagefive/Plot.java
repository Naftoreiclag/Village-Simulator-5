/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.scene.Node;

public class Plot
{
    PlotSerial data;
    World world;
    Node node;
    
    Plot(PlotSerial data, World world, Node node)
    {
        this.data = data;
        this.world = world;
        this.node = node;
    }

}
