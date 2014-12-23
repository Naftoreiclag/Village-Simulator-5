/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.ui.Picture;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector2d;
import naftoreiclag.villagefive.util.BlueprintGeoGen;
import org.lwjgl.BufferUtils;

public class BlueprintAppState extends AbstractAppState implements ActionListener, AnalogListener
{
    private Main app;
    private Node rootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private Camera cam;
    private ViewPort viewPort;
    
    
    
    Geometry grid;
    
    Geometry paper;
    
    List<Flag> flags = new ArrayList<Flag>();
    
    boolean leftClick;
    private RenderManager renderManager;

    public void onAction(String key, boolean isPressed, float tpf)
    {
        if(key.equals(KeyKeys.mouse_left))
        {
            leftClick = isPressed;
            
            if(isPressed)
            {
                this.onLeftPress(tpf);
            }
            else
            {
                this.onLeftRelease(tpf);
            }
        }
    }
    
    float scrollSpd = 25.0f;

    public boolean isDragging;
    
    public Vector3f originalPosition;
    
    public void onAnalog(String key, float value, float tpf)
    {
        if(key.equals(KeyKeys.mouse_scroll_up))
        {
            System.out.println(value);
            
            frustumSize -= value * tpf * scrollSpd;
            setFrustum();
            
        }
        if(key.equals(KeyKeys.mouse_scroll_down))
        {
            System.out.println(value);
            
            frustumSize += value * tpf * scrollSpd;
            setFrustum();
        }
        
        if(leftClick)
        {
            if(key.equals(KeyKeys.mouse_move_up) || key.equals(KeyKeys.mouse_move_down) || key.equals(KeyKeys.mouse_move_left) || key.equals(KeyKeys.mouse_move_right))
            {
                updateMousePos();
                if(mousePos != null && mouseDrag != null)
                {
                    paper.setLocalTranslation(new Vector3f(-mouseDrag.x + mousePos.x, -0.1f, -mouseDrag.y + mousePos.y).addLocal(originalPosition));
                    
                    //paper.setLotpfcalTranslation(, tpf, tpf);
                }
            }
        }
    }

    private void onLeftPress(float tpf)
    {
        updateMousePos();
        mouseDrag = mousePos;
        originalPosition = paper.getLocalTranslation().clone();
    }

    private void onLeftRelease(float tpf)
    {
        
    }
    
    public boolean mousePosAlreadyUpdated = false;
    private Vector2f mousePos;
    private Vector2f lastMousePos;
    private Vector2f mouseDrag;
    public void updateMousePos()
    {
        if(mousePosAlreadyUpdated)
        {
            return;
        }
        
        lastMousePos = mousePos;
        
        Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();

        paper.collideWith(ray, results);
        if(results.size() > 0)
        {
            Vector3f res = results.getClosestCollision().getContactPoint();
            mousePos = new Vector2f(res.x, res.z);
        }
        else
        {
            mousePos = null;
        }
        
        mousePosAlreadyUpdated = true;
    }

    private Geometry generatePaper()
    {
        FloatBuffer v = BufferUtils.createFloatBuffer(12);
        FloatBuffer t = BufferUtils.createFloatBuffer(8);
        IntBuffer i = BufferUtils.createIntBuffer(6);
        
        float b = 2;
        float w = plot.getWidth() + b;
        float h = plot.getHeight() + b;
        
        float tw = w + (2 * b);
        float th = h + (2 * b);
        
        tw /= 5;
        th /= 5;
        
        v.put(-b).put(0).put(-b);
        v.put(-b).put(0).put(h);
        v.put(w).put(0).put(h);
        v.put(w).put(0).put(-b);
        
        t.put(0).put(0);
        t.put(0).put(th);
        t.put(tw).put(th);
        t.put(tw).put(0);

        i.put(0).put(1).put(2);
        i.put(0).put(2).put(3);
        
        Mesh mesh = new Mesh();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, t);
        mesh.setBuffer(VertexBuffer.Type.Index,    3, i);

        mesh.updateBound();
        
        Geometry geo = new Geometry("", mesh);
        
        geo.setMaterial(assetManager.loadMaterial("Materials/blueprint.j3m"));
        
