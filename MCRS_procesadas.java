package Inicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MCRS_procesadas {

	//private String ruta_entorno_trabajo;					//Ruta donde se va a trabajar y dónde se encuentran almacenados los archivos con los datos que se van a leer
	private String base_datos;								//nombre del fichero de la BD que se va a utilizar
	//private Eventos eventos;								//Objeto que tiene almacenados todos los "eventos de seguridad" de la BD en un HashMap
	private MCRs mcrs;										//Objeto que tiene almacenados todas las "medidas de control del riesgo" de la BD en un HashMap
	private ConectorAccess ca;								//Conector a la BD access que nos permite obtener los resultsets con la información con la que queramos trabajar.
	private String tabla;									//Tabla que contiene la información de las MCR, sobe la cual se va a realizar la consulta
	private Responsables responsables;						//Objeto que contiene toda la información de la tabla Responsables
	private Documentos documentos;							//Objeto que contiene toda la información de la tabla Documentos
	//private ResultSet rs_eventos;
	//private ResultSet rs_mcrs;
	//private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 
	
	private HashMap<String, MCR_procesada> mcrs_procesadas = new HashMap<String, MCR_procesada>();		//Creamos un HashMap para almacenar las MCR procesadas, con clave Id largo = IdConsecuencia-IdCausa-IdMCR y valor la propia mcr_procesada

	
	public MCRS_procesadas(String ruta_entorno_trabajo, String archivo_bd, Eventos eventos, MCRs mcrs, ConectorAccess ca, Responsables responsables, Documentos documentos) {
		//this.ruta_entorno_trabajo = ruta_entorno_trabajo;
		this.base_datos = archivo_bd;
		//this.eventos = eventos;
		this.mcrs = mcrs;
		this.responsables = responsables;
		this.documentos = documentos;
		this.tabla = "CausaConsecuenciaMCR";
		this.ca = ca;	
	}

	public void procesarConsulta(String consulta) {

		Integer numero_fila;			//Almacena el ID del registro que se está leyendo en cada momento en el Recordset
		Integer IdConsecuencia;			//Almacena el ID del evento consecuencia que se está leyendo de la tabla "CausaConsecuenciaMCR"
		String EstadoIdConsecuencia;
		Integer IdCausa;				//Almacena el ID del evento causa que se está leyendo de la tabla "CausaConsecuenciaMCR"
		String EstadoIdCausa;
		Integer IdMCR;					//Almacena el ID de la medida de control del riesgo que se está leyendo de la tabla "CausaConsecuenciaMCR"
		String EstadoIdMCR;
		Integer OrdenMCR;
		String SubsistemaMCR;
		String EstadoSubsistema;
		String Componente;
		String Elemento;
		String Proceso;
		String EstadoProceso;
		Integer IdDocumento_de_requisitos;
		String EstadoIdDocumento;
		String Articulos;
		String EstadoArticulos;
		Integer IdAreaResponsable;
		String EstadoIdAreaResponsable;
		Integer IdMCRsiguiente;			//Almacena el ID de la medida de control del riesgo que existe a continuación de la actual, en la tabla "CausaConsecuenciaMCR", entendida como siguiente si consideramos que avanzamos hacia el accidente
		String MCRInicialOFinal;		//Indica si la MCR es inicial (I), final (F) o única (U) entre la Causa y la consecuencia


		try {
			
			ResultSet resultSet = ca.obtener_resultset(base_datos, tabla, consulta);		//obtenemos un resultset de la tabla "CausaConsecuenciaMCR"
			
//			System.out.println(consulta);
			
			while(resultSet.next()){
				
				numero_fila = resultSet.getInt("IdConsecuenciaCausa");
				IdConsecuencia = resultSet.getInt("IdConsecuencia");
				EstadoIdConsecuencia = resultSet.getString("EstadoIdConsecuencia");
				IdCausa = resultSet.getInt("IdCausa");
				EstadoIdCausa = resultSet.getString("EstadoIdCausa");
				IdMCR = resultSet.getInt("IdMCR");
				EstadoIdMCR = resultSet.getString("EstadoIdMCR");
				OrdenMCR = resultSet.getInt("OrdenMCR");
				SubsistemaMCR = resultSet.getString("SubsistemaMCR");
				EstadoSubsistema = resultSet.getString("EstadoSubsistema");
				//Componente = resultSet.getString("Componente");
				//Elemento = resultSet.getString("Elemento");
				Proceso = resultSet.getString("Proceso");
				EstadoProceso = resultSet.getString("EstadoProceso");
				IdDocumento_de_requisitos = resultSet.getInt("IdDocumento_de_requisitos");
				EstadoIdDocumento = resultSet.getString("EstadoIdDocumento");
				Articulos = resultSet.getString("Articulos");
				EstadoArticulos = resultSet.getString("EstadoArticulos");
				IdAreaResponsable = resultSet.getInt("IdAreaResponsable");
				EstadoIdAreaResponsable = resultSet.getString("EstadoIdAreaResponsable");
				//IdMCRsiguiente = resultSet.getInt("IdMCRsiguiente");
				//MCRInicialOFinal = resultSet.getString("MCRInicialOFinal");

				//Cargamos los campos que pueden ser nulos como "null"
				if (existe_valor( resultSet, "Componente"))
					Componente = resultSet.getString("Componente");
				else
					Componente = null;
				
				if (existe_valor( resultSet, "Elemento"))
					Elemento = resultSet.getString("Elemento");
				else
					Elemento = null;
				
				if (existe_valor( resultSet, "IdMCRsiguiente"))
					IdMCRsiguiente = resultSet.getInt("IdMCRsiguiente");
				else
					IdMCRsiguiente = null;
				
				if (existe_valor( resultSet, "MCRInicialOFinal"))
					MCRInicialOFinal = resultSet.getString("MCRInicialOFinal");
				else
					MCRInicialOFinal = null;
				
				RegistroCCM r = new RegistroCCM(numero_fila, IdConsecuencia,EstadoIdConsecuencia, IdCausa,EstadoIdCausa, IdMCR, EstadoIdMCR, OrdenMCR, SubsistemaMCR, EstadoSubsistema
						, Proceso, EstadoProceso, IdDocumento_de_requisitos, EstadoIdDocumento, Articulos, EstadoArticulos, IdAreaResponsable, EstadoIdAreaResponsable
						, Componente, Elemento, IdMCRsiguiente, MCRInicialOFinal);
				
				//COMENZAMOS A PROCESAR LOS DATOS OBTENIDOS

				//Introducimos las Mcr procesadas en el hashmap				
				introducirMcrProcesada( r );
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	
	private void introducirMcrProcesada(RegistroCCM r) {
		String IdMCR_procesada = r.getIdConsecuencia() + "-" + r.getIdCausa() + "-" + r.getIdMCR();
		//System.out.println(IdMCR_procesada);
		
		if( this.mcrs_procesadas.containsKey(IdMCR_procesada)) {
			MCR_procesada mcr_p = mcrs_procesadas.get(IdMCR_procesada);
			mcr_p.incorporarSubsistema(r, responsables, documentos);
		}
		else {
			MCR mcr = mcrs.getMCR(r.getIdMCR());
			MCR_procesada mcr_p = new MCR_procesada( IdMCR_procesada, mcr, r );
			this.mcrs_procesadas.put(IdMCR_procesada, mcr_p);
			mcr_p.incorporarSubsistema(r, responsables, documentos);

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
	 * Devuelve el objeto con la MCR procesada que tiene el id que se le pasa como parámetro
	 * @param idMCR_largo	El Id largo de la MCR que se quiere obtener
	 * @return la MCR procesada que tiene el Id largo que se ha pasado como parámetro
	 */
	public MCR_procesada getMCR_procesada(String idMCR_largo) {
		return this.mcrs_procesadas.get(idMCR_largo);
	}

}
