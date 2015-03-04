package com.maetrik.jumpingball.objetos;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.color.Color;

import android.graphics.drawable.shapes.Shape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.scenes.BaseScene;

public class Bola extends AnimatedSprite  {
	
	//-----------------------------------
	//CONSTANTS
	//-----------------------------------
	private static final float MAX_CARGA = 60;
	private static final float MIN_CARGA = 10;
	
	//-----------------------------------
	//VARIABLES
	//-----------------------------------
	private float salto;
	private boolean cargandoSalto;
	
	private Body body;
	private BaseScene scene;

	private PhysicsConnector conector;
	
	private boolean puedeReiniciar;
	
	private int numVidas;
	
	//-----------------------------------
	//CONSTRUCTOR
	//-----------------------------------
	public Bola(BaseScene scene) {
		
		super(scene.camera.getWidth()/2, 0, 
				scene.resourcesManager.texturaPersonaje, scene.vbom);
		this.animate(200);
		
		
		//Iniciamos el sprite
		this.scene = scene;
		
		
		//Generamos el body para el sprite y y le damos un identificador
		body = PhysicsFactory.createCircleBody(scene.physicsWorld, 
				this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1.0f, 0.0f, 0.0f));
		
		body.setUserData("saltador");
	
		
		//Añado el sprite a la escena
		scene.attachChild(this);
		
		//Registro el body
		conector = new PhysicsConnector(this, body, true, true);
		scene.physicsWorld.registerPhysicsConnector(conector);
		
		//Valor de la carga del salto 10 inicialmente
		cargandoSalto = false;
		
		puedeReiniciar = false;
		
		numVidas = 0;
		
	}

	//-----------------------------------
	//GETTERS & SETTERS
	//-----------------------------------
	public float getPosX() {
		return body.getPosition().x * PIXEL_TO_METER_RATIO_DEFAULT;
	}


	public float getPosY() {
		return  body.getPosition().y * PIXEL_TO_METER_RATIO_DEFAULT;
	}

	
	//-----------------------------------
	//CLASS METHODS
	//-----------------------------------
	public void saltar() {
		body.setLinearVelocity(new Vector2(0, salto*(-1)/5));
		salto = MIN_CARGA;
	}
	
	public void saltar(float salto) {
		body.setLinearVelocity(new Vector2(0, salto*1.009f));
		salto = MIN_CARGA;
	}
	
	public void addVidas() {
		numVidas++;
	}
	
	public int getNumVidas() {
		return numVidas;
	}
	
	public void downVidas() {
		numVidas--;
	}
	
	public void cargar() {
		if (cargandoSalto) {
			salto++;
			if (salto >= MAX_CARGA) {
				salto = (long)MAX_CARGA;
			}
		}
	}
	
	public void comienzaCarga() {
		cargandoSalto = true;
		salto = MIN_CARGA;
	}
	
	public void finalizarCarga() {
		cargandoSalto = false;
		this.setScale(1.0f);
	}
	
	
	public boolean estaMuerto() {
		if (getPosX() - this.getWidth()/2 < 0 || getPosY() - this.getHeight()/2 > Constants.ALTO_PANTALLA)
			return true;
		else return false;
	}
	
	public void setAutomatic(boolean automatic) {
		if (automatic == true) { //Se establece automatico
			scene.physicsWorld.unregisterPhysicsConnector(conector);
			
			this.setPosition(Constants.ANCHO_PANTALLA/2, Constants.ALTO_PANTALLA/4);
		}
		else {  //Se establece manual
			body = PhysicsFactory.createCircleBody(scene.physicsWorld, 
					this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1.0f, 0.0f, 0));
			conector = new PhysicsConnector(this, body, true, false);
			scene.physicsWorld.registerPhysicsConnector(conector);
			
		}
	}
	
	
	public void animacionReinicio(float x, float y, float width, float height) {
		//Modifier desplazamiento
		float anchoInit = this.getWidth();
		float altoInit = this.getHeight();
		float xInit = this.getX();
		float yInit = this.getY();
		float scaleX = width / anchoInit;
		float ScaleY = height / altoInit;
		ScaleModifier scaleModifier = new ScaleModifier(0.5f, scaleX, 1.0f, ScaleY, 1.0f);
		MoveModifier moveModifier = new MoveModifier(0.5f, x, xInit, y, yInit);
		ParallelEntityModifier parallelEntityModifier = new ParallelEntityModifier(scaleModifier,moveModifier) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				// TODO Auto-generated method stub
				super.onModifierFinished(pItem);
				puedeReiniciar = true;
			}
		};
		this.registerEntityModifier(parallelEntityModifier);  
	}
	
	
	public boolean puedeReiniciar() {
		return puedeReiniciar;
	}

}
