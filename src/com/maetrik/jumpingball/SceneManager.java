package com.maetrik.jumpingball;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.maetrik.jumpingball.scenes.BaseScene;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.LoadingScene;
import com.maetrik.jumpingball.scenes.MainMenuScene;
import com.maetrik.jumpingball.scenes.SplashScene;



public class SceneManager
{
    //---------------------------------------------
    // SCENES
    //---------------------------------------------
	
	private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene gameScene;
    public BaseScene loadingScene;

    
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final SceneManager INSTANCIA = new SceneManager();
    
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    
    private BaseScene escenaActual;  //Escena actual
    
    private Engine engine = ResourcesManager.getInstance().engine;
    
    private int numeroPartidas = 0, numSacarAnuncio = (int)(Math.random()*5)+1;
    
    public enum SceneType
    {
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_GAME,
        SCENE_LOADING,
        SCENE_NEW_RECORD
    }
    
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    //Metodo para carbiar de escena
    public void cambiar_a_escena(BaseScene scene)
    {
        engine.setScene(scene);
        escenaActual = scene;
        currentSceneType = scene.getSceneType();
    }
    
    
    public void cambiar_a_escena(SceneType sceneType)
    {
        switch (sceneType)
        {
            case SCENE_MENU:
            	cambiar_a_escena(menuScene);
                break;
            case SCENE_GAME:
            	cambiar_a_escena(gameScene);
                break;
            case SCENE_SPLASH:
            	cambiar_a_escena(splashScene);
                break;
            case SCENE_LOADING:
            	cambiar_a_escena(loadingScene);
                break;
            default:
                break;
        }
    }
    
    
    
    
    //Metodos para cambiar entre las distintas escenas del juego
    public void init_to_splashScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
        ResourcesManager.getInstance().loadSplashResources();
        splashScene = new SplashScene();
        escenaActual = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    
    
    public void splashScene_to_menuScene() {
        ResourcesManager.getInstance().loadMenuResources();
        menuScene = new MainMenuScene();
        cambiar_a_escena(menuScene);
        ResourcesManager.getInstance().unloadSplashResources();
        splashScene.disposeScene();
        splashScene = null;
    }
    
    
    public void menuScene_to_gameScene() {
    	ResourcesManager.getInstance().loadGameResources();
    	gameScene = new GameSceneBasic();
    	cambiar_a_escena(gameScene);
    	ResourcesManager.getInstance().unloadMenuResources();
    	menuScene.dispose();
    	menuScene = null;
    	//Hacemos visible publicidad
    	ResourcesManager.getInstance().actividad.setBannerVisibility(1,true);
    }
    
    
    public void gameScene_to_gameScene() {
    	gameScene.dispose();
    	gameScene = new GameSceneBasic();
    	cambiar_a_escena(gameScene);
    	//Comprobamos si se ha de sacar anuncio
    	if (numeroPartidas >= numSacarAnuncio) {  //Saco anuncio
    		ResourcesManager.getInstance().actividad.displayInterstitial();
    		numeroPartidas = 0;
    		numSacarAnuncio = (int)(Math.random()*5)+1;
    	}
    	else { //Si no se ha de sacar comprobamos que haya uno cargado
    		ResourcesManager.getInstance().actividad.loadIntersticial();
    		numeroPartidas++;
    	}
    }
    
    
    public void gameScene_to_menuScene() {
    	ResourcesManager.getInstance().loadMenuResources();
    	menuScene = new MainMenuScene();
    	cambiar_a_escena(menuScene);
    	ResourcesManager.getInstance().unloadGameResources();
    	gameScene.dispose();
    	gameScene = null;
    	//Hacemos invisible publicidad
    	ResourcesManager.getInstance().actividad.setBannerVisibility(1,false);
    	ResourcesManager.getInstance().actividad.setBannerVisibility(2,false);
    }
    
    
    public void menuScene_to_exit() {
    	System.exit(0);
    }
   
    
    public void menuScene_to_videoScene() {
    	
    }
    
    public void videoScene_to_menuScene() {
    	
    }
    
    public void loadingScene(SceneType fromScene, SceneType toScene) {
    	loadingScene = new LoadingScene(fromScene, toScene);
    	cambiar_a_escena(loadingScene);
    }
    
   
    
    
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static SceneManager getInstance()
    {
        return INSTANCIA;
    }

    
    public BaseScene getCurrentScene()
    {
        return escenaActual;
    }
    
    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }
}