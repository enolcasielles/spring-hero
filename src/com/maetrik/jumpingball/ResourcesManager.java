package com.maetrik.jumpingball;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackerTextureRegion;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
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
import org.andengine.util.debug.Debug;



public class ResourcesManager{
	
    //---------------------------------------------
    // CONSTANTS
    //---------------------------------------------
	private final String RUTA_SONIDO_BOTON = "Sonidos/boton.mp3";
	private final String RUTA_SONIDO_CARGAR = "Sonidos/cargar.mp3";
	private final String RUTA_SONIDO_SALTAR = "Sonidos/saltar.mp3";
	private final String RUTA_SONIDO_CAER = "Sonidos/caer.mp3";
	private final String RUTA_MUSICA_MAR = "Musica/mar.mp3";
	private final String RUTA_MUSICA_PAJAROS = "Musica/pajaros.mp3";
	
	
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
    
	
	private TexturePackTextureRegionLibrary texturePackLibrary;
	private TexturePack texturePack;
	public ITextureRegion texturaMountain; 
	public ITextureRegion texturaSuelo;
	public ITextureRegion[] texturaArboles;
	public ITextureRegion texturaMar1;
	public ITextureRegion texturaMar2;
	public ITextureRegion texturaMar3; 
	public ITiledTextureRegion texturaHeroe;
	public ITiledTextureRegion texturaPajaro;
	public ITextureRegion texturaOjos;
	public ITextureRegion texturaMuelle;
	public ITextureRegion texturaBloque;
	public ITextureRegion[] texturasNube;
	public TexturePackerTextureRegion textureFacebook;
	public TexturePackerTextureRegion textureHome;
	public TexturePackerTextureRegion textureNoadd;
	public TexturePackerTextureRegion textureNoVolme;
	public TexturePackerTextureRegion texturePlay;
	public TexturePackerTextureRegion textureRanking;
	public TexturePackerTextureRegion texturaRate;
	public TexturePackerTextureRegion texturaReplay;
	public TexturePackerTextureRegion texturaVolumen;
	public TexturePackerTextureRegion texturaGameOver;
	public TexturePackerTextureRegion texturaNombre;
	public TexturePackerTextureRegion texturaNorma;
	public TexturePackerTextureRegion texturaBlanco;
	public TexturePackerTextureRegion texturaResultado;
	public ITextureRegion[] texturaNumeros;
	
	public static final int BOTONFACE_ID = 0;
	public static final int BOTONHOME_ID = 1;
	public static final int BOTONNOADD_ID = 2;
	public static final int BOTONNOVOLUMEN_ID = 3;
	public static final int BOTONPLAY_ID = 4;
	public static final int BOTONRANKING_ID = 5;
	public static final int BOTONRATE_ID = 6;
	public static final int BOTONREPLAY_ID = 7;
	public static final int BOTONVOLUMEN_ID = 8;
	public static final int TEXTURA0_ID = 9;
	public static final int TEXTURA1_ID = 10;
	public static final int TEXTURA2_ID = 11;
	public static final int TEXTURA3_ID = 12;
	public static final int TEXTURA4_ID = 13;
	public static final int TEXTURA5_ID = 14;
	public static final int TEXTURA6_ID = 15;
	public static final int TEXTURA7_ID = 16;
	public static final int TEXTURA8_ID = 17;
	public static final int TEXTURA9_ID = 18;
	public static final int TEXTURAARBOL1_ID = 19;
	public static final int TEXTURAARBOL2_ID = 20;
	public static final int TEXTURAARBOL3_ID = 21;
	public static final int TEXTURABLOQUE_ID = 22;
	public static final int TEXTURAGAMEOVER_ID = 23;
	public static final int TEXTURAHEROE_ID = 24;
	public static final int TEXTURAMAR1_ID = 25;
	public static final int TEXTURAMAR2_ID = 26;
	public static final int TEXTURAMAR3_ID = 27;
	public static final int TEXTURAMOUNTAIN_ID = 28;
	public static final int TEXTURAMUELLE_ID = 29;
	public static final int TEXTURANOMBRE_ID = 30;
	public static final int TEXTURANUBE1_ID = 31;
	public static final int TEXTURANUBE2_ID = 32;
	public static final int TEXTURANUBE3_ID = 33;
	public static final int TEXTURANUBE4_ID = 34;
	public static final int TEXTURANUBE5_ID = 35;
	public static final int TEXTURANUMBERS_ID = 36;
	public static final int TEXTURAOJOS_ID = 37;
	public static final int TEXTURAPAJARO_ID = 38;
	public static final int TEXTURARESULTADO_ID = 39;
	public static final int TEXTURASUELO_ID = 40;
	public static final int TEXTURENORMA_ID = 41;
	
	
	//---------------------------------------------
	// SOUND
	//---------------------------------------------
	public Sound sonidoBoton;
	public Sound sonidoCargar;
	public Sound sonidoSaltar;
	public Sound sonidoCaer;
	public Music musica_mar;
	public Music musica_pajaro;


	
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    
    public void loadGameResources() {
    	loadGameGraphics();
    	loadGameAudio();
    }
    
    
    public void unloadGameResources() {
    	
    	texturePack.unloadTexture();
    	texturePack=null;

 
    	sonidoCargar = null;
    	sonidoSaltar = null;
    	sonidoBoton = null;
    	sonidoCaer = null;
    	musica_mar = null;
    	musica_pajaro = null;
    }
    

    
    
