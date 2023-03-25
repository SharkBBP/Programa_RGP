package Inicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//Creamos la clase responsables leyendo los datos de la Tabla responsables con un patrón Singleton
public class Responsables {

//		private String ruta_entorno_trabajo;					//Ruta donde se va a trabajar y dónde se encuentran almacenados los archivos con los datos que se van a leer
		private String base_datos;								//nombre del fichero de la BD que se va a utilizar
		private HashMap<Integer,Responsable> responsables = new HashMap<Integer, Responsable>();		//HashMap que almacena los Id de los Responsables como clave y los objetos responsables como valor
		private ConectorAccess ca;							//Conector a la BD access que nos permite obtener los resultsets con la información con la que queramos trabajar.

		
		public Responsables(String ruta_entorno_trabajo, String archivo_bd, ConectorAccess ca) {
			//this.ruta_entorno_trabajo = ruta_entorno_trabajo;
			this.base_datos = archivo_bd;
			this.ca = ca;
		}
		
	
		/**
		 * Devuelve el objeto Responsable que corresponde con el Id que se le pasa como argumento
		 * @param id	El Id del Responsable establecido en la tabla Responsables de la BD
		 * @return	El objeto Responsable que corresponde con el Id solicitado como parámetro
		 */
		public Responsable getResponsable(Integer id) {
			if( this.responsables.containsKey(id)) {
				return this.responsables.get(id);
			}
			else {
				System.out.println("No existe ningún responsable con el Id: " + id);
				return null;
			}
		}

		
		/**
		 * Lee todos los datos de la tabla Responsables de la BD, crea los objetos "Responsable" con los metadatos obtenidos de esa tabla y almacena esta información 
		 * en el HashMap correspondiente, estableciendo como clave para cada objeto el Id establecido en la citada tabla 
		 */
		public void leerResponsables() {
			
//			ca = new ConectorAccess(ruta_entorno_trabajo);	
			String consulta = "SELECT * FROM AreaResponsable";
			ResultSet resultSet = ca.obtener_resultset(base_datos, "AreaResponsable", consulta);		//obtenemos un resultset de la tabla "CausaConsecuenciaMCR"
				
			try {
				while( resultSet.next()) {
					Integer id = resultSet.getInt("IdAreaResponsable");
					String nombreResponsable = resultSet.getString("Nombre");
					
					Responsable responsable = new Responsable(id, nombreResponsable);
					responsables.put(id, responsable);
					
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}			
		}


		public boolean existeResponsable(Integer IdResponsable) {
			if( this.responsables.containsKey(IdResponsable))
				return true;
			else
				return false;
		}


		/**
		 * Permite obtener un ArrayList con todos los Ids de todos los responsables almacenados en este objeto
		 * @return un ArrayList con todos los Ids de todos los responsables almacenados
		 */
		public ArrayList<Integer> obtenerIds() {
			ArrayList<Integer> listaIds = new ArrayList<Integer>();
			Iterator<Integer> it = this.responsables.keySet().iterator();
			Integer idResponsable = 0;
			
			while( it.hasNext()) {
				idResponsable = it.next();
				
				listaIds.add(idResponsable);				
			}
			
			return listaIds;
		}
}
