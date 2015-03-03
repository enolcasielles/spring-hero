package com.maetrik.jumpingball.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.MainActivity;
import com.maetrik.jumpingball.SceneManager;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.SceneManager.SceneType;

/**
 * @author enolcasielles
 *
 */
public class FinalMenuDialog extends Rectangle {

	
	//-------------------------------
	//CONSTANTES
	//-------------------------------
	
	private final int NUEVO_RECORD = 1;
	private final int RESTART = 0;
	private final int FACEBOOK = 2;
	private final int GOOGLE = 3;
	private final int MENU = 1;

	

	private BaseScene scene;
	private int idItemSelected;

	
	private Sprite spriteJugar, spriteMenu, spriteFacebook, spriteGoogle;
	
	private Text titleText;
	
	//Ubicacion del touch en el screen
	private float posX, posY;
	
	
	
	//-------------------------------
	//CONSTRUCTOR
	//-------------------------------
	public FinalMenuDialog(BaseScene scene, final int score, boolean nuevoRecord) {
		
	    super(0,0,400,200,scene.vbom); //Genero el rectangulo
	    
	    
	    this.scene = scene;
	    idItemSelected = -1;
	    
	    this.setColor(0.796f,0.796f,0.796f); //Fondo
		
	    this.setPosition(0 - this.getWidth()  , 
	    		scene.camera.getHeight() / 2 - this.getHeight() /2);  //Lo posiciono en el centro
	    
		//Genero los textos y los aÃ±ado
		final Text scoreText = new Text(0,0,scene.resourcesManager.fuenteGame, "Score: " + score, new TextOptions(HorizontalAlign.LEFT), scene.vbom); 
		scoreText.setPosition(10, 10);
		this.attachChild(scoreText);
		
		final Text recordText = new Text(0,0,scene.resourcesManager.fuenteGame, "Record: " + Constants.RECORD, new TextOptions(HorizontalAlign.LEFT), scene.vbom); 
		recordText.setPosition(scoreText.getX(), scoreText.getY() + scoreText.getHeight() + 10);
		this.attachChild(recordText);
		
		//Genero el sprite con la carita del resultado
		final AnimatedSprite caritaResultado = new AnimatedSprite(0, 0, scene.resourcesManager.texturaCaritaResultado, scene.vbom);
		caritaResultado.setPosition(this.getWidth() - caritaResultado.getWidth() - 10, 10);
		if (nuevoRecord) {
			caritaResultado.setCurrentTileIndex(NUEVO_RECORD);
			//Actualizo el google games
			scene.resourcesManager.actividad.updateRecord(score);
		}
		this.attachChild(caritaResultado);
		
		
	    
	    //Por ultimo genero los sprites con los botones	
		spriteJugar = new Sprite(20, 250, 80,80, scene.resourcesManager.texturaBotonRestart , scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)  {
			   if (pSceneTouchEvent.isActionDown()) { //Seleccionamos
				   this.setScale(1.2f);
				   idItemSelected = RESTART;
				   posX = X;
				   posY = Y;
				   Log.i("FinalMenuDialog","evento pulsar");
				   return true;
			   }
			   if (pSceneTouchEvent.isActionUp()) { //Ocultamos
				   if (idItemSelected != -1) {
					   this.setScale(1.0f);
		        	   hidden();
		        	   Log.i("FinalMenuDialog","evento soltar");
		        	   return true;
				   }
	           }
			   if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) { //Deseleccionamos
				   if (Utils.calcula_distancia(posX,posY,X,Y)) {
					   this.setScale(1.0f);
					   idItemSelected = -1;
					   Log.i("FinalMenuDialog","evento mover");
				   }
				   return true;
			   }
	           return true;
	        }
		};
		spriteJugar.setPosition(16, 
				this.getHeight() - spriteJugar.getHeight() - 10 );
		this.attachChild(spriteJugar);
		
		spriteGoogle = new Sprite(20, 250,80,80, scene.resourcesManager.texturaShareGoogle , scene.vbom) {
			@Override
			public boolean  onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)  {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
					 if (Utils.calcula_distancia(posX,posY,X,Y)) {
							this.setScale(1.0f);
							idItemSelected = -1;
					 }
				}
				if (pSceneTouchEvent.isActionUp()) {
					 if (idItemSelected != -1) {
						 this.setScale(1.0f);
			        	 shareGoogle(score);
					 }
		        }
				if (pSceneTouchEvent.isActionDown()) {
					   this.setScale(1.2f);
					   idItemSelected = GOOGLE;
					   posX = X;
					   posY = Y;
				}
		        return true;
			}
		};
		spriteGoogle.setPosition(112, 
				this.getHeight() - spriteJugar.getHeight() - 10 );
		this.attachChild(spriteGoogle);
		
		
		spriteFacebook = new Sprite(380, 200, 80,80, scene.resourcesManager.texturaShareFacebook, scene.vbom) {
			@Override
			public boolean  onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)  {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
					 if (Utils.calcula_distancia(posX,posY,X,Y)) {
							this.setScale(1.0f);
							idItemSelected = -1;
					 }
				}
				if (pSceneTouchEvent.isActionUp()) {
					 if (idItemSelected != -1) {
						 this.setScale(1.0f);
			        	 shareFacebook(score);
					 }
		        }
				if (pSceneTouchEvent.isActionDown()) {
					   this.setScale(1.2f);
					   idItemSelected = FACEBOOK;
					   posX = X;
					   posY = Y;
				}
		        return true;
			}
		};
		spriteFacebook.setPosition(208, 
				spriteJugar.getY() );
		this.attachChild(spriteFacebook);
		
		spriteMenu = new Sprite(200,250, 80,80, scene.resourcesManager.texturaBotonMenu, scene.vbom) {
			   @Override
			   public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y)  {
				  if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
					  if (Utils.calcula_distancia(posX,posY,X,Y)) {
						  this.setScale(1.0f);
						   idItemSelected = -1; 
					  }
				  }
				  if (pSceneTouchEvent.isActionDown()) {
					   this.setScale(1.2f);
					   idItemSelected = MENU;
					   posX = X;
					   posY = Y;
				  }
				  if (pSceneTouchEvent.isActionUp()) {
					   if (idItemSelected != -1) {
						   this.setScale(1.0f);
			        	   hidden();
					   }
	              }
	              return true;
	           }
		};
		spriteMenu.setPosition(304, 
				this.getHeight() - spriteMenu.getHeight() - 10 );
		this.attachChild(spriteMenu);
		

		
		//Recolocamos los sprites en caso de que haya nuevo record para aÃ±adir el de facebook
		if (nuevoRecord) {
			//Un texto indicando el nuevo record
			final Text tmpText = new Text(0,0,scene.resourcesManager.fuenteGame, "New Record", new TextOptions(HorizontalAlign.LEFT), scene.vbom); 
			tmpText.setPosition(recordText.getX() + recordText.getWidth() + 50, recordText.getY());
			//tmpText.setScale(1.5f);
			tmpText.setColor(Color.RED);
			final AlphaModifier modifier = new AlphaModifier(1.0f, 0f, 1f);
			tmpText.registerEntityModifier(new LoopEntityModifier(modifier));
			this.attachChild(tmpText);
		}
		
		
		//Texto con el GameOver
		titleText = new Text(0, 0, scene.resourcesManager.fuenteMenu, "Game Over", new TextOptions(HorizontalAlign.LEFT), scene.vbom); 
	    titleText.setText("Game Over");
	    titleText.setScale(1.8f);
	    titleText.setPosition(scene.camera.getWidth()/2 - titleText.getWidth()/2 , 0 - titleText.getHeight());
	    titleText.setColor(Color.BLACK);
	    titleText.setVisible(false);
		
	}
	

	
	
	//-------------------------------------
	//CLASS METHODS
	//-------------------------------------
	
	public void destructor() {
		this.detachSelf();
		this.dispose();
		titleText.detachSelf();
		titleText.dispose();
	}
	
	//AÃ±ade el menu a la escena
	public void attachToScene (BaseScene scene) {
		this.setVisible(false);
		scene.attachChild(this);
		scene.attachChild(titleText);
	}
	
	//Lo hace visible
	public void show() {
		float x1 =  0 - this.getWidth();
		float x2 = scene.camera.getWidth()/2 - this.getWidth()/2;
		this.registerEntityModifier(
		    	new MoveXModifier(0.2f,x1,x2) {
		    		@Override
		    		protected void onModifierStarted(IEntity pItem) {
						  
		    		}
		    		@Override
		    		protected void onModifierFinished(IEntity pItem) {
		    			scene.registerTouchArea(spriteJugar);
		    			scene.registerTouchArea(spriteMenu);
		    			scene.registerTouchArea(spriteGoogle);
		    			scene.registerTouchArea(spriteFacebook);
		    			//Hago visible la publicidad
		    			scene.resourcesManager.actividad.setBannerVisibility(2, true);
		    		}
		    	});
		this.setVisible(true);
		
		MoveYModifier move1 = new MoveYModifier(0.2f, 0 - titleText.getHeight(), 45) {
			 @Override
	    	 protected void onModifierStarted(IEntity pItem) {
	    		titleText.setVisible(true);		
	    	 }
		};
		titleText.registerEntityModifier(move1);
		titleText.setVisible(true);
	}
	
	
	//Lo hace invisible
	private void hidden() {
		float x1 = scene.camera.getWidth()/2 - this.getWidth()/2;
		float x2 = scene.camera.getWidth();
		this.registerEntityModifier(
		    	new MoveXModifier(0.2f,x1,x2) {
			@Override
    		protected void onModifierStarted(IEntity pItem) {
				//Hago invisible la publicidad
				scene.resourcesManager.actividad.setBannerVisibility(2, false);
    		}
    		@Override
    		protected void onModifierFinished(IEntity pItem) {
    				switch (idItemSelected) {
    					case RESTART:
    						SceneManager.getInstance().loadingScene(SceneType.SCENE_GAME, SceneType.SCENE_GAME);
    						break;
    					case MENU:
    						SceneManager.getInstance().loadingScene(SceneType.SCENE_GAME, SceneType.SCENE_MENU);
    						break;
    				}
    		}
		});
		MoveYModifier move1 = new MoveYModifier(0.3f, 15, 0 - titleText.getHeight()) {
			 @Override
			protected void onModifierFinished(IEntity pItem) {
				 titleText.setVisible(false);	
			}
		};
		titleText.registerEntityModifier(move1);
		
	}
	

	
	
	//Metodo para para enviar al SDK de Facebook
	private void shareFacebook(int record) {
		
		if (FacebookDialog.canPresentShareDialog(scene.resourcesManager.actividad.getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(scene.resourcesManager.actividad)
				.setLink("http://play.google.com/store/apps/details?id=com.maetrik.jumpingball")   //Poner aqui la url del enlace de descarga en el play store
				.setPicture("https://dl.dropboxusercontent.com/u/74687711/ic_launcher2.png")
				.build();
			MainActivity.getUiLifecycleHelper().trackPendingDialogCall(shareDialog.present());
		} else {
			publishFeedDialog(record);
		}
		
	}
	
	
	
	//Metodo para compartir en el Google Game
	private void shareGoogle(int record) {
		scene.resourcesManager.actividad.signInClicked();
	}
	
	
	
	private void publishFeedDialog(int record) {
		
		/*
	    Bundle params = new Bundle();
	    params.putString("name", "¡Nuevo récord!");
	    params.putString("description", "¿Puedes superar mi récord de " + record + " puntos en JumpingBall? Pruébalo, ¡ES GRATIS!" );
	    params.putString("link", "https://developers.facebook.com/android");  //Poner link de google play de la app
	    params.putString("picture", "https://dl.dropboxusercontent.com/u/74687711/ic_launcher.png");

	    
	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(scene.activity,
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {

	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(scene.activity,
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(scene.activity.getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(scene.activity.getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(scene.activity.getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	            }

	        })
	        .build();
	    feedDialog.show();
	    */
		
		scene.resourcesManager.actividad.toastMassage("Facebook app not installed");
		
	}
	
}
