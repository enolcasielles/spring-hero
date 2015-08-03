package com.maetrik.jumpingball.objetos;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.objetos.Hero.OnStateChanged;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;

public class Pajaro extends AnimatedSprite {

	//CONSTANTES
	private final float TIEMPO_MOVIMIENTO = 1.0f;
	private final int NUM_FRAMES = 4;
	private final float TIEMPO_CAMBIO_FRAME = 0.05f;
	private final boolean IZQUIERDA = true;
	private final boolean DERECHA = false;
	
	//Variables
	private ContenedorBloques bloques;
	private Hero heroe;
	private GameSceneBasic scene;
	private int posicionActual;
	private float tiempoFrame;
	private int frameActual;
	private float tiempoAcumulado;
	private boolean puedeCambiarEstado;
	
	//Posibles estados
	private enum ESTADO {
		REPOSO_HEROE,
		REPOSO_BLOQUE,
		VOLANDO,
		FUERA
	}
	private ESTADO estado;
	
	private enum SENTIDO {
		DERECHA,
		IZQUIERDA
	}
	private SENTIDO sentido;
	

	
	public Pajaro(GameSceneBasic scene, ContenedorBloques bloques, Hero heroe) {
		super(Constants.ANCHO_PANTALLA+10, 0, scene.resourcesManager.texturaPajaro, scene.vbom);
		this.scene = scene;
		this.bloques = bloques;
		this.heroe = heroe;
		estado = ESTADO.FUERA;
		sentido = SENTIDO.DERECHA;
		frameActual = 0;
		tiempoFrame = 0;
		puedeCambiarEstado = false;
		tiempoAcumulado = 0;
		scene.getLayer(CAPAS.CAPA_PAJARO).attachChild(this);
		heroe.registerPajaro(this);
	}

	
	
	public void marchar() {
		setMovement(Pajaro.this.getX(), Constants.ANCHO_PANTALLA+10,
				Pajaro.this.getY(), 
				Utils.aleatorioEntre(Constants.FIRST_LINE, Constants.LAST_LINE-Constants.ALTO_SUELO),
				ESTADO.FUERA);
		estado = ESTADO.VOLANDO;
		cambiaEstado();
	}

	
	
