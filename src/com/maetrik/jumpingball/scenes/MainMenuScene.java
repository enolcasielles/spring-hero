package com.maetrik.jumpingball.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.content.Intent;

import com.google.android.gms.games.Games;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.SceneManager;
import com.maetrik.jumpingball.SceneManager.SceneType;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.VideoActivity;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener{
          
		// ===========================================================
        // Constants
        // ===========================================================
		private final int MENU_PLAY = 0;
		private final int MENU_LOGROS = 1;
		private final int MENU_HOWTOPLAY = 2;
		private final int APAGADO = 1;
		private final int ENCENDIDO = 0;
 
        // ===========================================================
        // Fields
        // ===========================================================
        private MenuScene menuChildScene;
        
        
        private Text recordText;
        private Text titleText;
        private IMenuItem playMenuItem;
        private IMenuItem logrosMenuItem;
        private IMenuItem howToPlayMenuItem;
        private Sprite copyrightSprite;
        
        private enum stateLoadItems {
        	NO_LOAD,
        	BUTTONS_LOADED,
        	TEXTS_LOADED,
        }
        private stateLoadItems currentState;
        private boolean puedeCargar;
        private long time;
		private AnimatedSprite botonSonido;
		
		private long tiempo;
   
        
        // ===========================================================
        // Methods for Superclass
        // ===========================================================
		@Override
		public void createScene() {
			iniatalizeVariables();
			loadHighScore();
			createBackground();
			createMenuChildScene();
		}


		@Override
		public void onBackKeyPressed() {
			if (System.currentTimeMillis() - tiempo < 1000) {
				SceneManager.getInstance().menuScene_to_exit();
			}
			else {
				this.resourcesManager.actividad.toastMassage("Press twice");
				tiempo=System.currentTimeMillis();
			}
		}

		@Override
		public void disposeScene() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public SceneType getSceneType() {
			return SceneType.SCENE_MENU;
		}
		
		

		@Override
		public boolean onMenuItemClicked(MenuScene pMenuScene,
				IMenuItem pMenuItem, float pMenuItemLocalX,
				float pMenuItemLocalY) {
		    switch(pMenuItem.getID()) {
	          case MENU_PLAY:
	        	resourcesManager.sonidoBoton.play();
	        	SceneManager.getInstance().loadingScene(SceneType.SCENE_MENU, SceneType.SCENE_GAME);
	            return true;
	          case MENU_LOGROS:
	        	resourcesManager.sonidoBoton.play();
	        	//SceneManager.getInstance().menuScene_to_exit();
	        	this.resourcesManager.actividad.showLogros();
	            return true;
	          case MENU_HOWTOPLAY:
	        	resourcesManager.sonidoBoton.play();
		        Intent i = new Intent(activity,VideoActivity.class);
		        activity.startActivity(i);
	        	return true;  
	          default:
	            return false;
	        }
		}
		
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			super.onManagedUpdate(pSecondsElapsed);
			
			//Miro si el motor ya ha cargado esta escena
			if (puedeCargar && SceneManager.getInstance().getCurrentSceneType() == SceneType.SCENE_MENU ) {
				 if (currentState == stateLoadItems.NO_LOAD) { //Muevo el play y el exit
					 //Pequeña pausa por si aun no se visualiza la escena
					 if (System.currentTimeMillis() - time < 500) return;
					 playMenuItem.registerEntityModifier(
					    	new MoveXModifier(0.2f, 0 - playMenuItem.getWidth(), camera.getWidth()/2 - playMenuItem.getWidth() - 50) {
					        @Override
				    		protected void onModifierStarted(IEntity pItem) {
								 playMenuItem.setVisible(true); 
				    		}
				    		@Override
				    		protected void onModifierFinished(IEntity pItem) {
				    		
				    		}
				     });
					 logrosMenuItem.registerEntityModifier(
						    	new MoveXModifier(0.2f, camera.getWidth(), camera.getWidth()/2 + 50) {
						        @Override
					    		protected void onModifierStarted(IEntity pItem) {
						        	logrosMenuItem.setVisible(true); 
									 puedeCargar = false;
					    		}
					    		@Override
					    		protected void onModifierFinished(IEntity pItem) {
					    			currentState = stateLoadItems.BUTTONS_LOADED;
					    			puedeCargar = true;
					    			howToPlayMenuItem.setVisible(true);
					    		}
					  });
				  }
				  else if (currentState == stateLoadItems.BUTTONS_LOADED) {
					  MoveYModifier move1 = new MoveYModifier(0.3f, 0 - titleText.getHeight(), 45) {
						 @Override
				    	 protected void onModifierStarted(IEntity pItem) {
				    		titleText.setVisible(true);		
				    	 }
					  };
					  titleText.registerEntityModifier(move1);
					  recordText.registerEntityModifier(new ScaleModifier(0.2f, 1.3f, 1.0f) {
				    		@Override
				    		protected void onModifierStarted(IEntity pItem) {
				    			recordText.setVisible(true);
				    			puedeCargar = false;
				    		}
				    		@Override
				    		protected void onModifierFinished(IEntity pItem) {
				    			currentState = stateLoadItems.TEXTS_LOADED;
				    		}
				      });
				  }					    	
			}
			
		} 
		
		//----------------------------
		//CLASS METHODS
		//----------------------------
		private void iniatalizeVariables() {
			currentState = stateLoadItems.NO_LOAD;
			puedeCargar = true;
			time = System.currentTimeMillis();
			Constants.SOUND = true;
		}
		
		private void loadHighScore() {
			Utils.loadRecord();
		}

		private void createBackground() {
		    attachChild(new Sprite(0, 0, resourcesManager.texturaMenu, vbom)
		    {
		        @Override
		        protected void preDraw(GLState pGLState, Camera pCamera) 
		        {
		            super.preDraw(pGLState, pCamera);
		            pGLState.enableDither();
		        }
		    });
		    
		    //Boton para quitar sonido
		    botonSonido = new AnimatedSprite(10, 400, 50, 50,resourcesManager.texturaBotonSonido, vbom) {
		    	@Override
		    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
		    		if (pSceneTouchEvent.isActionUp()) {
		    		  if (engine.getMusicManager().getMasterVolume() == 1.0f) {	
		    			this.setCurrentTileIndex(APAGADO);
		    			engine.getSoundManager().setMasterVolume(0.0f);
		    			engine.getMusicManager().setMasterVolume(0.0f);
		    			Constants.SOUND = false;
		    			VideoActivity.MUSICA = false;
		    		  }
		    		  else {
		    			 this.setCurrentTileIndex(ENCENDIDO);
		    			 engine.getSoundManager().setMasterVolume(1.0f);
			    		 engine.getMusicManager().setMasterVolume(1.0f);
			    		 Constants.SOUND = true;
			    		 VideoActivity.MUSICA = true;
		    		  }
		    		}
		    		return false;
		    	}
		    };
		    botonSonido.setPosition(5, camera.getHeight() - botonSonido.getHeight() - 5);
		    if (engine.getMusicManager().getMasterVolume() == 1.0f) {
		    	botonSonido.setCurrentTileIndex(ENCENDIDO);
		    	VideoActivity.MUSICA = true;
		    }
		    else {
		    	botonSonido.setCurrentTileIndex(APAGADO);
		    	VideoActivity.MUSICA = false;
		    }
		    this.registerTouchArea(botonSonido);
		    this.attachChild(botonSonido);
		}

		private void createMenuChildScene()
		{
		    menuChildScene = new MenuScene(camera);
		    menuChildScene.setPosition(0, 0);
		    
		    playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.texturaBotonPlay, vbom), 1.2f, 1);
		    logrosMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LOGROS, resourcesManager.texturaBotonLogro, vbom), 1.2f, 1);
		    howToPlayMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_HOWTOPLAY, resourcesManager.texturaBotonHowTo, vbom), 1.2f, 1);
		    
		    recordText = new Text(0, 0, resourcesManager.fuenteMenu, "Record:0123456789", new TextOptions(HorizontalAlign.LEFT), vbom); 
		    recordText.setText("Record: " + Constants.RECORD);
		    recordText.setPosition(camera.getWidth() - recordText.getWidth() - 5, 
		    		               camera.getHeight() - recordText.getHeight() - 5);
		    recordText.setColor(Color.BLACK);
		    recordText.setVisible(false);
		    
		    titleText = new Text(0, 0, resourcesManager.fuenteMenu, "JumpingBall", new TextOptions(HorizontalAlign.LEFT), vbom); 
		    titleText.setText("JumpingBall");
		    titleText.setScale(2.0f);
		    titleText.setPosition(camera.getWidth()/2 - titleText.getWidth()/2 , 5);
		    titleText.setColor(Color.BLACK);
		    titleText.setVisible(false);
		    
		    menuChildScene.addMenuItem(playMenuItem);
		    menuChildScene.addMenuItem(logrosMenuItem);
		    menuChildScene.addMenuItem(howToPlayMenuItem);
		    
		    menuChildScene.buildAnimations();
		    menuChildScene.setBackgroundEnabled(false);
		  
		    menuChildScene.setOnMenuItemClickListener(this);
		    
		    playMenuItem.setPosition(camera.getWidth()/2 , camera.getHeight() / 2 - playMenuItem.getHeight() / 2);
	        playMenuItem.setVisible(false);
	        logrosMenuItem.setPosition(camera.getWidth()/2, 
		    					camera.getHeight() / 2 - logrosMenuItem.getHeight() / 2 );
	        logrosMenuItem.setVisible(false);
		    
		    
		    copyrightSprite = new Sprite(titleText.getWidth()+2, 0, resourcesManager.texturaCopyright, vbom);
		    copyrightSprite.setScale(0.5f);
		    
		    howToPlayMenuItem.setPosition(botonSonido.getX() + botonSonido.getWidth() + 15 , botonSonido.getY());
	        howToPlayMenuItem.setVisible(false);
	        howToPlayMenuItem.setSize(52, 52);
		    
		    
		    setChildScene(menuChildScene);
		    this.attachChild(recordText);
		    this.attachChild(titleText);
		    titleText.attachChild(copyrightSprite);
		    
		}
		

}