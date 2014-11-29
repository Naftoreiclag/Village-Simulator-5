/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector2f;

public class PlayerController extends EntityController implements ActionListener
{
    public Entity puppet;
    
    public void setEntity(Entity entity)
    {
        this.puppet = entity;
    }
    
    float speed = 1f;
    
    boolean turningLeft = false;
    boolean turningRight = false;
    boolean movingFwd = false;
    boolean movingBwd = false;

    public void tick(float tpf)
    {
        if(!movingFwd && !movingBwd && !turningLeft && !turningRight)
        {
            return;
        }
        
        Vector2f dir = new Vector2f();
        
        if(movingFwd)
        {
            dir.x += 1;
        }
        if(movingBwd)
        {
            dir.x -= 1;
        }
        if(turningLeft)
        {
            dir.y -= 1;
        }
        if(turningRight)
        {
            dir.y += 1;
        }
        
        dir.normalizeLocal();
        dir.mult(tpf * speed);
        
        puppet.move(dir);
    }

    public void onAction(String key, boolean isPressed, float tpf)
    {
        System.out.println("key " + key + " = " + isPressed + ";");
        if(key.equals("Walk Forward"))
        {
            movingFwd = isPressed;
        }
        if(key.equals("Walk Backward"))
        {
            movingBwd = isPressed;
        }
        if(key.equals("Rotate Left"))
        {
            turningLeft = isPressed;
        }
        if(key.equals("Rotate Right"))
        {
            turningRight = isPressed;
        }
    }

}