	private void loadGameGraphics() {
		//Parseo el fichero
    	try {
    		  texturePack = new TexturePackLoader(actividad.getTextureManager(), "gfx/")
    		      	.loadFromAsset(actividad.getAssets(), "textura_game0.xml");
    		  texturePack.loadTexture();
    		  texturePackLibrary = texturePack.getTexturePackTextureRegionLibrary();
    	} catch (final TexturePackParseException e)  {
    	      Debug.e(e);
    	}

    	texturaMountain = texturePackLibrary.get(TEXTURAMOUNTAIN_ID);
    	texturaSuelo = texturePackLibrary.get(TEXTURASUELO_ID);
    	texturaArboles = new ITextureRegion[Constants.NUM_ARBOLES];
    	texturaArboles[0] = texturePackLibrary.get(TEXTURAARBOL1_ID);
    	texturaArboles[1] = texturePackLibrary.get(TEXTURAARBOL2_ID);
    	texturaArboles[2] = texturePackLibrary.get(TEXTURAARBOL3_ID);
    	texturaMar1 = texturePackLibrary.get(TEXTURAMAR1_ID);
    	texturaMar2 = texturePackLibrary.get(TEXTURAMAR2_ID);
    	texturaMar3 = texturePackLibrary.get(TEXTURAMAR3_ID);
    	texturaHeroe = texturePackLibrary.get(TEXTURAHEROE_ID,2,1);
    	texturaPajaro = texturePackLibrary.get(TEXTURAPAJARO_ID,5,1);
    	texturaOjos = texturePackLibrary.get(TEXTURAOJOS_ID);
    	texturaMuelle = texturePackLibrary.get(TEXTURAMUELLE_ID);
    	texturasNube = new ITextureRegion[Constants.NUM_NUBES];
    	texturasNube[0] = texturePackLibrary.get(TEXTURANUBE1_ID);
    	texturasNube[1] = texturePackLibrary.get(TEXTURANUBE2_ID);
    	texturasNube[2] = texturePackLibrary.get(TEXTURANUBE3_ID);
    	texturasNube[3] = texturePackLibrary.get(TEXTURANUBE4_ID);
    	texturasNube[4] = texturePackLibrary.get(TEXTURANUBE5_ID);
    	texturaBloque = texturePackLibrary.get(TEXTURABLOQUE_ID);
    	textureFacebook = texturePackLibrary.get(BOTONFACE_ID);
    	textureHome = texturePackLibrary.get(BOTONHOME_ID);
    	textureNoadd = texturePackLibrary.get(BOTONNOADD_ID);
    	textureNoVolme = texturePackLibrary.get(BOTONNOVOLUMEN_ID);
    	texturePlay = texturePackLibrary.get(BOTONPLAY_ID);
    	textureRanking = texturePackLibrary.get(BOTONRANKING_ID);
    	texturaRate = texturePackLibrary.get(BOTONRATE_ID);
    	texturaReplay = texturePackLibrary.get(BOTONREPLAY_ID);
    	texturaVolumen = texturePackLibrary.get(BOTONVOLUMEN_ID);
    	texturaNumeros = new ITextureRegion[10];
    	texturaNumeros[0] = texturePackLibrary.get(TEXTURA0_ID);
    	texturaNumeros[1] = texturePackLibrary.get(TEXTURA1_ID);
    	texturaNumeros[2] = texturePackLibrary.get(TEXTURA2_ID);
    	texturaNumeros[3] = texturePackLibrary.get(TEXTURA3_ID);
    	texturaNumeros[4] = texturePackLibrary.get(TEXTURA4_ID);
    	texturaNumeros[5] = texturePackLibrary.get(TEXTURA5_ID);
    	texturaNumeros[6] = texturePackLibrary.get(TEXTURA6_ID);
    	texturaNumeros[7] = texturePackLibrary.get(TEXTURA7_ID);
    	texturaNumeros[8] = texturePackLibrary.get(TEXTURA8_ID);
    	texturaNumeros[9] = texturePackLibrary.get(TEXTURA9_ID);
    	texturaGameOver = texturePackLibrary.get(TEXTURAGAMEOVER_ID);
    	texturaNombre = texturePackLibrary.get(TEXTURANOMBRE_ID);
    	texturaResultado = texturePackLibrary.get(TEXTURARESULTADO_ID);
    	texturaNorma = texturePackLibrary.get(TEXTURENORMA_ID);
    	
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
    		
    		sonidoCaer = SoundFactory.createSoundFromAsset(actividad.getEngine().getSoundManager(), 
    				actividad, RUTA_SONIDO_CAER);
    		sonidoCaer.setLooping(false);
    		
    		sonidoBoton = SoundFactory.createSoundFromAsset(actividad.getEngine().getSoundManager(), 
    				actividad, RUTA_SONIDO_BOTON);
    		sonidoBoton.setLooping(false);
   
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	//Cargamos la musica
    	try {
    		musica_mar = MusicFactory.createMusicFromAsset(actividad.getEngine().getMusicManager(), 
    				actividad, RUTA_MUSICA_MAR);
    		musica_mar.setLooping(true);
    		musica_pajaro = MusicFactory.createMusicFromAsset(actividad.getEngine().getMusicManager(), 
    				actividad, RUTA_MUSICA_PAJAROS);
    		musica_pajaro.setLooping(true);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}	
    	
    	//Volumen
    	if (Constants.SOUND) {
    		musica_mar.setVolume(1.0f);
    		musica_pajaro.setVolume(1.0f);
    	}
    	else {
    		musica_mar.setVolume(0.0f);
    		musica_pajaro.setVolume(0.0f);
    	}
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
