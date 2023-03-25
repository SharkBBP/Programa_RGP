package Inicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Documentos {

	//private String ruta_entorno_trabajo;					//Ruta donde se va a trabajar y dónde se encuentran almacenados los archivos con los datos que se van a leer
	private String base_datos;								//nombre del fichero de la BD que se va a utilizar
	private HashMap<Integer,Documento> documentos = new HashMap<Integer, Documento>();	//HashMap que almacena como claves los Id de los documentos en la base de datos y como valores los objetos Documentos con todos los metadatos asociados a los mismos establecidos en esa tabla "Documentos"
	private ConectorAccess ca;							//Conector a la BD access que nos permite obtener los resultsets con la información con la que queramos trabajar.

	public Documentos (String ruta_entorno_trabajo, String archivo_bd, ConectorAccess ca) {
		//this.ruta_entorno_trabajo = ruta_entorno_trabajo;
		this.base_datos = archivo_bd;
		this.ca = ca;
	}
	
	/**
	 * Devuelve el objeto documento que corresponde al IdDocumento introducido como parámetro
	 * @param id	El Id del documento que queremos obtener
	 * @return		El objeto Documento que tiene como Id el identificador introducido como parámetro
	 */
	public Documento getDocumento(Integer id) {
		if( this.documentos.containsKey(id)) {
			return this.documentos.get(id);
		}
		else {
			System.out.println("No existe ningún responsable con el Id: " + id);
			return null;
		}
	}
	
	
	/**
	 * Lee la tabla "Documentos" de la Base de Datos y crea los distintos objetos "Documento" con la información asociada a cada uno de los registros de esta tabla
	 * Almacena cada objeto "Documento" en el HashMap correspondiente.
	 */
	public void leerDocumentos() {
		
//		ca = new ConectorAccess(ruta_entorno_trabajo);	
		String consulta = "SELECT * FROM Documentos";
		ResultSet resultSet = ca.obtener_resultset(base_datos, "Documentos", consulta);		//obtenemos un resultset de la tabla "CausaConsecuenciaMCR"
			
		try {
			while( resultSet.next()) {
				Integer IdDocumento = resultSet.getInt("IdDocumento");
				String Codigo = resultSet.getString("Codigo");
				String Titulo = resultSet.getString("Titulo");
				String Enlaces = resultSet.getString("Enlaces");
				String Version = resultSet.getString("Version");
				String Fecha = resultSet.getString("Fecha");
				String Tipo = resultSet.getString("Tipo");

				Documento documento = new Documento(IdDocumento, Codigo, Titulo,Enlaces,Version,Fecha,Tipo);
				
				this.documentos.put(IdDocumento, documento);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}			
	}

	public boolean existeDocumento(Integer IdDocumento) {
		if (this.documentos.containsKey(IdDocumento))
			return true;
		else 
			return false;
	}

	public ArrayList<Integer> obtenerIds() {
		ArrayList<Integer> listaIds = new ArrayList<Integer>();
		Iterator<Integer> it = this.documentos.keySet().iterator();
		Integer idDocumento = 0;
		
		while( it.hasNext()) {
			idDocumento = it.next();
			
			listaIds.add(idDocumento);				
		}
		
		return listaIds;	}

	
}
