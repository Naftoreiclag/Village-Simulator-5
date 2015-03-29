/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.File;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.addon.AddonEntityInfo;
import naftoreiclag.villagefive.addon.AddonManager;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.PhysWorld;
import naftoreiclag.villagefive.world.body.EntityBody;
import org.dyn4j.geometry.Circle;

public class AddonEntity extends Entity
{
    static
    {
        SAM.ASSETS.registerLocator("addons", FileLocator.class);
    }
    
    public final AddonEntityInfo data;

    AddonEntity(AddonEntityInfo entity)
    {
        this.data = entity;
    }

    Entity duplicate()
    {
        return new AddonEntity(data);
    }
    
    @Override
    public String getEntityId()
    {
        return data.parent.id + ":" + data.id;
    }

    @Override
    public void createNode()
    {
        node = new Node();
        Spatial geo = SAM.ASSETS.loadModel(data.dir + "\\" + data.model.meshFile);
        data.model.materialOverride.modify(geo);

        node.attachChild(geo);
    }

    @Override
    public void createBody(PhysWorld world)
    {
        Vec2 location = this.getLocation();

        body = new EntityBody(this);
        this.setLocation(location);
        body.addFixture(new Circle(data.radius), 5);
        world.addBody(body);

    }

}