	public void dispose() {
		this.detachSelf();
		this.dispose();
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		super.onManagedUpdate(pSecondsElapsed);
		
		if (!puedeCambiarEstado) {
			tiempoAcumulado += pSecondsElapsed;
			if (tiempoAcumulado >= 3) puedeCambiarEstado = true;
			//Si estoy volando actualizo animacion
			if (estado == ESTADO.VOLANDO) {
				tiempoFrame+=pSecondsElapsed;
				if (tiempoFrame >= TIEMPO_CAMBIO_FRAME) {
					tiempoFrame = 0;
					frameActual++;
					if (frameActual >= NUM_FRAMES) frameActual = 0;
					this.setCurrentTileIndex(frameActual);
				}
			}
			return;
		}
		
		double aleatorio;
		
		//Si esty fuera trato de salir
		if (estado == ESTADO.FUERA) {
			aleatorio = Math.random();
			if (aleatorio < 0.05) {  //Entro en la pantalla
				aleatorio = Math.random();
				if (aleatorio < 0.5) {  //Voy al heroe
					setMovement(Constants.ANCHO_PANTALLA, 
							heroe.getX()+heroe.getWidth()/2-this.getWidth()/2, 
							Utils.aleatorioEntre(Constants.FIRST_LINE, Constants.LAST_LINE-Constants.ALTO_SUELO),
							heroe.getY()-this.getHeight(),ESTADO.REPOSO_HEROE);
					estado = ESTADO.VOLANDO;
					cambiaEstado();
				}
				else {  //Voy al bloque
					Bloque b = bloques.getBloque(1);
					setMovement(Constants.ANCHO_PANTALLA, 
							b.getX()+b.getWidth()/2-this.getWidth()/2, 
							Utils.aleatorioEntre(Constants.FIRST_LINE, Constants.LAST_LINE-Constants.ALTO_SUELO),
							b.getY()-this.getHeight(),ESTADO.REPOSO_BLOQUE);
					estado = ESTADO.VOLANDO;
					cambiaEstado();
				}
			}
		}
		
		//Si estoy volando actualizo animacion
		if (estado == ESTADO.VOLANDO) {
			tiempoFrame+=pSecondsElapsed;
			if (tiempoFrame >= TIEMPO_CAMBIO_FRAME) {
				tiempoFrame = 0;
				frameActual++;
				if (frameActual >= NUM_FRAMES) frameActual = 0;
				this.setCurrentTileIndex(frameActual);
			}
		}
		
		if (estado == ESTADO.REPOSO_BLOQUE) {
			//Aleatoriamente cambio de sentido
			aleatorio = Math.random();
			if (aleatorio < 0.1) cambiaSentido();
			//Aleatoriamente me voy
			aleatorio = Math.random();
			if (aleatorio < 0.05) {  //Me voy 
				aleatorio = Math.random();
				if (aleatorio < 0.5) {  //Voy al heroe
					setMovement(this.getX(), 
							heroe.getX()+heroe.getWidth()/2-this.getWidth()/2, 
							this.getY(),
							heroe.getY()-this.getHeight(),ESTADO.REPOSO_HEROE);
					estado = ESTADO.VOLANDO;
					cambiaEstado();
				}
				else {  //Me voy de la pantalla
					setMovement(this.getX(), 
							Constants.ANCHO_PANTALLA + 10, 
							this.getY(),
							Utils.aleatorioEntre(Constants.FIRST_LINE, Constants.LAST_LINE-Constants.ALTO_SUELO),
							ESTADO.FUERA);
					estado = ESTADO.VOLANDO;
					cambiaEstado();
				}
			}
		}
		
		if (estado == ESTADO.REPOSO_HEROE) {
			this.setCurrentTileIndex(NUM_FRAMES);  //Le asigno la ultima, reposo
			//Aleatoriamente cambio de sentido
			aleatorio = Math.random();
			if (aleatorio < 0.02) this.setFlippedHorizontal(DERECHA);
			if (aleatorio > 0.08) this.setFlippedHorizontal(IZQUIERDA);
			//Aleatoriamente me voy
			aleatorio = Math.random();
			if (aleatorio < 0.05) {  //Me voy 
				aleatorio = Math.random();
				if (aleatorio < 0.5) {  //Voy al bloque
					Bloque b = bloques.getBloque(1);
					setMovement(this.getX(), 
							b.getX()+b.getWidth()/2-this.getWidth()/2, 
							this.getY(),
							b.getY()-this.getHeight(),ESTADO.REPOSO_BLOQUE);
					estado = ESTADO.VOLANDO;
					cambiaEstado();
				}
				else {  //Me voy de la pantalla
					setMovement(this.getX(), 
							Constants.ANCHO_PANTALLA + 10, 
							this.getY(),
							Utils.aleatorioEntre(Constants.FIRST_LINE, Constants.LAST_LINE-Constants.ALTO_SUELO),
							ESTADO.FUERA);
					estado = ESTADO.VOLANDO;
					cambiaEstado();
				}
			}
		}
		
	}



	//Metodo privado para definir movimiento entre dos puntos
	private void setMovement(float xInit, float xFin, float yInit, float yFin, final ESTADO estFinal) {
		if (xFin > xInit) {  //Se muece a la derecha
			this.setFlippedHorizontal(DERECHA);
		}
		else {
			this.setFlippedHorizontal(IZQUIERDA);
		}
		this.registerEntityModifier(new MoveModifier(TIEMPO_MOVIMIENTO, xInit, xFin, yInit, yFin){
			@Override
			protected void onModifierFinished(IEntity pItem) {
				if (estFinal == ESTADO.REPOSO_BLOQUE || estFinal == ESTADO.REPOSO_HEROE) {
					Pajaro.this.setCurrentTileIndex(NUM_FRAMES);
				}
				Pajaro.this.estado = estFinal;
			}
		});
	
	}
	
	
	//Metodo que cambia el sentido del pajaro cuando esta en reposo
	private void cambiaSentido() {
		if (estado == ESTADO.REPOSO_BLOQUE || estado == ESTADO.REPOSO_HEROE) {
			if (sentido == SENTIDO.DERECHA){
				this.setFlippedHorizontal(IZQUIERDA);
				sentido = SENTIDO.IZQUIERDA;
			}
			else {
				this.setFlippedHorizontal(DERECHA);
				sentido = SENTIDO.DERECHA;
			}
			cambiaEstado();
		}
	}
	
	
	private void cambiaEstado() {
		tiempoAcumulado = 0;
		puedeCambiarEstado = false;
	}
	
}
