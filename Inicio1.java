package Inicio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class Inicio2 {

	//private static final String logMain = null;


	public static void main(String[] args) throws IOException, SQLException {
		
				
		//String ruta_entorno_trabajo = "C:\\Users\\2809002\\OneDrive - ADIF\\0 - SGSC\\PROGRAMAS RGP - Visual Basic - Access\\";		//Ruta dónde se van a almacenar los archivos de trabajo
		String ruta_entorno_trabajo = "C:\\Users\\afbin\\OneDrive - ADIF\\0 - SGSC\\PROGRAMAS RGP - Visual Basic - Access\\";		//Ruta dónde se van a almacenar los archivos de trabajo
		//String ruta_entorno_trabajo = "C:\\Users\\jesus.garijo\\OneDrive - ADIF\\0-2020\\REUNIONES Y COMISIONES\\03.- Grupos de Trabajo SGSC\\GT RGP\\Escenarios de Riesgo\\Base Datos Access\\Generador\\";
		String archivo_bd = "REP-ACCESS-V3.28.accdb";																				//Nombre de la BD con la que se va a trabajar. Debe estar almacenada en la ruta de trabajo
		//String archivo_bd = "Prueba_eliminar_ciclos_V02.accdb";																				//Nombre de la BD con la que se va a trabajar. Debe estar almacenada en la ruta de trabajo
		//String archivo_bd = "Prueba_FreeMind-01.accdb";
		
/*		
		Boolean imprimirSubsistemas = false;
		Boolean imprimirProcesos = false;
		Boolean imprimirResponsables = false;
		Boolean imprimirDocumentos = false;
*/
		
		//EscritorDeFicheros ef = new EscritorDeFicheros();
		Errores errores = new Errores();
		
		//Conectamos con la BD a través del Conector para Access
		ConectorAccess ca = new ConectorAccess(ruta_entorno_trabajo);

		//Analizamos los argumentos recibidos
		AnalizadorArgumentos aa = new AnalizadorArgumentos(args);
		
/*
		imprimirSubsistemas = aa.getImprimirSubsistemas();
		imprimirProcesos = aa.getImprimirProcesos();
		imprimirResponsables = aa.getImprimirResponsables();
		imprimirDocumentos = aa.getImprimirDocumentos();
*/
		
		//Creamos el objeto "eventos" que almacena en un hashmap todos los eventos de seguridad, teniendo como clave del hashmap la ID respectiva del Evento en la BD
		Eventos eventos = new Eventos(ruta_entorno_trabajo, archivo_bd);
		eventos.leer_eventos();
		
		//Creamos el objeto "mcrs" que almacena en un hashmap todas las medidas de control del riesgos, teniendo como clave del hashmap la Id de la MCR en la BD
		MCRs mcrs = new MCRs(ruta_entorno_trabajo, archivo_bd);
		mcrs.leer_MCRs();
		
		//Creamos el objeto "responsables" para almacenar todas las áreas responsables. Contiene un HashMap con todas las áreas responsables, cuya clave es el ID en la BD
		Responsables responsables = new Responsables(ruta_entorno_trabajo, archivo_bd, ca);
		responsables.leerResponsables();
		aa.setResponsables(responsables);		//Añadimos los responsables al analizador de argumentos
		
		//Creamos el objeto "documentos" para almacenar todos los documentos de la tabla documentos. Contiene un HashMap con todas las áreas responsables, cuya clave es el ID en la BD
		Documentos documentos = new Documentos(ruta_entorno_trabajo, archivo_bd, ca);
		documentos.leerDocumentos();
		aa.setDocumentos(documentos);			//Añadimos los documentos al analizador de argumentos
		
		aa.procesarArgumentos();				//Procesamos los argumentos recibidos como parámetros 
		
		//Comprobamos la tabla "CausaConsecuenciaMCR" para ver qué errores contiene antes de procesarla
		CausaConsecuenciaMCR ccm = new CausaConsecuenciaMCR(ruta_entorno_trabajo, archivo_bd, eventos, mcrs, ca);
		//ccm.realizarComprobaciones();
		
		
		//Con todos los datos anteriores cargados en el analizador de argumentos, generamos la consulta que vamos a realizar a la base de datos.
		String consulta = aa.obtenerConsulta();

		
		
		
		//Creamos el objeto "mcrs_procesadas" que almacena en un hashmap todas las medidas de control del riesgos que tienen un IdMCR_largo (Cadena de texto que concatena el "IdConsecuencia-IdCausa-IdMCR". Ej: IdMCR_largo = 3-123-25), teniendo como clave del hashmap la IdMCR_largo de la MCR en la BD
		//Se asocian a las MCR procesadas todo el resto de entidades que dependen de ellas (Subsistemas, Procesos, Responsables, Documentos y Artículos)
		MCRS_procesadas mcrs_procesadas = new MCRS_procesadas(ruta_entorno_trabajo, archivo_bd, eventos, mcrs, ca, responsables, documentos);

		
		/*		String consulta1 = "SELECT *"		//Consulta que se quiere hacer a la tabla "CausaConsecuenciaMCR", para representar posteriormente la información indicada en la misma en el fichero freemind
				+ " FROM CausaConsecuenciaMCR"
				//Solamente procesamos los registros que tienen todos los datos completos (Excepto 'Componente' y 'Elemento')
				+ " WHERE  CausaConsecuenciaMCR.IdConsecuenciaCausa IS NOT NULL AND CausaConsecuenciaMCR.IdConsecuencia IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdConsecuencia IS NOT NULL AND CausaConsecuenciaMCR.IdCausa IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdCausa IS NOT NULL AND CausaConsecuenciaMCR.IdMCR IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdMCR IS NOT NULL AND CausaConsecuenciaMCR.OrdenMCR IS NOT NULL AND CausaConsecuenciaMCR.SubsistemaMCR IS NOT NULL AND CausaConsecuenciaMCR.EstadoSubsistema IS NOT NULL AND CausaConsecuenciaMCR.Proceso IS NOT NULL AND CausaConsecuenciaMCR.EstadoProceso IS NOT NULL AND CausaConsecuenciaMCR.IdDocumento_de_requisitos IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdDocumento IS NOT NULL AND CausaConsecuenciaMCR.Articulos IS NOT NULL AND CausaConsecuenciaMCR.EstadoArticulos IS NOT NULL AND CausaConsecuenciaMCR.IdAreaResponsable IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdAreaResponsable IS NOT NULL";
*/
		//String consulta1 = "SELECT * FROM CausaConsecuenciaMCR WHERE  CausaConsecuenciaMCR.IdConsecuenciaCausa IS NOT NULL AND CausaConsecuenciaMCR.IdConsecuencia IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdConsecuencia IS NOT NULL AND CausaConsecuenciaMCR.IdCausa IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdCausa IS NOT NULL AND CausaConsecuenciaMCR.IdMCR IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdMCR IS NOT NULL AND CausaConsecuenciaMCR.OrdenMCR IS NOT NULL AND CausaConsecuenciaMCR.SubsistemaMCR IS NOT NULL AND CausaConsecuenciaMCR.EstadoSubsistema IS NOT NULL AND CausaConsecuenciaMCR.Proceso IS NOT NULL AND CausaConsecuenciaMCR.EstadoProceso IS NOT NULL AND CausaConsecuenciaMCR.IdDocumento_de_requisitos IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdDocumento IS NOT NULL AND CausaConsecuenciaMCR.Articulos IS NOT NULL AND CausaConsecuenciaMCR.EstadoArticulos IS NOT NULL AND CausaConsecuenciaMCR.IdAreaResponsable IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdAreaResponsable IS NOT NULL AND (CausaConsecuenciaMCR.SubsistemaMCR = \"Infraestructura\" AND CausaConsecuenciaMCR.Proceso = \"Mantenimiento\") ";

		mcrs_procesadas.procesarConsulta(consulta);
			
		//Procesamos la tabla "CausaConsecuenciaMCR" para ir asociando cada evento con sus respectivas MCR
		//Crea el esqueleto del grafo, representando los Eventos de Seguridad, las Medidas de Control del riesgo y Sus relaciones entre sí.
		Relaciones relaciones = new Relaciones(ruta_entorno_trabajo, archivo_bd, eventos, mcrs, mcrs_procesadas,ca);
		relaciones.procesar_eventos(aa.imprimirSoloEventos());

		//Creamos el grafo que usaremos para imprimir los ficheros Graphviz y FreeMind
		Grafo grafo = new Grafo(eventos, mcrs, mcrs_procesadas);
		NivelNodo nivelNodo = new NivelNodo(-1);											//Creamos el nivel de nodo con valor "-1" para que al imprimir el evento crítico en un fichero "Graphviz" la rutina de impresión sirva para todos los eventos incluido el crítico, ya que para llevar un control del nivel de los nodos, lo primero que hace al imprimir un nodo es aumentar si nivel en 1. Por tanto si creamo el nivel con valor -1 el evento crítico tendrá valor -1 + 1(que se suma al entrar en la rutina de impresión) = 0 que es el nivel que corresponde al evento crítico
		grafo.imprimirGrafoFreeMind(aa.getEventoCritico(), nivelNodo, 1);				//El primer parámetro de "imprimirGrafoFM indica qué evento vamos a tomar como Evento Crítico de la representación en forma de árbol en FreeMind
																							//El segundo parámetro indica el nivel inicial del evento. En esta llamada siempre se debe mantener el 0 como valor del parámetro
																							//El tercer parámetro es el número de niveles de eventos que se desean imprimir (causas y consecuencias de primer nivel, de segundo nivel, etc). Si se pone un número muy alto (Ej:1000), se imprimirá el árbol completo
		
/*		NivelNodo nivelNodo = null;
		
		for( int i = 1; i <= 805; i++) {
			//Grafo grafo = new Grafo(eventos, mcrs, mcrs_procesadas);
			nivelNodo = new NivelNodo(-1);
			grafo.imprimirGrafoFreeMind(i, nivelNodo, 1);
		}
*/	
		
		//Conexion_BD cbd = new Conexion_BD();
		//cbd.conectar();
		
		//ConectorAccess ca = new ConectorAccess(ruta_entorno_trabajo);
		//ca.obtener_resultset(archivo_bd, tabla);
		
		
		//ca.imprimir_result_set(rs_eventos);
		
		//Imprimimos un log con los errores de ejecución del programa
		errores.anexar_errores(ccm.imprimirErrores());
		Date fecha = new Date();
		//int anno = new Year().getValue();
		//int mes =  new MonthDay().getMonthValue();
		//int dia = fecha.getDay();
		//long fecha2 = fecha.getTime();
		@SuppressWarnings("deprecation")
		String f = fecha.getYear() + "." + fecha.getMonth()+"."+ fecha.getDay() + "-" + fecha.getHours()+"_" + fecha.getMinutes();
		errores.imprimirErrores( "Errores de Ejecución" + "-" + archivo_bd + "-" + f + ".txt");
		
		System.out.println( "fin - Inicio");


		
	}

	

	
	

	
}
