package com.maetrik.jumpingball.scenes;

import java.util.HashMap;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.SceneManager;
import com.maetrik.jumpingball.SceneManager.SceneType;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.objetos.ContenedorBloques;
import com.maetrik.jumpingball.objetos.ContenedorNubes;
import com.maetrik.jumpingball.objetos.Hero;
import com.maetrik.jumpingball.objetos.Mar;
import com.maetrik.jumpingball.objetos.Mountains;
import com.maetrik.jumpingball.objetos.Number;
import com.maetrik.jumpingball.objetos.Pajaro;
import com.maetrik.jumpingball.objetos.Pajaro2;
import com.maetrik.jumpingball.objetos.Suelo;



public class GameSceneBasic extends BaseScene implements IOnSceneTouchListener  {
	
	//-----------------------------------
	//CONSTANTS
	//-----------------------------------
	
	//-----------------------------------
	//VARIABLES
	//-----------------------------------	
	
	private Hero heroe;
	private ContenedorBloques bloques;
	private ContenedorNubes nubes;
	private Mountains mountains;
	private Suelo suelo;
	private Pajaro pajaro;
	private Pajaro2 pajaro2;
	private Mar mar;
	
	private boolean comienza;
	
	private Number scoreText;
	private boolean nuevoRecord;
	private boolean finPartida;
	
	private FinalMenu menuFinal;
	private InicialMenu menuInicial;
	
	public enum CAPAS {
		CAPA_MOUNTAINS,
		CAPA_NUBES,
		CAPA_SUELO_ARBOLES,
		CAPA_MAR_1,
		CAPA_BLOQUES_HERO,
		CAPA_MAR_2,
		CAPA_MAR_3,
		CAPA_PAJARO,
		CAPA_MENUS
	};
	private HashMap<CAPAS, Entity> capas;
	
	//Objeto para controlar el motor de fisicas
	private PhysicsWorld physicsWorld;
	
	
	//Estado
	private enum ESTADO {
		MENU_INICIAL,
		MENU_FINAL,
		JUEGO
	};
	private static ESTADO estado;
	
	
	//-----------------------------------
	//SUPERCLASS METHODS
	//-----------------------------------	
	@Override
	public void createScene() {
		initPhysics();
		initMusic();
		initializeVariabes();
		createBackground();
		createObjects();
	}

	@Override
	public void onBackKeyPressed() {
		resourcesManager.sonidoBoton.stop();
		resourcesManager.sonidoCargar.stop();
		resourcesManager.sonidoSaltar.stop();
		resourcesManager.sonidoCaer.stop();
		resourcesManager.musica_mar.pause();
		resourcesManager.musica_mar.seekTo(0);
		resourcesManager.musica_pajaro.pause();
		resourcesManager.musica_pajaro.seekTo(0);
		SceneManager.getInstance().gameScene_to_gameScene(true);
	}

