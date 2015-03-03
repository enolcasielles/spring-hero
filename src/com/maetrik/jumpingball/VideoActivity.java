package com.maetrik.jumpingball;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	
	private VideoView video;
	private ImageButton botonSalir;
	
	public static boolean MUSICA;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.video_activity);
		
		final float volume;
		if (MUSICA) volume = 1.0f;
		else volume = 0.0f;
		
		//Recupero la vista del video
		video = (VideoView) findViewById(R.id.videoView1);
	    video.setMediaController(null);
	    video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video2));
	    video.start();
	    video.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setVolume(volume, volume);
			}
		});
	    video.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				video.start();
			}
		});
	    
	    //Recupero el boton de salir
	    botonSalir = (ImageButton) this.findViewById(R.id.botonSalirVideo);
	    botonSalir.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				video.stopPlayback();
				VideoActivity.this.finish();	
			}
		} );
		
	}

}
