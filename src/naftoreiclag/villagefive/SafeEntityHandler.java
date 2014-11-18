/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive;

import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;

class SafeEntityHandler
{
    List<Entity> entities = new ArrayList<Entity>();
    
    Node rootNode;
    
    SafeEntityHandler(Node rootNode)
    {
        this.rootNode = rootNode;
    }

    void attachEntity(Entity entity)
    {
        entities.add(entity);
        entity.bindSceneGraph(rootNode);
    }
}
