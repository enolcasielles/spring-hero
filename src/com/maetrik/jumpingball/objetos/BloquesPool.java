package com.maetrik.jumpingball.objetos;

import java.util.ArrayList;

import com.maetrik.jumpingball.Constants;

import android.provider.MediaStore.Video;

public class BloquesPool {
	
	//Contenedor
	ArrayList<Bloque> bloques;
	
	private boolean esPrimero;
	
	
	public BloquesPool() {
		bloques = new ArrayList<Bloque>();
		esPrimero = true;
	}
	
	public void addBloque(Bloque b) {
		if (esPrimero == true) {
			esPrimero = false;  //Evitamos añadir el primer bloque que se intente, ya que no nos interesa
		}
		else {
			bloques.add(b);
		}
	}
	
	public Bloque getBloque() {
		int size = bloques.size();
		if (size > 0) {
			Bloque b = bloques.get(size-1);
			bloques.remove(size-1);
			return b;
		}
		return null;
	}
	
	
	public void dispose() {
		for (int i=0 ; i<bloques.size() ; i++) {
			bloques.get(i).dispose();
		}
		bloques.clear();
	}

}
