package com.maetrik.jumpingball.objetos;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;

import android.R.integer;
import android.transition.Scene;

import com.maetrik.jumpingball.scenes.BaseScene;
import com.maetrik.jumpingball.scenes.FinalMenu;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;

public class Number {
	
	private Sprite[] numerosSprites;
	private boolean[] numerosUsados;
	private ArrayList<Sprite> spritesVisibles;
	private ArrayList<Sprite> spritesRepetidos;
	private GameSceneBasic scene;
	
	public Number(GameSceneBasic scene) {
		this.scene = scene;
		numerosSprites = new Sprite[10];
		numerosUsados = new boolean[10];
		for (int i=0; i<10 ; i++) {
			numerosSprites[i] = new Sprite(0, 0, scene.resourcesManager.texturaNumeros[i], scene.vbom);
		}
		spritesVisibles = new ArrayList<Sprite>();
		spritesRepetidos = new ArrayList<Sprite>();
	}
	
	
	
	public void setNumber(int number, float x, float y) {
		//Opero el numero para obtener sus digitos
		ArrayList<Integer> digitos = new ArrayList<Integer>();
		while(true) {
			int tmp = number / 10;
			digitos.add(new Integer(number - tmp*10));
			if (tmp == 0) break;
			number = tmp;
		}
		
		//Calculo el ancho total
		float ancho = digitos.size()*numerosSprites[0].getWidth();
		float xIni = x - ancho/2;
		float alto = numerosSprites[0].getHeight();
		float yIni = y - alto/2;
		
		//SAco los anteriores Sprites de la escena
		for(Sprite sprite : spritesVisibles) {
			sprite.detachSelf();
		}
		spritesVisibles.clear();  //Elimino los anteriores del array
		
		//Elimino los repetidos
		for (Sprite sprite : spritesRepetidos) {
			sprite.detachSelf();
			sprite.dispose();
		}
		spritesRepetidos.clear();
		for (int i=0 ; i<10 ; i++) {
			numerosUsados[i] = false;
		}
		
		//Finalmente posiciono los nuevos
		int contador = 0;
		for (int i=digitos.size()-1 ; i>=0 ; i--) {
			int num = digitos.get(i);
			Sprite tmp;
			if (numerosUsados[num] == false)  {
				tmp = numerosSprites[num];
				numerosUsados[num] = true;  //MArco este como usado 
			}
			else {
				tmp = new Sprite(0, 0, scene.resourcesManager.texturaNumeros[num], scene.vbom);
				spritesRepetidos.add(tmp);
			}
			spritesVisibles.add(tmp);
			tmp.setPosition(xIni+contador*tmp.getWidth(), yIni);
			contador++;
			scene.getLayer(CAPAS.CAPA_MENUS).attachChild(tmp);
		}
	}
	
	
	public void setVisible(boolean visibitlity) {
		for(int i=0 ; i<10 ; i++) {
			numerosSprites[i].setVisible(visibitlity);
		}
	}
	
	public void dispose() {
		for(int i=0 ; i<10 ; i++) {
			numerosSprites[i].detachSelf();
			numerosSprites[i].dispose();
		}
		spritesVisibles.clear();
		for (Sprite sprite : spritesRepetidos) {
			sprite.detachSelf();
			sprite.dispose();
		}
		spritesRepetidos.clear();
	}

}
