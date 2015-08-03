package com.maetrik.jumpingball.objetos;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

public class Muelle extends Sprite{
	
	private GameSceneBasic scene;
	
	private final float CONSTANTE_CARGA = 2.0f;
	private final float SCALE_MIN = 0.1f;
	
	private enum ESTADO {
		CARGANDO,
		CARGADO_MAXIMO,
		DESCARGANDO,
		REPOSO
	}
	private ESTADO estado;
	
	private float yRef, altoRef;
	private Hero hero;
	
	public Muelle(GameSceneBasic scene, Bloque bloque, Hero hero) {
		
		super(0,0, scene.resourcesManager.texturaMuelle, scene.vbom);
		
		this.scene = scene;
		
		//Posiciono el muelle debajo del hero
		this.setX(bloque.getX() + bloque.getWidth()/2 - this.getWidth()/2);
		this.setY(bloque.getY() - this.getHeight());
		
		yRef = this.getY();
		altoRef = this.getHeight();
		
		this.hero = hero;
		
		estado = ESTADO.REPOSO;
		
		scene.getLayer(CAPAS.CAPA_BLOQUES_HERO).attachChild(this);
		
	}
	
	
	public void iniciarCarga() {
		estado = ESTADO.CARGANDO;
		yRef = this.getY();
	}
	
	public void finalizaCarga() {
		this.setVisible(false);
	}
	
	public void iniciaDescarga(float yRef) {
		estado = ESTADO.DESCARGANDO;
		this.setScaleY(0.0f);
		this.setY(yRef - 0.5f*this.getHeight());
		this.setVisible(true);
		this.yRef = this.getY();
		hero.setY(yRef - hero.getHeight());
	}
	
	
	public boolean isCargando() {
		return estado == ESTADO.CARGANDO;
	}
	
	public boolean isReposo() {
		return estado == ESTADO.REPOSO;
	}
	
	
	
	public void dispose() {
		this.detachSelf();
		this.dispose();
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		super.onManagedUpdate(pSecondsElapsed);
		
		//Si esta cargando
		if (estado == ESTADO.CARGANDO) {
			float scaleAnt = this.getScaleY();
			if (scaleAnt <= SCALE_MIN) {
				estado = ESTADO.CARGADO_MAXIMO;
				return;
			}
			this.setScaleY(scaleAnt-pSecondsElapsed*CONSTANTE_CARGA);
			float yAnt = this.getY();
			this.setY(yRef + altoRef*0.5f*(1-this.getScaleY()));
			float yAct = this.getY();
			float despl = yAct - yAnt;
			hero.setY(hero.getY()+2*despl);
		}
		
		
		if (estado == ESTADO.DESCARGANDO) {
			float scaleAnt = this.getScaleY();
			this.setScaleY(scaleAnt+pSecondsElapsed*CONSTANTE_CARGA);
			if(scaleAnt >= 1.0) estado = ESTADO.REPOSO;  //No entra mas en este if
			float yAnt = this.getY();
			this.setY(yRef - altoRef*0.5f*(this.getScaleY()));
			float yAct = this.getY();
			float despl = yAct - yAnt;
			hero.setY(hero.getY()+2*despl);
		}

	}

}
