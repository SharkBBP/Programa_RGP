package Inicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CausaConsecuenciaMCR {

	
	//private String ruta_entorno_trabajo;					//Ruta donde se va a trabajar y dónde se encuentran almacenados los archivos con los datos que se van a leer
	private String base_datos;								//nombre del fichero de la BD que se va a utilizar
	private String tabla = "CausaConsecuenciaMCR";			//Nombre de la tabla que almacena todas las relaciones existentes en la tabla "CausaConsecuenciaMCR", en la que se relacionan causas, consecuencias, MCR, y demás información relevante
	//private Eventos eventos;								//Objeto que tiene almacenados todos los "eventos de seguridad" de la BD en un HashMap
	//private MCRs mcrs;									//Objeto que tiene almacenados todas las "medidas de control del riesgo" de la BD en un HashMap
	private ConectorAccess ca;								//Conector a la BD access que nos permite obtener los resultsets con la información con la que queramos trabajar.
	//private ResultSet rs_eventos;
	//private ResultSet rs_mcrs;
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 
	private Boolean matrizCompatibilidad [][][];

	
	public CausaConsecuenciaMCR (String ruta_entorno_trabajo, String base_datos, Eventos eventos, MCRs mcrs, ConectorAccess ca) {
		//this.ruta_entorno_trabajo = ruta_entorno_trabajo;
		this.base_datos = base_datos;
		//this.eventos = eventos;
		//this.mcrs = mcrs;
		this.ca = ca;
		this.matrizCompatibilidad = leerMatrizCompatibilidad();		//Leemos la tabla "CompatibilidadSubsistemaProcesoResponsable" y la almacenamos en una matriz de 3 dimensiones: Área Responsable, Proceso, Subsistema
	}
	
	/**
	 * Invoca a distintos métodos de esta clase para que se realicen las comprobaciones sobre los datos de entrada utilizados en la consulta.
	 * Si los datos de entrada son erróneos se registran los errores detectados en el fichero log de errores que se genera al ejecutar el programa
	 */
	public void realizarComprobaciones() {
		this.comprobarCausas();
		this.comprobarOrdenMCR();
		this.comprobarRepeticionOrdenMCR();
		this.comprobarCompatibilidadSubsistemaProcesoResponsable();
		//this.pruebaMatriz();				//Descomentar si se quiere imprimir en consola las compatibilidades existentes entre Áreas responsables, Subsistemas y Procesos
	}
	
	/**
	 * Muestra en pantalla las compatibilidades que están almacenadas en la "matrizCompatibilidad", en la que se almacenan con True los casos en los que 
	 * es compatible un Área Responsable con un determinado Subsistema y un determinado Proceso.
	 */
	@SuppressWarnings("unused")
	private void pruebaMatriz() {
		/*if(compatible(1 , 7, 5)) {
			System.out.println("1 , 7, 5" );
		}
		if(compatible(2 , 6, 4)) {
			System.out.println("2 , 6, 4" );
		}
		if(compatible(7 ,1 ,1 )) {
			System.out.println("7 ,1 ,1" );
		}
		if(compatible(15 ,5 ,2 )) {
			System.out.println("15 ,5 ,2" );
		}
		if(compatible( 38, 2, 2)) {
			System.out.println("38, 2, 2" );
		}
		if(compatible(47 ,3 ,1 )) {
			System.out.println("47 ,3 ,1" );
		}*/
		
		for(int k = 1; k < 56; k++) {
			for(int i = 1; i < 9 ; i++) {
				for( int j = 1; j < 6; j++ ) {
					if( matrizCompatibilidad[i][j][k]) {
						System.out.println(" - Area responsable: " + this.obtenerAreaResponsable(k) + "\t - Subsistema: " + this.obtenerSubsistema(i) + "\t - Proceso: " + this.obtenerProceso(j) );
					}
				}
			}
		}
	}

	/**
	 * Realiza un barrido por la tabla "CausaConsecuenciaMCR" para comprobar imprimir los errores existentes cuando existe algún tipo de incompatibilidad entre
	 * el Área Responsable, el Subsistema y el Proceso almacenados. El resultado de ejecutar este método se imprime en el informe de errores
	 */
	private void comprobarCompatibilidadSubsistemaProcesoResponsable() {
		
		Integer fila = -1;
		Integer id_subsistema = -1;
		Integer id_proceso = -1;
		Integer id_area_responsable = -1; 
		String area_responsable = null;
		String proceso = null;
		String subsistema = null;
		Integer contador = 0;
		
		ResultSet resultSet = ca.obtener_resultset(base_datos, tabla, "SELECT CausaConsecuenciaMCR.IdConsecuenciaCausa, CausaConsecuenciaMCR.IdAreaResponsable, CausaConsecuenciaMCR.SubsistemaMCR, CausaConsecuenciaMCR.Proceso\r\n"
				+ "FROM CausaConsecuenciaMCR WHERE CausaConsecuenciaMCR.SubsistemaMCR IS NOT NULL AND CausaConsecuenciaMCR.Proceso IS NOT NULL AND CausaConsecuenciaMCR.IdAreaResponsable IS NOT NULL\r\n"
				+ "ORDER BY IdConsecuenciaCausa");		//obtenemos un resultset de la tabla "CausaConsecuenciaMCR"
		
		try {
			
			this.imprimirEncabezadoErrores("COMPROBACIÓN COMPATIBILIDAD SUBSISTEMAS-PROCESOS-RESPONSABLES");

			
			while(resultSet.next()  ) {
				fila = resultSet.getInt("IdConsecuenciaCausa");
				//if ( fila ==  899 )
					//System.out.println(fila );
				id_area_responsable = resultSet.getInt("IdAreaResponsable");
				area_responsable = obtenerAreaResponsable(id_area_responsable);
				subsistema = resultSet.getString("SubsistemaMCR");
				id_subsistema = obtenerIdSubsistema( subsistema);
				proceso = resultSet.getString("Proceso");
				id_proceso = obtenerIdProceso(proceso );

				/*if( fila == 2737 || fila == 1062) {
					System.out.println(fila  + "   " + contador);
				}
				if(fila == 899 || id_area_responsable == -1 || id_subsistema == -1 || id_proceso == -1 )
					System.out.println(fila + "-" + id_area_responsable +"-" + id_subsistema + "-" + id_proceso)	;	*/	
				
				if( !compatible(id_area_responsable, id_subsistema, id_proceso)) {
					contador++;
					this.escribirError("La fila: "+ fila + " de la tabla CausaConsecuenciaMCR tiene una incompatibilidad entre "
							+ "el Area Responsable: " + area_responsable + " - Subsistema: " + subsistema + " - Proceso: " + proceso );
				}
				//System.out.println(fila +" - "+ contador + " - Area Responsable: " +id_area_responsable + "-"+ area_responsable + " - Subsistema: " + subsistema + " - Proceso: " + proceso);
			}
			resultSet.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Indica si existe compatibilidad entre el Área Responsable, el Subsistema y el Proceso que se le pasa como parámetro al método. 
	 * @param area_responsable	Id del área responsable, tal y como se encuentran almacenados en la tabla "CompatibilidadSubsistemaProcesoResponsable"
	 * @param subsistema Id del Subsistema, tal y como se encuentra almacenado, de acuerdo con los índices indicados en la tabla "Subsistemas"
	 * @param proceso Id del Proceso, tal y como se encuentra almacenado, de acuerdo con los índices indicados en la tabla "Procesos"
	 * @return True si son compatibles el Área Responsable, el Proceso y el Subsistema introducidos como parámetros
	 */
	private boolean compatible(Integer area_responsable, Integer subsistema, Integer proceso) {
		return matrizCompatibilidad[subsistema][proceso][area_responsable];
	}

	/**
	 * Permite obtener el "Id" asignado al proceso que se le pasa como parámetro. Este Id se utilizará como índice para buscar el valor en la matriz de compatibilidad
	 * @param proceso	El proceso del cual queremos conocer su Id 
	 * @return	El Id del proceso introducido como parámetro
	 */
	private Integer obtenerIdProceso(String proceso) {
		
		Integer p = -1;
		
		switch ( proceso ) {
			
		case "Diseño":
		case "1":
			p = 1;
			break;
		case "Construcción":
		case "2":
			p = 2;
			break;
		case "Puesta en Servicio":
		case "3":
			p = 3;
			break;
		case "Mantenimiento":
		case "4":
			p = 4;
			break;
		case "Explotación y gestión del tráfico":
		case "5":
			p = 5;
			break;
		default:
			System.out.println("Error en tabla CausaConsecuenciaMCR. Se ha incluido el proceso: " + proceso + " y no está entre los permitidos.");
			break;
		}
	
		return p;		
	}
	
	
	/**
	 * Permite obtener el nombre asignado al proceso cuyo Id se le pasa como parámetro. 
	 * @param id	El id proceso del cual queremos conocer el nombre 
	 * @return	El Nombre del proceso cuyo Id hemos introducido como parámetro
	 */
	private String obtenerProceso(Integer id) {
		String p = null;
		
		switch ( id ) {
		
		case 1:
			p = "Diseño";
			break;
		case 2:
			p = "Construcción";
			break;
		case 3:
			p = "Puesta en Servicio";
			break;
		case 4:
			p = "Mantenimiento";
			break;
		case 5:
			p = "Explotación y gestión del tráfico";
			break;
		default:
			System.out.println("Error en tabla CausaConsecuenciaMCR. Se ha incluido el id_proceso: " + id + " y no está entre los permitidos.");
			break;
		}
		
		return p;
		
	}

	
	/**
	 * Permite obtener el "Id" asignado al subsistema que se le pasa como parámetro. Este Id se utilizará como índice para buscar el valor en la matriz de compatibilidad
	 * @param proceso	El subsistema del cual queremos conocer su Id 
	 * @return	El Id del subsistema introducido como parámetro
	 */
	private Integer obtenerIdSubsistema(String subsistema) {
		Integer s = -1;
		
		switch ( subsistema ) {
			case "Infraestructura":
			case "1":
				s = 1;
				break;
			case "Energía":
			case "2":
				s = 2;
				break;				
			case "Control, Mando y Señalización en tierra":
			case "3":
				s = 3;
				break;				
			case "Control, Mando y Señalización a bordo":
			case "4":
				s = 4;
				break;				
			case "Material Rodante":
			case "5":
				s = 5;
				break;				
			case "Mantenimiento":
			case "6":
				s = 6;
				break;				
			case "Explotación y gestión del tráfico":
			case "7":
				s = 7;
				break;				
			case "Estaciones y Servicios Logísticos":
			case "8":
				s = 8;
				break;
			default:
				System.out.println("Error en tabla CausaConsecuenciaMCR. Se ha incluido el subsistema: " + subsistema + " y no está entre los permitidos.");
				break;
		}
		
		return s;
	}

	
	/**
	 * Permite obtener el nombre asignado al subsistema cuyo Id se le pasa como parámetro. 
	 * @param id	El id subsistema del cual queremos conocer el nombre 
	 * @return	El Nombre del subsistema cuyo Id hemos introducido como parámetro
	 */
	private String obtenerSubsistema(Integer id) {
		
		String s = null;
		
		switch ( id ) {
			case 1:
				s = "Infraestructura";
				break;
			case 2:
				s = "Energía";
				break;				
			case 3:
				s = "Control, Mando y Señalización en tierra";
				break;				
			case 4:
				s = "Control, Mando y Señalización a bordo";
				break;				
			case 5:
				s = "Material Rodante";
				break;				
			case 6:
				s = "Mantenimiento";
				break;				
			case 7:
				s = "Explotación y gestión del tráfico";
				break;				
			case 8:
				s = "Estaciones y Servicios Logísticos";
				break;
			default:
				System.out.println("Error en tabla CausaConsecuenciaMCR. Se ha incluido el id_subsistema: " + id + " y no está entre los permitidos.");
				break;
		}
		
		return s;
		
	}
	
	
	/**
	 * Permite obtener el nombre asignado al area responsable que se le pasa como parámetro. 
	 * @param id	El id del area responsable de la cual queremos conocer el nombre 
	 * @return	El Nombre del area responsable cuyo Id hemos introducido como parámetro
	 */
	private String obtenerAreaResponsable( Integer id) {
		
		String area_responsable = null;
		
		try {
			
			String consulta = "SELECT DISTINCT AreaResponsable.Nombre\r\n"
					+ "FROM AreaResponsable\r\n"
					+ "WHERE AreaResponsable.IdAreaResponsable = " + id;

			ResultSet resultSet = ca.obtener_resultset(base_datos, tabla, consulta);
			
			while ( resultSet.next() )
				area_responsable =  resultSet.getString("Nombre");
			
			resultSet.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return area_responsable;

	}
	
	/**
	 * Permite obtener una matriz en la que se almacenan True si el área responsable, el Subsistema y el Proceso cuyos "Ids" se han introducido como índices de la matriz son compatibles
	 * entre sí
	 * @return Una matriz de booleanos, en la que los elementos almacenados permiten saber si los el área responsable, el Subsistema y el Proceso cuyos "Ids" se han introducido como índices de la matriz son compatibles
	 * entre sí
	 */
	private Boolean[][][] leerMatrizCompatibilidad() {

		//Integer fila;
		
		Integer numeroAreasResponsables = 55+1;		//Este número hay que incrementarlo manualmente si aumenta el número de Áreas Responsables. Hay que sumar un 1 porque no vamos a usar el índice 0 
		Integer numeroProcesos = 5+1;					//Este número hay que incrementarlo manualmente si aumenta el número de Procesos. Hay que sumar un 1 porque no vamos a usar el índice 0
		Integer numeroSubsistemas = 8+1;				//Este número hay que incrementarlo manualmente si aumenta el número de Subsistemas. Hay que sumar un 1 porque no vamos a usar el índice 0
		
		Boolean matrizCompatibilidad[][][] = new Boolean[numeroSubsistemas][numeroProcesos][numeroAreasResponsables] ;
		
		Integer areaResponsable = -1;			//Variable que almacena el índice que corresponde a la dimensión de la matriz relacionada con  el Id del Área Responsable
		
		
		Boolean proceso [] = new Boolean[numeroProcesos] ;				//Variable que almacena el índice que corresponde a la dimensión de la matriz relacionada con  el tipo de proceso relacionado. 
																		//Los índices asignados se indican a continuación
		Integer proc_disenno = 1;
		Integer proc_construccion = 2;
		Integer proc_puesta_en_servicio = 3; 							//En la tabla no se ha metido el proceso de puestas en servicio, pero si se incluye hay que cargar las compatibilidades en la tabla de la base de datos
		Integer proc_mantenimiento = 4;
		Integer proc_explotacion = 5;


		
		
		Boolean subsistema [] = new Boolean[numeroSubsistemas] ;		//Variable que almacena el índice que corresponde a la dimensión de la matriz relacionada con el tipo de subsistema relacionado.
																		//Los índices asignados se indican a continuación.
		Integer subs_infraestructura = 1;
		Integer subs_energia = 2;
		Integer subs_cms_tierra = 3;
		Integer subs_cms_a_bordo = 4;
		Integer subs_material_rodante = 5;
		Integer subs_mantenimiento = 6;
		Integer subs_explotacion_trafico = 7;
		Integer subs_estaciones_y_ssll = 8;
		
		//Boolean es_compatible = false;
		
		//Inicializamos la matriz todo a false
		//for( int i = j = k = 0; i<);
						
		ResultSet rs = ca.obtener_resultset(base_datos, "CompatibilidadSubsistemaProcesoResponsable", "SELECT * FROM CompatibilidadSubsistemaProcesoResponsable");		//obtenemos un resultset de la tabla "CausaConsecuenciaMCR"
		

		try {	
			
			while(rs.next()) {
				areaResponsable = rs.getInt("IdAreaResponsable");
				
				//fila = rs.getRow();			// Es la fila de la tabla "CompatibilidadSubsistemaProcesoResponsable" que se está leyendo
				
				proceso [proc_disenno] = this.obtenerCompatibilidad(rs.getString("proc_disenno"));
				proceso [proc_construccion] = this.obtenerCompatibilidad(rs.getString("proc_construccion"));
				//proceso [proc_puesta_en_servicio] = false; 			//Cuando se hayan cargado los datos en la BD eliminar esta fila y descomentar la fila inferior
				proceso [proc_puesta_en_servicio] = this.obtenerCompatibilidad(rs.getString("proc_puesta_en_servicio"));
				proceso [proc_explotacion] = this.obtenerCompatibilidad(rs.getString("proc_explotacion"));
				proceso [proc_mantenimiento] = this.obtenerCompatibilidad(rs.getString("proc_mantenimiento"));

				subsistema [subs_infraestructura] = this.obtenerCompatibilidad(rs.getString("subs_infraestructura"));
				subsistema [subs_energia] = this.obtenerCompatibilidad(rs.getString("subs_energia"));
				subsistema [subs_cms_tierra] = this.obtenerCompatibilidad(rs.getString("subs_cms_tierra"));
				subsistema [subs_cms_a_bordo] = this.obtenerCompatibilidad(rs.getString("subs_cms_a_bordo"));
				subsistema [subs_material_rodante] = this.obtenerCompatibilidad(rs.getString("subs_material_rodante"));
				subsistema [subs_mantenimiento] = this.obtenerCompatibilidad(rs.getString("subs_mantenimiento"));
				subsistema [subs_explotacion_trafico] = this.obtenerCompatibilidad(rs.getString("subs_explotacion_trafico"));
				subsistema [subs_estaciones_y_ssll] = this.obtenerCompatibilidad(rs.getString("subs_estaciones_y_ssll"));
				
				//Integer indice_proceso = proceso.length;
				//Integer indice_subsistema = subsistema.length;
				
				
				for( int i_p = 1; i_p < numeroProcesos; i_p++) {
					for( int i_s = 1; i_s < numeroSubsistemas; i_s++) {
						//System.out.println("i_p: " + i_p + "   \ti_s: " + i_s);
						//if( proceso [i_p] == true && subsistema [i_s] == true)
							//System.out.println("i_p: " + i_p + "   \ti_s: " + i_s);

						matrizCompatibilidad[i_s][i_p][areaResponsable] = proceso [i_p] && subsistema [i_s];
					}
				}
				
				
	/*			if( !this.existe_valor(resultSet, "IdCausa")) {
					this.escribirError("Error Tabla: " + "CompatibilidadSubsistemaProcesoResponsable" + " Fila: " + fila + " -- Existe una incompatibilidad");
				} */
			}
			rs.close();
			
			
			//return matrizCompatibilidad;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return matrizCompatibilidad;
	}
		
		
	
	/**
	 * Permite obtener booleanos que devuelven True si el valor almacenado en la tabla "CompatibilidadSubsistemaProcesoResponsable" tiene almacenado el valor "COMPATIBLE"
	 * y False en caso contrario
	 * @param string La cadena de texto que se quiere comprobar
	 * @return true si la cadena que se pasa como parámetro es: "COMPATIBLE"
	 */
	private Boolean obtenerCompatibilidad(String string) {

		if ( string.compareTo("COMPATIBLE") == 0) {
			return true;
		}
		else 
		{
			return false;
		}
	
	}

	/**
	 * Comprueba que el campo "Causa" no esté vacío, para que se puedan asociar las MCR correctamente
	 */
	public void comprobarCausas() {
		
		Integer fila = 0;
		
		ResultSet resultSet = ca.obtener_resultset(base_datos, tabla, "SELECT CausaConsecuenciaMCR.IdConsecuenciaCausa, CausaConsecuenciaMCR.IdConsecuencia, CausaConsecuenciaMCR.IdCausa\r\n"
				+ "FROM CausaConsecuenciaMCR");		//obtenemos un resultset de la tabla "CausaConsecuenciaMCR"
		
		this.imprimirEncabezadoErrores("COMPROBACIÓN CAUSAS NO NULAS");
		
		try {
			
			while(resultSet.next()) {
				fila = resultSet.getInt("IdConsecuenciaCausa");
				if( !this.existe_valor(resultSet, "IdCausa")) {
					this.escribirError("Error Tabla: " + tabla + " Fila: " + fila + " -- No existe definida ninguna causa");
				}
			}
			resultSet.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Comprueba cuáles son las MCR a las que no se ha asignado ningún OrdenMCR
	 */
	public void comprobarOrdenMCR() {
		
		Integer fila = 0;
		String IdMCR;
		//String IdConsecuencia;
		//String IdCausa ;
		//String IdMCR_largo; 
		
		ResultSet resultSet = ca.obtener_resultset(base_datos, tabla,"SELECT CausaConsecuenciaMCR.IdConsecuenciaCausa, CausaConsecuenciaMCR.IdConsecuencia, CausaConsecuenciaMCR.IdCausa, CausaConsecuenciaMCR.IdMCR, CausaConsecuenciaMCR.OrdenMCR\r\n"
				+ "FROM CausaConsecuenciaMCR\r\n"
				+ "WHERE CausaConsecuenciaMCR.OrdenMCR IS NULL AND CausaConsecuenciaMCR.IdMCR IS NOT NULL\r\n"
				+ "ORDER BY  CausaConsecuenciaMCR.IdConsecuenciaCausa\r\n"
				+ ";\r\n"
				+ "");
		
		this.imprimirEncabezadoErrores("Comprobación de MCRs que no tienen asignado un OrdenMCR");
		
		//Comprobamos las MCRs no nulas que no tienen asignado un OrdenMCR
		try {
			while(resultSet.next()) {
				fila = resultSet.getInt("IdConsecuenciaCausa");
				IdMCR = resultSet.getString("IdMCR");
				if( !this.existe_valor(resultSet, "IdCausa")) {
					this.escribirError("Error Tabla: " + tabla + "\t Fila: " + fila + "\tIdMCR: " + IdMCR + "\t -- No se ha definido el 'OrdenMCR' para esta MCR");
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Comprueba si en la tabla CausaConsecuenciaMCR se ha repetido el orden de alguna MCR en dos o mas MCR distintas
	 */
	public void comprobarRepeticionOrdenMCR() {
		
		Integer fila = 0;
		Integer IdMCR;
		Integer IdConsecuencia;
		Integer IdCausa ;
		String IdMCR_largo; 
		Integer OrdenMCR;
		HashMap<String, HashMap<Integer, ArrayList<Integer>>> listadoMCRs = new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();		//El primer HM almacena como clave el ID largo de la MCR (con el IDConsecuencia-IdCausa-IdMCR), el 2º HM tiene como clave el 'OrdenMCR' de la MCR que estamos analizando, el ArrayList, son las filas en las que aparece ese número de orden para la MCR
		
		ResultSet resultSet = ca.obtener_resultset(base_datos, tabla,"SELECT CausaConsecuenciaMCR.IdConsecuenciaCausa, CausaConsecuenciaMCR.IdConsecuencia, CausaConsecuenciaMCR.IdCausa, CausaConsecuenciaMCR.IdMCR, CausaConsecuenciaMCR.OrdenMCR\r\n"
				+ "FROM CausaConsecuenciaMCR\r\n"
				+ "WHERE CausaConsecuenciaMCR.OrdenMCR IS NOT NULL AND CausaConsecuenciaMCR.IdMCR IS NOT NULL AND CausaConsecuenciaMCR.IdCausa IS NOT NULL\r\n"
				+ "ORDER BY  CausaConsecuenciaMCR.IdConsecuenciaCausa\r\n"
				+ ";\r\n"
				+ "");
		
		this.imprimirEncabezadoErrores("Comprobación de MCRs tienen distintos OrdenMCR asignados para la misma Causa-Consecuencia");
		
		//Comprobamos las MCRs no nulas que no tienen asignado un OrdenMCR
		try {
			while(resultSet.next()) {
				fila = resultSet.getInt("IdConsecuenciaCausa");
				IdConsecuencia = resultSet.getInt("IdConsecuencia");
				IdCausa = resultSet.getInt("IdCausa");
				IdMCR = resultSet.getInt("IdMCR");
				IdMCR_largo = IdConsecuencia + "-" + IdCausa   + "-" + IdMCR;
				OrdenMCR = resultSet.getInt("OrdenMCR");
				
				ArrayList<Integer> filas = new ArrayList<Integer>();		//Creamos un arrayList que almacenará el número de fila del resultset que estamos analizando
				HashMap<Integer, ArrayList<Integer>> orden = new HashMap<Integer, ArrayList<Integer>>();		//Este hashmap almacena como clave el OrdenMCR y como valor un arraylist con las filas de la tabla que contienen la información que se está analizando en cad momento 
				
				if (listadoMCRs.containsKey(IdMCR_largo)) {								//Si el listado de MCRs ya contiene a la MCR
					if(listadoMCRs.get(IdMCR_largo).containsKey(OrdenMCR)) {			// Si el orden de esa MCR ya esta asociado a la misma
						listadoMCRs.get(IdMCR_largo).get(OrdenMCR).add(fila);			// Le añadimos el número de fila en la que se almacena esa información
					}
					else {
						listadoMCRs.get(IdMCR_largo).put(OrdenMCR, filas);				//Sino añadimos el arraylist con la primera fila de esa información
					}
				}
				else {											//Si el listado no contiene a esa MCR
					filas.add(fila);								//Añadimos la fila al arraylist
					orden.put(OrdenMCR, filas);						//Añadimos el arraylist de filas (como valor) al hashmap que tiene como clave el orden de la MCR
					listadoMCRs.put(IdMCR_largo, orden);			//Añadimos el arraylist de filas (como valor) al hashmap que tiene como clave el orden de la MCR
				}
				
			}
		//	System.out.println(listadoMCRs);

			Collection<String> ccm = listadoMCRs.keySet();
			Iterator<String> it = ccm.iterator();
			StringBuffer sb = new StringBuffer();
			
			while(it.hasNext()) {
				String s = it.next();

				HashMap<Integer, ArrayList<Integer>> ordenes = listadoMCRs.get(s);
				if(ordenes.size()>1) {
					sb.append("Error en tabla " + tabla + " la Consecuencia-Causa-MCR: "+ s //+ "\n");
							+ " tiene asignados los siguientes 'OrdenMCR' " + ordenes + " en las filas indicadas entre corchetes \n");
				}
				
			}
			/*Collection<HashMap<String,<HashMap<Integer, ArrayList<Integer>>>> mcrs = listadoMCRs.;
			//Iterator<HashMap<Integer, ArrayList<Integer>>> it = mcrs.iterator();
			StringBuffer s = new StringBuffer();
			while(it.hasNext()) {

				HashMap<Integer, ArrayList<Integer>> ordenMcr = it.next();
				
				System.out.println(ordenMcr);
				if(ordenMcr.size()>1) {									//Si la MCR (para una Causa y Consecuencia determinadas) tiene más de un número de orden
					//s.append(ordenMcr.toString() + "\n");
					s.append("Error en tabla " + tabla + " la Consecuencia-Causa-MCR: "+ ordenMcr.keySet().toString() 
							+ " tiene asignados los siguientes 'OrdenMCR' " + ordenMcr.values().toString() + "\n");
//					Collection< ArrayList<Integer>> ordenes = it.next().values();
					
					
							
				}
			}*/

			this.escribirError(sb.toString());
			
			resultSet.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Escribe el encabezado de cada apartado de las comprobaciones del fichero de errores, para facilitar la identificación de los errores que 
	 * se van a listar a continuación
	 * @param string Cadena de texto que describe la comprobación que se está realizando sobre la tabla
	 */
	private void imprimirEncabezadoErrores(String string) {
		this.escribirError("\n\n************************************************************\n");
		this.escribirError("*        " + string + "         *\n");
		this.escribirError("************************************************************\n");

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
	
}
