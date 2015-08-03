package com.maetrik.jumpingball.objetos;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.objetos.Hero.OnStateChanged;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;


/**
 * Clase para representar el suelo y los arboles
 * @author Enol Casielles
 *
 */
public class Suelo {

	private Sprite suelo;
	private ArrayList<Sprite> arboles;
	
	private GameSceneBasic scene;
	
	private final float VELOCIDAD = 220;
	
	private enum ESTADO {
		MOVER,
		PARADO
	}
	private ESTADO estado;
	
	public Suelo(GameSceneBasic scene, Hero hero) {
		this.scene = scene;
		
		//Añado el suelo
		suelo = new Sprite(0, Constants.LAST_LINE-Constants.ALTO_SUELO, scene.resourcesManager.texturaSuelo, scene.vbom);
		scene.getLayer(CAPAS.CAPA_SUELO_ARBOLES).attachChild(suelo);
		
		Sprite tmp;
		//Añado los primeros arboles
		arboles = new ArrayList<Sprite>();
		for (int i = 0; i < 4; i++) {
			float x;
			if (i!=0) {
				x = arboles.get(i-1).getX() + arboles.get(i-1).getWidth();
			}
			else x = 0;
			x += Utils.aleatorioEntre(Constants.MIN_SEPARATION_ARBOL, Constants.MAX_SEPARATION_ARBOL);
			tmp = new Sprite(x, Constants.LAST_LINE - Constants.ALTO_SUELO - Constants.ALTO_ARBOL, 
					scene.resourcesManager.texturaArboles[(int)(Math.random()*Constants.NUM_ARBOLES)], scene.vbom);
			tmp.setScale(0.8f);
			tmp.setY(tmp.getY() + 0.1f*tmp.getHeight());
			arboles.add(tmp);
			scene.getLayer(CAPAS.CAPA_SUELO_ARBOLES).attachChild(tmp);
		}
		
		//Estado inicial
		estado = ESTADO.PARADO;
		
		//Me registro como oyente del heroe
		hero.addListener(new OnStateChanged() {
			@Override
			public void iniciaMovimiento() {
				estado = ESTADO.MOVER;	
			}
			@Override
			public void finalizaMovimiento() {
				estado = ESTADO.PARADO;
			}
		});
	}
	
	
	
	public void parar() {
		estado = ESTADO.PARADO;
	}
	
	
	public void mover() {
		estado = ESTADO.MOVER;
	}
	
	
	public void update(float sec) {
		//Si el estado es mver muevo todos los arboles y compruebo si el primero se sale de la pantalla
		if (estado == ESTADO.MOVER) {
			for (Sprite sprite : arboles) {
				sprite.setX(sprite.getX() - VELOCIDAD*sec);
			}
			if (arboles.get(0).getX() + arboles.get(0).getWidth() < 0) generaNuevo();
		}
	}
	
	
	public void dispose() {
		suelo.detachSelf();
		suelo.dispose();
		for (Sprite sprite : arboles) {
			sprite.detachSelf();
			sprite.dispose();
		}
		arboles.clear();
	}
	
	
	
	private void generaNuevo() {
		Sprite tmpUltimo = arboles.get(arboles.size()-1);
		float x = tmpUltimo.getX() + tmpUltimo.getWidth() + Utils.aleatorioEntre(Constants.MIN_SEPARATION_ARBOL, Constants.MAX_SEPARATION_NUBE);
		Sprite tmp = new Sprite(x, Constants.LAST_LINE - Constants.ALTO_SUELO - Constants.ALTO_ARBOL, 
				scene.resourcesManager.texturaArboles[(int)(Math.random()*Constants.NUM_ARBOLES)], scene.vbom);
		//Elimino el primero
		arboles.get(0).detachSelf();
		arboles.get(0).dispose();
		arboles.remove(0);
		//Añado el nuevo
		scene.getLayer(CAPAS.CAPA_SUELO_ARBOLES).attachChild(tmp);
		arboles.add(tmp);	
	}
}
