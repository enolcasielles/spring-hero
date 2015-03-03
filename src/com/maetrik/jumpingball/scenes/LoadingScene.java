package com.maetrik.jumpingball.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.SceneManager;
import com.maetrik.jumpingball.SceneManager.SceneType;

public class LoadingScene extends BaseScene {
	
	private Text texto;
	private int tmp;
	private long time, timeInit;
	private boolean tiempoMinimoPasado;
	
	private SceneType fromScene;
	private SceneType toScene;
	
	
	public LoadingScene(SceneType fromScene, SceneType toScene) {
		super();
		this.fromScene = fromScene;
		this.toScene = toScene;
	}
	

	@Override
	public void createScene() {
		
		setBackground(new Background(0.796f,0.796f,0.796f));
		texto = new Text(0, 0, resourcesManager.fuenteLoading, "Loading.....", vbom);
		texto.setPosition(camera.getWidth()/2 - texto.getWidth() / 2, 
	    		camera.getHeight()/2 - texto.getHeight() / 2);
		texto.setColor(Color.BLACK);
		attachChild(texto);
		tmp = 1;
		time = System.currentTimeMillis();
		timeInit = System.currentTimeMillis();
		tiempoMinimoPasado = false;
		
		Thread thr = new Thread() {
        	public void run() {
        		//Cargo los recursos segun el caso
        		while (!tiempoMinimoPasado); //Esperamos que pase el tiempo minimo
        		if (fromScene == SceneType.SCENE_MENU && toScene == SceneType.SCENE_GAME) {
        			SceneManager.getInstance().menuScene_to_gameScene();
        		}
        		if (fromScene == SceneType.SCENE_GAME && toScene == SceneType.SCENE_MENU) {
        			SceneManager.getInstance().gameScene_to_menuScene();
        		}
        		if (fromScene == SceneType.SCENE_GAME && toScene == SceneType.SCENE_GAME) {
        			SceneManager.getInstance().gameScene_to_gameScene();
        		}
        	}
        };
        thr.start();
	}

	@Override
	public void onBackKeyPressed() {
		return;
	}

	@Override
	public void disposeScene() {
		texto.detachSelf();
		texto.dispose();
		texto = null;
	}
	
	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		//Actualizo el texto
		if (System.currentTimeMillis() - time > 350) {
			time = System.currentTimeMillis();
			//Actualizamos el texto
			switch (tmp) {
			   case 0:
				 texto.setText(Constants.TEXT_LOADING);
				 tmp = 1;
				 break;
			   case 1:
				 texto.setText(Constants.TEXT_LOADING + ".");
				 tmp = 2;
				 break;
			   case 2:
				 texto.setText(Constants.TEXT_LOADING + "..");
				 tmp = 3;
				 break;
			   case 3:
				 texto.setText(Constants.TEXT_LOADING + "...");
				 tmp = 4;
				 break;
			   case 4:
				 texto.setText(Constants.TEXT_LOADING + "....");
				 tmp = 0;
				 break;
			}
		}
		
		if (System.currentTimeMillis() - timeInit > 500) {
			tiempoMinimoPasado = true;
		}
		

	}		
	
	
}
