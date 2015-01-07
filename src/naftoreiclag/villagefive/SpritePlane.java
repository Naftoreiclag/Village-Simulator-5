/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;

public class SpritePlane
{
    List<Sprite> sprites = new ArrayList<Sprite>();
    
    public int width;
    public int height;
    
    ViewPort viewPort;
    Node rootNode;
    public SpritePlane(ViewPort viewPort)
    {
        this.viewPort = viewPort;
        rootNode = new Node();
        rootNode.updateGeometricState();
        this.viewPort.attachScene(rootNode);
        
        this.width = viewPort.getCamera().getWidth();
        this.height = viewPort.getCamera().getHeight();
    }
    
    public void add(Sprite sprite)
    {
        rootNode.attachChild(sprite.picture);
        rootNode.updateGeometricState();
    }
    
    public void tick(float tpf)
    {
        
    }
}
