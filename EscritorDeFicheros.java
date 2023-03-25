package Inicio;


import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
//import java.io.IOException;


/**
 * Genera ficheros para escribir sobre ellos y 
 * escribe la información deseada sobre los ficheros generados
 * @author AFB
 *
 */
public class EscritorDeFicheros {

	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 

	public EscritorDeFicheros() {
		
	}
	
	
	/**
	 * Crea un fichero con el nombre indicado como parámetro, dentro de la ruta de trabajo
	 * @param nombre_fichero
	 * @return
	 */
	public File crear_fichero( String nombre_fichero) {
	
		File file = null;
		
		try {
			
			file = new File ( nombre_fichero );
			
			if( file.createNewFile()) {
					//No Hacer nada
			}
			else {
				System.out.println("El fichero de nombre: " + nombre_fichero + " ya existe");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return file;
	}
	
	/**
	 * Escribe en un fichero la información que se le pase como parámetro
	 * @param nombre_fichero 	El nombre que se le quiera dar al fichero
	 * @param sb		El StringBuffer dónde está almacenada la información que se quiere escribir en el fichero
	 */
	public void escribir_en_fichero(File nombre_fichero, StringBuffer sb) {
		
		FileWriter file_writer;
		
		try {
			file_writer = new FileWriter(nombre_fichero);
			file_writer.write(sb.toString());
			file_writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Escribe en un fichero la información que se le pase como parámetro
	 * @param nombre_fichero 	El nombre que se le quiera dar al fichero
	 * @param sb		El StringBuffer dónde está almacenada la información que se quiere escribir en el fichero
	 */
	public void escribir_en_fichero(String nombre_fichero, StringBuffer sb) {
		
		//File fichero = new File(nombre_fichero);
		FileWriter file_writer;
		
		try {
			file_writer = new FileWriter(nombre_fichero);
			file_writer.write(sb.toString());
			file_writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Devuelve un StringBuffer con los errores generados durante la ejecución de esta clase
	 * @return un StringBuffer con los errores generados durante la ejecución de esta clase
	 */
	public StringBuffer imprimirErrores() {

		return this.logErrores;
	}
	
	
	/**
	 * Escribe los errores correspondientes a la ejecución de esta clase en el log de errores
	 * @param error El error lógico que se ha producido al ejecutar esta clase 
	 */
	public void escribirError( String error) {
		this.logErrores.append(error + "\n");
	}

	
}
