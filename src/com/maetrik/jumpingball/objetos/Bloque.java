package com.maetrik.jumpingball.objetos;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import android.R.integer;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.scenes.GameSceneBasic;

public class Bloque {

	//---------------------
	//CONSTANTS
	//---------------------
	private final float VELOCIDAD = 120;
	private final float TIEMPO_SUBIDA = 0.1f;
	
	private final int ESTA_BAJANDO = 0;
	private final int ESTA_SUBIENDO = 1;
	private final int ESTA_PARADA = 2;
	
	
	//---------------------
	//VARIABLES
	//---------------------
	private Body body;
	private Rectangle rect;
	private Rectangle r;
	private float posX, posY, ancho, alto;
	private float separacion;
	private boolean superado;
	
	private int estado;
	private float vel;
	
	private OnEstadoCarga onEstadoCarga;
	
	
	//---------------------
	//CONSTRUCTORS
	//---------------------
	public Bloque(GameSceneBasic scene, float posX, float ancho, float alto, boolean esPrimero) {
		this.superado = false;
		this.ancho = ancho;
		this.alto = alto;
		this.posY = Constants.ALTO_PANTALLA -  alto;
		this.posX = posX;
		this.separacion =
		  (float)Math.random() * (Constants.MAX_SEPARATION - Constants.MIN_SEPARATION) + Constants.MIN_SEPARATION; 
		rect = new Rectangle(this.posX, this.posY, this.ancho, this.alto, scene.vbom);
		rect.setColor(0.514f,0.514f,0.514f);
		if (!esPrimero) {
			r = new Rectangle(0.0f, 0.0f, this.ancho, 30, scene.vbom);
			r.setColor(1.0f, 1.0f, 1.0f);
			rect.attachChild(r);
		}
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(1.0f, 0.0f, 0.0f);
		this.body = PhysicsFactory.createBoxBody(scene.physicsWorld, rect, BodyType.StaticBody, wallFixtureDef);
		
		rect.setZIndex(0);
		scene.getChild(3).attachChild(rect);
		
		//Registro el body
		scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(rect, this.body, true, true));
		
		estado = ESTA_PARADA;
				
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
		this.r.setY(0.0f);
		this.posX = Constants.ANCHO_PANTALLA;
		this.posY = Constants.ALTO_PANTALLA -  alto;
		this.separacion =
		  (float)Math.random() * (Constants.MAX_SEPARATION - Constants.MIN_SEPARATION) + Constants.MIN_SEPARATION; 		
	}
	
	
	
	public void update(float seconds) {
		if (estado == ESTA_BAJANDO) {
			this.r.setY(this.r.getY()+1.5f);
		}
		
		else if(estado == ESTA_SUBIENDO) {
			this.r.setY(this.r.getY() - vel*seconds);
			if (this.r.getY() <= 0) {
				this.r.setY(0.0f);
				estado = ESTA_PARADA;
				onEstadoCarga.descargaFinalizada();
			}
		}
	}
	
	
	public void iniciaCarga() {
		estado = ESTA_BAJANDO;
	}
	
	public void finalizaCarga(OnEstadoCarga oec) {
		this.onEstadoCarga = oec;
		estado = ESTA_SUBIENDO;
		//Calculo velocidad subida
		vel = this.r.getY() / TIEMPO_SUBIDA;
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
	
	
	
	public interface OnEstadoCarga {
		 public abstract void descargaFinalizada();
	}
	

}
