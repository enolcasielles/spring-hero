package com.maetrik.jumpingball.objetos;

import java.util.ArrayList;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;


public class Hero extends AnimatedSprite  {
	
	//-----------------------------------
	//CONSTANTS
	//-----------------------------------
	private static final float MAX_CARGA = 60;
	private static final float MIN_CARGA = 0;
	private final float CONSTANTE_CARGA = 50;
	private final float MULTIPLICADOR_SALTO=1;
	private final int FRAME_NORMAL = 0;
	private final int FRAME_SALTO = 1;
	
	//-----------------------------------
	//VARIABLES
	//-----------------------------------
	private float salto, velocidad;
	
	private int multiplicador, score;
	
	private GameSceneBasic scene;
	private ContenedorBloques bloques;
	
	private enum ESTADO {
		CARGANDO,
		REPOSO,
		SALTANDO,
		MURIENDO
	};
	private ESTADO estado;
	
	//Siguiente bloque que el heroe ha de estar controlando
	private Bloque bloque;
	
	private Body body; 
	private final FixtureDef objectFixtureDef;
	private PhysicsConnector physicsConnector;
	
	//Array con todos los oyentes de la interface
	private ArrayList<OnStateChanged> oyentes;
	
	//Ojos
	private Ojos ojos;
	
	//Muelle
	private Muelle muelle;
	
	//Pajaro
	private Pajaro pajaro;
	
	
	
	//-----------------------------------
	//CONSTRUCTOR
	//-----------------------------------
	public Hero(GameSceneBasic scene, ContenedorBloques bloques) {
		
		super(0, 0, scene.resourcesManager.texturaHeroe, scene.vbom);
		
		this.muelle = new Muelle(scene, bloques.getBloque(0),this);
		
		//Posiciono el heroe encima del muelle
		this.setX(bloques.getBloque(0).getX() + bloques.getBloque(0).getWidth()/2 - this.getWidth()/2);
		
		this.setY(muelle.getY() - this.getHeight());
		
		this.setCurrentTileIndex(FRAME_NORMAL);
		
		//Iniciamos la escena
		this.scene = scene;
		
		this.bloques = bloques;
	
		//AÃ±ado el sprite a la escena
		scene.getLayer(CAPAS.CAPA_BLOQUES_HERO).attachChild(this);

		estado = ESTADO.REPOSO;
		
		velocidad = 0;
		
		score = 0;
		multiplicador = 1;
		
		//De momento no tiene que controlar ningun bloque
		bloque = null;
		
		//Asigno el body
		objectFixtureDef = PhysicsFactory.createFixtureDef(1.0f, 0.0f, 1.0f);
		this.body = PhysicsFactory.createBoxBody(scene.getPhysicsWorld(), this, BodyType.DynamicBody, objectFixtureDef);
		physicsConnector = new PhysicsConnector(this, body, true, false);
		//Registro el conector para que el sprite siga al body
		//scene.getPhysicsWorld().registerPhysicsConnector(physicsConnector);
		
		//Identificador para reconocer este body
		body.setUserData("hero");
		
		//Registro el conecntor
		scene.getPhysicsWorld().setContactListener(createContactListener());
		
		//Inicio el contenedor de oyentes
		oyentes = new ArrayList<Hero.OnStateChanged>();
		
		//Defino los ojos, el propio objeto se encarga de añadirlos al heroe
		ojos = new Ojos(Constants.OJO_IZQ_X_REF, Constants.OJO_IZQ_Y_REF, scene, this);
		
	}

	
	//-----------------------------------
	//CLASS METHODS
	//-----------------------------------
	
	
	public void dispose() {
		muelle.detachSelf();
		muelle.dispose();
		ojos.detachSelf();
		ojos.dispose();
		this.detachSelf();
		this.dispose();
	}
	
	public boolean estaMuerto() {
		if (getY() - this.getHeight() > Constants.LAST_LINE) {
			scene.resourcesManager.sonidoCaer.play();
			return true;	
		}
		else return false;
	}
	


	//Registra el pajar
	public void registerPajaro(Pajaro pajaro) {
		this.pajaro = pajaro;
	}
	
	
	/**
	 * Inicia la carga del saltador si es posible y establece modo cargando
	 */
	public void iniciarCarga() {
		if (this.estado == ESTADO.REPOSO && muelle.isReposo()) {
			estado = ESTADO.CARGANDO;
			scene.resourcesManager.sonidoCargar.play();
			//Desactivo Body
			scene.getPhysicsWorld().unregisterPhysicsConnector(physicsConnector);
			//scene.getPhysicsWorld().destroyBody(this.body);
			
			muelle.iniciarCarga();
			ojos.iniciarCarga();
			pajaro.marchar();
		}
	}
	
	
	/**
	 * Finaliza la carga del saltador si es posible y establece modo salto
	 */
	public void finalizaCarga() {
		if (this.estado == ESTADO.CARGANDO) {
			estado = ESTADO.SALTANDO;
			velocidad = salto*(-1)*MULTIPLICADOR_SALTO;
			scene.resourcesManager.sonidoSaltar.play();
			//Registro el conector para que el sprite siga al body
			scene.getPhysicsWorld().registerPhysicsConnector(physicsConnector);
			
			this.body.setLinearVelocity(0, velocidad);
			this.setCurrentTileIndex(FRAME_SALTO);
			//Inicio movimiento en bloques
			bloques.iniciaMovimiento(salto);
			salto = MIN_CARGA;
			for (OnStateChanged oyente : oyentes) {
				oyente.iniciaMovimiento();
			}
			muelle.finalizaCarga();
		}
	}
	
	
	
