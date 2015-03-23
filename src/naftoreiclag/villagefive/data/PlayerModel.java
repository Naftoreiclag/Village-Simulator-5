/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.data;

import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.scene.Node;

public interface PlayerModel
{
    public static final String anim_walk = "gdfsagrearewfgrethrhfsd";
    public static final String anim_standstill = "grafWEDClo;iu56ygfferwehr";
    public static final String anim_closeEye = "reagsdgerfesdi676trefrwehr";
    public static final String anim_openEye = "grahfdgfdWEhhrtgdffgrwehr";
    
    public abstract void playAnimation(String name);
    public abstract void load();
    public abstract Node getNode();
}
