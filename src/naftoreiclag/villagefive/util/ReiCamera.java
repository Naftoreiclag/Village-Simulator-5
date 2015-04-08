/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.util;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

// Build a better mousetrap
public class ReiCamera {
    private final Camera camera;
    
    public final Vector3f targetLocation;
    public final Vector3f linearInterpLocation;
    public final Vector3f smoothInterpLocation;
    
    public final Vector3f targetView;
    public final Vector3f linearInterpView;
    public final Vector3f smoothInterpView;
    
    public float maxSpd = 5.0f;
    public float smoothing = 5f;

    public ReiCamera(Camera cam) {
        this.camera = cam;

        this.targetLocation = camera.getLocation().clone();
        this.linearInterpLocation = this.targetLocation.clone();
        this.smoothInterpLocation = this.targetLocation.clone();
        this.targetView = targetLocation.add(camera.getDirection());
        this.linearInterpView = this.targetView.clone();
        this.smoothInterpView = this.targetView.clone();
    }
    
    public void setMode(SmoothMode mode) {
        this.mode = mode;
    }
    
    public SmoothMode mode = SmoothMode.linear;

    public void setLocation(Vector3f where) {
        targetLocation.set(where);
    }

    public void setLocation(float x, float y, float z) {
        targetLocation.set(x, y, z);
    }

    public void lookAt(Vector3f where) {
        targetView.set(where);
    }

    void lookAt(float x, float y, float z) {
        targetView.set(x, y, z);
    }

    public void tick(float deltaT) {
        if(mode == SmoothMode.none) {
            return;
        }

        // Interpolate linearly
        float dummyLocDist = linearInterpLocation.distance(targetLocation);
        if(dummyLocDist > maxSpd * deltaT) {
            linearInterpLocation.addLocal(targetLocation.subtract(linearInterpLocation).normalizeLocal().multLocal(maxSpd * deltaT));
            linearInterpView.addLocal(targetView.subtract(linearInterpView).divideLocal(dummyLocDist / (maxSpd * deltaT)));
        } else {
            linearInterpLocation.set(targetLocation);
            linearInterpView.set(targetView);

        }

        // Depending on the smoothing mode, update the camera position accordingly
        if(mode == SmoothMode.linear) {
            getCamera().setLocation(linearInterpLocation);
            getCamera().lookAt(linearInterpView, Vector3f.UNIT_Y);
        } else if(mode == SmoothMode.smooth) {
            smoothInterpLocation.addLocal(targetLocation.subtract(smoothInterpLocation).multLocal(deltaT * smoothing));
            smoothInterpView.addLocal(targetView.subtract(smoothInterpView).multLocal(deltaT * smoothing));

            getCamera().setLocation(smoothInterpLocation);
            getCamera().lookAt(smoothInterpView, Vector3f.UNIT_Y);
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public static enum SmoothMode {
        none,
        linear,
        smooth
    }
}
