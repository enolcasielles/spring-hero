package com.maetrik.jumpingball.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;

import com.maetrik.jumpingball.SceneManager.SceneType;

public class SplashScene extends BaseScene {
	
	private AnimatedSprite splash;

	@Override
	public void createScene() {
		
		setBackground(new Background(0.796f,0.796f,0.796f));
		
		splash = new AnimatedSprite(0, 0, resourcesManager.texturaSplash, vbom);
		splash.setScale(0.7f);
		splash.animate(200);
		splash.setPosition(camera.getWidth() / 2 - splash.getWidth() / 2, 
				camera.getHeight() / 2 - splash.getHeight() / 2);
		attachChild(splash);
	
		
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
