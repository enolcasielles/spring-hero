package com.maetrik.jumpingball.objetos;

import java.util.ArrayList;

import com.maetrik.jumpingball.Constants;

import android.provider.MediaStore.Video;

public class NubesPool {
	
	//Contenedor
	ArrayList<Nube> bloques;
	
	private boolean esPrimero;
	
	
	public NubesPool() {
		bloques = new ArrayList<Nube>();
		esPrimero = true;
	}
	
	public void addBloque(Nube b) {
		if (esPrimero == true) {
			esPrimero = false;  //Evitamos añadir el primer bloque que se intente, ya que no nos interesa
		}
		else {
			bloques.add(b);
		}
	}
	
	public Nube getBloque() {
		int size = bloques.size();
		if (size > 0) {
			Nube b = bloques.get(size-1);
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
