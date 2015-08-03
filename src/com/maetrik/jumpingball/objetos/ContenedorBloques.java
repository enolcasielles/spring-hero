package com.maetrik.jumpingball.objetos;

import java.util.ArrayList;

import android.R.integer;
import android.util.Log;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.scenes.BaseScene;
import com.maetrik.jumpingball.scenes.GameSceneBasic;

public class ContenedorBloques {
	
	private ArrayList<Bloque> bloques;
	private GameSceneBasic scene;
	private float anchoMax, anchoMin, incrAltoMax;
	
	
	//-----------------------------------------------------------
	//CONSTRUCTORS
	//-----------------------------------------------------------
	/*
	 * Genera un contenedor de bloques para el modo de juego basico
	 * */
	public ContenedorBloques(GameSceneBasic scene) {
		this.scene = scene;
		bloques = new ArrayList<Bloque>();
		
		//Genero primer bloque
		Bloque bloque =  new Bloque(scene, 100, Constants.ANCHO_BLOQUES,Utils.aleatorioEntre(Constants.MIN_ALTO_BLOQUES, Constants.MAX_ALTO_BLOQUES) );
		bloque.setSuperado(true);
		bloques.add(bloque);
		
		//Genero los otros 4 bloques bloques
		for (int i=0 ; i<4 ; i++) {
			Bloque b = new Bloque(scene, bloques.get(i).getX() + bloques.get(i).getSeparacion(), Constants.ANCHO_BLOQUES,Utils.aleatorioEntre(Constants.MIN_ALTO_BLOQUES, Constants.MAX_ALTO_BLOQUES));
			bloques.add(b);
		}
	}
	
	
	
	/**
	 * Actualiza el bloque que el heroe ha de controlar. Redefine el conjunto de bloques cuando el primero se 
	 * sale de la pantalla
	 * @param hero  El heroe con el que realizar las comprobaciones
	 */
	public void update(Hero hero) {
		
		//Compruebo si el primero se ha salido
		if (bloques.get(0).getX() + bloques.get(0).getWidth() < 0) { //Lo elimino
			Bloque bTemp = bloques.get(0);  //Almaceno temporalmente el primer bloque
			bloques.remove(0);  //Lo elimino del array
			bTemp.redefinir(bloques.get(bloques.size()-1));  //Lo redefino a partir del ultimo
			bloques.add(bTemp); //Lo añado de nuevo
		}
		
		//Compruebo que bloque ha de observar el heroe, el primero que este sin superar
		for (int i=0 ; i<bloques.size() ; i++) {
			Bloque bloque = bloques.get(i);
			if (!bloque.isSuperado()) {
				hero.registerBloque(bloque);
				 //Compruebo que no sobrepase el bloque sin tocarlo
				if (bloque.check(hero)) {  //PAsado sin tocar
					 bloque.setSuperado(true);
					 hero.registerBloque(bloques.get(i+1));  //Registro como observador el siguiente bloque
					 hero.aumentaMultiplicdor();
				}
				break;   //Salimos del for
			}
		}
	}
	
	
	/**
	 * Recupera un bloque del contenedor a partir de su indice
	 */
	public Bloque getBloque(int index) {
		return bloques.get(index);
	}
	
	
	/**
	 * Activa el movimiento en todos los bloques
	 */
	public void iniciaMovimiento(float velocidad) {
		for (Bloque bloque : bloques) {
			bloque.iniciaMovimiento(velocidad);
		}
	}
	
	
	/**
	 * Finaliza movimiento en todos los bloques
	 */
	public void finalizaMovimiento() {
		for (Bloque bloque : bloques) {
			bloque.finalizaMovimiento();
		}
	}
	
	
	public void dispose() {
		for (int i=0 ; i<bloques.size() ; i++) {
			bloques.get(i).dispose();
		}
		bloques.clear();
	}
	
	

}
