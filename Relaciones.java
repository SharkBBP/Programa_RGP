package Inicio;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.HashMap;

public class Relaciones {
	
	private String ruta_entorno_trabajo;					//Ruta donde se va a trabajar y dónde se encuentran almacenados los archivos con los datos que se van a leer
	private String base_datos;								//nombre del fichero de la BD que se va a utilizar
	private String tabla = "CausaConsecuenciaMCR";			//Nombre de la tabla que almacena todas las relaciones existentes en la tabla "CausaConsecuenciaMCR", en la que se relacionan causas, consecuencias, MCR, y demás información relevante
	private Eventos eventos;								//Objeto que tiene almacenados todos los "eventos de seguridad" de la BD en un HashMap
	private MCRs mcrs;										//Objeto que tiene almacenados todas las "medidas de control del riesgo" de la BD en un HashMap
	private MCRS_procesadas mcrs_p;
	//private Responsables responsables;
	//private Documentos documentos;
	private ConectorAccess ca;								//Conector a la BD access que nos permite obtener los resultsets con la información con la que queramos trabajar.
	//private ResultSet rs_eventos;
	//private ResultSet rs_mcrs;
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 

	
	public Relaciones (String ruta_entorno_trabajo, String base_datos, Eventos eventos, MCRs mcrs, MCRS_procesadas mcrs_procesadas, ConectorAccess ca) {
		this.ruta_entorno_trabajo = ruta_entorno_trabajo;
		this.base_datos = base_datos;
		this.eventos = eventos;
		this.mcrs = mcrs;
		this.mcrs_p = mcrs_procesadas;
		//this.responsables = responsables;
		//this.documentos = documentos;
		this.ca = ca;
	}

	
	/**
	 * Clase que procesa la información almacenada en la tabla "CausaConsecuenciaMCR" y almacena la información de la misma en los objetos "eventos" y "mcrs"
	 * asociando, por ejemplo, las "mcr" que están asociadas a una determinada causa de un evento y/o a su consecuencia.
	 * @param imprimirSoloEventos 
	 */
	public void procesar_eventos(Boolean imprimirSoloEventos) {
		try {	
			//Integer numero_fila;			//Almacena el ID del registro que se está leyendo en cada momento en el Recordset
			Integer IdConsecuencia;			//Almacena el ID del evento consecuencia que se está leyendo de la tabla "CausaConsecuenciaMCR"
			Integer IdCausa;				//Almacena el ID del evento causa que se está leyendo de la tabla "CausaConsecuenciaMCR"
			String EstadoCausa;
			//Integer IdMCR;					//Almacena el ID de la medida de control del riesgo que se está leyendo de la tabla "CausaConsecuenciaMCR"
			//Integer IdMCRsiguiente;			//Almacena el ID de la medida de control del riesgo que existe a continuación de la actual, en la tabla "CausaConsecuenciaMCR", entendida como siguiente si consideramos que avanzamos hacia el accidente
			//String MCRInicialOFinal;		//Indica si la MCR es inicial (I), final (F) o única (U) entre la Causa y la consecuencia
			//Integer OrdenMCR;
			
			//String campos_busqueda = "IdConsecuencia, IdCausa"; 		//Almacena los campos que se van a buscar que se van a utilizar en la consulta para obtener el resultset
			
	//		ConectorAccess ca = new ConectorAccess(ruta_entorno_trabajo);
			//ResultSet resultSet = ca.obtener_resultset(base_datos, tabla, campos_busqueda, "IdConsecuenciaCausa");		//obtenemos un resultset de la tabla "CausaConsecuenciaMCR"

/*			String consulta = "SELECT IdConsecuencia, IdCausa"		//Consulta que se quiere hacer a la tabla "CausaConsecuenciaMCR", para representar posteriormente la información indicada en la misma en el fichero freemind
					+ " FROM CausaConsecuenciaMCR"
					//Solamente procesamos los registros que tienen todos los datos completos (Excepto 'Componente' y 'Elemento')
					+ " WHERE  CausaConsecuenciaMCR.IdConsecuenciaCausa IS NOT NULL AND CausaConsecuenciaMCR.IdConsecuencia IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdConsecuencia IS NOT NULL AND CausaConsecuenciaMCR.IdCausa IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdCausa IS NOT NULL AND CausaConsecuenciaMCR.IdMCR IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdMCR IS NOT NULL AND CausaConsecuenciaMCR.OrdenMCR IS NOT NULL AND CausaConsecuenciaMCR.SubsistemaMCR IS NOT NULL AND CausaConsecuenciaMCR.EstadoSubsistema IS NOT NULL AND CausaConsecuenciaMCR.Proceso IS NOT NULL AND CausaConsecuenciaMCR.EstadoProceso IS NOT NULL AND CausaConsecuenciaMCR.IdDocumento_de_requisitos IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdDocumento IS NOT NULL AND CausaConsecuenciaMCR.Articulos IS NOT NULL AND CausaConsecuenciaMCR.EstadoArticulos IS NOT NULL AND CausaConsecuenciaMCR.IdAreaResponsable IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdAreaResponsable IS NOT NULL";
*/
			
			//String consulta = "SELECT DISTINCT CausaConsecuenciaMCR.IdConsecuencia, Eventos.NombreEvento, CausaConsecuenciaMCR.IdCausa, Eventos_1.NombreEvento FROM Eventos AS Eventos_1 INNER JOIN (Eventos INNER JOIN CausaConsecuenciaMCR ON Eventos.IdEvento = CausaConsecuenciaMCR.IdConsecuencia) ON Eventos_1.IdEvento = CausaConsecuenciaMCR.IdCausa ORDER BY Eventos.NombreEvento, Eventos_1.NombreEvento";
			String consulta = "SELECT DISTINCT CausaConsecuenciaMCR.IdConsecuencia, Eventos.NombreEvento, CausaConsecuenciaMCR.IdCausa, Eventos_1.NombreEvento, CausaConsecuenciaMCR.EstadoIdCausa\r\n"
					+ "FROM Eventos AS Eventos_1 INNER JOIN (Eventos INNER JOIN CausaConsecuenciaMCR ON Eventos.IdEvento = CausaConsecuenciaMCR.IdConsecuencia) ON Eventos_1.IdEvento = CausaConsecuenciaMCR.IdCausa\r\n"
					+ "ORDER BY Eventos.NombreEvento, Eventos_1.NombreEvento";
			
			//System.out.println(consulta);
			
			ResultSet resultSet = ca.obtener_resultset(base_datos, tabla, consulta);
			
			
			
			//Recorremos el resultset para extraer la información del mismo hacia las distintas variables.
			while(resultSet.next()){
				
				//Convertimos en null los valores en blanco en la base de datos
		
				if (existe_valor( resultSet, "IdConsecuencia"))
					IdConsecuencia = resultSet.getInt("IdConsecuencia");
				else
					IdConsecuencia = null;
				
				if (existe_valor( resultSet, "IdCausa"))
					IdCausa = resultSet.getInt("IdCausa");
				else
					IdCausa = null;
				
				if (existe_valor( resultSet, "EstadoIdCausa"))
					EstadoCausa = resultSet.getString("EstadoIdCausa");
				else
					EstadoCausa = null;
				
/*				if (existe_valor( resultSet, "IdMCR"))
					IdMCR = resultSet.getInt("IdMCR");
				else
					IdMCR = null;
*/
				/*
				if (existe_valor( resultSet, "IdMCRsiguiente"))
					IdMCRsiguiente = resultSet.getInt("IdMCRsiguiente");
				else
					IdMCRsiguiente = null;
				
				if (existe_valor( resultSet, "MCRInicialOFinal"))
					MCRInicialOFinal = resultSet.getString("MCRInicialOFinal");
				else
					MCRInicialOFinal = null;

				if (existe_valor( resultSet, "OrdenMCR"))
					OrdenMCR = resultSet.getInt("OrdenMCR");
				else
					OrdenMCR = null;*/

				//System.out.println(numero_fila +" "+ IdConsecuencia +" "+ IdCausa  +" "+ IdMCR  +" "+ IdMCRsiguiente  +" "+ MCRInicialOFinal  +" "+ OrdenMCR);
				
				
				//Introducimos los eventos causa y consecuencia relacionados con cada evento
				if( IdConsecuencia != null) {
				
					if( IdCausa != null) {
						
						Evento evento_consecuencia = eventos.getEvento(IdConsecuencia);
						Evento evento_causa = eventos.getEvento(IdCausa);
						
						evento_causa.insertarEstado(IdConsecuencia, EstadoCausa);		//Introducimos el estado del evento causa, que depende de la consecuencia con la cual esté relacionado
						
						evento_consecuencia.introduce_evento_causa(IdCausa, evento_causa);
						evento_causa.introduce_evento_consecuencia(IdConsecuencia, evento_consecuencia);
					}
					else {
						//System.out.println( "Error en tabla: CausaConsecuenciaMCR \t fila: " + numero_fila + "- No existe Causa");		
					}
				}
				else {
					//System.out.println( "Error en tabla: CausaConsecuenciaMCR \t fila: " + numero_fila + "- No existe Consecuencia");
				}
				
				// Procesamos las mcrs que se encuentran entre la causa y la consecuencia
				if( !imprimirSoloEventos && IdCausa != null && IdConsecuencia != null ) {
					procesar_mcrs(IdCausa, IdConsecuencia);
				}
	       }
			
			resultSet.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Carga las MCRs en los eventos Causa y Consecuencia indicados como parámetros, preparados para que se puedan imprimir en el orden correcto
	 * en un fichero FreeMind
	 * @param IdCausa	Id del evento causante sobre el cual se quiere obtener el listado de MCRs
	 * @param IdConsecuencia Ide del evento consecuencia sobre el cual se quiere obtener el listado de MCRs
	 */
	private void procesar_mcrs(Integer IdCausa, Integer IdConsecuencia) {
		Integer IdMCR = 0;

		ArrayList<MCR> lista_mcrs_preventivas = new ArrayList<MCR>();
		ArrayList<MCR> lista_mcrs_reactivas = new ArrayList<MCR>();
		
		ArrayList<MCR_procesada> mcrs_procesadas_preventivas = new ArrayList<MCR_procesada>();
		ArrayList<MCR_procesada> mcrs_procesadas_reactivas = new ArrayList<MCR_procesada>();

		
		try {
			
			//Procesamos las mcrs reactivas asociadas al evento causa cuando se relaciona con el evento consecuencia
			String consulta1 = "SELECT DISTINCT CausaConsecuenciaMCR.IdConsecuencia, CausaConsecuenciaMCR.IdCausa ,CausaConsecuenciaMCR.IdMCR, CausaConsecuenciaMCR.OrdenMCR "
					+ "FROM CausaConsecuenciaMCR "
					+ "WHERE (((CausaConsecuenciaMCR.IdConsecuencia)=" + IdConsecuencia + ") AND ((CausaConsecuenciaMCR.IdCausa)=" + IdCausa + ") "
					+ "AND CausaConsecuenciaMCR.IdMCR IS NOT NULL AND CausaConsecuenciaMCR.IdConsecuencia IS NOT NULL AND CausaConsecuenciaMCR.IdCausa IS NOT NULL) "
					+ "ORDER BY CausaConsecuenciaMCR.OrdenMCR ";
			ConectorAccess ca = new ConectorAccess(ruta_entorno_trabajo);
			ResultSet resultSet = ca.obtener_resultset(base_datos, tabla, consulta1);
			
			while(resultSet.next()){
				
				if (existe_valor( resultSet, "IdMCR"))
					IdMCR = resultSet.getInt("IdMCR");
				else
					IdMCR = null;
				
				if( IdMCR != null) {
					lista_mcrs_reactivas.add(mcrs.getMCR(IdMCR));
					
					String idMCR_largo = resultSet.getInt("IdConsecuencia") + "-" + resultSet.getInt("IdCausa") + "-" + resultSet.getInt("IdMCR");
					
					if ( this.mcrs_p.getMCR_procesada(idMCR_largo) != null ) {
						mcrs_procesadas_reactivas.add(this.mcrs_p.getMCR_procesada(idMCR_largo));
					}
				}
			}
			
			if (lista_mcrs_reactivas.size() > 0 ) {
				eventos.getEvento(IdCausa).introduce_mcrs_reactivas(IdConsecuencia, lista_mcrs_reactivas );
				eventos.getEvento(IdCausa).introduce_mcrs_procesadas_reactivas(IdConsecuencia, mcrs_procesadas_reactivas );
			}
			
			//Procesamos las mcrs preventivas asociadas al evento consecuencia cuando se relaciona con el evento causa
			String consulta2 = "SELECT DISTINCT CausaConsecuenciaMCR.IdConsecuencia, CausaConsecuenciaMCR.IdCausa, CausaConsecuenciaMCR.IdMCR, CausaConsecuenciaMCR.OrdenMCR "
					+ "FROM CausaConsecuenciaMCR "
					+ "WHERE (((CausaConsecuenciaMCR.IdConsecuencia)=" + IdConsecuencia + ") AND ((CausaConsecuenciaMCR.IdCausa)=" + IdCausa + ") "
					+ "AND CausaConsecuenciaMCR.IdMCR IS NOT NULL) "
					+ "ORDER BY CausaConsecuenciaMCR.OrdenMCR DESC";
			resultSet = ca.obtener_resultset(base_datos, tabla, consulta2);
			
			while(resultSet.next()){
				
				if (existe_valor( resultSet, "IdMCR"))
					IdMCR = resultSet.getInt("IdMCR");
				else
					IdMCR = null;
				
				if( IdMCR != null) {
					lista_mcrs_preventivas.add(mcrs.getMCR(IdMCR));		
					
					String idMCR_largo = resultSet.getInt("IdConsecuencia") + "-" + resultSet.getInt("IdCausa") + "-" + resultSet.getInt("IdMCR");
					
					if ( this.mcrs_p.getMCR_procesada(idMCR_largo) != null ) {
						mcrs_procesadas_preventivas.add(this.mcrs_p.getMCR_procesada(idMCR_largo));
					}
				}
			}
			
			if (lista_mcrs_preventivas.size() > 0 ) {
				eventos.getEvento(IdConsecuencia).introduce_mcrs_preventivas(IdCausa, lista_mcrs_preventivas );
				eventos.getEvento(IdConsecuencia).introduce_mcrs_procesadas_preventivas(IdCausa, mcrs_procesadas_preventivas);

			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}


	/**
	 * Permite comprobar si el valor almacenado en el campo de un registro de la tabla contiene algún valor de la tabla o es un null
	 * @param resultSet Registro de la tabla que se va a comprobar
	 * @param campo	Campo del registro sobre el que se va a hacer la comprobación
	 * @return True si el campo almacena algún dato, False si está vacío (null)
	 * @throws SQLException
	 */
	public boolean existe_valor(ResultSet resultSet, String campo) throws SQLException {
		if ( resultSet.getString(campo) != null)
			return true;
		else
			return false;
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