	@Override 
	public void disposeScene() {
		heroe.dispose();
		bloques.dispose();
		nubes.dispose();
		mar.dispose();
		mountains.dispose();
		pajaro.dispose();
		pajaro2.dispose();
		suelo.dispose();
		heroe.dispose();
	}
	
	
	
	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public static void setMenu(boolean menu) {
		if (menu) {
			estado = ESTADO.MENU_INICIAL;
		}
		else estado = ESTADO.JUEGO;
	}
	
	
	public void iniciar() {
		estado = ESTADO.JUEGO;
		menuInicial.setVisible(false);
		scoreText.setNumber(0, Constants.ANCHO_PANTALLA/2, Constants.FIRST_LINE + Constants.Y_SCORE);
		scoreText.setVisible(true);
	}
	

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if (!finPartida) {  //Si la partida no ha finalizado actualizo
			
			
			//Compruebo si el heroe ha muerto
			if (heroe.estaMuerto()) {
				//Almaceno record por si ha sido actualizado
				Utils.saveRecord();
				
				//Indico que no se necesita musica
				Constants.MUSIC=false;			
				
				scoreText.dispose();
				scoreText = null;
				
				finPartida = true;  //La partida ha finalizado
				estado = ESTADO.MENU_FINAL;
				
				//Saco el menu final
				FinalMenu.setRecordAndBest(heroe.getScore(), (int)Constants.RECORD);
				menuFinal.setVisible(true);
				
				
				//Oculto el boton
				//MoveXModifier move1 = new MoveXModifier(0.3f, botonCarga.getX(), camera.getWidth());
				//botonCarga.registerEntityModifier(move1);
				//botonCargaDinamico.registerEntityModifier(move1);
				
			}
			
			else {  //Actualizo todos los objetos
				mountains.update(pSecondsElapsed);
				suelo.update(pSecondsElapsed);
				mar.update(pSecondsElapsed);
			}
			
		}
		
		
		//Las nubes se actualizan siempre
		nubes.update(pSecondsElapsed);
		
	}
	
	
	public void setScore(int score) {
		scoreText.setNumber(score, Constants.ANCHO_PANTALLA/2, Constants.FIRST_LINE + Constants.Y_SCORE);
	}
	
	
	
	public PhysicsWorld getPhysicsWorld() {
		return physicsWorld;
	}
	
	
	/**
	 * Devuelve la capa solicitada
	 */
	public Entity getLayer(CAPAS capa) {
		return capas.get(capa);
	}
	
	
	//-----------------------------------
	//INTERFACE METHODS
	//-----------------------------------	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (estado == ESTADO.JUEGO) {
			if (pSceneTouchEvent.isActionDown() && comienza == true ) {
				//resourcesManager.sonidoCargar.play();
				heroe.iniciarCarga();
				return true;
			}
			if (pSceneTouchEvent.isActionUp() && comienza == true ) {
				//Genero un salto si estoy en una posicion que pueda saltar
				resourcesManager.sonidoCargar.stop();
				resourcesManager.sonidoSaltar.play();
				heroe.finalizaCarga();
				return true;
			}
		}
		return false;
	};

	
	//-----------------------------------
	//PRIVATE CLASS METHODS
	//-----------------------------------	
	
	private void initPhysics() {
	    physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
	    this.registerUpdateHandler(physicsWorld);
	}
	
	
	private void initMusic() {
		resourcesManager.musica_mar.play();
		resourcesManager.musica_pajaro.play();
	}
	
	private void initializeVariabes() {
		comienza = true;
		nuevoRecord = false;
		finPartida = false;
		Constants.MUSIC = true;
		this.setOnSceneTouchListener(this);
		capas = new HashMap<GameSceneBasic.CAPAS, Entity>();
	}

	
	private void createBackground() {
		
		//Posicion camara
		camera.setCenter(camera.getWidth()/2, camera.getHeight()/2);
		
		//Inicio las capas
		Entity tmp = new Entity();
		capas.put(CAPAS.CAPA_MOUNTAINS, tmp);
		this.attachChild(tmp);
		
		tmp = new Entity();
		capas.put(CAPAS.CAPA_NUBES, tmp);
		this.attachChild(tmp);
		
		tmp = new Entity();
		capas.put(CAPAS.CAPA_SUELO_ARBOLES, tmp);
		this.attachChild(tmp);
		
		tmp = new Entity();
		capas.put(CAPAS.CAPA_MAR_1, tmp);
		this.attachChild(tmp);
		
		tmp = new Entity();
		capas.put(CAPAS.CAPA_BLOQUES_HERO, tmp);
		this.attachChild(tmp);
		
		tmp = new Entity();
		capas.put(CAPAS.CAPA_MAR_2, tmp);
		this.attachChild(tmp);
		
		tmp = new Entity();
		capas.put(CAPAS.CAPA_MAR_3, tmp);
		this.attachChild(tmp);
		
		tmp = new Entity();
		capas.put(CAPAS.CAPA_PAJARO, tmp);
		this.attachChild(tmp);
		
		tmp = new Entity();
		capas.put(CAPAS.CAPA_MENUS, tmp);
		this.attachChild(tmp);
		
		 //Cargo el fondo
		setBackground(new Background(Constants.FONDO_R, Constants.FONDO_G, Constants.FONDO_B));
		
	}

	
	private void createObjects() {
		//Genero el contenedor de bloques
		bloques = new ContenedorBloques(this);
		//Creo el objeto que manejara y representara al saltador
		heroe = new Hero(this,bloques);
		//Inicio el contenedor de nubes
		nubes = new ContenedorNubes(this);
		//Inicio las montañas
		mountains = new Mountains(this,heroe);
		//Inicio el suelo
		suelo = new Suelo(this,heroe);
		//Inicio el mar
		mar = new Mar(this,heroe);
		//Inicio los pajaros
		pajaro = new Pajaro(this, bloques, heroe);
		pajaro2 = new Pajaro2(this);
		
		//Creo el menu final
		menuFinal = new FinalMenu(this);
		
		//Creo el menu inicial
		menuInicial = new InicialMenu(this);
		if (estado == ESTADO.MENU_INICIAL) menuInicial.setVisible(true);
		if (estado==ESTADO.JUEGO) menuInicial.setVisible(false);
		
		//Numero para el score
		scoreText = new Number(this);
		if(estado == ESTADO.JUEGO) {
			scoreText.setNumber(0, Constants.ANCHO_PANTALLA/2, Constants.FIRST_LINE + Constants.Y_SCORE);
			scoreText.setVisible(true);
		}
	}
	
	
	
	
	
	
}
