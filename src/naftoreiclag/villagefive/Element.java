/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import naftoreiclag.villagefive.util.math.Vec2;

public abstract class Element
{
    public List<Element> followers = new ArrayList<Element>();
    public Element leader = null;
    
    private Vec2 localLoc = new Vec2(0, 0);
    
    public void setDepth(double x)
    {
        this.depth = x;
        
        // Re-sort
        if(this.plane.elements.contains(this))
        {
            this.plane.elements.remove(this);
        }
        this.plane.elements.add(this);
        
        whereSpatialWouldHaveBeenUpdated();
    }
    
    public abstract boolean collides(Vec2 absPoint);
    
    public static Comparator<Element> depthCompare = new Comparator<Element>()
    {
        @Override
        public int compare(Element right, Element left)
        {
            if(right.depth == left.depth)
            {
                return 0;
            }
            else if(right.depth > left.depth)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
    };
    
    protected double depth;
    protected Vec2 absLoc = new Vec2(0, 0);
    protected Vec2 origin = new Vec2(0, 0);
    public SpritePlane plane;
    
    public double width;
    public double height;
    
    public void addFollower(Element glue)
    {
        this.followers.add(glue);
        
        glue.leader = this;
    
    }
    
    
    private void followLeader()
    {
        if(leader != null)
        {
            this.absLoc = this.localLoc.add(leader.absLoc);
        }
        else
        {
            this.absLoc = this.localLoc;
        }
        for(Element e : followers)
        {
            e.followLeader();
        }
        whereSpatialWouldHaveBeenUpdated();
    }
    
    private void updateLoc()
    {
        followLeader();
        
    }
    
    public abstract void whereSpatialWouldHaveBeenUpdated();
    
    public void setOrigin(double x, double y)
    {
        this.origin.set(x, y);
        updateLoc();
    }
    public void setOrigin(float x, float y)
    {
        this.origin.set(x, y);
        updateLoc();
    }
    public void setOrigin(Vec2 newLoc)
    {
        this.origin.set(newLoc);
        updateLoc();
    }

    public void setOriginMid()
    {
        this.origin.set(width / 2d, height / 2d);
        updateLoc();
    }
    
    public void setLoc(double x, double y)
    {
        this.localLoc.set(x, y);
        updateLoc();
    }
    public void setLoc(float x, float y)
    {
        this.localLoc.set(x, y);
        updateLoc();
    }
    public void setLoc(Vec2 newLoc)
    {
        this.localLoc.set(newLoc);
        updateLoc();
    }
    
    

    void setPlane(SpritePlane plane)
    {
        this.plane = plane;
    }

}
