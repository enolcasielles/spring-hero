package com.maetrik.jumpingball.objetos;

import java.util.ArrayList;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.objetos.Hero.OnStateChanged;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;

public class Mar {
	
	private ArrayList<Sprite> capa1;
	private ArrayList<Sprite> capa2;
	private ArrayList<Sprite> capa3;
	
	private final float VELOCIDAD = 100;
	
	private GameSceneBasic scene;
	
	private enum ESTADO {
		MOVER,
		PARAR
	};
	private ESTADO estado;
	
	public Mar(GameSceneBasic scene, Hero hero) {
		
		this.scene = scene;
		
		Sprite tmp;
		
		float yMin = Constants.LAST_LINE - Constants.ALTO_MAR;
		float yMax = yMin + 20;
		
		//Capa 1
		capa1 = new ArrayList<Sprite>();
		boolean simetrico = true;
		for (int i=0 ; i<2 ; i++) {
			if (i==0) tmp = new Sprite(0, yMin, scene.resourcesManager.texturaMar1, scene.vbom);
			else tmp = new Sprite(capa1.get(i-1).getX()+capa1.get(i-1).getWidth()-1, yMin, scene.resourcesManager.texturaMar1, scene.vbom);
			if (simetrico) {
				tmp.setFlippedHorizontal(true);
				simetrico = false;
			}
			else {
				simetrico = true;
			}
			capa1.add(tmp);
			scene.getLayer(CAPAS.CAPA_MAR_1).attachChild(tmp);
		}
		
		//Capa 2
		capa2 = new ArrayList<Sprite>();
		simetrico = true;
		for (int i=0 ; i<2 ; i++) {
			if (i==0) tmp = new Sprite(0, yMin, scene.resourcesManager.texturaMar2, scene.vbom);
			else tmp = new Sprite(capa2.get(i-1).getX()+capa2.get(i-1).getWidth()-1, yMin, scene.resourcesManager.texturaMar2, scene.vbom);
			if (simetrico) {
				tmp.setFlippedHorizontal(true);
				simetrico = false;
			}
			else {
				simetrico = true;
			}
			capa2.add(tmp);
			scene.getLayer(CAPAS.CAPA_MAR_2).attachChild(tmp);
		}
		
		//Capa 3
		capa3 = new ArrayList<Sprite>();
		simetrico = true;
		for (int i=0 ; i<2 ; i++) {
			if (i==0) tmp = new Sprite(0, yMin, scene.resourcesManager.texturaMar3, scene.vbom);
			else tmp = new Sprite(capa3.get(i-1).getX()+capa3.get(i-1).getWidth()-1, yMin, scene.resourcesManager.texturaMar3, scene.vbom);
			if (simetrico) {
				tmp.setFlippedHorizontal(true);
				simetrico = false;
			}
			else {
				simetrico = true;
			}
			capa3.add(tmp);
			scene.getLayer(CAPAS.CAPA_MAR_3).attachChild(tmp);
		}
		
		
		//Registro modificadores
		for (Sprite sprite : capa1) {
			sprite.registerEntityModifier(
					new LoopEntityModifier(
							new SequenceEntityModifier(new MoveYModifier(1.0f,yMin,yMax),
													   new MoveYModifier(1.0f,yMax,yMin))));
		}
		for (Sprite sprite : capa2) {
			sprite.registerEntityModifier(
					new LoopEntityModifier(
							new SequenceEntityModifier(new MoveYModifier(0.5f,sprite.getY(),yMax),
													   new MoveYModifier(1.0f,yMax,yMin),
													   new MoveYModifier(0.5f,yMin,sprite.getY()))));
		}
		for (Sprite sprite : capa3) {
			sprite.registerEntityModifier(
					new LoopEntityModifier(
							new SequenceEntityModifier(new MoveYModifier(1.0f,yMax,yMin),
									   				   new MoveYModifier(1.0f,yMin,yMax))));
		}
		
		this.estado = ESTADO.PARAR;
		
		//Me registro como oyente del heroe
		hero.addListener(new OnStateChanged() {
			@Override
			public void iniciaMovimiento() {
				estado = ESTADO.MOVER;
			}
			@Override
			public void finalizaMovimiento() {
				estado = ESTADO.PARAR;
			}
		});
	}
	
	
	public void dispose() {
		for (Sprite sprite : capa1) {
			sprite.detachSelf();
			sprite.dispose();
		}
		for (Sprite sprite : capa2) {
			sprite.detachSelf();
			sprite.dispose();
		}
		for (Sprite sprite : capa3) {
			sprite.detachSelf();
			sprite.dispose();
		}
		capa1.clear();
		capa2.clear();
		capa3.clear();
	}
	
	
	
	/**
	 * Mueve los mares si su estado lo permite 
	 * @param sec Los segundos desde el ultimo update
	 */
	public void update(float sec) {
		if (estado == ESTADO.MOVER) {
			
			
			//Actualizo capa1
			for (Sprite sprite : capa1) {
				sprite.setX(sprite.getX() - VELOCIDAD*sec);
				sprite.setX(sprite.getX() - VELOCIDAD*sec);
			}
			
			//Compruebo se se ha de mover el primero
			Sprite tmp = capa1.get(0);
			if (tmp.getX() + tmp.getWidth() < 0) {
				tmp.setX(capa1.get(capa1.size()-1).getX()+tmp.getWidth()-1);
				capa1.add(tmp);
				capa1.remove(0);
			}
			
			
			//Actualizo capa2
			for (Sprite sprite : capa2) {
				sprite.setX(sprite.getX() - VELOCIDAD*sec);
				sprite.setX(sprite.getX() - VELOCIDAD*sec);
			}
			
			
			//Compruebo se se ha de mover el primero
			tmp = capa2.get(0);
			if (tmp.getX() + tmp.getWidth() < 0) {
				tmp.setX(capa2.get(capa2.size()-1).getX()+tmp.getWidth()-1);
				capa2.add(tmp);
				capa2.remove(0);
			}
			
			
			//Actualizo capa3
			for (Sprite sprite : capa3) {
				sprite.setX(sprite.getX() - VELOCIDAD*sec);
				sprite.setX(sprite.getX() - VELOCIDAD*sec);
			}
			
			
			//Compruebo se se ha de mover el primero
			tmp = capa3.get(0);
			if (tmp.getX() + tmp.getWidth() < 0) {
				tmp.setX(capa3.get(capa3.size()-1).getX()+tmp.getWidth()-1);
				capa3.add(tmp);
				capa3.remove(0);
			}
			
		}
	}

}
