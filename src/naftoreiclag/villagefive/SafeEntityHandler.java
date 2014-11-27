/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive;

import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;

public class SafeEntityHandler
{
    List<Entity> entities = new ArrayList<Entity>();
    Node rootNode;
    Space space;

    SafeEntityHandler(Node rootNode, Space space)
    {
        this.rootNode = rootNode;
        this.space = space;
    }

    void attachEntity(Entity entity)
    {
        entities.add(entity);
        entity.bindSceneGraph(rootNode);
        entity.bindPhysics(space);
    }

    void update(float tpf)
    {
        for(Entity e : entities)
        {
            e.update(tpf);
        }
        
    }
}
