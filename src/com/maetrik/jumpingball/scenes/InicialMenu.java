package com.maetrik.jumpingball.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.transition.Scene;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.SceneManager;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.objetos.Number;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;

public class InicialMenu extends Rectangle {
	
	//CONSTANTES
	
	//VARIABLES
	private Sprite titleGame, botonPlay, botonSound, botonNoSound, botonRate;
	private GameSceneBasic scene;
	
	private float xTouch, yTouch;
	
	private enum BOTON_PULSADO {
		PLAY,
		SOUND,
		NOSOUND,
		RATE
	}
	private BOTON_PULSADO boton;
	
	
	public InicialMenu(GameSceneBasic scene) {
		
		super(0, 0, Constants.ANCHO_PANTALLA, Constants.ALTO_PANTALLA, scene.vbom);
		
		float Y_GAME_TITLE = 0.5f*(Constants.FIRST_LINE + Constants.LAST_LINE) - 250f;
		float Y_BOTON_PLAY = 0.5f*(Constants.FIRST_LINE + Constants.LAST_LINE) + 50f;
		float Y_BOTONES = 0.5f*(Constants.FIRST_LINE + Constants.LAST_LINE) + 200f;
		float SEPARACION_BOTONES = 30;
	
		
		this.scene = scene;
	
		this.setAlpha(0.6f);
		
		//background = new Sprite(0, 0, scene.resourcesManager.textureBlanco, scene.vbom);
		//this.attachChild(background);
		
		
		titleGame =  new Sprite(0, 0, scene.resourcesManager.texturaNombre, scene.vbom);
		titleGame.setPosition(Constants.ANCHO_PANTALLA/2 - titleGame.getWidth()/2, Y_GAME_TITLE);
		titleGame.setScale(1.6f);
		this.attachChild(titleGame);
		
		botonPlay =  new Sprite(0, 0, scene.resourcesManager.texturePlay, scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					this.setScale(1.2f);
					xTouch = pTouchAreaLocalX;
					yTouch = pTouchAreaLocalY;
					boton = BOTON_PULSADO.PLAY;
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
		botonPlay.setPosition(Constants.ANCHO_PANTALLA/2 - botonPlay.getWidth()/2, Y_BOTON_PLAY);
		this.attachChild(botonPlay);
		
		botonSound =  new Sprite(0, 0, scene.resourcesManager.texturaVolumen, scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					this.setScale(1.2f);
					xTouch = pTouchAreaLocalX;
					yTouch = pTouchAreaLocalY;
					boton = BOTON_PULSADO.SOUND;
					botonPulsado(boton);
					return true;
				}
				return false;
			}
		};
		botonSound.setPosition(SEPARACION_BOTONES, Y_BOTONES);
		this.attachChild(botonSound);
		
		botonNoSound =  new Sprite(0, 0, scene.resourcesManager.textureNoVolme, scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					xTouch = pTouchAreaLocalX;
					yTouch = pTouchAreaLocalY;
					boton = BOTON_PULSADO.NOSOUND;
					botonPulsado(boton);
					return true;
				}
				return false;
			}
		};
		botonNoSound.setPosition(SEPARACION_BOTONES, Y_BOTONES);
		this.attachChild(botonNoSound);
		botonNoSound.setVisible(false);
		
		botonRate =  new Sprite(0, 0, scene.resourcesManager.texturaRate, scene.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if (pSceneTouchEvent.isActionDown()) {
					this.setScale(1.2f);
					xTouch = pTouchAreaLocalX;
					yTouch = pTouchAreaLocalY;
					boton = BOTON_PULSADO.RATE;
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
		botonRate.setPosition(Constants.ANCHO_PANTALLA - SEPARACION_BOTONES - botonRate.getWidth(), Y_BOTONES);
		this.attachChild(botonRate);
		
		
		
		//Añado esta capa a la escena
		scene.getLayer(CAPAS.CAPA_MENUS).attachChild(this);
		super.setVisible(false);

		
	}
	
	
	
	public void setVisible(boolean v) {
		
		super.setVisible(v);
		if (v){  //Registro escuchadores en todos los botones
			scene.registerTouchArea(botonPlay);
			scene.registerTouchArea(botonSound);
			scene.registerTouchArea(botonNoSound);
			scene.registerTouchArea(botonRate);
		}
		
		else {
			scene.unregisterTouchArea(botonPlay);
			scene.unregisterTouchArea(botonSound);
			scene.unregisterTouchArea(botonNoSound);
			scene.unregisterTouchArea(botonRate);
		}
		
		
	}
	
	
	
	public void dispose() {
		//background.detachSelf();
		//background.dispose();
		botonPlay.detachSelf();
		botonPlay.dispose();
		botonRate.detachSelf();
		botonRate.dispose();
		botonSound.detachSelf();
		botonSound.dispose();
		botonNoSound.detachSelf();
		botonNoSound.dispose();
		titleGame.detachSelf();
		titleGame.dispose();
		this.detachSelf();
		this.dispose();
		
	}
	
	
	
	
	private void botonPulsado(BOTON_PULSADO boton) {
		if (boton == null) return;
		else {
			scene.resourcesManager.sonidoBoton.play();
			if (boton == BOTON_PULSADO.PLAY) {
				scene.iniciar();
			}
			if (boton == BOTON_PULSADO.SOUND) {
				botonSound.setVisible(false);
				botonNoSound.setVisible(true);
			}
			if (boton == BOTON_PULSADO.NOSOUND) {
				botonSound.setVisible(true);
				botonNoSound.setVisible(false);
			}
			if (boton == BOTON_PULSADO.RATE) {
				
			}
		}
	}
	
	
	
	

}
