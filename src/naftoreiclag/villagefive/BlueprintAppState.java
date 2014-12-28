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
import com.jme3.material.RenderState.BlendMode;
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
import naftoreiclag.villagefive.Plot.Decal;
import naftoreiclag.villagefive.Plot.Face;
import naftoreiclag.villagefive.Plot.Vertex;

import naftoreiclag.villagefive.util.BlueprintGeoGen;
import naftoreiclag.villagefive.util.SmoothAnglef;
import naftoreiclag.villagefive.util.SmoothScalarf;
import naftoreiclag.villagefive.util.Vector2d;

import org.lwjgl.BufferUtils;

// This class is a BEAST
// Locations of flags, rooms, and stuff are stored as doubles
public class BlueprintAppState extends AbstractAppState implements ActionListener, AnalogListener
{
    public BlueprintAppState()
	{
	    plotData.setWidth(15);
	    plotData.setHeight(20);
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

    Material strokeMat;
    
    private Node editorRootNode;
    private Node stateRootNode;
    
    private Node b_flag;
    private Node gridDetailed;
    private Geometry gridFreeform;
	private Geometry paper;
    
    private boolean showingGrid = true;
    
    private SmoothScalarf frustumSize = new SmoothScalarf();
    
    private float scrollSpd = 25.0f;
    private boolean leftClick;
    
	private Vector2f screenMouseLoc; // Mouse coords relative to the camera (0,0)
    private Vector2f mouseLoc; // Mouse coords relative to the rootNode
    
    ViewPort preview;
    DirectionalLightShadowRenderer dlsr;

	@Override
	public void initialize(AppStateManager stateManager, Application app)
	{
	    super.initialize(stateManager, app);
	    setupVariableWrappings(app);
        
        setupMaterials();
        setupModels();
        
	    bindKeys();
	    setupCamera();
        
	    setupWallpaper();
        
        stateRootNode = new Node();
        trueRootNode.attachChild(stateRootNode);
        
	    editorRootNode = new Node();
        editorRootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
	    stateRootNode.attachChild(editorRootNode);
        
        setupAesteticsMispelled();
	    
        editorRootNode.attachChild(paper);
        editorRootNode.attachChild(this.gridDetailed);
	}
    
    @Override
    public void cleanup()
    {
        stateRootNode.removeFromParent();
        renderManager.removePreView(preview);
        viewPort.removeProcessor(dlsr);
        
        inputManager.removeListener(this);
        
        storePlotData();
    }

    
    private void setupAesteticsMispelled()
    {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.4f));
        stateRootNode.addLight(al);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.6f));
        sun.setDirection(new Vector3f(-0.96f, -2.69f, 0.69f).normalizeLocal());
        stateRootNode.addLight(sun);
        
        dlsr = new DirectionalLightShadowRenderer(assetManager, 2048, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.5f);
        dlsr.setLambda(0.55f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        viewPort.addProcessor(dlsr);
    }
    
    @Deprecated
    private Flag spawnLooseFlag(Vector2f pos)
    {
        Flag flag = new Flag(pos);
        
        Spatial flagSpt = b_flag.clone();
        flagSpt.setLocalTranslation(pos.x, 0, pos.y);
        flag.setSpatial(flagSpt);
        editorRootNode.attachChild(flagSpt);
        
        flags.add(flag);
        return flag;
    }
    
    private void spawnRoom(Room room)
    {
        room.updateSpatial();
        
        for(Flag f : room.flags)
        {
            f.rooms.add(room);
        }
        
        rooms.add(room);
    }
    
    // Spawn a new flag to split an existing wall
    private Flag spawnSplittingFlag(Flag first, Flag second, double dist)
    {
        System.out.println("spawn");
        /*
         *          new flag
         *            v
         * A----------N----------B
         * 
         */
        
        Vector2d a = first.loc;
        Vector2d b = second.loc;
        Vector2d an = b.subtract(a).normalizeLocal().multiplyLocal(dist);
        Vector2d n = a.add(an);
        System.out.println(a);
        System.out.println(b);
        System.out.println(an);
        System.out.println(n);
        
        Flag newFlag = new Flag();
        newFlag.loc = n;
        System.out.println(newFlag.loc);
        
        Spatial flagSpt = b_flag.clone();
        flagSpt.setLocalTranslation((float) newFlag.loc.a, 0, (float) newFlag.loc.b);
        newFlag.setSpatial(flagSpt);
        editorRootNode.attachChild(flagSpt);
        
        flags.add(newFlag);
        
        
        List<Room> affectedRooms = new ArrayList<Room>();
        
        for(Room r : first.rooms)
        {
            if(!affectedRooms.contains(r))
            {
                affectedRooms.add(r);
            }
        }
        for(Room r : second.rooms)
        {
            if(!affectedRooms.contains(r))
            {
                affectedRooms.add(r);
            }
        }
        
        System.out.println(affectedRooms.size() + " rooms affected ");
        for(Room r : affectedRooms)
        {
            System.out.println(r.flags.size() + " afsaf");
            Flag prevFlag = r.flags.get(r.flags.size() - 1);
            for(int i = 0; i < r.flags.size(); ++ i)
            {
                Flag currFlag = r.flags.get(i);
                
                if((currFlag == second && prevFlag == first) || (currFlag == first && prevFlag == second))
                {
                    System.out.println("inserted");
                    r.flags.add(i, newFlag);
                    newFlag.rooms.add(r);
                    break;
                }
                
                prevFlag = currFlag;
            }
        }
        
        return newFlag;
        
    }
    
    private void spawnDoor(Flag a, Flag b)
    {
        Door door = new Door(a, b);
        
        a.doors.add(door);
        b.doors.add(door);
        
        door.updateSpatial();
        
        doors.add(door);
    }
    

	@Override
	public void update(float tpf)
	{
        super.update(tpf);
        
        if(tool != null)
        {
            tool.tick(tpf);
        }
        tickFrustum(tpf);
	}

	public void onAction(String key, boolean isPressed, float tpf)
    {
        if(key.equals(KeyKeys.mouse_left))
        {
            leftClick = isPressed;
            
            if(isPressed)
            {
                tool.onClick(tpf);
            }
            else
            {
                tool.onClickRelease(tpf);
            }
        }
        
        if(key.equals(KeyKeys.num_1))
        {
            if(isPressed)
            {
                switchTool(dragger, tpf);
                System.out.println("drag");
            }
        }
        if(key.equals(KeyKeys.num_3))
        {
            if(isPressed)
            {
                toggleGridView();
            }
        }
        if(key.equals(KeyKeys.num_5))
        {
            if(isPressed)
            {
                switchTool(this.flagMover, tpf);
                System.out.println("drafla");
            }
        }
        if(key.equals(KeyKeys.num_6))
        {
            if(isPressed)
            {
                switchTool(this.roomer, tpf);
                System.out.println("roomer");
            }
        }
    }
    
    private void toggleGridView()
    {
        if(this.showingGrid)
        {
            this.gridDetailed.removeFromParent();
            editorRootNode.attachChild(this.gridFreeform);
            this.showingGrid = false;
        }
        else
        {
            this.gridFreeform.removeFromParent();
            editorRootNode.attachChild(this.gridDetailed);
            
            this.showingGrid = true;
        }
    }
    
    private void switchTool(Tool other, float tpf)
    {
        tool.onDeselect(tpf);
        tool = other;
        other.onSelect(tpf);
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
            updateMouseLoc();
        }

        if(key.equals(KeyKeys.mouse_move_up) || key.equals(KeyKeys.mouse_move_down) || key.equals(KeyKeys.mouse_move_left) || key.equals(KeyKeys.mouse_move_right))
        {
            updateMouseLoc();
            tool.whileMouseMove(tpf);
        }
    }
    
    
    public void updateMouseLoc()
    {
        Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 1.0f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();

        paper.collideWith(ray, results);
        if(results.size() > 0)
        {
            Vector3f res = results.getClosestCollision().getContactPoint();
            screenMouseLoc = new Vector2f(res.x, res.z);
            mouseLoc = screenMouseLoc.subtract(editorRootNode.getLocalTranslation().x, editorRootNode.getLocalTranslation().z);
        }
        else
        {
            screenMouseLoc = null;
            mouseLoc = null;
        }
    }

    private void setupModels()
    {
        b_flag = (Node) assetManager.loadModel("Models/BlueFlag.mesh.j3o");
        //b_flag.scale(0.3f);
        //flagger.setFlagModel();
        
        
	    paper = makePaperGeo();
        paper.move(0, -0.01f, 0);
	    gridDetailed = makeDetailedGridNode();
	    gridFreeform = makeFreeformGridNode();
    }


    private void updateFrustum()
    {
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-1000, 1000, -aspect * frustumSize.x, aspect * frustumSize.x, frustumSize.x, -frustumSize.x);
    }

    private void bindKeys()
    {
        inputManager.addListener(this, KeyKeys.mouse_left);
        inputManager.addListener(this, KeyKeys.mouse_scroll_up);
        inputManager.addListener(this, KeyKeys.mouse_scroll_down);
        inputManager.addListener(this, KeyKeys.mouse_move_up);
        inputManager.addListener(this, KeyKeys.mouse_move_down);
        inputManager.addListener(this, KeyKeys.mouse_move_left);
        inputManager.addListener(this, KeyKeys.mouse_move_right);
        inputManager.addListener(this, KeyKeys.num_0, KeyKeys.num_1, KeyKeys.num_2, KeyKeys.num_3, KeyKeys.num_4, KeyKeys.num_5, KeyKeys.num_6, KeyKeys.num_7, KeyKeys.num_8, KeyKeys.num_9);
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
        preview = renderManager.createPreView("background", cam);
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
        cam.setParallelProjection(true);
        frustumSize.enableClamp(2f, 20);
        updateFrustum();
        
        // top-dwon
        cam.setLocation(Vector3f.UNIT_Y);
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z.mult(-1.0f));
        // iso
        cam.setLocation(new Vector3f(-1f, 1f, -1f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }

    private void tickFrustum(float tpf)
    {
        frustumSize.tick(tpf);
        updateFrustum();
    }
    
    
    
    public Node makeDetailedGridNode()
    {
        Material wholeMat = this.strokeMat.clone();
        wholeMat.setColor("Color", new ColorRGBA(1.0f, 1.0f, 1.0f, 0.8f));
        
        Material halfMat = wholeMat.clone();
        halfMat.setColor("Color", new ColorRGBA(1.0f, 1.0f, 1.0f, 0.2f));
        
        int wid = plotData.getWidth();
        wid /= 2;
        int hei = plotData.getHeight();
        hei /= 2;
        
        BlueprintGeoGen evenLines = new BlueprintGeoGen();
        for(float x = 0; x <= wid; ++ x)
        {
            evenLines.addLine(x * 2, 0, x * 2, plotData.getHeight());
        }
        for(float y = 0; y <= hei; ++ y)
        {
            evenLines.addLine(0, y * 2, plotData.getWidth(), y * 2);
        }
        
        if(wid % 2 == 1)
        {
            evenLines.addLine(plotData.getWidth(), 0, plotData.getWidth(), plotData.getHeight());
        }
        if(hei % 2 == 1)
        {
            evenLines.addLine(0, plotData.getHeight(), plotData.getWidth(), plotData.getHeight());
        }
        Mesh wholeMesh = evenLines.bake(0.04f, 10.0f, 1.0f, 1.0f);
        
        Geometry wholeGeo = new Geometry("", wholeMesh);
        wholeGeo.setMaterial(wholeMat);
        wholeGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        BlueprintGeoGen oddLines = new BlueprintGeoGen();
        for(float x = 0; x < wid; ++ x)
        {
            oddLines.addLine((x * 2) + 1, 0, (x * 2) + 1, plotData.getHeight());
        }
        for(float y = 0; y < hei; ++ y)
        {
            oddLines.addLine(0, (y * 2) + 1, plotData.getWidth(), (y * 2) + 1);
        }
        Mesh halfMesh = oddLines.bake(0.04f, 10.0f, 1.0f, 1.0f);
        
        
        Geometry halfGeo = new Geometry("Filled Grid", halfMesh);
        halfGeo.setMaterial(halfMat);
        halfGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        Node ret = new Node();
        ret.attachChild(wholeGeo);
        ret.attachChild(halfGeo);
        
        return ret;
    }
    private Geometry makeFreeformGridNode()
    {
        BlueprintGeoGen maker = new BlueprintGeoGen();
        maker.addRect(0, 0, plotData.getWidth(), plotData.getHeight());
        Mesh mesh = maker.bake(0.02f, 20.0f, 1.0f, 1.0f);
        
        Geometry geo = new Geometry("Empty Grid", mesh);
        geo.setMaterial(this.strokeMat);
        geo.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        return geo;
    }
    private Geometry makePaperGeo()
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
    
    private void setupMaterials()
    {
        this.strokeMat = assetManager.loadMaterial("Materials/Stroke.j3m");
        strokeMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
    }

    private void storePlotData()
    {
        Vertex[] verts = new Vertex[flags.size()];
        for(int i = 0; i < flags.size(); ++ i)
        {
            Flag orig = flags.get(i);
            Vertex trans = new Vertex();
            
            trans.setX(orig.getLoc().a);
            trans.setZ(orig.getLoc().b);
            trans.setId(i);
            
            orig.id = i;
            
            verts[i] = trans;
        }
        plotData.setVerticies(verts);
        
        Face[] faces = new Face[rooms.size()];
        for(int i = 0; i < rooms.size(); ++ i)
        {
            Room orig = rooms.get(i);
            Face trans = new Face();
            
            int[] vertices = new int[orig.flags.size()];
            for(int j = 0; j < orig.flags.size(); ++ j)
            {
                vertices[j] = orig.flags.get(j).id;
            }
            trans.setVertexes(vertices);
            
            faces[i] = trans;
        }
        plotData.setFaces(faces);
        
        Decal[] edges = new Decal[doors.size()];
        for(int i = 0; i < doors.size(); ++ i)
        {
            Door door = doors.get(i);
            Decal trans = new Decal();
            
            trans.setVertA(door.a.id);
            trans.setVertA(door.b.id);
            
            
            edges[i] = trans;
        }
        plotData.setEdges(edges);
        
    }
    
    public abstract class Tool
    {
        abstract void onSelect(float tpf);
        abstract void onDeselect(float tpf);
        abstract void onClick(float tpf);
        abstract void whileMouseMove(float tpf);
        abstract void onClickRelease(float tpf);
        abstract void tick(float tpf);
    }
    
    public class Dragger extends Tool
    {
        public Vector3f preclickRootNodeLoc;
        public Vector2f preclickMouseLoc;
    

        @Override
        void onSelect(float tpf)
        {
        }

        @Override
        void onDeselect(float tpf)
        {
        }
        @Override
        void onClick(float tpf)
        {
            preclickMouseLoc = screenMouseLoc;
            preclickRootNodeLoc = editorRootNode.getLocalTranslation().clone();
        }

        @Override
        void whileMouseMove(float tpf)
        {
            if(leftClick)
            {
                if(screenMouseLoc != null && preclickMouseLoc != null)
                {
                    editorRootNode.setLocalTranslation(new Vector3f(-preclickMouseLoc.x + screenMouseLoc.x, -0.1f, -preclickMouseLoc.y + screenMouseLoc.y).addLocal(preclickRootNodeLoc));
                }
            }
        }

        @Override
        void onClickRelease(float tpf)
        {
        }

        @Override
        void tick(float tpf)
        {
        }
    };
    public Dragger dragger = new Dragger();
    
    public class Roomer extends Tool
    {

        @Override
        void onSelect(float tpf)
        {
        }

        @Override
        void onDeselect(float tpf)
        {
        }

        @Override
        void onClick(float tpf)
        {
            if(mouseLoc == null)
            {
                return;
            }
            
            Flag a = spawnLooseFlag(mouseLoc.clone().addLocal(-5f, -5f));
            Flag b = spawnLooseFlag(mouseLoc.clone().addLocal(5f, -5f));
            Flag c = spawnLooseFlag(mouseLoc.clone().addLocal(5f, 5f));
            Flag d = spawnLooseFlag(mouseLoc.clone().addLocal(-5f, 5f));
            
            spawnDoor(a, b);
            
            Room room = new Room(a, b, c, d);
            
            spawnRoom(room);
        }

        @Override
        void whileMouseMove(float tpf)
        {
        }

        @Override
        void onClickRelease(float tpf)
        {
        }

        @Override
        void tick(float tpf)
        {
        }

        
    }
    private Roomer roomer = new Roomer();
    
    public class FlagMover extends Tool
    {
        public Vector2f preclickFlagLoc;
        public Vector2f preclickMouseLoc;
        
        public LinePoint nearestPoint;
        public Flag nearestFlag;
        public Flag dragFlag;

        @Override
        void onSelect(float tpf)
        {
            dragFlag = null;
        }

        @Override
        void onDeselect(float tpf)
        {
        }
        @Override
        void onClick(float tpf)
        {
            if(mouseLoc != null)
            {
                if(nearestFlag != null)
                {
                    dragFlag = nearestFlag;

                    preclickMouseLoc = mouseLoc;
                    preclickFlagLoc = new Vector2f((float) dragFlag.getLoc().a, (float) dragFlag.getLoc().b);
                }
                else if(nearestPoint != null)
                {
                    Flag newFlag = spawnSplittingFlag(nearestPoint.a, nearestPoint.b, nearestPoint.distance);
                    
                    dragFlag = newFlag;
                    preclickMouseLoc = mouseLoc;
                    preclickFlagLoc = new Vector2f((float) dragFlag.getLoc().a, (float) dragFlag.getLoc().b);
                }
            }
        }

        @Override
        void whileMouseMove(float tpf)
        {
            // If we are dragging a flag around
            if(leftClick && dragFlag != null)
            {
                // If the mouse is somewhere on the paper
                if(mouseLoc != null)
                {
                    // Continue moving it to the mouse
                    dragFlag.setLoc(new Vector2d(-preclickMouseLoc.x + mouseLoc.x + preclickFlagLoc.x, -preclickMouseLoc.y + mouseLoc.y + preclickFlagLoc.y));
                }
                else
                {
                    // Stop dragging since they dragged it off the paper
                    dragFlag = null;
                }
            }
            
            // Not currently dragging a flag around
            else
            {
                // Search for the next flag they will pick up
                nearestFlag = findClosestFlag();
                
                // If there is not a flag nearby
                if(nearestFlag == null)
                {
                    // Try find a valid spot for a flag
                    
                    nearestPoint = findClosestPoint();
                    
                    if(nearestPoint != null)
                    {
                    System.out.println("nearest: " + nearestPoint.distance);
                        
                    }
                    
                    if(nearestPoint == null)
                    {
                        
                    }

                }
                else
                {
                    nearestPoint = null;
                }
            }
        }

        @Override
        void onClickRelease(float tpf)
        {
            dragFlag = null;
        }

        @Override
        void tick(float tpf)
        {
        }
        
        double selectionRadius = 0.25d;
        private Flag findClosestFlag()
        {
            // Make sure the mouse is somewhere on the paper
            if(mouseLoc == null)
            {
                return null;
            }
            
            // Standard search algorithm
            
            // Keep track of the closest flag
            double bestDist = selectionRadius;
            Flag bestFlag = null;
            for(Flag flag : flags)
            {
                double dist =  Math.sqrt(((mouseLoc.x - flag.getLoc().a) * (mouseLoc.x - flag.getLoc().a)) + ((mouseLoc.y - flag.getLoc().b) * (mouseLoc.y - flag.getLoc().b)));
                
                if(dist < bestDist)
                {
                    bestFlag = flag;
                }
            }
            
            return bestFlag;
        }
        
        // improve pls
        private LinePoint findClosestPoint()
        {
            // Make sure the mouse is somewhere on the paper
            if(mouseLoc == null)
            {
                return null;
            }
            
            Vector2d c = new Vector2d(mouseLoc.x, mouseLoc.y);
            
            // Keep track of the closest point
            double bestDist = selectionRadius;
            LinePoint bestLp = null;
            
            for(Room r : rooms)
            {
                Flag prevFlag = r.flags.get(r.flags.size() - 1);
                for(int i = 0; i < r.flags.size(); ++ i)
                {
                    Flag currFlag = r.flags.get(i);
                    
                    /*
                     *                 C   mouse pos
                     *                 |
                     *                 |
                     *                 |
                     *                 |
                     *    A------------D-----------B  flags
                     *                distance
                     */
                    
                    
                    Vector2d a = prevFlag.getLoc();
                    Vector2d b = currFlag.getLoc();
                    Vector2d ab = b.subtract(a);
                    Vector2d ac = c.subtract(a);
                    
                    double ab_distsq = ab.magnitudeSquared();
                    double ac_dot_ab = ac.dotProduct(ab);
                    
                    double fractionOfAB = ac_dot_ab / ab_distsq;
                    
                    // If it is off the wall (lol)
                    if(fractionOfAB > 1 || fractionOfAB < 0)
                    {
                        prevFlag = currFlag;
                        continue;
                    }
                    
                    double trueDist = Math.sqrt(ab_distsq) * fractionOfAB;
                    
                    Vector2d d = a.add(ab.multiply(fractionOfAB));
                    Vector2d dc = c.subtract(d);
                    
                    double distFromMouse = Math.sqrt(dc.magnitudeSquared());
                    
                    if(distFromMouse < bestDist)
                    {
                        bestDist = trueDist;
                        bestLp = new LinePoint(prevFlag, currFlag, trueDist);
                    }
                    
                    prevFlag = currFlag;
                }
            }
            
            return bestLp;
        }
        
        
    }
    private FlagMover flagMover = new FlagMover();
    
    private Tool tool = dragger;
    
    public List<Flag> flags = new ArrayList<Flag>();
    public List<Door> doors = new ArrayList<Door>();
    public List<Room> rooms = new ArrayList<Room>();
    
    public class Flag
    {
        private Vector2d loc;
        public Spatial spatial;
        private int id;
        
        private List<Room> rooms = new ArrayList<Room>();
        public List<Door> doors = new ArrayList<Door>();
        
        public Flag()
        {
            loc = new Vector2d();
        }
        
        public Flag(Vector2f loc)
        {
            this.loc = new Vector2d(loc.x, loc.y);
        }
        
        public Flag(float x, float y)
        {
            this.loc = new Vector2d(x, y);
        }

        public void setSpatial(Spatial spatial)
        {
            this.spatial = spatial;
        }
        
        public void removeSpatial()
        {
            this.spatial.removeFromParent();
        }

        private void setId(int i)
        {
        }

        /**
         * @return the loc
         */
        public Vector2d getLoc()
        {
            return loc;
        }

        /**
         * @param loc the loc to set
         */
        public void setLoc(Vector2d loc)
        {
            this.loc = loc;
            
            spatial.setLocalTranslation((float) loc.a, 0f, (float) loc.b);
            
            for(Room room : rooms)
            {
                room.updateSpatial();
            }
            for(Door door : doors)
            {
                //door.updateLoc();
                door.updateSpatial();
            }
        }
    }

    
    public static class LinePoint
    {
        public LinePoint(Flag a, Flag b, double dist)
        {
            this.a = a;
            this.b = b;
            this.distance = dist;
        }
        
        public Flag a;
        public Flag b;
        public double distance;
    }
    
    public class Door
    {
        public Flag a;
        public Flag b;
        
        double distance = 2f;
        
        private Spatial spt;

        private Door(Flag a, Flag b)
        {
            this.a = a;
            this.b = b;
        }

        /**
         * @return the spt
         */
        public Spatial getSpt()
        {
            return spt;
        }

        public void updateSpatial()
        {
            if(spt != null)
            {
                spt.removeFromParent();
            }
            
            spt = makeSpt();
            
            editorRootNode.attachChild(spt);
        }
        
        private Spatial makeSpt()
        {
            BlueprintGeoGen bgg = new BlueprintGeoGen();
            
            Vector2f dir = new Vector2f((float) (b.loc.a - a.loc.a), (float) (b.loc.b - a.loc.b)).normalizeLocal();
            
            Vector2f perp = new Vector2f(-dir.y, dir.x).multLocal(0.5f);
            
            Vector2f pos = new Vector2f((float) a.loc.a, (float) a.loc.b).addLocal(dir.mult((float) distance));

            bgg.addLine(pos.add(perp), pos.subtract(perp));
            
            Mesh m = bgg.bake(0.05f, 20, 1, 1);

            Geometry spt = new Geometry("", m);
            spt.setMaterial(strokeMat);

            return spt;

        }
    }
    
    public class Room
    {
        public List<Flag> flags = new ArrayList<Flag>();
        
        public List<Room> interiorRooms; // Holes
        
        
        // "Bedroom", "kitchen", "library", etc
        public String name;
        
        public Room(Flag ... flags)
        {
            for(int i = 0; i < flags.length; ++ i)
            {
                this.flags.add(flags[i]);
            }
        }

        private Spatial spt;

        public Spatial getSpt()
        {
            return spt;
        }

        
        public void updateSpatial()
        {
            if(spt != null)
            {
                spt.removeFromParent();
            }
            
            spt = makeSpt();
            
            editorRootNode.attachChild(spt);
        }
        
        private Spatial makeSpt()
        {
            BlueprintGeoGen bgg = new BlueprintGeoGen();

            for(int i = 0; i < flags.size(); ++i)
            {

                Vector2d a = flags.get(i).getLoc();
                Vector2d b;
                if(i < flags.size() - 1)
                {
                    b = flags.get(i + 1).getLoc();
                }
                else
                {
                    b = flags.get(0).getLoc();
                }

                bgg.addLine(a, b);
            }

            Mesh m = bgg.bake(0.05f, 20, 1, 1);

            Geometry spt = new Geometry("", m);
            spt.setMaterial(strokeMat);

            return spt;

        }
    }
    
    
}
