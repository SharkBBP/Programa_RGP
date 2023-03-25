package Inicio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Lector_Archivos {
	

	public Lector_Archivos() {
		// TODO Auto-generated constructor stub
	}

	public void leer_archivo(String ruta_archivo) {
		try {
				File miArchivo = new File( ruta_archivo);
				Scanner lector = new Scanner( miArchivo );
				while (lector.hasNextLine()) {
					String data = lector.nextLine();
					System.out.println(data);
				}
				lector.close();
		} catch (FileNotFoundException e) {
			System.out.println("Ha ocurrido un error al intentar abrir el archivo de la ruta: " + ruta_archivo );
		}
	}
}
