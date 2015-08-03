package com.maetrik.jumpingball;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.CroppedResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;



public class MainActivity extends BaseGameActivity implements GoogleApiClient.ConnectionCallbacks, 
															  GoogleApiClient.OnConnectionFailedListener {
	

	//===================================================
	//CONSTANTES
	//===================================================
    static final int ANCHO_CAMARA = 480;
    static final int ALTO_CAMARA = 1024; 
    private CroppedResolutionPolicy cropResolutionPolicy;
    private static final String MY_AD_UNIT_ID = "ca-app-pub-4115086807184374/9230102445";	
    private static final String MY_INTERSTICIAL_AD_UNIT_ID_STRING = "ca-app-pub-4115086807184374/3183568845";
    private static final String MY_BANNER_GAME_OVER_AD_UNIT_ID = "ca-app-pub-4115086807184374/6320319647";
    
		
	//===================================================
	//VARIABLES
	//===================================================
		
	private Camera mCamera;  //Camara
	
	public ProgressDialog progressDialog = null;
	
	private static UiLifecycleHelper uiHelper;
	
	private AdView mAdView;    //Banner continuo
	//private AdView mAdView2;   //Banner gameOver
	private AdRequest request;
	
	private InterstitialAd interstitial;
	
	private GoogleApiClient mGoogleApiClient;
	private static int RC_SIGN_IN = 9001;
	private static int REQUEST_LEADERBOARD = 9002;
	private static int REQUEST_ACHIEVEMENTS = 9003;
	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInflow = true;
	private boolean mSignInClicked = false;
	
	
	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(pSavedInstanceState);
        
        // Create the Google Api Client with access to the Play Game services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();
        
	};
	
	@Override
	protected void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	    if (this.isGameLoaded())
	        if (ResourcesManager.getInstance().musica_mar != null && ResourcesManager.getInstance().musica_pajaro != null ) {
	        	if (Constants.MUSIC == true) {
	        		ResourcesManager.getInstance().musica_pajaro.resume();
	        		ResourcesManager.getInstance().musica_mar.resume();
	        	}
	    }
	    AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	    if (this.isGameLoaded())
	        if (ResourcesManager.getInstance().musica_mar != null && ResourcesManager.getInstance().musica_pajaro != null) {
	        	ResourcesManager.getInstance().musica_mar.pause();
	        	ResourcesManager.getInstance().musica_pajaro.pause();
	        }
	    AppEventsLogger.deactivateApp(this);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
        System.exit(0);	
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
	    super.onStop();
	    mGoogleApiClient.disconnect();
	}
	
	
	@Override
	public void onConnected(Bundle connectionHint) {
	    // The player is signed in. Hide the sign-in button and allow the
	    // player to proceed.
	}
	
	
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
	    if (mResolvingConnectionFailure) {
	        // already resolving
	        return;
	    }

	    // if the sign-in button was clicked or if auto sign-in is enabled,
	    // launch the sign-in flow
	    if (mSignInClicked || mAutoStartSignInflow) {
	    	mAutoStartSignInflow = false;
	        mSignInClicked = false;
	        mResolvingConnectionFailure = true;

	        // Attempt to resolve the connection failure using BaseGameUtils.
	        // The R.string.signin_other_error value should reference a generic
	        // error string in your strings.xml file, such as "There was
	        // an issue with sign-in, please try again later."
	        if (!BaseGameUtils.resolveConnectionFailure(MainActivity.this,
	                mGoogleApiClient, connectionResult,
	                RC_SIGN_IN, getString(R.string.signin_other_error))) {
	            mResolvingConnectionFailure = false;
	        }
	    }

	    // Put code here to display the sign-in button
	}

	
	
	
	@Override
	public void onConnectionSuspended(int i) {
	    // Attempt to reconnect
	    mGoogleApiClient.connect();
	}
	
	
	
	@Override
    protected void onSetContentView() {
 
        
		final FrameLayout frameLayout = new FrameLayout(this);
        final FrameLayout.LayoutParams frameLayoutLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                             FrameLayout.LayoutParams.MATCH_PARENT);
 
        //Inicio el banner continuo
        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
    	mAdView.setAdUnitId(MY_AD_UNIT_ID);
    	
        mAdView.refreshDrawableState();
        mAdView.setVisibility(AdView.INVISIBLE);
        final FrameLayout.LayoutParams adViewLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                             FrameLayout.LayoutParams.WRAP_CONTENT,
                                             Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
       
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                getResources().getDisplayMetrics());
 
        adViewLayoutParams.topMargin = height / 2;
        
        //Inicio el banner discontinuo
        /*
        mAdView2 = new AdView(this);
        mAdView2.setAdSize(AdSize.BANNER);
    	mAdView2.setAdUnitId(MY_BANNER_GAME_OVER_AD_UNIT_ID);
    	mAdView2.refreshDrawableState();
        mAdView2.setVisibility(AdView.INVISIBLE);
        */
        final FrameLayout.LayoutParams adView2LayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                             FrameLayout.LayoutParams.WRAP_CONTENT,
                                             Gravity.CENTER_HORIZONTAL|Gravity.TOP);
        
        //Inicio el intersticial
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(MY_INTERSTICIAL_AD_UNIT_ID_STRING);
        
        request = new AdRequest.Builder()
    		.addTestDevice("YOUR DEVICE HASH")
    		.build();
   	 
        //Cargo todos los anuncios
        mAdView.loadAd(request);
        //mAdView2.loadAd(request);
        interstitial.loadAd(request);
       
        this.mRenderSurfaceView = new RenderSurfaceView(this);
        mRenderSurfaceView.setRenderer(mEngine,this);
 
        final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams =
                new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                		android.view.ViewGroup.LayoutParams.MATCH_PARENT,Gravity.CENTER); 
 
        frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
        frameLayout.addView(mAdView, adViewLayoutParams);
        //frameLayout.addView(mAdView2, adView2LayoutParams);
       
        this.setContentView(frameLayout, frameLayoutLayoutParams);
       
        
        
    }
	
	
	//Controla la visibilidad de los banners
	public void setBannerVisibility(final int banner, final boolean bol) {
		/*
		this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	if (banner == 1) {
	        		if (bol) MainActivity.this.mAdView.setVisibility(View.VISIBLE);
		    		else MainActivity.this.mAdView.setVisibility(View.INVISIBLE);
	        	}
	        	if (banner == 2) {
	        		if (bol) MainActivity.this.mAdView2.setVisibility(View.VISIBLE);
		    		else MainActivity.this.mAdView2.setVisibility(View.INVISIBLE);
	        	}
	        }
	    });
	    */
	}
	
	
	//Muestra el intersticial
	public void displayInterstitial() {
		this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	if (interstitial.isLoaded()) {
	      	      interstitial.show();
	      	    }
	        }
	    });
	}
	
	
	//Muestra un mensaje por Toast
	public void toastMassage(final String msg) {
		this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
	        }
	    });
	    
	}
	
	
	//Carga un intersticial
	public void loadIntersticial() {
		this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	//Inicio el intersticial si no hay ninguno cargado
	    		if (interstitial.isLoaded() == false) {
	    			interstitial = new InterstitialAd(MainActivity.this);
	    	        interstitial.setAdUnitId(MY_INTERSTICIAL_AD_UNIT_ID_STRING);
	    	        //Lo cargo
	    	        interstitial.loadAd(request);
	    		}
	        }
	    });
	}
	
		
	
	public RenderSurfaceView getRenderer() {
		return mRenderSurfaceView; 
	}
	
	public Engine getEngine() {
		return mEngine;
	}
	
	public static UiLifecycleHelper getUiLifecycleHelper() {
		return uiHelper;
	}
	
	public BaseGameActivity getSuper() {
		return this;
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	    
	    
	    if (requestCode == RC_SIGN_IN) {
	        mSignInClicked = false;
	        mResolvingConnectionFailure = false;
	        if (resultCode == RESULT_OK) {
	            mGoogleApiClient.connect();
	        } else {
	            // Bring up an error dialog to alert the user that sign-in
	            // failed. The R.string.signin_failure should reference an error
	            // string in your strings.xml file that tells the user they
	            // could not be signed in, such as "Unable to sign in."
	            BaseGameUtils.showActivityResultError(this,
	                requestCode, resultCode, R.string.signin_failure);
	        }
	    }
	    
	}
	
	
	
	// Call when the sign-in button is clicked
	public void signInClicked() {
	    mSignInClicked = true;
	    if(mGoogleApiClient.isConnected()) {
	    	startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
	    			getString(R.string.leaderboard_score)), REQUEST_LEADERBOARD);
	    }
	    else {
	    	 mGoogleApiClient.connect();
	    }
	}
	
	
	//Update record on games
	public void updateRecord(int record) {
		if(mGoogleApiClient.isConnected()) {
			Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_score), record);
		}
	}
	
	
	public void newLogro(String str) {
		if(mGoogleApiClient.isConnected()) {
			Games.Achievements.unlock(mGoogleApiClient, str);
		}
	}
	
	
	public void showLogros() {
		mSignInClicked = true;
		if(mGoogleApiClient.isConnected()) {
			startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
		}
		else {
	    	 mGoogleApiClient.connect();
	    }
	}
	

	//===================================================
	//METODOS SUPERCLASE
	//===================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		//Inicio la camara
		this.mCamera = new Camera(0,0, ANCHO_CAMARA,ALTO_CAMARA);
		
		/* Definimos las opciones del Engine:
			1. Pantalla completa (true)
			2. Modo horizontal
			3. Resolucion de pantalla. Esto permite trabajar para una cierta 
	       	   resolucion y que AndEngine se encargue de adaptarlo a la del
	       	   terminal
	    	4. La camara que se usara */
		
		cropResolutionPolicy = new CroppedResolutionPolicy(ANCHO_CAMARA, ALTO_CAMARA);
		
		EngineOptions eo = new EngineOptions (true,ScreenOrientation.PORTRAIT_FIXED,
				cropResolutionPolicy,this.mCamera);
		
		//Habilitamos sonidos y musica en el motor
		eo.getAudioOptions().setNeedsMusic(true);
		eo.getAudioOptions().setNeedsSound(true);
		
		//Impedimos que la pantalla se apague por inactividad
		eo.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		
		return eo;
	}
	
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	
	
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
	    //Iniciamos manejador de recursos
		ResourcesManager.iniciar(mEngine, this, mCamera, getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}



	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		//Ajusto la primer y ultima linea del area visible
		Constants.FIRST_LINE = cropResolutionPolicy.getTop();
		Constants.LAST_LINE = cropResolutionPolicy.getBottom();
		//Inicio con escena del menu
		SceneManager.getInstance().init_to_gameScene(pOnCreateSceneCallback);	
	}


	
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
	    /*
		mEngine.registerUpdateHandler(new TimerHandler(2.0f, new ITimerCallback() 
	    {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	                mEngine.unregisterUpdateHandler(pTimerHandler);
	                SceneManager.getInstance().splashScene_to_menuScene();
	            }
	    }));
	    */
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	

	
	
	//Sobrescribimos este metodo para que al pulsar en el boton de atras se llame al metodo de cada escena
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}
	
	

}
