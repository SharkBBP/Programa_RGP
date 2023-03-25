package Inicio;

import java.io.File;
import java.util.Date;

/**
 * Clase destinada a imprimir los grafos indicados en cada momento en función de las opciones de impresion que se determinen por el usuario
 * Puede generar archivos tipo graphviz o tipo FreeMind, según sea el caso y la opción seleccionada por el usuario
 * @author shark1
 *
 */
public class Grafo {
	
	private Eventos eventos;
	//private MCRs mcrs;
	//private MCRS_procesadas mcrs_p;
	private StringBuffer sb = new StringBuffer();						//StringBuffer que almacena la información del archivo que se va a imprimir para generar el Freemind o el Graphviz
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 

	
	/**
	 * Constructor de la clase. Contiene todos los objetos necesarios para representar el grafo
	 * Esta clase está destinada, fundamentalmente a la generación (impresión) de los archivos FreeMind y Graphviz que representan la información almacenada en la base de datos
	 * @param eventos HashMap con todos los eventos de seguridad recogidos en la tabla Eventos de la Base de datos
	 * @param mcrs	HashMap con todas las Medidas de Control del Riesgo recogidas en la tabla MCRs de la Base de datos
	 * @param mcrs_procesadas HashMap con las MCRs procesadas (las mcrs procesadas tienen un idLargo, en el que el primer número es el id de la Consecuencia, el segundo el id de la Causa y el tercero la id de la procia mcr sin procesar
	 */
	public Grafo(Eventos eventos, MCRs mcrs, MCRS_procesadas mcrs_procesadas) {
			
		this.eventos = eventos;
		//this.mcrs = mcrs;
		//this.mcrs_p = mcrs_procesadas;
	}

	
	/**
	 * Método que inicia la generación del archivo FreeMind que representará la información en forma de árbol, en el que el nodo inicial será
	 * el evento crítico y, en función de las opciones de impresión que se utilicen, se imprimirán las causas, las consecuencias o ambas
	 * @param IdEvento  El Id del evento que se va a imprimir
	 * @param nivelNodo	El nivel de nodo en el Freemind. Los nodos especiales son el de nivel 0, que es evento crítico (raíz del árbol) y los de nivel 1 (que deben indicar si se ubican a la derecha o izquierda del evento crítico
	 * @param nivelMaximo En teoría es el máximo nivel de evento crítico que se quiere representar, por ejemplo, por si se quisiese representar un bow tie.
	 */
	public void imprimirGrafoFreeMind(int IdEvento, NivelNodo nivelNodo, int nivelMaximo) {
		
		System.out.println("Grafo.imprimirGrafoFreemind:Linea 45");
		
		sb = new StringBuffer();		
		NivelNodo nivelEvento = new NivelNodo(0);	//Establecemos el nivel inicial del nodo raíz en 0
		
		if( eventos.existeEvento(IdEvento)) {	
			Evento e = eventos.getEvento(IdEvento);
			
					
			//Escribimos el encabezado del SB que corresponde al fichero FM
			imprimirEncabezadoGrafoFM();
			
			//Escribimos los nodos del grafo, empezando por el nodo central
			//e.imprimeGrafoFreeMind(sb, true, true, 0, nivelMaximo, nivelNodo);				//el nivel del nodo inicial por el que se empieza a imprimir es el 0
			e.imprimeGrafoFreeMind(sb, true, true, nivelEvento, nivelMaximo, nivelNodo, null);				//el nivel del nodo inicial por el que se empieza a imprimir es el 0
			
			//Escribimos el cierre del archivo freemind
			imprimirFinalGrafoFM();
			
			//Escribimos el archivo a formato FreeMind
			EscritorDeFicheros ef = new EscritorDeFicheros();
			Date fecha = new Date();
			@SuppressWarnings("deprecation")
			String f2 = fecha.getYear() + "." + fecha.getMonth()+"."+ fecha.getDay() + "-" + fecha.getHours()+"_" + fecha.getMinutes();
	
			File f = ef.crear_fichero("FM-Evento-"+ IdEvento+ " - " + f2 + ".mm");
			ef.escribir_en_fichero( f , sb);
		}
		else {
			System.out.println( "No existe ningún Evento con el Id: " + IdEvento);
		}
		
	}
	
	
	/**
	 * Imprime el código que debe encabezar el archivo Freemind para que sea procesable por la aplicación
	 */
	public void imprimirEncabezadoGrafoFM() {
		
		sb.append("<map version=\"1.0.1\">\r\n"
				+ "<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->" + "\n");
	}
	
	
	/**
	 * Imprime la cadena de texto de cierre del archivo FreeMind, para que sea procesada por la aplicación
	 */
	public void imprimirFinalGrafoFM() {
		sb.append("</map>");
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
