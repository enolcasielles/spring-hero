package com.maetrik.jumpingball;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;



public class ResourcesManager{
	
    //---------------------------------------------
    // CONSTANTS
    //---------------------------------------------
	private final String RUTA_SONIDO_BOTON = "Sonidos/boton.wav";
	private final String RUTA_SONIDO_CARGAR = "Sonidos/cargar.mp3";
	private final String RUTA_SONIDO_SALTAR = "Sonidos/saltar.mp3";
	private final String RUTA_MUSICA = "Musica/musica.mp3";
	
	
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCIA = new ResourcesManager();
    
    public Engine engine;
    public MainActivity actividad;
    public Camera camara;
    public VertexBufferObjectManager vbom;
    
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    
    private BitmapTextureAtlas mAtlasSplash;
    public ITiledTextureRegion texturaSplash;
    
    private BitmapTextureAtlas mAtlasMenu;     
	public ITextureRegion texturaMenu; 
	public ITextureRegion texturaBotonPlay; 
	public ITextureRegion texturaBotonLogro;
	public ITextureRegion texturaBotonHowTo;
	public ITextureRegion texturaCopyright;
	public ITiledTextureRegion texturaBotonSonido;
	
	private BitmapTextureAtlas mAtlasGame;
	public ITiledTextureRegion texturaPersonaje; 
	public ITiledTextureRegion texturaCaritaResultado;
	public ITextureRegion texturaShareFacebook;
	public ITextureRegion texturaShareGoogle;
	public ITextureRegion texturaBotonMenu;
	public ITextureRegion texturaBotonRestart; 
	public ITextureRegion texturaBotonPushAndJump;
	public ITextureRegion texturaBotonPushedAndJump;
	
	
	
	//---------------------------------------------
	// SOUND
	//---------------------------------------------
	public Sound sonidoBoton;
	public Sound sonidoCargar;
	public Sound sonidoSaltar;
	public Music musica;
	
	
	
	//---------------------------------------------
	// FONTS
	//---------------------------------------------
	public Font fuenteGame;
	public Font fuenteMenu;
	public Font fuenteLoading;
	
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
	
	
	public void loadGlobalResources() {
    	FontFactory.setAssetBasePath("fuentes/");  //Indicamos donde se encuentran
    	final ITexture fontTextureLoading = new BitmapTextureAtlas(actividad.getTextureManager(),
    			256,256,TextureOptions.BILINEAR);  //Textura en donde cargaremos fuente
    	fuenteLoading = FontFactory.createFromAsset(actividad.getFontManager(), fontTextureLoading,
    				actividad.getAssets(), "Droid.ttf", 25, true, Color.WHITE_ABGR_PACKED_INT); //Definimos fuente
    	fuenteLoading.load();  //La cargamos
    	
    	//Cargamos los sonidos
    	try {
    		sonidoBoton = SoundFactory.createSoundFromAsset(actividad.getEngine().getSoundManager(), 
    				actividad, RUTA_SONIDO_BOTON);
    		sonidoBoton.setLooping(false);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	final ITexture fontTextureMenu = new BitmapTextureAtlas(actividad.getTextureManager(),
    			256,256,TextureOptions.BILINEAR);  //Textura en donde cargaremos fuente
    	fuenteMenu = FontFactory.createFromAsset(actividad.getFontManager(), fontTextureMenu,
    				actividad.getAssets(), "Droid.ttf", 40, true, Color.WHITE_ABGR_PACKED_INT); //Definimos fuente
    	fuenteMenu.load();  //La cargamos
    	
	}
	
	
	public void unloadGlobalResources() {
		fuenteLoading.unload();
		fuenteLoading = null;
		sonidoBoton = null;
		fuenteMenu.unload();
    	fuenteMenu = null;
	}
	
    
    public void loadSplashResources()  {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	mAtlasSplash = new BitmapTextureAtlas(actividad.getTextureManager(), 960, 120, TextureOptions.BILINEAR);
    	texturaSplash = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlasSplash, actividad, "bolaSplash.png", 0, 0,8,1);
    	mAtlasSplash.load();
    	loadGlobalResources();
    }
    
    
    public void unloadSplashResources() {
    	mAtlasSplash.unload();
    	texturaSplash = null;
    }
    
    
    public void loadMenuResources() {
    	loadMenuGraphics();
    	loadMenuFonts();
    	loadMenuAudio();
    }
    
