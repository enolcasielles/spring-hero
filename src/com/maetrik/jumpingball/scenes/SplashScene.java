package com.maetrik.jumpingball.scenes;

import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;

import android.R.integer;

import com.maetrik.jumpingball.Constants;
import com.maetrik.jumpingball.SceneManager.SceneType;

public class SplashScene extends BaseScene {
	
	private Sprite splash, splash2;

	@Override
	public void createScene() {
		
		float y1 = Constants.FIRST_LINE;
		float y2 = Constants.LAST_LINE;
		
		//splash = new Sprite(Constants.ANCHO_PANTALLA/2, Constants.FIRST_LINE, resourcesManager.texturaSplash, vbom);
		//splash2 = new Sprite(Constants.ANCHO_PANTALLA/2, Constants.LAST_LINE, resourcesManager.texturaSplash2, vbom);
		
		splash2.setPosition(splash2.getX(), splash2.getY()-splash2.getHeight());
		
		attachChild(splash);
		attachChild(splash2);
	
		
	}

	@Override
	public void onBackKeyPressed() {
		return;
	}

	@Override
	public void disposeScene() {
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
	    this.dispose();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SPLASH;
	}
	
	

}