        return geo;
    }

    private void setFrustum()
    {
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
    }
    
    public static class Flag
    {
        public final Vector2f loc;
        
        public Flag()
        {
            loc = new Vector2f();
        }
        
        public Flag(float x, float y)
        {
            loc = new Vector2f(x, y);
        }
    }
    
    Plot plot = new Plot();
    
    public BlueprintAppState()
    {
        plot.setWidth(7);
        plot.setHeight(5);
    }
    
    float frustumSize = 10.0f;

    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        
        
        this.app = (Main) app;
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.cam = this.app.getCamera();
        this.viewPort = this.app.getViewPort();
        this.renderManager = this.app.getRenderManager();
        
        Picture wallpaper = new Picture("background");
        Material background = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = assetManager.loadTexture("Textures/blueprintCrumple.jpg");
        texture.setWrap(WrapMode.Repeat);
        background.setTexture("ColorMap", texture);
        wallpaper.setMaterial(background);
        wallpaper.setWidth(Main.width);
        wallpaper.setHeight(Main.height);
        wallpaper.setPosition(0, 0);
        wallpaper.getMesh().setBuffer(Type.TexCoord, 2, new float[]
        {
            0, 0,
            Main.width / 512f, 0,
            Main.width / 512f, Main.height / 512f,
            0, Main.height / 512f
        });

        ViewPort preview = renderManager.createPreView("background", cam);
        preview.setClearFlags(true, true, true);
        preview.attachScene(wallpaper);
        wallpaper.updateGeometricState();
        
        paper = generatePaper();
        paper.move(0, -0.01f, 0);
        rootNode.attachChild(paper);
        
        
        inputManager.addListener(this, KeyKeys.mouse_left);
        inputManager.addListener(this, KeyKeys.mouse_scroll_up);
        inputManager.addListener(this, KeyKeys.mouse_scroll_down);
        inputManager.addListener(this, KeyKeys.mouse_move_up);
        inputManager.addListener(this, KeyKeys.mouse_move_down);
        inputManager.addListener(this, KeyKeys.mouse_move_left);
        inputManager.addListener(this, KeyKeys.mouse_move_right);
        
        Node knight = (Node) assetManager.loadModel("Models/Knight.mesh.j3o");
        //rootNode.attachChild(knight);

        
        viewPort.setClearFlags(false, true, true);
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.0f));
        rootNode.addLight(al);

        viewPort.setBackgroundColor(new ColorRGBA(20f / 255f, 51f / 255f, 101f / 255f, 1.0f));
        
        cam.setParallelProjection(true);
        setFrustum();
    
        
        cam.setLocation(Vector3f.UNIT_Y);
        
        // top-dwon
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z.mult(-1.0f));
        // iso
        cam.setLocation(Vector3f.UNIT_XYZ);
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    
        Node grid = makeGrid();
        rootNode.attachChild(grid);
        
        
    }
    @Override
    public void update(float tpf)
    {
        super.update(tpf);
        
        mousePosAlreadyUpdated = false;
    }
    
    public Node makeGrid()
    {
        Material wholeMat = assetManager.loadMaterial("Materials/Stroke.j3m");
        wholeMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        wholeMat.setColor("Color", new ColorRGBA(1.0f, 1.0f, 1.0f, 0.8f));
        
        Material halfMat = wholeMat.clone();
        halfMat.setColor("Color", new ColorRGBA(1.0f, 1.0f, 1.0f, 0.2f));
        
        BlueprintGeoGen wholeLines = new BlueprintGeoGen();
        for(float x = 0; x <= plot.getWidth(); ++ x)
        {
            wholeLines.addLine(x, 0, x, plot.getHeight());
        }
        for(float y = 0; y <= plot.getHeight(); ++ y)
        {
            wholeLines.addLine(0, y, plot.getWidth(), y);
        }
        Mesh wholeMesh = wholeLines.bake(0.02f, 20.0f, 1.0f, 1.0f);
        
        Geometry wholeGeo = new Geometry("", wholeMesh);
        wholeGeo.setMaterial(wholeMat);
        wholeGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        BlueprintGeoGen halfLines = new BlueprintGeoGen();
        for(float x = 0; x < plot.getWidth(); ++ x)
        {
            halfLines.addLine(x + 0.5f, 0, x + 0.5f, plot.getHeight());
        }
        for(float y = 0; y < plot.getHeight(); ++ y)
        {
            halfLines.addLine(0, y + 0.5f, plot.getWidth(), y + 0.5f);
        }
        Mesh halfMesh = halfLines.bake(0.02f, 20.0f, 1.0f, 1.0f);
        
        
        Geometry halfGeo = new Geometry("", halfMesh);
        halfGeo.setMaterial(halfMat);
        halfGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        Node ret = new Node();
        ret.attachChild(wholeGeo);
        ret.attachChild(halfGeo);
        
        return ret;
    }
}
