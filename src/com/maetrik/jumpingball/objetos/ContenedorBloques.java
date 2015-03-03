package com.maetrik.jumpingball.objetos;

import java.util.ArrayList;

import android.R.integer;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.scenes.BaseScene;
import com.maetrik.jumpingball.scenes.GameSceneBasic;

public class ContenedorBloques {
	
	private ArrayList<Bloque> bloques;
	private BloquesPool recolector;
	private GameSceneBasic scene;
	private float anchoMax, anchoMin, incrAltoMax;
	
	//Modos de juego, comportamiento del contenedor
	public enum MODO_JUEGO {
		BASICO,
		AVANZADO
	};
	
	private MODO_JUEGO modoJuego;
	
	
	//-----------------------------------------------------------
	//CONSTRUCTORS
	//-----------------------------------------------------------
	/*
	 * Genera un contenedor de bloques para el modo de juego basico
	 * */
	public ContenedorBloques(GameSceneBasic scene) {
		this.scene = scene;
		bloques = new ArrayList<Bloque>();
		//Genero el primer bloque
		bloques.add( new Bloque(scene, 0.0f, Constants.ANCHO_PANTALLA,Constants.ALTO_PANTALLA/2 )); 
		//Almaceno el modo de juego
		modoJuego = MODO_JUEGO.BASICO;
		//Inicio el recolector de bloques usados
		recolector = new BloquesPool();
	}
	
	/*
	 * Genera un contenedor de bloques para el modo de juego especificado en el segundo parametro
	 */
	public ContenedorBloques(GameSceneBasic scene, MODO_JUEGO mod) {
		this.scene = scene;
		bloques = new ArrayList<Bloque>();
		//Genero el primer bloque
		bloques.add( new Bloque(scene, 0.0f, Constants.ANCHO_PANTALLA,Constants.ALTO_PANTALLA/2 )); 
		anchoMax = Constants.MAX_ANCHO_INIT;
		anchoMin = Constants.MAX_ANCHO_INIT;
		incrAltoMax = 0;
		//Almaceno modo de juego
		modoJuego = mod;
		//Inicio el recolector de bloques usados
		recolector = new BloquesPool();
		//Inicio el recolector con 10 bloques 
		for (int i=0 ; i<10 ; i++) {
			recolector.addBloque(new Bloque(scene,scene.camera.getWidth(),Constants.ANCHO,Constants.ALTO_PANTALLA/2));
		}
	}
	
	
	
	public void update(float pSecondsElapsed, float score) {
			//Compruebo si ha salido el bloque inicial
			if (bloques.get(0).getPosX() + bloques.get(0).getAncho() < 0) { //Lo elimino
				recolector.addBloque(bloques.get(0)); //Añado el bloque al recolector
				bloques.remove(0); //Lo elimino del array
			}
			//Muevo todos
			for (Bloque b : bloques) {
				b.mover(pSecondsElapsed);
			}
			//Compruebo se se ha de sacar uno nuevo
			Bloque b = bloques.get(bloques.size()-1); //Recupero el ultimo bloque
			if (b.getPosX() < Constants.ANCHO_PANTALLA - (b.getAncho() + b.getSeparacion())) { //Añado uno nuevo
				if (modoJuego == MODO_JUEGO.AVANZADO) {
					float ancho = ((float)Math.random() * (anchoMax -  anchoMin)) +  anchoMin; 
					float alto = (float)Math.random() * incrAltoMax - incrAltoMax/2;
					alto = b.getPosY() + alto;
					if (alto < Constants.ALTO_PANTALLA/4 ) alto = Constants.ALTO_PANTALLA/4;
					if (alto > Constants.ALTO_PANTALLA*3/4) alto = Constants.ALTO_PANTALLA*3/4;
					bloques.add(new Bloque(scene,scene.camera.getWidth(), ancho,alto));
				}
				if (modoJuego == MODO_JUEGO.BASICO) {
					Bloque temp = recolector.getBloque();
					if (temp==null) { //Si el recolector esta vacio genero uno nuevo
					  bloques.add(new Bloque(scene,scene.camera.getWidth(),Constants.ANCHO,Constants.ALTO_PANTALLA/2));
					}
					else { //Sino uso el del recolector, ubicandolo en su posicion
					  temp.redefinir();
					  bloques.add(temp);
					}
				}

			}
			
			//Ajusto la dificultad
			if (modoJuego == MODO_JUEGO.AVANZADO) updateDificultad(score);
	}
	
	
	public boolean nuevoSuperado(float x) {
		//Compruebo si hay algun bloque nuevo superado
		for (int i = 0; i< bloques.size(); i++) {
			Bloque b = bloques.get(i);
			if (b.getPosX() < x && b.isSuperado() == false) { 
				b.setSuperado(true);
				return true;
			}
		}
		return false;
	}
	
	
	public void dispose() {
		for (int i=0 ; i<bloques.size() ; i++) {
			bloques.get(i).dispose();
		}
		bloques.clear();
		recolector.dispose();
	}
	
	
	private void updateDificultad(float score) {
		if (score < 10) { //Si esta entre 0 y 9
			
		}
		else if (score < 20 ) {  //Si esta entre 10 y 19
			if (anchoMax == Constants.MAX_ANCHO_INIT) {
				anchoMax /= 2;
				anchoMin /= 2;
			}
		}
		else if (score < 50) {  //Si esta entre 20 y 49
		  if (anchoMin > Constants.MIN_ANCHO_INIT) {
				anchoMax-=0.1;
				anchoMin-=0.1;
		  }
		}
		else if (score < 80) {  //Si esta entre 50 y 79
			if (incrAltoMax < Constants.INCR_ALTO_MAX_INIT) {
				incrAltoMax++;	
			}
		}
		else {  //Si es 80 o mayor
			
		}
	}

}
