package com.maetrik.jumpingball;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.maetrik.jumpingball.scenes.BaseScene;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
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
    public void init_to_gameScene(OnCreateSceneCallback pOnCreateSceneCallback) {
    	ResourcesManager.getInstance().loadGameResources();
    	GameSceneBasic.setMenu(true);
    	gameScene = new GameSceneBasic();
    	escenaActual = gameScene;
    	pOnCreateSceneCallback.onCreateSceneFinished(gameScene);
    }
    
    public void init_to_splashScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
        //ResourcesManager.getInstance().loadSplashResources();
        splashScene = new SplashScene();
        escenaActual = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    
    

    
    
    public void gameScene_to_gameScene(boolean menu) {
    	gameScene.dispose();
    	GameSceneBasic.setMenu(menu);
    	gameScene = new GameSceneBasic();
    	cambiar_a_escena(gameScene);
    	//Comprobamos si se ha de sacar anuncio
    	if (numeroPartidas >= numSacarAnuncio) {  //Saco anuncio
    		ResourcesManager.getInstance().actividad.displayInterstitial();
    		numeroPartidas = 0;
    		numSacarAnuncio = (int)(Math.random()*5)+5;
    	}
    	else { //Si no se ha de sacar comprobamos que haya uno cargado
    		ResourcesManager.getInstance().actividad.loadIntersticial();
    		numeroPartidas++;
    	}
    }
    

    
    
    public void menuScene_to_exit() {
    	System.exit(0);
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