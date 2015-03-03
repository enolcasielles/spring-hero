package com.maetrik.jumpingball.objetos;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.scenes.BaseScene;

public class Bloque {

	//---------------------
	//CONSTANTS
	//---------------------
	private final float VELOCIDAD = 120;
	
	//---------------------
	//VARIABLES
	//---------------------
	private Body body;
	private Rectangle rect;
	private float posX, posY, ancho, alto;
	private float separacion;
	private boolean superado;
	
	
	//---------------------
	//CONSTRUCTORS
	//---------------------
	public Bloque(BaseScene scene, float posX, float ancho, float alto) {
		this.superado = false;
		this.ancho = ancho;
		this.alto = alto;
		this.posY = Constants.ALTO_PANTALLA -  alto;
		this.posX = posX;
		this.separacion =
		  (float)Math.random() * (Constants.MAX_SEPARATION - Constants.MIN_SEPARATION) + Constants.MIN_SEPARATION; 
		rect = new Rectangle(this.posX, this.posY, this.ancho, this.alto, scene.vbom);
		rect.setColor(0.514f,0.514f,0.514f);
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(1.0f, 0.0f, 0.0f);
		this.body = PhysicsFactory.createBoxBody(scene.physicsWorld, rect, BodyType.StaticBody, wallFixtureDef);
		
		rect.setZIndex(0);
		scene.attachChild(rect);
		scene.sortChildren();
		
		//Registro el body
		scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect, this.body, true, true));
				
	}
	
	
	public void dispose() {
		rect.detachSelf();
		rect.dispose();
		rect = null;
		body = null;
	}

	
	//----------------------------
	//CLASS LOGIC
	//----------------------------
	public void mover(float segundosPasados) {
		if (this.body != null) {
			this.posX -= VELOCIDAD * segundosPasados;  //EspacioRecorrido = Velocidad * tiempo
			float x = (this.posX + this.ancho/2) / PIXEL_TO_METER_RATIO_DEFAULT;
			float y = body.getPosition().y;
			this.body.setTransform(x, y, 0);
		}
	}
	
	
	public boolean puedeApoyar(float x, float y) {
		if (x >= this.posX && x < this.posX + this.ancho){
			if (y > this.posY - 30 && y < this.posY + 30) return true;
		}
		return false;
	}
	
	public void redefinir(float ancho, float alto) {
		this.superado = false;
		rect.setWidth(ancho);
		rect.setHeight(alto);
		this.posX = Constants.ANCHO_PANTALLA;
		this.posY = Constants.ALTO_PANTALLA -  alto;
		this.separacion =
		  (float)Math.random() * (Constants.MAX_SEPARATION - Constants.MIN_SEPARATION) + Constants.MIN_SEPARATION; 
				
	}
	
	public void redefinir() {
		this.superado = false;
		this.posX = Constants.ANCHO_PANTALLA;
		this.posY = Constants.ALTO_PANTALLA -  alto;
		this.separacion =
		  (float)Math.random() * (Constants.MAX_SEPARATION - Constants.MIN_SEPARATION) + Constants.MIN_SEPARATION; 		
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
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}


	public float getPosX() {
		return posX;
	}
	
	public float getPosY() {
		return posY;
	}


	public void setPosX(float posX) {
		this.posX = posX;
	}


	public float getAncho() {
		return ancho;
	}


	public void setAncho(float ancho) {
		this.ancho = ancho;
	}


	public float getSeparacion() {
		return separacion;
	}


	public void setSeparacion(float separacion) {
		this.separacion = separacion;
	}
	
	
	

}
