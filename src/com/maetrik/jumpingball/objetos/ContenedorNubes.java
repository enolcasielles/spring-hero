package com.maetrik.jumpingball.objetos;

import java.util.ArrayList;

import android.R.integer;

import com.google.android.gms.games.Game;
import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.ResourcesManager;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.scenes.BaseScene;
import com.maetrik.jumpingball.scenes.GameSceneBasic;

public class ContenedorNubes {
	
	private ArrayList<Nube> nubes;
	private NubesPool recolector;
	private GameSceneBasic scene;
	private float anchoMax, anchoMin, incrAltoMax;

	
	
	//-----------------------------------------------------------
	//CONSTRUCTORS
	//-----------------------------------------------------------
	/*
	 * Genera un contenedor de bloques para el modo de juego basico
	 * */
	public ContenedorNubes(GameSceneBasic scene) {
		this.scene = scene;
		nubes = new ArrayList<Nube>();
		//Genero el primer bloque
		nubes.add(new Nube(scene, Constants.ANCHO_PANTALLA,
				Constants.FIRST_LINE + Utils.aleatorioEntre(Constants.MIN_Y_NUBE, Constants.MAX_Y_NUBE),
				scene.resourcesManager.texturasNube[(int)(Math.random()*Constants.NUM_NUBES)]));
		//Inicio el recolector de bloques usados
		recolector = new NubesPool();
	}
	
	
	
	
	public void update(float pSecondsElapsed) {
			//Compruebo si ha salido el bloque inicial
			if (nubes.get(0).getSprite().getX() + nubes.get(0).getSprite().getWidth() < 0) { //Lo elimino
				recolector.addBloque(nubes.get(0)); //Añado el bloque al recolector
				nubes.remove(0); //Lo elimino del array
			}
			//Muevo todos
			for (Nube b : nubes) {
				b.mover(pSecondsElapsed);
			}
			//Compruebo se se ha de sacar uno nuevo
			Nube b = nubes.get(nubes.size()-1); //Recupero el ultimo bloque
			if (b.getSprite().getX() < Constants.ANCHO_PANTALLA - (b.getSprite().getWidth()*b.getSprite().getScaleX() + b.getSeparacion())) { //Añado uno nuevo
				Nube temp = recolector.getBloque();
				if (temp==null) { //Si el recolector esta vacio genero uno nuevo
					nubes.add(new Nube(scene, scene.camera.getWidth(), 
						Constants.FIRST_LINE + Utils.aleatorioEntre(Constants.MIN_Y_NUBE, Constants.MAX_Y_NUBE),
						scene.resourcesManager.texturasNube[(int)(Math.random()*Constants.NUM_NUBES)]));
				}
				else { //Sino uso el del recolector, ubicandolo en su posicion
				  temp.redefinir();
				  nubes.add(temp);
				}

			}
	}
	
	
	
	
	public void dispose() {
		for (int i=0 ; i<nubes.size() ; i++) {
			nubes.get(i).dispose();
		}
		nubes.clear();
		recolector.dispose();
	}
	

}
