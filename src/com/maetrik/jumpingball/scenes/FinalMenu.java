package com.maetrik.jumpingball.scenes;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.facebook.widget.FacebookDialog;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.MainActivity;
import com.maetrik.jumpingball.SceneManager;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.objetos.Number;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;

public class FinalMenu extends Rectangle {
	
	//CONSTANTES
	
	//^Posicion de los numeros RESPECTO A SU CENTRO
	private float X_RECORD;
	private float Y_RECORD;
	private float X_BEST;
	private float Y_BEST;
	
	//VARIABLES
	private Sprite background, titleGameOver, botonRestart, botonMenu,
			botonMarcadores, botonFacebook, botonNoads,resultado;
	private GameSceneBasic scene;
	private static int record, best;
	
	private Number numeroRecord, numeroBest;
	
	private float xTouch, yTouch;
	
	private enum BOTON_PULSADO {
		RESTAR,
		MENU,
		FACEBOOK,
		RANKING,
		NO_ADS
	}
	private BOTON_PULSADO boton;
	
	
	public FinalMenu(GameSceneBasic scene) {
		
		super(0, 0, Constants.ANCHO_PANTALLA, Constants.ALTO_PANTALLA, scene.vbom);
		
		this.setAlpha(0.6f);
		
		float Y_GAME_OVER = 0.5f*(Constants.FIRST_LINE + Constants.LAST_LINE) - 250f;
		float Y_RESULTADO = 0.5f*(Constants.FIRST_LINE + Constants.LAST_LINE) - 120f;
		float Y_BOTON_RESTART = 0.5f*(Constants.FIRST_LINE + Constants.LAST_LINE) + 50f;
		float Y_BOTONES = 0.5f*(Constants.FIRST_LINE + Constants.LAST_LINE) + 200f;
		float SEPARACION_BOTONES = 30;
		
		X_RECORD = Constants.ANCHO_PANTALLA/2 - 65;
		Y_RECORD = Y_RESULTADO + 45;
		X_BEST = Constants.ANCHO_PANTALLA/2 + 40;
		Y_BEST = Y_RESULTADO + 45;
		
		this.scene = scene;
	
		//background = new Sprite(0, 0, scene.resourcesManager.textureBlanco, scene.vbom);
		//this.attachChild(background);
		
		
		titleGameOver =  new Sprite(0, 0, scene.resourcesManager.texturaGameOver, scene.vbom);
		titleGameOver.setPosition(Constants.ANCHO_PANTALLA/2 - titleGameOver.getWidth()/2, Y_GAME_OVER);
		titleGameOver.setAlpha(1.3f);
		titleGameOver.setScale(1.6f);
		this.attachChild(titleGameOver);
		
		botonRestart =  new Sprite(0, 0, scene.resourcesManager.texturaReplay, scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					this.setScale(1.2f);
					xTouch = pTouchAreaLocalX;
					yTouch = pTouchAreaLocalY;
					boton = BOTON_PULSADO.RESTAR;
					return true;
				}
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
					if (Utils.calcula_distancia(xTouch, yTouch, pTouchAreaLocalX, pTouchAreaLocalY)) {
						this.setScale(1.0f);
						boton = null;
						return true;
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1.0f);
					botonPulsado(boton);
					return true;
				}
				return false;
			}
		};
		botonRestart.setPosition(Constants.ANCHO_PANTALLA/2 - botonRestart.getWidth()/2, Y_BOTON_RESTART);
		this.attachChild(botonRestart);
		
		botonMenu =  new Sprite(0, 0, scene.resourcesManager.textureHome, scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					this.setScale(1.2f);
					xTouch = pTouchAreaLocalX;
					yTouch = pTouchAreaLocalY;
					boton = BOTON_PULSADO.MENU;
					return true;
				}
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
					if (Utils.calcula_distancia(xTouch, yTouch, pTouchAreaLocalX, pTouchAreaLocalY)) {
						this.setScale(1.0f);
						boton = null;
						return true;
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1.0f);
					botonPulsado(boton);
					return true;
				}
				return false;
			}
		};
		//Posiciono bien los botones
		float ancho = 4*botonMenu.getWidth() + 3*(SEPARACION_BOTONES);
		botonMenu.setPosition(Constants.ANCHO_PANTALLA/2 - (0.5f*ancho), Y_BOTONES);
		this.attachChild(botonMenu);
		
		botonMarcadores =  new Sprite(0, 0, scene.resourcesManager.textureRanking, scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					this.setScale(1.2f);
					xTouch = pTouchAreaLocalX;
					yTouch = pTouchAreaLocalY;
					boton = BOTON_PULSADO.RANKING;
					return true;
				}
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
					if (Utils.calcula_distancia(xTouch, yTouch, pTouchAreaLocalX, pTouchAreaLocalY)) {
						this.setScale(1.0f);
						boton = null;
						return true;
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1.0f);
					botonPulsado(boton);
					return true;
				}
				return false;
			}
		};
		botonMarcadores.setPosition(botonMenu.getX()+botonMenu.getWidth()+SEPARACION_BOTONES, Y_BOTONES);
		this.attachChild(botonMarcadores);
		
		botonFacebook =  new Sprite(0, 0, scene.resourcesManager.textureFacebook, scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					this.setScale(1.2f);
					xTouch = pTouchAreaLocalX;
					yTouch = pTouchAreaLocalY;
					boton = BOTON_PULSADO.FACEBOOK;
					return true;
				}
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
					if (Utils.calcula_distancia(xTouch, yTouch, pTouchAreaLocalX, pTouchAreaLocalY)) {
						this.setScale(1.0f);
						boton = null;
						return true;
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1.0f);
					botonPulsado(boton);
					return true;
				}
				return false;
			}
		};
		botonFacebook.setPosition(botonMarcadores.getX() + botonMarcadores.getWidth() + SEPARACION_BOTONES, Y_BOTONES);
		this.attachChild(botonFacebook);
		
		botonNoads =  new Sprite(0, 0, scene.resourcesManager.textureNoadd, scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					this.setScale(1.2f);
					xTouch = pTouchAreaLocalX;
					yTouch = pTouchAreaLocalY;
					boton = BOTON_PULSADO.NO_ADS;
					return true;
				}
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
					if (Utils.calcula_distancia(xTouch, yTouch, pTouchAreaLocalX, pTouchAreaLocalY)) {
						this.setScale(1.0f);
						boton = null;
						return true;
					}
				}
				if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1.0f);
					botonPulsado(boton);
					return true;
				}
				return false;
			}
		};
		botonNoads.setPosition(botonFacebook.getX() + botonFacebook.getWidth() + SEPARACION_BOTONES, Y_BOTONES);
		this.attachChild(botonNoads);
	
		
		resultado = new Sprite(0, 0, scene.resourcesManager.texturaResultado, scene.vbom);
		resultado.setPosition(Constants.ANCHO_PANTALLA/2 - resultado.getWidth()/2, Y_RESULTADO);
		this.attachChild(resultado);
		
		
		//Añado esta capa a la escena
		scene.getLayer(CAPAS.CAPA_MENUS).attachChild(this);
		super.setVisible(false);
		
		
		numeroBest = new Number(scene);
		numeroRecord = new Number(scene);
		
		
	}
	
	
	
	public void setVisible(boolean v) {
		
		super.setVisible(v);
		if (v){  //Registro escuchadores en todos los botones
			scene.registerTouchArea(botonFacebook);
			scene.registerTouchArea(botonMarcadores);
			scene.registerTouchArea(botonNoads);
			scene.registerTouchArea(botonMenu);
			scene.registerTouchArea(botonRestart);
			numeroBest.setNumber(best, X_BEST, Y_BEST);
			numeroRecord.setNumber(record, X_RECORD, Y_RECORD);
		}
		
		else {
			scene.unregisterTouchArea(botonFacebook);
			scene.unregisterTouchArea(botonMarcadores);
			scene.unregisterTouchArea(botonNoads);
			scene.unregisterTouchArea(botonMenu);
			scene.unregisterTouchArea(botonRestart);
		}
		
		
	}
	
	
	public static void setRecordAndBest(int record, int best) {
		FinalMenu.record = record;
		FinalMenu.best = best;
	}
	
	
	public void dispose() {
		//background.detachSelf();
		//background.dispose();
		botonFacebook.detachSelf();
		botonFacebook.dispose();
		botonMarcadores.detachSelf();
		botonMarcadores.dispose();
		botonNoads.detachSelf();
		botonNoads.dispose();
		botonRestart.detachSelf();
		botonRestart.dispose();
		botonMenu.detachSelf();
		botonMenu.dispose();
		resultado.detachSelf();
		resultado.dispose();
		titleGameOver.detachSelf();
		titleGameOver.dispose();
		numeroBest.dispose();
		numeroRecord.dispose();
		this.detachSelf();
		this.dispose();
		
	}
	
	
	
	
	private void botonPulsado(BOTON_PULSADO boton) {
		if (boton == null) return;
		else {
			scene.resourcesManager.sonidoBoton.play();
			if (boton == BOTON_PULSADO.RESTAR) {
				SceneManager.getInstance().gameScene_to_gameScene(false);
			}
			if (boton == BOTON_PULSADO.MENU) {
				SceneManager.getInstance().gameScene_to_gameScene(true);
			}
			if (boton == BOTON_PULSADO.FACEBOOK) {
				if (FacebookDialog.canPresentShareDialog(scene.resourcesManager.actividad.getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
					FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(scene.resourcesManager.actividad)
						.setLink("http://play.google.com/store/apps/details?id=com.maetrik.jumpingball")   //Poner aqui la url del enlace de descarga en el play store
						.setPicture("https://dl.dropboxusercontent.com/u/74687711/ic_launcher2.png")
						.build();
					MainActivity.getUiLifecycleHelper().trackPendingDialogCall(shareDialog.present());
				} else {
					scene.resourcesManager.actividad.toastMassage("Facebook app not installed");
				}
			}
			if (boton == BOTON_PULSADO.RANKING) {
				scene.resourcesManager.actividad.signInClicked();
			}
			if (boton == BOTON_PULSADO.NO_ADS) {

			}
		}
	}
	
	
	
	

}
