package Inicio;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;

public class MCRs {

	
	private String ruta_entorno_trabajo;			//Ruta dónde se encuentran los archivos de trabajo
	private String base_datos;						//Archivo que contiene la información de los eventos a leer
	private String tabla = "MCR";					//Tabla de la BD que almacena la información relativa a las Medidas de control del riesgo
	private HashMap<Integer, MCR> hm_mcrs = new HashMap<Integer, MCR>();			//Almacena las MCR en un hashMap. Las key son las ID de las MCR en la BD, los "values" son las propias MCRs
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 

	
	public MCRs(String ruta_entorno_trabajo, String base_datos) {
		this.ruta_entorno_trabajo = ruta_entorno_trabajo;
		this.base_datos = base_datos;
	}
	
	
	/**
	 * Crea un resultset para leer la información de la tabla "MCR" de la BD y extrae la información de la misma 
	 * necesaria para crear cada una de las MCR. Posteriormente, almacena cada una de las MCR en el hashMap
	 */
	public void leer_MCRs() {
		try {	
			ConectorAccess ca = new ConectorAccess(ruta_entorno_trabajo);
			Integer IdMCR;
			String NombreMCR;
			//String Descripcion_Evento;
			MCR mcr;
			
			ResultSet resultSet = ca.obtener_resultset(base_datos, tabla,"*", "Nombre");
			
			while(resultSet.next()){
				IdMCR = Integer.valueOf(resultSet.getString("IdMCR"));
				NombreMCR = resultSet.getString("Nombre");
	            //Printing Results
				mcr = new MCR(IdMCR, NombreMCR );
				this.hm_mcrs.put(IdMCR, mcr);
				//System.out.println(listaMCRs.size());
				
				//if (IdEvento == 553)
	            //System.out.println(IdMCR + "-" + NombreMCR);

	       }
			
			resultSet.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	/**
	 * Devuelve la MCR que tiene como ID el parámetro que se pasa como argumento al método
	 * @param IdMCR El ID de la MCR en la BD
	 * @return La MCR que tiene como ID el valor pasado como parámetro
	 */
	public MCR getMCR(Integer IdMCR) {
		return hm_mcrs.get(IdMCR);
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
