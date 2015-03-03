package com.maetrik.jumpingball.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import com.maetrik.jumpingball.ResourcesManager;
import com.maetrik.jumpingball.SceneManager.SceneType;


public abstract class BaseScene extends Scene
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    public Engine engine;
    public Activity activity;
    public ResourcesManager resourcesManager;
    public VertexBufferObjectManager vbom;
    public Camera camera;
    public PhysicsWorld physicsWorld;
    
    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------
    
    public BaseScene()
    {
        this.resourcesManager = ResourcesManager.getInstance();
        this.engine = resourcesManager.engine;
        this.activity = resourcesManager.actividad;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camara;
        createScene();
    }
    
    //---------------------------------------------
    // ABSTRACTION
    //---------------------------------------------
    
    public abstract void createScene();
    
    public abstract void onBackKeyPressed();
    
    public abstract void disposeScene();
    
    public abstract SceneType getSceneType();
  
}