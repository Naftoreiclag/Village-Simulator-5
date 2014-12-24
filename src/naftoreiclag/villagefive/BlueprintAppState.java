/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.KeyKeys;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
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
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.ui.Picture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


import naftoreiclag.villagefive.util.BlueprintGeoGen;
import naftoreiclag.villagefive.util.SmoothAnglef;
import naftoreiclag.villagefive.util.SmoothScalarf;

import org.lwjgl.BufferUtils;

public class BlueprintAppState extends AbstractAppState implements ActionListener, AnalogListener
{
    public BlueprintAppState()
	{
	    plotData.setWidth(7);
	    plotData.setHeight(5);
	}

	private Main app;
    private Node trueRootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private Camera cam;
    private SmoothAnglef camAng;
    private ViewPort viewPort;
	private RenderManager renderManager;
    
    Plot plotData = new Plot();
    private List<Flag> flags = new ArrayList<Flag>();
    
    private Tool tool;

    private void setupAesteticsMispelled()
    {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.4f));
        trueRootNode.addLight(al);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.6f));
        sun.setDirection(new Vector3f(-0.96f, -2.69f, 0.69f).normalizeLocal());
        trueRootNode.addLight(sun);
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 2048, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.5f);
        dlsr.setLambda(0.55f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        viewPort.addProcessor(dlsr);
    }
    public static enum Tool
    {
        drag,
        placeFlag,
        illuminati,
        protractor,
        ruler
    }
    
    private Node rootNode;
    
    private Spatial flag;
	private Spatial helperGrid;
	private Spatial aePaper;
    
    private SmoothScalarf frustumSize = new SmoothScalarf();
    
    private float scrollSpd = 25.0f;
    private boolean leftClick;
	public boolean isDragging;
    
	public Vector3f originalPosition;
	private Vector2f mouseDrag;
	private Vector2f mousePos;

	@Override
	public void initialize(AppStateManager stateManager, Application app)
	{
	    super.initialize(stateManager, app);
	    setupVariableWrappings(app);
        
        setupModels();
        
	    setupInput();
	    setupCamera();
        
	    setupWallpaper();
        
	    rootNode = new Node();
	    trueRootNode.attachChild(rootNode);
        
        setupAesteticsMispelled();
        
        rootNode.attachChild(flag.clone());
	    
	    aePaper = generatePaper();
	    aePaper.move(0, -0.01f, 0);
        aePaper.setShadowMode(RenderQueue.ShadowMode.Receive);
	    rootNode.attachChild(aePaper);
	    Node grid = makeGrid();
        grid.setShadowMode(RenderQueue.ShadowMode.Receive);
	    rootNode.attachChild(grid);
	}
    
    public void placeFlag(Vector2f pos)
    {
        
    }

	@Override
	public void update(float tpf)
	{
	    super.update(tpf);
        tickFrustum(tpf);
	}

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
    
    public void onAnalog(String key, float value, float tpf)
    {
        if(key.equals(KeyKeys.mouse_scroll_up))
        {
            frustumSize.tx -= value * tpf * scrollSpd;
        }
        if(key.equals(KeyKeys.mouse_scroll_down))
        {
            frustumSize.tx += value * tpf * scrollSpd;
        }

        if(key.equals(KeyKeys.mouse_move_up) || key.equals(KeyKeys.mouse_move_down) || key.equals(KeyKeys.mouse_move_left) || key.equals(KeyKeys.mouse_move_right))
        {
            updateMousePos();
        }

        if(leftClick)
        {
            if(key.equals(KeyKeys.mouse_move_up) || key.equals(KeyKeys.mouse_move_down) || key.equals(KeyKeys.mouse_move_left) || key.equals(KeyKeys.mouse_move_right))
            {
                updateMousePos();
                if(mousePos != null && mouseDrag != null)
                {
                    rootNode.setLocalTranslation(new Vector3f(-mouseDrag.x + mousePos.x, -0.1f, -mouseDrag.y + mousePos.y).addLocal(originalPosition));
                    
                    //paper.setLotpfcalTranslation(, tpf, tpf);
                }
            }
        }
    }

    private void onLeftPress(float tpf)
    {
        mouseDrag = mousePos;
        originalPosition = rootNode.getLocalTranslation().clone();
    }

    private void onLeftRelease(float tpf)
    {
        
    }
    
    
    public void updateMousePos()
    {
        Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();

        aePaper.collideWith(ray, results);
        if(results.size() > 0)
        {
            Vector3f res = results.getClosestCollision().getContactPoint();
            mousePos = new Vector2f(res.x, res.z);
        }
        else
        {
            mousePos = null;
        }
    }

    private void setupModels()
    {
        flag = (Spatial) assetManager.loadModel("Models/BlueFlag.mesh.j3o");
        flag.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
    }

    private Geometry generatePaper()
    {
        FloatBuffer v = BufferUtils.createFloatBuffer(12);
        FloatBuffer t = BufferUtils.createFloatBuffer(8);
        IntBuffer i = BufferUtils.createIntBuffer(6);
        
        float b = 2;
        float w = plotData.getWidth() + b;
        float h = plotData.getHeight() + b;
        
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

    private void updateFrustum()
    {
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-1000, 1000, -aspect * frustumSize.x, aspect * frustumSize.x, frustumSize.x, -frustumSize.x);
    }

    private void setupInput()
    {
        inputManager.addListener(this, KeyKeys.mouse_left);
        inputManager.addListener(this, KeyKeys.mouse_scroll_up);
        inputManager.addListener(this, KeyKeys.mouse_scroll_down);
        inputManager.addListener(this, KeyKeys.mouse_move_up);
        inputManager.addListener(this, KeyKeys.mouse_move_down);
        inputManager.addListener(this, KeyKeys.mouse_move_left);
        inputManager.addListener(this, KeyKeys.mouse_move_right);
    }

    private void setupWallpaper()
    {
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
	    viewPort.setClearFlags(false, true, true);
    }

    private void setupVariableWrappings(Application app)
    {
        this.app = (Main) app;
        this.trueRootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.cam = this.app.getCamera();
        this.viewPort = this.app.getViewPort();
        this.renderManager = this.app.getRenderManager();
    }

    private void setupCamera()
    {
        /*
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.0f));
        trueRootNode.addLight(al);
        */
        
        cam.setParallelProjection(true);
        frustumSize.enableClamp(1, 10);
        updateFrustum();
        
        // top-dwon
        cam.setLocation(Vector3f.UNIT_Y);
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z.mult(-1.0f));
        // iso
        cam.setLocation(Vector3f.UNIT_XYZ);
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }

    private void tickFrustum(float tpf)
    {
        frustumSize.tick(tpf);
        updateFrustum();
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
    
    public Node makeGrid()
    {
        Material wholeMat = assetManager.loadMaterial("Materials/Stroke.j3m");
        wholeMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        wholeMat.setColor("Color", new ColorRGBA(1.0f, 1.0f, 1.0f, 0.8f));
        
        Material halfMat = wholeMat.clone();
        halfMat.setColor("Color", new ColorRGBA(1.0f, 1.0f, 1.0f, 0.2f));
        
        BlueprintGeoGen wholeLines = new BlueprintGeoGen();
        for(float x = 0; x <= plotData.getWidth(); ++ x)
        {
            wholeLines.addLine(x, 0, x, plotData.getHeight());
        }
        for(float y = 0; y <= plotData.getHeight(); ++ y)
        {
            wholeLines.addLine(0, y, plotData.getWidth(), y);
        }
        Mesh wholeMesh = wholeLines.bake(0.02f, 20.0f, 1.0f, 1.0f);
        
        Geometry wholeGeo = new Geometry("", wholeMesh);
        wholeGeo.setMaterial(wholeMat);
        wholeGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        BlueprintGeoGen halfLines = new BlueprintGeoGen();
        for(float x = 0; x < plotData.getWidth(); ++ x)
        {
            halfLines.addLine(x + 0.5f, 0, x + 0.5f, plotData.getHeight());
        }
        for(float y = 0; y < plotData.getHeight(); ++ y)
        {
            halfLines.addLine(0, y + 0.5f, plotData.getWidth(), y + 0.5f);
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
