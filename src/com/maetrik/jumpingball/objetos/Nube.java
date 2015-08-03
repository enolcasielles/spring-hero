package com.maetrik.jumpingball.objetos;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.scenes.BaseScene;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;

public class Nube {

	//---------------------
	//CONSTANTS
	//---------------------
	
	//---------------------
	//VARIABLES
	//---------------------
	private float separacion;
	private Sprite nube;
	private float alpha, velocidad, escala;
	
	
	//---------------------
	//CONSTRUCTORS
	//---------------------
	public Nube(GameSceneBasic scene, float posX, float posY, ITextureRegion textura) {
		
		//Saco un numero entre 0 y 2
		int num = (int)(Math.random()*3);
		if (num==0) {
			alpha = 0.2f;
			velocidad = 40;
			escala = 1.0f;
		}
		if (num==1) {
			alpha = 0.4f;
			velocidad = 60;
			escala = 0.5f;
		}
		if (num==2) {
			alpha = 0.8f;
			velocidad = 80;
			escala = 0.35f;
		}
		
		nube = new Sprite(posX, posY,textura, scene.vbom);	
		this.nube.setAlpha(alpha); 
		this.nube.setScale(escala);
		//Separacion con la siguiente nube
		this.separacion = Utils.aleatorioEntre(Constants.MIN_SEPARATION_NUBE, Constants.MAX_SEPARATION_NUBE);
		this.separacion*=escala*0.5f; 
	
		scene.getLayer(CAPAS.CAPA_NUBES).attachChild(nube);
		
	}
	
	
	public void dispose() {
		nube.detachSelf();
		nube.dispose();
		nube = null;
	}

	
	//----------------------------
	//CLASS LOGIC
	//----------------------------
	public void mover(float segundosPasados) {
		nube.setX(nube.getX() - velocidad * segundosPasados);  //EspacioRecorrido = Velocidad * tiempo
	}
	
	
	
	public void redefinir() {
		//Saco un numero entre 0 y 2
		int num = (int)(Math.random()*3);
		if (num==0) {
			alpha = 0.2f;
			velocidad = 40;
			escala = 1.0f;
		}
		if (num==1) {
			alpha = 0.4f;
			velocidad = 60;
			escala = 0.5f;
		}
		if (num==2) {
			alpha = 0.8f;
			velocidad = 80;
			escala = 0.35f;
		}
		nube.setX(Constants.ANCHO_PANTALLA);
		nube.setY(Constants.FIRST_LINE + Utils.aleatorioEntre(Constants.MIN_Y_NUBE, Constants.MAX_Y_NUBE));
		this.separacion =
		  (float)Math.random() * (Constants.MAX_SEPARATION_NUBE - Constants.MIN_SEPARATION_NUBE) + Constants.MIN_SEPARATION_NUBE; 		
	
		this.nube.setAlpha(alpha); 
		this.nube.setScale(escala);
	
	}
	

	//-----------------------------
	//GETTERS
	//-----------------------------
	public float getSeparacion() {
		return separacion;
	}


	public void setSeparacion(float separacion) {
		this.separacion = separacion;
	}
	
	public Sprite getSprite() {
		return nube;
	}
}
	
	