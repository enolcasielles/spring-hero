package com.maetrik.jumpingball.objetos;

import org.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.math.Vector2;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.objetos.Hero.OnStateChanged;
import com.maetrik.jumpingball.scenes.GameSceneBasic;

public class Ojos extends Sprite {
	
	private Vector2[] posiciones;
	private final int POSICION_SALTO = 0;
	private GameSceneBasic scene;
	
	private float xRef, yRef;  //Variables que representan la posicion inicial de los ojos, es decir la ultima del vector
	
	private enum ESTADO {
		SALTANDO,
		CARGANDO,
		REPOSO
	}
	private ESTADO estado;
	
	
	public Ojos(float x, float y, GameSceneBasic scene, Hero heroe) {
		
		super(x, y, scene.resourcesManager.texturaOjos, scene.vbom);
		
		this.scene = scene;
		
		xRef = x;
		yRef = yRef;
		
		//Defino las posibles posiciones
		posiciones = new Vector2[9];
		posiciones[POSICION_SALTO] = new Vector2(0.707f, -0.707f);
		posiciones[1] = new Vector2(0.707f, 0.707f);
		posiciones[2] = new Vector2(1,0);
		posiciones[3] = new Vector2(0, 1);
		posiciones[4] = new Vector2(0, -1);
		posiciones[5] = new Vector2(-0.707f, -0.707f);
		posiciones[6] = new Vector2(-1, 0);
		posiciones[7] = new Vector2(-0.707f, 0.707f);
		posiciones[8] = new Vector2(0, 0);
		
		
		//Me ato al heroe
		heroe.attachChild(this);
		
		//Me registro como oyente del hereo
		heroe.addListener(new OnStateChanged() {
			@Override
			public void iniciaMovimiento() {
				estado = ESTADO.SALTANDO;
				Ojos.this.setX(xRef + posiciones[POSICION_SALTO].x*Constants.RADIO_PERMITIDO_OJOS);
				Ojos.this.setX(yRef + posiciones[POSICION_SALTO].y*Constants.RADIO_PERMITIDO_OJOS);
			}
			@Override
			public void finalizaMovimiento() {
				estado = ESTADO.REPOSO;
			}
		});
		
		estado = ESTADO.REPOSO;
		
	}
	
	
	
	public void dispose() {
		this.detachSelf();
		this.dispose();
	}
	
	
	public void iniciarCarga() {
		estado = ESTADO.CARGANDO;
		//*************COMPLETAR******************
	}
	
	
	//-------------------------------------------------------
	//SUPERCLASS METHODS
	//-------------------------------------------------------
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		super.onManagedUpdate(pSecondsElapsed);
		
		if (estado == ESTADO.REPOSO) {  //Puedo jugar con los ojos
			//Numero al azar entre 0 y 1
			double aleatorio = Math.random();
			if (aleatorio < 0.01) {  //Efectuo un movimiento
				//Otro aleatorio para dar mas peso al centro
				aleatorio = Math.random();
				if (aleatorio < 0.5) {  //Posicion del centro
					this.setX(xRef + posiciones[8].x*Constants.RADIO_PERMITIDO_OJOS);
					this.setY(yRef + posiciones[8].y*Constants.RADIO_PERMITIDO_OJOS);
				}
				else {  //Aleatoriamente cualquier otra posicion, numero entre 1 y 8 incluidos
					int aleatorio2 = (int)(Math.random()*8) + 1;
					this.setX(xRef + posiciones[aleatorio2].x*Constants.RADIO_PERMITIDO_OJOS);
					this.setY(yRef + posiciones[aleatorio2].y*Constants.RADIO_PERMITIDO_OJOS);
				}
			}
		}
		
	}

}
