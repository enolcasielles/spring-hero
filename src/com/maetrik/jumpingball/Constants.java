package com.maetrik.jumpingball;

import java.io.InputStream;
import java.security.PublicKey;
import java.util.ArrayList;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;

public class Constants {
	
	
	public static String TEXT_LOADING = "Loading.";
	public static long RECORD = 0;
	public static boolean SOUND;
	public static boolean MUSIC;
	
	
	//PANTALLA, CANVAS y USUARIO
	public static float ANCHO_PANTALLA = 480;
	public static float ALTO_PANTALLA = 1024;
	public static float FIRST_LINE;
	public static float LAST_LINE;
	
	
	//HEROE
	public static final float ANCHO_HEROE = 39.5f;
	public static final float ALTO_HEROE = 55;
	public static final float OJO_IZQ_X_REF = 0;
	public static final float OJO_IZQ_Y_REF = 0;
	public static final float SEPARACION_OJOS = 10;
	public static final float RADIO_PERMITIDO_OJOS = 2;
	
	//BLOQUES
	public static float MAX_SEPARATION = 300;
	public static float MIN_SEPARATION = 150;
	public static float ANCHO_BLOQUES = 48;
	public static float MIN_ALTO_BLOQUES = 250;
	public static float MAX_ALTO_BLOQUES = 450;
	
	
	//COLOR DE FONDO
	public static final float FONDO_R = 1.0f;
	public static final float FONDO_G = 0.8f;
	public static final float FONDO_B = 0.6f;
	
	//MONTAÑAS
	public static final float ALTO_MOUNTAIN = 262;
	public static final float ANCHO_MOUNTAIN = 1020;
	public static final float TRANSPARENCIA_MOUNTAIN = 0.85f;
	
	//NUBES
	public static final float MAX_ALPHA_NUBE = 0.9f;
	public static final float MIN_ALPHA_NUBE = 0.2f;
	public static final float  MAX_SEPARATION_NUBE = 10;
	public static final float  MIN_SEPARATION_NUBE = 5;
	public static final float  MAX_Y_NUBE = 150;
	public static final float MIN_Y_NUBE = 50;
	public static final int NUM_NUBES = 5;
	
	//SUELO y ARBLOES
	public static final float ALTO_SUELO = 125;
	public static final float ANCHO_SUELO = 981;
	public static final float MAX_SEPARATION_ARBOL = 300;
	public static final float MIN_SEPARATION_ARBOL = 150;
	public static final float ALTO_ARBOL = 141;
	public static final float ANCHO_ARBOL = 77;
	public static final int NUM_ARBOLES = 3;
	
	//MAR
	public static final float ANCHO_MAR = 981;
	public static final float ALTO_MAR = 125;
	
	//Muelle
	public static final float ANCHO_MUELLE = 33;
	public static final float ALTO_MUELLE = 31;
	
	//Pajaro
	public static final float ANCHO_PAJARO = 18;
	public static final float ALTO_PAJARO = 18;
	
	
	//Puntuacion y Mensaje inicial
	public static final float Y_SCORE = 200;
	
	
	//Titulo del juego
	public static final float ANCHO_TITULO = 201;
	public static final float ALTO_TITULO = 100;

	
	
}
