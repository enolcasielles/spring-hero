package com.maetrik.jumpingball.objetos;

import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.sprite.AnimatedSprite;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.Utils;
import com.maetrik.jumpingball.scenes.GameSceneBasic;
import com.maetrik.jumpingball.scenes.GameSceneBasic.CAPAS;

public class Pajaro2 extends AnimatedSprite {
	
	private float tiempo, tiempoSacar;
	
	public Pajaro2(GameSceneBasic scene) {
		super(-50, Constants.MAX_Y_NUBE,scene.resourcesManager.texturaPajaro,scene.vbom);
		scene.getLayer(CAPAS.CAPA_PAJARO).attachChild(this);
		tiempo = 0;
		tiempoSacar = Utils.aleatorioEntre(7, 22);
		long framesDuration[] = {50,50,50,50,0};
		this.animate(framesDuration);
	}
	
	
	public void dispose() {
		this.detachSelf();
		this.dispose();
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		super.onManagedUpdate(pSecondsElapsed);
		
		if (tiempo >= tiempoSacar) {  //Muevo el pajaro
			this.setY(Utils.aleatorioEntre(Constants.MIN_Y_NUBE, Constants.MAX_Y_NUBE) + Constants.FIRST_LINE);
			if (Math.random() < 0.5) {
				this.setFlippedHorizontal(false);
				this.registerEntityModifier(new MoveXModifier(2.0f,-50,Constants.ANCHO_PANTALLA+50));
			}
			else {
				this.setFlippedHorizontal(true);
				this.registerEntityModifier(new MoveXModifier(2.0f,Constants.ANCHO_PANTALLA+50,-50));
			}
			tiempo = 0;
			tiempoSacar = Utils.aleatorioEntre(7, 22);
		}
		
		tiempo += pSecondsElapsed;
		
	}
	

}
