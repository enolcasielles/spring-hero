package com.maetrik.jumpingball.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.R;
import com.maetrik.jumpingball.SceneManager;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.SceneManager.SceneType;
import com.maetrik.jumpingball.objetos.Bola;
import com.maetrik.jumpingball.objetos.ContenedorBloques;



public class GameSceneBasic extends BaseScene implements IOnSceneTouchListener  {
	
	//-----------------------------------
	//CONSTANTS
	//-----------------------------------
	
	//-----------------------------------
	//VARIABLES
	//-----------------------------------	
	
	private Bola saltador;
	private boolean puedeSaltar;
	private ContenedorBloques bloques;
	
	private boolean comienza;
	
	private Text texto;
	private Text scoreText;
	private Sprite botonCarga, botonCargaDinamico;
	private int score;
	private boolean nuevoRecord;
	private boolean finPartida;

	
	FinalMenuDialog fms;
	
	
	
	//-----------------------------------
	//SUPERCLASS METHODS
	//-----------------------------------	
	@Override
	public void createScene() {
		initializeVariabes();
		createPhysics();
		createBackground();
		createObjects();
	}

	@Override
	public void onBackKeyPressed() {
		resourcesManager.sonidoBoton.stop();
		resourcesManager.sonidoCargar.stop();
		resourcesManager.sonidoSaltar.stop();
		resourcesManager.musica.pause();
		resourcesManager.musica.seekTo(0);
		SceneManager.getInstance().loadingScene(SceneType.SCENE_GAME, SceneType.SCENE_MENU);
	}

