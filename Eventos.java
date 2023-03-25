package Inicio;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
import java.sql.ResultSet;
//import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.Set;


/**
 * Almacena el conjunto de eventos incluidos en la BD del RGP
 * @author shark1
 * @version 0.0.1.
 * @date 03/01/2022
 */
public class Eventos {

	private String ruta_entorno_trabajo;			//Ruta donde se va a trabajar y dónde se encuentran almacenados los archivos con los datos que se van a leer
	private String base_datos;				//Archivo que contiene la información de los eventos a leer
	private String tabla = "Eventos";
	private HashMap<Integer, Evento> eventos = new HashMap<Integer, Evento>();			//HashMap que contiene los IDs y los eventos de seguridad
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase Eventos
	/**
	 * Constructor de Eventos
	 * @param ruta_entorno_trabajo
	 */
	public Eventos(String ruta_entorno_trabajo, String base_datos) {
		this.ruta_entorno_trabajo = ruta_entorno_trabajo;	//Se establece la ruta de trabajo del entorno, es dónde se encuentran almacenado los archivos con la información
		this.base_datos = base_datos;
	}
	
	
	/**
	 * Permite leer la información de la tabla de eventos y crea un HashMap con cada uno de los eventos, en el que la clave es el id del evento y 
	 * el valor devuelto es el evento que tenía ese id. 
	 */
	public void leer_eventos() {
		try {	
			ConectorAccess ca = new ConectorAccess(ruta_entorno_trabajo);
			Integer IdEvento;
			String NombreEvento;
			Evento evento;
			
			//Obtenemos un resultset de la tabla "Eventos"
			ResultSet resultSet = ca.obtener_resultset(base_datos, tabla, "*", "NombreEvento");

			//Creamos los objetos "Evento" y los almacenamos en el HashMap, utilizando como clave el ID del evento
			while(resultSet.next()){
	            IdEvento = Integer.valueOf(resultSet.getString("IdEvento"));
	            NombreEvento = resultSet.getString("NombreEvento").toUpperCase();
				evento = new Evento(IdEvento, NombreEvento );
				this.eventos.put(IdEvento, evento);
	       }
			
			resultSet.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Devuelve el evento que tiene como Id el parámetro que se pasa como argumento
	 * @param IdEvento -> El Id que tiene el evento en la BD
	 */
	public Evento getEvento(Integer IdEvento) {
		return eventos.get(IdEvento);
	}
	
	
	/**
	 * Devuelve el hashmap que tiene almacenados todos los eventos de la tabla, indicando como clave el id correspondiente a cada evento
	 * @return
	 */
	public HashMap<Integer, Evento> getEventos() {
		return this.eventos;
	}
	
	/**
	 * Devuelve un StringBuffer con los errores generados durante la ejecución de esta clase
	 * @return un StringBuffer con los errores generados durante la ejecución de esta clase
	 */
	public StringBuffer imprimirErrores() {

		Collection<Evento> lista_eventos = eventos.values();
		Iterator<Evento> it = lista_eventos.iterator();
		
		while(it.hasNext()) {
			Evento e = it.next();
			if ( e.imprimirErrores() != null) {
				this.logErrores.append(e.imprimirErrores());
			}
		}
		
		return this.logErrores;
	}
	
	
	/**
	 * Escribe los errores correspondientes a la ejecución de esta clase en el log de errores
	 * @param error El error lógico que se ha producido al ejecutar esta clase (Evento)
	 */
	public void escribirError( String error) {
		this.logErrores.append(error + "\n");
	}


	public boolean existeEvento(int idEvento) {
		
		if(this.eventos.containsKey(idEvento)) {
			return true;
		}
		else
			return false;
	}

}


