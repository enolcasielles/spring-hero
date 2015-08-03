package com.maetrik.jumpingball.objetos;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;


public class Bloque extends Sprite {

	//---------------------
	//CONSTANTS
	//---------------------
	private final float VELOCIDAD_MAX = 12;
	private final float FACTOR_VELOCIDAD = 0.4288f;
	
	//---------------------
	//VARIABLES
	//---------------------
	private float separacion;
	private boolean superado;
	private float alto;
	
	private Body body;

	
	
	//---------------------
	//CONSTRUCTORS
	//---------------------
	public Bloque(GameSceneBasic scene, float posX, float ancho, float alto) {
		super(posX, Constants.LAST_LINE-alto, scene.resourcesManager.texturaBloque, scene.vbom);
		this.superado = false;
		this.alto = alto;
		this.separacion =
		  (float)Math.random() * (Constants.MAX_SEPARATION - Constants.MIN_SEPARATION) + Constants.MIN_SEPARATION; 
		scene.getLayer(CAPAS.CAPA_BLOQUES_HERO).attachChild(this);
		
		//Asigno el body
		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1.0f, 0.0f, 1.0f);
		this.body = PhysicsFactory.createBoxBody(scene.getPhysicsWorld(), this, BodyType.KinematicBody, objectFixtureDef);
		
		//Registro el conector para que el sprite siga al body
		scene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
		
	}
	
	
	public void dispose() {
		this.detachSelf();
		this.dispose();
	}

	
	//----------------------------
	//CLASS LOGIC
	//----------------------------
	public void iniciaMovimiento(float velocidad) {
		this.body.setLinearVelocity(FACTOR_VELOCIDAD*velocidad*(-1), 0.0f);
	}
	
	public void finalizaMovimiento() {
		this.body.setLinearVelocity(0.0f, 0.0f);
	}
	
	
	
	/**
	 * Comprueba si el heroe pasa un blouqe sin tocarlo
	 * @param hero  El heroe
	 * @return  True si pasa sin tocarlo, false en caso contrario
	 */
	public boolean check(Hero hero) {
		if (hero.getX() > this.getX() + this.getWidth()) {  //Pasado sin tocar
			return true;
		}
		return false;
	}
	
	
	public void redefinir(Bloque bloque) {
		this.superado = false;
		this.setX(bloque.getX() + bloque.getWidth() + bloque.getSeparacion());
		this.separacion =
		  (float)Math.random() * (Constants.MAX_SEPARATION - Constants.MIN_SEPARATION) + Constants.MIN_SEPARATION; 	
		
		//Ajusto al body a la nueva posicion del sprite
		final float xBody = (this.getX() + this.getWidth()/2) / PIXEL_TO_METER_RATIO_DEFAULT;  //Paso de pixeles a metros
		final float yBody = (this.body.getPosition().y);
		this.body.setTransform(xBody, yBody, 0.0f);
	}
	


	

	//-----------------------------
	//GETTERS
	//-----------------------------
	public boolean isSuperado() {
		return superado;
	}

	public void setSuperado(boolean superado) {
		this.superado = superado;
	}


	public float getSeparacion() {
		return separacion;
	}


	public void setSeparacion(float separacion) {
		this.separacion = separacion;
	}
	
}
