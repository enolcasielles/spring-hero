package com.maetrik.jumpingball.objetos;

import org.andengine.entity.sprite.Sprite;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.objetos.Hero.OnStateChanged;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;

public class Mountains { 
	
	//CONSTANTS
	private final float VELOCIDAD = 170;
	
	
	//VARIABLES
	private enum ESTADO {
		PARADA,
		MOVIMIENTO
	};
	private ESTADO estado;
	
	private Sprite[] mountains;
	private GameSceneBasic scene;
	
	public Mountains(GameSceneBasic scene, Hero hero) {
		mountains = new Sprite[2];
		mountains[0] = new Sprite(0.0f, Constants.LAST_LINE-Constants.ALTO_SUELO + 3 - Constants.ALTO_MOUNTAIN, scene.resourcesManager.texturaMountain, scene.vbom);
		mountains[1] = new Sprite(Constants.ANCHO_MOUNTAIN, Constants.LAST_LINE-Constants.ALTO_SUELO + 3 - Constants.ALTO_MOUNTAIN, scene.resourcesManager.texturaMountain, scene.vbom);
		mountains[0].setAlpha(0.95f);
		mountains[1].setAlpha(0.95f);
		scene.getLayer(CAPAS.CAPA_MOUNTAINS).attachChild(mountains[0]);
		scene.getLayer(CAPAS.CAPA_MOUNTAINS).attachChild(mountains[1]);
		this.scene = scene;
		estado = ESTADO.PARADA;
		
		//Me registro como oyente del heroe
		hero.addListener(new OnStateChanged() {
			@Override
			public void iniciaMovimiento() {
				estado = ESTADO.MOVIMIENTO;	
			}
			@Override
			public void finalizaMovimiento() {
				estado = ESTADO.PARADA;
			}
		});
	}
	
	
	/**
	 * Mueve las montañas si su estado lo permite 
	 * @param sec Los segundos desde el ultimo update
	 */
	public void update(float sec) {
		if (estado == ESTADO.MOVIMIENTO) {
			mountains[0].setX(mountains[0].getX() - VELOCIDAD*sec);
			mountains[1].setX(mountains[1].getX() - VELOCIDAD*sec);
			
			//Compruebo si se han de cambiar
			if (mountains[0].getX() + mountains[0].getWidth() < 0) {
				mountains[0].detachSelf();
				mountains[0].dispose();
				mountains[0] = mountains[1];
				mountains[1] = new Sprite(Constants.ANCHO_MOUNTAIN, Constants.LAST_LINE-Constants.ALTO_SUELO + 3 - Constants.ALTO_MOUNTAIN, scene.resourcesManager.texturaMountain, scene.vbom);
				mountains[1].setAlpha(0.95f);
				scene.getLayer(CAPAS.CAPA_MOUNTAINS).attachChild(mountains[1]);
			}
		}
	}
	
	
	public void dispose() {
		mountains[0].detachSelf();
		mountains[0].dispose();
		mountains[1].detachSelf();
		mountains[1].dispose();
	}

}
