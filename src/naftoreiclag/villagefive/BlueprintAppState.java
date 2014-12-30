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
import naftoreiclag.villagefive.PlotSerial.Decal;
import naftoreiclag.villagefive.PlotSerial.Face;
import naftoreiclag.villagefive.PlotSerial.Vertex;

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
    
    PlotSerial plotData = new PlotSerial();

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
        Flag flag = new Flag();
        flag.loc = new Vector2d((float) pos.x, (float) pos.y);
        flag.updateSpatial();
        
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
    private Flag spawnSplittingFlag(LinePoint point)
    {
        Flag newFlag = new Flag();
        newFlag.loc = point.calcLoc();
        newFlag.updateSpatial();
        
        flags.add(newFlag);
        
        
        // Find all the rooms that will be affected by this change
        List<Room> affectedRooms = new ArrayList<Room>();
        for(Room room : point.a.rooms)
        {
            if(!affectedRooms.contains(room))
            {
                affectedRooms.add(room);
            }
        }
        for(Room room : point.b.rooms)
        {
            if(!affectedRooms.contains(room))
            {
                affectedRooms.add(room);
            }
        }
        
        // Insert the new flag and update each room
        for(Room room : affectedRooms)
        {
            // Standard edge iteration
            Flag prevFlag = room.flags.get(room.flags.size() - 1);
            for(int i = 0; i < room.flags.size(); ++ i)
            {
                // Standard edge iteration
                Flag currFlag = room.flags.get(i);
                
                // If this is the proper insertion point in the vertex path
                if((currFlag == point.a && prevFlag == point.b) || (currFlag == point.b && prevFlag == point.a))
                {
                    // Insert it here
                    room.flags.add(i, newFlag);
                    
                    // Make sure the new flag is a part of the family
                    newFlag.rooms.add(room);
                    
                    // Move on to the next room
                    break;
                }
                
                // Standard edge iteration
                prevFlag = currFlag;
            }
        }
        
        // Find all the doors that will be affected by this change
        List<Door> affectedDoors = new ArrayList<Door>();
        for(Door door : point.a.doors)
        {
            if(!affectedDoors.contains(door))
            {
                affectedDoors.add(door);
            }
        }
        for(Door door : point.b.doors)
        {
            if(!affectedDoors.contains(door))
            {
                affectedDoors.add(door);
            }
        }
        
        // Insert the new flag and update each door
        for(Door door : affectedDoors)
        {
            // If the new flag is on the same wall that the door is
            if(door.loc.a == point.a && door.loc.b == point.b)
            {
                // Closer to the second point than the door
                if(point.distance >= door.loc.distance)
                {
                    door.loc.b.doors.remove(door);
                    door.loc.b = newFlag;
                }
                else
                {
                    door.loc.distance -= point.distance;
                    
                    door.loc.a.doors.remove(door);
                    door.loc.a = newFlag;
                }
                
                // Make sure the new flag is a part of the family
                newFlag.doors.add(door);

                // Move on to the next room
                break;
            }
            
            // Same, but if the door's a/b values are swapped
            if(door.loc.b == point.a && door.loc.a == point.b)
            {
                // Closer to the second point than the door
                if(point.distance >= door.loc.distance)
                {
                    door.loc.distance -= point.invertedDistance();
                    
                    door.loc.a.doors.remove(door);
                    door.loc.a = newFlag;
                }
                else
                {
                    door.loc.b.doors.remove(door);
                    door.loc.b = newFlag;
                }
                
                // Make sure the new flag is a part of the family
                newFlag.doors.add(door);

                // Move on to the next room
                break;
            }
        }
        
        return newFlag;
        
    }
    
    private Door spawnDoor(Flag a, Flag b)
    {
        Door door = new Door();
        
        door.loc.a = a;
        door.loc.b = b;
        door.loc.distance = 5f;
        
        a.doors.add(door);
        b.doors.add(door);
        
        door.updateSpatial();
        
        doors.add(door);
        
        return door;
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

    // 
    private void storePlotData()
    {
        Vertex[] verts = new Vertex[flags.size()];
        for(int i = 0; i < flags.size(); ++ i)
        {
            Flag orig = flags.get(i);
            
            // Give every flag its id // This is a genius idea
            orig.id = i;
            
            Vertex trans = new Vertex();
            trans.setX(orig.getLoc().a);
            trans.setZ(orig.getLoc().b);
            trans.setId(orig.id);
            
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
            
            trans.setVertA(door.loc.a.id);
            trans.setVertB(door.loc.b.id);
            trans.setDistance(door.loc.distance);
            trans.width = door.width;
            
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
            
            Door door = spawnDoor(a, b);
            
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
                    Flag newFlag = spawnSplittingFlag(nearestPoint);
                    
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
        
        // Farthest distance something can be from the mouse and still be considered "close"
        double selectionRadius = 0.25d;
        
        // Find the closest flag to the mouse
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
        
        // Find the closest point on an edge to the mouse
        private LinePoint findClosestPoint()
        {
            // Make sure the mouse is somewhere on the paper
            if(mouseLoc == null)
            {
                return null;
            }
            
            // Location of the mouse in vec2d form
            Vector2d c = new Vector2d(mouseLoc.x, mouseLoc.y);
            
            // Keep track of the optimal point
            double bestDistFromMouse = selectionRadius;
            LinePoint bestLp = null;
            
            // Loop through all the rooms, since there is no list of edges yet
            for(Room r : rooms)
            {
                // Standard looping through all the edges by iterating over the vertexes while remembering where the previous one was
                Flag prevFlag = r.flags.get(r.flags.size() - 1);
                for(int i = 0; i < r.flags.size(); ++ i)
                {
                    // Standard dge iteration
                    Flag currFlag = r.flags.get(i);
                    
                    /* Map:
                     *  
                     *                 C   mouse pos
                     *                 |
                     *                 |
                     *                 |
                     *                 |
                     *    A------------D-----------B  flags
                     *                distance
                     */
                    
                    // Nice aliases
                    Vector2d a = prevFlag.getLoc();
                    Vector2d b = currFlag.getLoc();
                    
                    // 
                    Vector2d ab = b.subtract(a);
                    Vector2d ac = c.subtract(a);
                    
                    // Magic math
                    double ab_distsq = ab.magnitudeSquared();
                    double ac_dot_ab = ac.dotProduct(ab);
                    
                    // Distance of D from A as a fraction of the distance of B from A
                    double fractionOfAB = ac_dot_ab / ab_distsq;
                    
                    // If it is off the wall (lol)
                    if(fractionOfAB > 1 || fractionOfAB < 0)
                    {
                        // Standard edge iteration
                        prevFlag = currFlag;
                        
                        // Skip further calculation
                        continue;
                    }
                    
                    // Actual distance of D from A
                    double trueDist = Math.sqrt(ab_distsq) * fractionOfAB;
                    
                    // Find the closest point (d) and its distance from the mouse (c)
                    Vector2d d = a.add(ab.multiply(fractionOfAB));
                    Vector2d dc = c.subtract(d);
                    double distFromMouse = Math.sqrt(dc.magnitudeSquared());
                    
                    // Compare this data to the best stuff we found so far
                    if(distFromMouse < bestDistFromMouse)
                    {
                        // It is even closer!
                        
                        // Record this
                        bestDistFromMouse = distFromMouse;
                        bestLp = new LinePoint();
                        bestLp.a = prevFlag;
                        bestLp.b = currFlag;
                        bestLp.distance = trueDist;
                    }
                    
                    // Standard edge iteration
                    prevFlag = currFlag;
                }
            }
            
            // The best, or null if none were found to be closer than selectionRadius
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
        
        
        public void removeSpatial()
        {
            this.spatial.removeFromParent();
        }

        public Vector2d getLoc()
        {
            return loc;
        }

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
                door.updateSpatial();
            }
        }

        private void updateSpatial()
        {
            if(spatial != null)
            {
                spatial.removeFromParent();
            }
            
            spatial = b_flag.clone();
            spatial.setLocalTranslation((float) loc.a, 0, (float) loc.b);
            editorRootNode.attachChild(spatial);
        }
    }

    
    public class Door
    {
        LinePoint loc = new LinePoint();
        
        float width = 3f;
        
        private Spatial spatial;

        public Spatial getSpatial()
        {
            return spatial;
        }

        public void updateSpatial()
        {
            if(spatial != null)
            {
                spatial.removeFromParent();
            }
            
            spatial = makeSpt();
            
            editorRootNode.attachChild(spatial);
        }
        
        private Spatial makeSpt()
        {
            BlueprintGeoGen bgg = new BlueprintGeoGen();
            
            Vector2d lenD = loc.b.loc.subtract(loc.a.loc);
            Vector2f len = new Vector2f((float) lenD.a, (float) lenD.b);
            Vector2f perp = new Vector2f(-len.y, len.x);
            
            Vector2d baseD = loc.calcLoc();
            Vector2f base = new Vector2f((float) baseD.a, (float) baseD.b);
            
            len.normalizeLocal().multLocal(width);
            perp.normalizeLocal().multLocal(0.5f);
            Vector2f A = base;
            Vector2f B = base.add(len);
            Vector2f C = B.subtract(perp);
            Vector2f D = A.subtract(perp);
            A.addLocal(perp);
            B.addLocal(perp);
            
            bgg.addRect(A, B, C, D);
            
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

        private Spatial spatial;

        public Spatial getSpatial()
        {
            return spatial;
        }
        
        public void updateSpatial()
        {
            if(spatial != null)
            {
                spatial.removeFromParent();
            }
            
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

            spatial = new Geometry("", m);
            spatial.setMaterial(strokeMat);
            
            editorRootNode.attachChild(spatial);
        }
    }
    
    public class LinePoint
    {
        public Flag a;
        public Flag b;
        public double distance; // from A
        
        public Vector2d calcLoc()
        {
            /*
             *          new flag
             *            v
             * A----------N----------B
             * 
             */

            Vector2d A = a.loc;
            Vector2d B = b.loc;
            Vector2d an = B.subtract(A).normalizeLocal().multiplyLocal(distance);
            return A.add(an);
        }
        
        // Distance from B
        public double invertedDistance()
        {
            return Math.sqrt(a.loc.distanceSquared(b.loc)) - distance;
        }
    }
}