	/**
	 * Inicia la muerte del heroe
	 */
	public void iniciaMuerte() {
		if (this.estado == ESTADO.SALTANDO) {
			this.estado = ESTADO.MURIENDO;
			bloques.finalizaMovimiento();
			for (OnStateChanged oyente : oyentes) {
				oyente.finalizaMovimiento();
			}
		}
	}
	
	
	public Body getBody() {
		return body;
	}
	
	
	/**
	 * Debera llamarse cuando el heroe caiga correctamente en un bloque
	 */
	public void pasaNivel(float yRef) {
		if (this.estado == ESTADO.SALTANDO) {
			this.estado = ESTADO.REPOSO;
			velocidad = 0;
			this.body.setLinearVelocity(0, velocidad);
			this.setCurrentTileIndex(FRAME_NORMAL);
			if (multiplicador != 1) multiplicador *=2;
			score += multiplicador;
			if (score >= Constants.RECORD) {
				Constants.RECORD = score;
				Utils.saveRecord();
			}
			multiplicador = 1;
			bloques.finalizaMovimiento();
			for (OnStateChanged oyente : oyentes) {
				oyente.finalizaMovimiento();
			}
			//Desactivo Body
			scene.getPhysicsWorld().unregisterPhysicsConnector(physicsConnector);
			muelle.iniciaDescarga(yRef);
		}
		scene.setScore(score);
	}
	
	
	/**
	 * Aumenta el multplicador
	 */
	public void aumentaMultiplicdor() {
		multiplicador++;
	}
	
	
	/**
	 * Devuelve el score actual del heroe
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
	
	/**
	 * Registra el bloque que el heroe ha de observar
	 * @param b  El bloque ha observar
	 */
	public void registerBloque(Bloque b) {
		bloque = b;
	}
	
	
	
	
	//Interface para avisar a quien quiera de que ha de iniciar movimiento o detenerlo
	public interface OnStateChanged {
		public abstract void iniciaMovimiento();
		public abstract void finalizaMovimiento();
	}
	
	
	
	/**
	 * Añade un oyente al contenedor
	 */
	public void addListener(OnStateChanged onStateChanged) {
		oyentes.add(onStateChanged);
	}
	
	
	/**
	 * Elimina un oyente del contenedor
	 */
	public void removeListener(OnStateChanged onStateChanged) {
		oyentes.remove(onStateChanged);
	}
	
	//---------------------------------------------------
	//SUPERCLASS METHODS
	//---------------------------------------------------
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		super.onManagedUpdate(pSecondsElapsed);
		
		
		//Si estoy cargando aumento valor del salto
		if (estado == ESTADO.CARGANDO) {
			if (muelle.isCargando()) salto+=pSecondsElapsed*CONSTANTE_CARGA;
		}
		
		//Si estoy saltando actualizo velocidad y compruebo si muere o pasa de nivel
		if (estado == ESTADO.SALTANDO) {
			velocidad+=pSecondsElapsed*CONSTANTE_CARGA;  //Disminuyo la velocidad 
			this.body.setLinearVelocity(0, velocidad);
			//Muevo los bloques y le envio el objeto para que actualice el bloque que ha de observar y compruebe si lo ha pasado sin tocar
			bloques.update(this);
		}
	}
	
	
	
	
	//----------------------------------------------------
	//	PRIVATE METHODS
	//----------------------------------------------------

	//Crea el listener para el contacto entre objetos. Asignar al objeto que maneja el motor de fisica
	private ContactListener createContactListener() {
		ContactListener contactListener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				
				if (bloque == null) return;  //Si aun no hay bloque me salgo
				
				//Cuando se llame a este metodo sera que se ha producido un contacnto entre un bloque y el heroe
				//Compruebo si el contacto ha sido en la parte horizontal o vertical
				float yHero = Hero.this.getY() + Hero.this.getHeight();
            	float yBloque = Hero.this.bloque.getY();
            	
            	if (yHero > yBloque) {  //El contacto se ha producido en la parte vwertical
            		Hero.this.iniciaMuerte();
            	}
            	
            	else {  //El contacto se ha producido en la parte horizontal
            		Hero.this.pasaNivel(yBloque);
            		Hero.this.bloque.setSuperado(true);  //Anoto el bloque como superado
            		Hero.this.scene.getPhysicsWorld().setGravity(new Vector2(0, 0));
            	}
            	
			}

			@Override
			public void endContact(Contact contact) {
				Hero.this.scene.getPhysicsWorld().setGravity(new Vector2(0, SensorManager.GRAVITY_EARTH));
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
               
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
               
			}
		};
		return contactListener;
	}
}