    public void unloadMenuResources() {
    	mAtlasMenu.unload();
    	texturaMenu = null;
    	texturaBotonPlay = null;
    	texturaBotonLogro = null;
    	texturaCopyright=null;
    	texturaBotonHowTo=null;
    }
    
    
    public void loadGameResources() {
    	loadGameGraphics();
    	loadGameFonts();
    	loadGameAudio();
    }
    
    
    public void unloadGameResources() {
    	mAtlasGame.unload();
    	
    	fuenteGame.unload();
    	fuenteGame = null;
    	texturaPersonaje = null;
    	texturaCaritaResultado = null;
    	texturaShareFacebook = null;
    	texturaShareGoogle = null;
    	texturaBotonMenu = null;
    	texturaBotonSonido = null;
    	texturaBotonRestart = null;
 
    	sonidoCargar = null;
    	sonidoSaltar = null;
    	musica = null;
    }
    
    
    
    
    //-----------------------------------------------------
    //PRIVATE METHODS
    //-----------------------------------------------------
    private void loadMenuGraphics() {
    	//Definimos el atlas para las texturas del menu
    	mAtlasMenu = new BitmapTextureAtlas(actividad.getTextureManager(),1020,1020,TextureOptions.DEFAULT);
		
    	//Definimos las texturas
    	texturaMenu = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasMenu, actividad,
				"menu.png", 0, 0); //720x480
		
		texturaBotonPlay = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasMenu, actividad,
				"botonPlay.png", 0, 480);  //120x120
		
		texturaBotonLogro = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasMenu, actividad,
				"botonLogros.png",121 , 480); //120x120
		
		texturaBotonSonido = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlasMenu, actividad,
    			"botonSonido.png", 242, 480, 2, 1); // 450x196
		
		texturaCopyright = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasMenu, actividad,
    			"copyright.png", 0, 601); // 32x32
		
		texturaBotonHowTo = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasMenu, actividad,
    			"botonHelp.png", 32, 601); // 32x32
		
		
		//Cargamos el atlas
		mAtlasMenu.load();
    }
    
    private void loadMenuFonts() {
    	
    }
    
    private void loadMenuAudio() {

    	
    	//Cargamos la musica
    	
    }
    
    
	private void loadGameGraphics() {
    	//Definimos el atlas para las texturas del juego
    	mAtlasGame = new BitmapTextureAtlas(actividad.getTextureManager(),1024,256,TextureOptions.DEFAULT);
		
    	texturaPersonaje = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlasGame, actividad,
    			"bola.png", 0, 0, 8, 1); // 256x32
    	
    	texturaBotonPushAndJump =BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasGame, actividad,
    			"botonPushandjump.png", 0, 121); // 120x120
    	
    	texturaBotonPushedAndJump =BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasGame, actividad,
    			"botonPushedandjump.png", 121, 121); // 120x120
    	
    	texturaCaritaResultado = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mAtlasGame, actividad,
    			"caritaResultado.png", 256, 0, 2, 1); // 64x32
    	
    	texturaBotonRestart = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasGame, actividad,
				"botonRestart.png", 624, 0); //120x120
    	
    	texturaBotonMenu = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasGame, actividad,
				"botonMenu.png", 384, 0); //120x120
    	
    	texturaShareFacebook = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasGame, actividad,
				"shareFacebook.png", 504, 0); //120x120
    	
    	texturaShareGoogle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlasGame, actividad,
				"shareGoogle.png", 744, 0); //120x120
    	

    	//Cargamos el atlas
    	mAtlasGame.load(); 
	}
	
	
	private void loadGameFonts() {
    	//Cargamos la fuente del game
    	final ITexture fontTextureGame = new BitmapTextureAtlas(actividad.getTextureManager(),
    			256,256,TextureOptions.BILINEAR);  //Textura en donde cargaremos fuente
    	fuenteGame = FontFactory.createFromAsset(actividad.getFontManager(), fontTextureGame,
    				actividad.getAssets(), "Droid.ttf", 25, true, Color.WHITE_ABGR_PACKED_INT); //Definimos fuente
    	fuenteGame.load();  //La cargamos
	}

    
    private void loadGameAudio() {
    	//Cargamos los sonidos
    	try {
    		sonidoSaltar = SoundFactory.createSoundFromAsset(actividad.getEngine().getSoundManager(), 
    				actividad, RUTA_SONIDO_SALTAR);
    		sonidoSaltar.setLooping(false);  
    		sonidoCargar = SoundFactory.createSoundFromAsset(actividad.getEngine().getSoundManager(), 
    				actividad, RUTA_SONIDO_CARGAR);
    		sonidoCargar.setLooping(false);
    		sonidoCargar.setVolume(0.5f);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	//Cargamos la musica
    	try {
    		musica = MusicFactory.createMusicFromAsset(actividad.getEngine().getMusicManager(), 
    				actividad, RUTA_MUSICA);
    		musica.setLooping(true);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}	
    	
    	//Volumen
    	if (Constants.SOUND) musica.setVolume(1.0f);
    	else musica.setVolume(0.0f);
	}
    
    
    
    //Incia el manejador
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void iniciar(Engine engine, MainActivity activity, Camera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().actividad = activity;
        getInstance().camara = camera;
        getInstance().vbom = vbom;
        getInstance().loadGlobalResources();
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    //Obtiene una instacia del manejador
    public static ResourcesManager getInstance()
    {
        return INSTANCIA;
    }
}