	@Override 
	public void disposeScene() {
		if(fms != null) fms.destructor();
		saltador.dispose();
		bloques.dispose();
	}
	
	
	
	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if (!finPartida) {  //Si la partida no ha finalizado actualizo
			
			//Cargo el salto, el sabra si ha de aumentar o no
			saltador.cargar();
			
			//Compruebo si ha perdido
			if (saltador.estaMuerto()) {
				saltador.finalizarCarga();  //Por si estaba cargando al finalizar
				//Has muerto
				
				//Almaceno record por si ha sido actualizado
				Utils.saveRecord();
				
				//Indico que no se necesita musica
				Constants.MUSIC=false;
				
				resourcesManager.musica.pause();
				resourcesManager.musica.seekTo(0);
				resourcesManager.sonidoSaltar.stop();
				resourcesManager.sonidoCargar.stop();
				scoreText.detachSelf();
				scoreText.dispose();
				saltador.setVisible(false);
				fms = new FinalMenuDialog(this,score,nuevoRecord);
				fms.attachToScene(this);
				fms.show();
				finPartida = true;  //La partida ha finalizado
				
				//Oculto el boton
				MoveYModifier move1 = new MoveYModifier(0.3f, botonCarga.getX(), camera.getWidth());
				botonCarga.registerEntityModifier(move1);
				botonCargaDinamico.registerEntityModifier(move1);
				
			}
			
			
			if (comienza == true) {  //Ya ha comenzado
				bloques.update(pSecondsElapsed,score);
				if (bloques.nuevoSuperado(saltador.getPosX())) { //Si estoy en manual compruebo si hay alguno superado
					score++;
					scoreText.setText(""+score);
					if (score > Constants.RECORD) {
						Constants.RECORD = score;
						nuevoRecord = true;
					}
					//Compruebo logros
					if (score==10) this.resourcesManager.actividad.newLogro(this.activity.getString(R.string.achievement_score10));
					if (score==25) this.resourcesManager.actividad.newLogro(this.activity.getString(R.string.achievement_score25));
					if (score==50) this.resourcesManager.actividad.newLogro(this.activity.getString(R.string.achievement_score50));
					if (score==100) this.resourcesManager.actividad.newLogro(this.activity.getString(R.string.achievement_score100));
					if (score==250) this.resourcesManager.actividad.newLogro(this.activity.getString(R.string.achievement_score250));
				}
			}
			
		}
		
	}
	
	
	//-----------------------------------
	//INTERFACE METHODS
	//-----------------------------------	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (physicsWorld != null && !finPartida) {
			if (pSceneTouchEvent.isActionUp() && comienza == false) {  //Solo se dara el primer tap
				resourcesManager.musica.play();
				comienza = true;  //Comienza la partida 
				texto.detachSelf();
				texto.dispose();
				texto = null;
				return true;
			}
		}
		return false;
	};

	
	//-----------------------------------
	//PRIVATE CLASS METHODS
	//-----------------------------------	
	private void initializeVariabes() {
		score = -1;	
		comienza = false;
		puedeSaltar = true;
		nuevoRecord = false;
		finPartida = false;
		Constants.MUSIC = true;
		this.setOnSceneTouchListener(this);
	}
	
	private void createPhysics() {
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, SensorManager.GRAVITY_EARTH), false); 
	    registerUpdateHandler(physicsWorld);
	    physicsWorld.setContactListener(contactListener());
	}
	
	private void createBackground() {
		
		//Posicion camara
		camera.setCenter(camera.getWidth()/2, camera.getHeight()/2);
		
		//Cargo el fondo
		setBackground(new Background(0.392f, 0.584f, 0.929f));
		
		texto = new Text(0, 0, resourcesManager.fuenteGame, "Tap to start!", new TextOptions(HorizontalAlign.LEFT), vbom); 
	    texto.setPosition(camera.getWidth()/2 - texto.getWidth() / 2, camera.getHeight()/3);
	    texto.setScale(1.2f);
	    this.attachChild(texto);
	    
	    scoreText = new Text(0, 0, resourcesManager.fuenteGame, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom); 
	    scoreText.setText("0");
	    scoreText.setPosition(camera.getWidth()/2, 5);
	    scoreText.setScale(1.5f);
	    this.attachChild(scoreText);
	    
	    //Inicio el boton de carga
	    botonCarga = new Sprite(0, 0, resourcesManager.texturaBotonPushAndJump, vbom) {
	    	@Override
	    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
	    			float pTouchAreaLocalX, float pTouchAreaLocalY) {
	    		// TODO Auto-generated method stub
	    		return botonTocado(pSceneTouchEvent);
	    	}
	    };
	    botonCargaDinamico = new Sprite(0, 0, resourcesManager.texturaBotonPushedAndJump, vbom);
	    botonCarga.setPosition(camera.getWidth()-botonCarga.getWidth()-20, camera.getHeight() - botonCarga.getHeight() - 20);
	    botonCargaDinamico.setPosition(botonCarga.getX(), botonCarga.getY());
	    botonCarga.setZIndex(2);
	    botonCargaDinamico.setZIndex(1);
	    this.attachChild(botonCarga);
	    this.attachChild(botonCargaDinamico);
	    this.registerTouchArea(botonCarga);
	    this.sortChildren();
	    
	    //Boton dinamico sin transparencia
	    botonCargaDinamico.setAlpha(0.0f);
		
	}
	
	
	private void createObjects() {
		//Creo el objeto que manejara y representara al saltador
		saltador = new Bola(this,botonCargaDinamico);
		//Inicio el contenedor de objetos
		bloques = new ContenedorBloques(this);
	}
	
	
	
	private ContactListener contactListener()
	{
	    ContactListener contactListener = new ContactListener()
	    {
	        public void beginContact(Contact contact)
	        {
	        	puedeSaltar = true;
	            final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();

	            if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	                if (x2.getBody().getUserData().equals("saltador"))
	                {
	                    puedeSaltar = true;
	                }
	            }
	        }

	        public void endContact(Contact contact)
	        {
	        	puedeSaltar = false;
	            final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();

	            if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	                if (x2.getBody().getUserData().equals("saltador"))
	                {
	                    puedeSaltar = false;
	                }
	            }
	        }

	        public void preSolve(Contact contact, Manifold oldManifold)
	        {

	        }

	        public void postSolve(Contact contact, ContactImpulse impulse)
	        {

	        }
	    };
	    return contactListener;
	}
	
	
	
	private boolean botonTocado(final TouchEvent pSceneTouchEvent) {
		if (physicsWorld != null && !finPartida) {
			if (pSceneTouchEvent.isActionDown() && comienza == true ) {
				resourcesManager.sonidoCargar.play();
				saltador.comienzaCarga();
				return true;
			}
			if (pSceneTouchEvent.isActionUp() && comienza == true ) {
				//Genero un salto si estoy en una posicion que pueda saltar
				resourcesManager.sonidoCargar.stop();
				resourcesManager.sonidoSaltar.play();
				saltador.finalizarCarga();
				if (puedeSaltar) {
					saltador.saltar();
				}
				return true;
			}
		}
		return false;
	}

	
}
