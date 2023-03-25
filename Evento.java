package Inicio;

import java.util.ArrayList;
//import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Clase que almacena la información relacionada con los eventos de seguridad
 * @author AFB
 * @version 0.0.1
 * @date 03/01/2022
 */
public class Evento {
	
	private Integer IdEvento;					//El id (entero) que se utiliza como clave para acceder a la información del registro que almacena la información del evento buscado en la BD del RGP
	private String NombreEvento;				//El nombre asignado al evento a tratar
	private HashMap<Integer, Evento> Eventos_Consecuencias = new HashMap<Integer, Evento>();	//Hashmap que almacena los eventos asociados a cada evento como consecuencia del evento actual. La id del evento consecuencia se usa como clave del Hashmap. El value del hashMap es el evento que tiene esa ID como clave
	private ArrayList<Evento> Eventos_Consecuencias_ordenados = new ArrayList<Evento>();		//ArrayList que almacena todos los eventos consecuencia del actual, almacenados alfabéticamente
	private HashMap<Integer, Evento> Eventos_Causas = new HashMap<Integer, Evento>();			//Hashmap que almacena los eventos asociados a cada evento causa del evento actual. La id del evento causa se usa como clave del Hashmap. El value del hashMap es el evento que tiene esa ID como clave
	private ArrayList<Evento> Eventos_Causas_ordenados = new ArrayList<Evento>();				//ArrayList que almacena todos los eventos causa del actual, almacenados alfabéticamente
	private HashMap<Integer, ArrayList<MCR>> MCR_Consecuencias = new HashMap<Integer, ArrayList<MCR>>();	//Hashmap que almacena las MCR asociadas a cada evento consecuencia (del evento actual). La id del evento consecuencia se usa como clave del Hashmap. El value del hashMap es el arrayList con todas las MCR asociadas a esa consecuencia
	private HashMap<Integer, ArrayList<MCR>> MCR_Causas = new HashMap<Integer, ArrayList<MCR>>();			//Hashmap que almacena las MCR asociadas a cada evento causa (del evento actual). La id del evento causa se usa como clave del Hashmap. El value del hashMap es el arrayList con todas las MCR asociadas a esa causa
	private HashMap<Integer, ArrayList<MCR_procesada>> MCR_Consecuencias_procesadas = new HashMap<Integer, ArrayList<MCR_procesada>>();	//Hashmap que almacena las MCR asociadas a cada evento consecuencia (del evento actual). La id del evento consecuencia se usa como clave del Hashmap. El value del hashMap es el arrayList con todas las MCR asociadas a esa consecuencia
	private HashMap<Integer, ArrayList<MCR_procesada>> MCR_Causas_procesadas = new HashMap<Integer, ArrayList<MCR_procesada>>();			//Hashmap que almacena las MCR asociadas a cada evento causa (del evento actual). La id del evento causa se usa como clave del Hashmap. El value del hashMap es el arrayList con todas las MCR asociadas a esa causa
	private HashSet<Integer> eventosCausaYaImpresos = new HashSet<Integer>();				//Conjunto que contiene los Ids de todos los eventos que han sido impresos ya en el árbol del freemind. Se crea este conjunto para evitar imprimir los ciclos que puedan aparecer en el grafo cuando se va a imprimir el árbol
	private HashSet<Integer> eventosConsecuenciaYaImpresos = new HashSet<Integer>();				//Conjunto que contiene los Ids de todos los eventos que han sido impresos ya en el árbol del freemind. Se crea este conjunto para evitar imprimir los ciclos que puedan aparecer en el grafo cuando se va a imprimir el árbol
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 
	private HashMap<Integer, String> estados = new HashMap<Integer, String>();	
	
	//private String Descripcion;		//Descripción detallada del evento tratado. 
	//private ArrayList<String> Subsistemas = new ArrayList<String>();
	
	public Evento(Integer IdEvento, String NombreEvento) {
		this.IdEvento = IdEvento;						//Corresponde al ID que tiene asignado el evento en la BD
		this.NombreEvento = NombreEvento;				//Corresponde al nombre que tiene asignado el evento en la BD
		this.logErrores.append("\n\n ### ERRORES CLASE EVENTO ###\n"); //Escribimos el encabezado de los errores al ejecutar esta clase
		this.eventosCausaYaImpresos.add(IdEvento);						//Incluimos entre los eventos causa ya impresos el propio evento actual
		this.eventosConsecuenciaYaImpresos.add(IdEvento);				//Incluimos entre los eventos consecuencia ya impresos el propio evento actual

		//this.Descripcion = Descripcion;				//Corresponde a la descripción que tiene asignada el evento en la BD
	}
	
	/**
	 * Introduce un evento en el hashMap de eventos causas
	 */
	public void introduce_evento_causa(Integer ID_causa, Evento evento) {
		
		if ( ! Eventos_Causas.containsKey(ID_causa)) {			//Si la causa ya existe asociada a ese evento
			Eventos_Causas.put(ID_causa, evento);
			this.Eventos_Causas_ordenados.add(evento);
		}
	}
	
	/**
	 * Introduce un evento en el hashMap de eventos consecuencias
	 */
	public void introduce_evento_consecuencia(Integer ID_consecuencia, Evento evento) {
		
		if ( ! Eventos_Consecuencias.containsKey(ID_consecuencia)) {			//Si la causa ya existe asociada a ese evento
			Eventos_Consecuencias.put(ID_consecuencia, evento);
			this.Eventos_Consecuencias_ordenados.add(evento);
		}
	}
	
	
	/**
	 * Introduce una MCR dentro del HashMap de MCRs de "causas"
	 */
	public void introduce_mcr_causa(Integer ID_causa, MCR mcr) {
		ArrayList<MCR> lista ;
		
		if ( ! MCR_Causas.containsKey(ID_causa)) {			//Si la causa ya existe asociada a ese evento
			lista = new ArrayList<MCR>();
			lista.add(mcr);			
			MCR_Causas.put(ID_causa, lista);
		}
		else {
			lista = MCR_Causas.get(ID_causa);
			lista.add(mcr);
			MCR_Causas.replace(ID_causa, lista);			
		}
	}
	
	/**
	 * Introduce una MCR dentro del HashMap de MCRs de "consecuencias"
	 */
	public void introduce_mcr_consecuencia(Integer ID_consecuencia, MCR mcr) {
		ArrayList<MCR> lista ;
		if ( ! MCR_Consecuencias.containsKey(ID_consecuencia)) {
			lista = new ArrayList<MCR>();
			lista.add(mcr);			
			MCR_Consecuencias.put(ID_consecuencia, lista);
		}
		else {
			lista = MCR_Consecuencias.get(ID_consecuencia);
			lista.add(mcr);
			MCR_Consecuencias.replace(ID_consecuencia, lista);			
		}
	}
	
	
	/**
	 * Devuelve una cadena de texto con los datos del evento
	 */
	public String toString() {
		String s = "Evento: " + IdEvento + "-" + NombreEvento + 
				"\n\tMCR Causas: " + this.MCR_Causas_procesadas.toString() +
				"\n\tMCR Conscuencias: " + this.MCR_Consecuencias_procesadas.toString();
		 
		 return s;
	}

	
	/**
	 * Devuelve el nombre asignado a este evento
	 * @return el nombre del evento
	 */
	public String getNombreEvento() {
		return this.NombreEvento;
	}

	/**
	 * Devuelve el id asignado a este evento
	 * @return Id del evento en BD en formato cadena de texto
	 */
	public String getIdEvento() {
		new String();
		return String.valueOf(this.IdEvento);
	}
	
	
	/**
	 * Escribe en el StringBuffer todas las etiquetas y datos necesarios para generar los nodos Eventos de Seguridad del fichero FreeMind
	 * @param sb	StringBuffer en el que se escriben todos los datos del fichero FreeMind para poder imprimir el grafo que se quiere generar
	 * @param imprimeCausas	Booleano que indica si se quieren imprimir las causas del evento indicado
	 * @param imprimeConsecuencias Booleano que indica si se quieren imprimir las consecuencias del evento indicado
	 * @param nivelEvento Indica con un entero el nivel hasta el cual se quiere imprimir el árbol de causas y el árbol de consecuencias (Casusas de primer nivel, causas de 2º nivel, etc)
	 * @param nivelMaximo Indica el nivel máximo hasta el que se quieren imprimir los eventos de seguridad, tanto causas como consecuencias (Ej: si se pone un 2 se imprimirían hasta las causas de segundo nivel)
	 * @param nivelNodo Indica cuál es el nivel del nodo actual en el Freemind, este nivel es absoluto y tiene en cuenta cuantos nodos hay de separación hasta el nodo raíz.
	 */
	private void imprimeInformacionNodoFM( StringBuffer sb, Boolean imprimeCausas, Boolean imprimeConsecuencias, NivelNodo nivelEvento, int nivelMaximo, NivelNodo nivelNodo, Integer IdConsecuencia ) {
		
		String etiquetaFormatoPrefijo = "";		//Es la etiqueta que se va a utilizar antes del texto del nodo para dar formato al mismo. Depende del estado de la misma
		String etiquetaFormatoSufijo = "";		//Es la etiqueta que se va a utilizar después del texto del nodo para dar formato al mismo. Depende del estado de la misma

		if (nivelEvento.getNivelNodo() <= nivelMaximo ) {	
			//Añadimos la información nuclear del nodo al StringBuffer
			sb.append("\n<node BACKGROUND_COLOR=\"#99ffff\" ");
			nivelNodo.aumentarNivelNodo();
			
			String estado = this.estados.get(IdConsecuencia);
			
			if( estado == null ) {
				sb.append("BACKGROUND_COLOR=\"#99FFFF\" ");
			}
			else {
				switch ( estado ) {
					case "Consolidado": 
						sb.append("BACKGROUND_COLOR=\"#99FFFF\" ");
						etiquetaFormatoPrefijo = "";
						etiquetaFormatoSufijo = "";
						break;
					case "Eliminado": 
						sb.append("BACKGROUND_COLOR=\"#FF3333\" ");
						etiquetaFormatoPrefijo = "<u>";
						etiquetaFormatoSufijo = "</u>";
						break;
					case "Modificado": 
						sb.append("BACKGROUND_COLOR=\"#FF9900\" ");
						etiquetaFormatoPrefijo = "<i><b>";
						etiquetaFormatoSufijo = "</b></i>";
						break;
					case "Nuevo": 
						sb.append("BACKGROUND_COLOR=\"#66FF00\" ");
						etiquetaFormatoPrefijo = "<b>";
						etiquetaFormatoSufijo = "</b>";
						break;
					case "Revision_GT":
						sb.append("BACKGROUND_COLOR=\"#6666FF\" ");
						etiquetaFormatoPrefijo = "<b>";
						etiquetaFormatoSufijo = "</b>";
						break;
					case "Eliminacion_Consolidada":
						sb.append("BACKGROUND_COLOR=\"#000000\" ");
						break;
					default:
						sb.append("BACKGROUND_COLOR=\"#A020F0\" ");
						System.out.println("Formato MCR - Opción no prevista");
						etiquetaFormatoPrefijo = "";
						etiquetaFormatoSufijo = "";
						break;		
				}
			}
			
			if (nivelNodo.getNivelNodo() == 1 && imprimeConsecuencias) {
				sb.append("POSITION=\"right\" ");		//Si el nivel del nodo es 1 y hay que imprimir las causas se indica que este nodo se representa a la izquierda
			}
			else if (nivelNodo.getNivelNodo() == 1 && imprimeCausas) {
				sb.append("POSITION=\"left\" ");		//Si el nivel del nodo es 1 y hay que imprimir las consecuencias se indica que este nodo se representa a la derecha
			}
			//sb.append("STYLE=\"bubble\" TEXT=\"" + NombreEvento + "\">\n");
			formatearTexto(sb, NombreEvento, etiquetaFormatoPrefijo, etiquetaFormatoSufijo );
			
			
			
			//Añadimos la información de los atributos del nodo al StringBuffer
			sb.append("<icon BUILTIN=\"help\"/>");
			sb.append("<attribute_layout NAME_WIDTH=\"10\" VALUE_WIDTH=\"50\"/>");
			sb.append("<attribute NAME=\"ID\" VALUE=\"" + IdEvento + "\"/>\n");
		}
	}


	/**
	 * Permite imprimir el contenido de cada nodo en varias líneas, de forma que sea mucho más cuadrada la celda del nodo y se pueda visualizar mejor el diagrama Freemind
	 * @param sb	El StringBuffer que almacena la información del archivo FreeMind
	 * @param nombreEvento		El nombre asignado al evento de seguridad en la base de datos
	 * @param etiquetaFormatoSufijo 
	 * @param etiquetaFormatoPrefijo 
	 */
	private void formatearTexto(StringBuffer sb, String nombreEvento, String etiquetaFormatoPrefijo, String etiquetaFormatoSufijo) {
		
		Integer longitudMaximaLinea = 20;
		String[] palabras = nombreEvento.trim().split(" ");
		Integer longitudLinea = 0;
		Integer numeroPalabras = palabras.length;
		String palabra;
		Boolean anadirPalabra = true;
		
			
		sb.append("STYLE=\"bubble\" >");
		sb.append("\n<richcontent TYPE=\"NODE\"><html>");
		sb.append("\n<head>\n</head>");	
		sb.append("\n<body>");
		
		//Hacemos que la longitud de la línea sea, al menos, igual a la longitud de la palabra más larga.
		for( int i=0 ; i < numeroPalabras; i++) {
			palabra = palabras[i];
			
			if( palabra.length() > longitudMaximaLinea) {
				longitudMaximaLinea = palabra.length();
			}
		}

		//
		for( int i=0 ; i < numeroPalabras; i++) {
			String linea = palabras[i];
			longitudLinea = linea.length();
			anadirPalabra = true;
			
			while( anadirPalabra && i < (numeroPalabras-1)) {
				String siguientePalabra = palabras[i+1];
				
				if( longitudLinea + 1 + siguientePalabra.length() < longitudMaximaLinea) {
					linea = linea + " " + siguientePalabra;
					i++;
					longitudLinea = linea.length();
				}
				else {
					anadirPalabra = false;
				}
			} 
			
			//Añadimos la línea al StringBuffer
			sb.append("\n<p>");
			sb.append("\n" + etiquetaFormatoPrefijo + linea + etiquetaFormatoSufijo );
			sb.append("\n</p>");
		}
		sb.append("\n</body>");
		sb.append("\n</html>");
		sb.append("\n</richcontent>\n");
	}

	
	/**
	 * Imprime el nodo central de un archivo FreeMind con el atributo correspondiente a su ID
	 * @param sb	El StringBuffer que se va a utilizar para generar todo el cuerpo del archivo FreeMind, excepto la cabecera y el fin
	 * @param imprimeCausas 	Booleano que indica si se quieren imprimir las causas en el archivo FreeMind
	 * @param imprimeConsecuencias		Booleano que indica si se quieren imprimir las consecuencias del nodo en el archivo FreeMind
	 * @param nivelEvento Representa el nivel del evento que se está imprimiendo. El nivel 0 corresponde al evento crítico, el 1 a las causas o consecuencias de 1er nivel, etc.
	 * @param nivelMaximo Representa el nivel máximo de eventos que se quieren representar (No está implementado aún). Si fuese un nivel 1, se representaría un BowTie puro. 
	 * Si fuese un número alto representaría todas las causas y todas las consecuencias del evento crítico 
	 * @param nivelNodo Representa el nivel de números de nodos de separación entre el nodo que se imprime (puede ser un evento o una mcr) y el evento crítico
	 */
	public void imprimeGrafoFreeMind(StringBuffer sb, Boolean imprimeCausas, Boolean imprimeConsecuencias, NivelNodo nivelEvento, int nivelMaximo, NivelNodo nivelNodo, Integer IdConsecuencia) {
		
		//Imprimimos la información correspondiente al nodo actual en el StringBuffer dónde está la información del archivo FreeMind
		imprimeInformacionNodoFM(sb, imprimeCausas, imprimeConsecuencias, nivelEvento, nivelMaximo, nivelNodo, IdConsecuencia);
		
		//System.out.println(nivelEvento.getNivelNodo());
		
		if( nivelEvento.getNivelNodo() < nivelMaximo ) {
			
			//Imprimimos el arbol de consecuencias respecto del nodo central (evento crítico)
			if ( imprimeConsecuencias && Eventos_Consecuencias.size() > 0 ) {

				if( this.Eventos_Consecuencias_ordenados.size() > 0) {
					Iterator<Evento> it = this.Eventos_Consecuencias_ordenados.iterator();
					
					while( it.hasNext()) {
						Evento e = it.next();
						
						incluirEventosConsecuenciaYaImpresosEn( e );				//Incluye en el evento que se pasa como parámetro un "Set" con todos los eventos que han sido impresos anteriormente en el árbol del FreeMind
											
						//Si el evento "e" no ha sido impreso previamente, se procede a imprimir sus consecuencias y sus MCR asociadas
						if( eventoConsecuenciaNoImpresoPreviamente(e)) {
		
							//Se imprimen las MCR situadas entre el evento actual y su evento consecuencia
							ArrayList<MCR_procesada> l = MCR_Consecuencias_procesadas.get(Integer.parseInt(e.getIdEvento()));
	
							if(l != null ) {
								imprimirListadoMCRConsecuencias(sb, Integer.parseInt(e.getIdEvento()), false, imprimeConsecuencias, nivelNodo);
							}
							
							//Imprimimos los siguientes eventos consecuencia
							nivelEvento.aumentarNivelNodo(); 					//Incrementamos el nivel de evento antes de imprimir el siguiente evento
							e.imprimeGrafoFreeMind(sb, false, imprimeConsecuencias, nivelEvento , nivelMaximo, nivelNodo, null);
							nivelEvento.disminuirNivelNodo();					//Decrementamos el nivel de evento después de imprimir el siguiente evento
							
							//Vamos "cerrando" los nodos que habíamos abierto correspondientes a las MCRs
							if(l != null && l.size() > 0) {
								for(int i = 0; i < l.size(); i++) {
									sb.append("</node>\n");		
									nivelNodo.disminuirNivelNodo();
	
								}
							}
						}
						
						resetearEventosConsecuenciaYaImpresos( e );				//Elimina el evento que se pasa como parámetro del "HashSet" "eventosCausaYaImpresos", para que pueda ser impreso ese evento en otras ramas del árbol del freemind
					}
				}
			}

			
			//Imprimimos el árbol de causas con respecto al nodo central (evento crítico
			if ( imprimeCausas && Eventos_Causas.size() > 0 ) {
				
				if( this.Eventos_Causas_ordenados.size() > 0 ) {
					Iterator<Evento> it = this.Eventos_Causas_ordenados.iterator();
					
					while( it.hasNext()) {
						Evento e = it.next();
							
						incluirEventosCausaYaImpresos( e );				//Incluye en el evento que se pasa como parámetro un "Set" con todos los eventos que han sido impresos anteriormente en el árbol del FreeMind

						
						if( eventoCausaNoImpresoPreviamente(e)) {
					
							//Se imprimen las MCR situadas entre el evento actual y su evento causa
							ArrayList<MCR_procesada> l = MCR_Causas_procesadas.get(Integer.parseInt(e.getIdEvento()));
	
							if(l != null && l.size() > 0) {
								imprimirListadoMCRcausas (sb, Integer.parseInt(e.getIdEvento()),  imprimeCausas, false, nivelNodo);
							}
	
							//Imprimimos los siguientes eventos causa
							nivelEvento.aumentarNivelNodo();					//Incrementamos el nivel de evento antes de imprimir el siguiente evento
							e.imprimeGrafoFreeMind(sb, imprimeCausas, false, nivelEvento , nivelMaximo, nivelNodo, this.IdEvento);
							nivelEvento.disminuirNivelNodo();					//Decrementamos el nivel de evento después de imprimir el siguiente evento
							
							//Vamos "cerrando" los nodos que habíamos abierto correspondientes a las MCRs
							if(l != null && l.size() > 0) {
								for(int i = 0; i < l.size(); i++) {
									sb.append("</node>\n");		
									nivelNodo.disminuirNivelNodo();
	
								}
							}
						}
						
						resetearEventosCausaYaImpresos( e );				//Elimina el evento que se pasa como parámetro del "HashSet" "eventosCausaYaImpresos", para que pueda ser impreso ese evento en otras ramas del árbol del freemind

					}
				}
			}
		}
		
		
	
		//Cerramos el nodo que habíamos abierto
		nivelNodo.disminuirNivelNodo();
		sb.append("</node>\n");		
	}
	
	/**
	 * Deja como evento causa ya impreso únicamente el id del propio evento que se pasa como parámetro
	 * @param e El evento cuyos EventosYaImpresos se quiere resetear
	 */
	private void resetearEventosCausaYaImpresos(Evento e) {
		e.eventosCausaYaImpresos.clear();
		e.eventosCausaYaImpresos.add(e.IdEvento);
		
	}

	/**
	 * Deja como evento consecuencia ya impreso únicamente el id del propio evento que se pasa como parámetro
	 * @param e El evento cuyos EventosYaImpresos se quiere resetear
	 */
	private void resetearEventosConsecuenciaYaImpresos(Evento e) {
		e.eventosConsecuenciaYaImpresos.clear(); 
		e.eventosConsecuenciaYaImpresos.add(e.IdEvento);		
	}

	/**
	 * Indica si el evento pasado como parámetro ha sido impreso ya previamente en las causas del árbol que están entre el evento actual y el evento raíz
	 * @param e Evento que se quiere comprobar si ha sido ya impreso entre el evento actual y el evento raíz
	 * @return si el evento pasado como parámetro ha sido impreso ya previamente en las causas del árbol que están entre el evento actual y el evento raíz
	 */
	private boolean eventoCausaNoImpresoPreviamente(Evento e) {
		Integer id = e.IdEvento; 
		return !this.eventosCausaYaImpresos.contains(id);
	}

	/**
	 * Indica si el evento pasado como parámetro ha sido impreso ya previamente en las consecuencias del árbol que están entre el evento actual y el evento raíz
	 * @param e Evento que se quiere comprobar si ha sido ya impreso entre el evento actual y el evento raíz
	 * @return si el evento pasado como parámetro ha sido impreso ya previamente en las consecuencias del árbol que están entre el evento actual y el evento raíz
		 */
	private boolean eventoConsecuenciaNoImpresoPreviamente(Evento e) {
		Integer id = e.IdEvento;
		return !this.eventosConsecuenciaYaImpresos.contains(id);
	}

	/**
	 * Incluye en el evento introducido como parámetro el Id del evento actual, para que quede registrado que ya ha sido impreso previamente y que no se debe volver a imprimir
	 * @param e	El evento en el que se quiere introducir el Id del evento actual como evento ya impreso
	 */
	private void incluirEventosConsecuenciaYaImpresosEn(Evento e) {
		e.setEventosConsecuenciaYaImpresos(this.eventosConsecuenciaYaImpresos);
		e.addEventoConsecuenciaYaImpreso(e.IdEvento);
	}

	/**
	 * Incluye en el HashSet "eventosConsecuenciasYaImpresos" el id del evento que se le pasa como parámetro. Tiene el objetivo de que este evento no se imprima más de una vez 
	 * en las consecuencias y evitar así los problemas de impresión asociados a los ciclos que se pueden producir en los grafos
	 * @param idEvento	Id del evento que se quiere incluir en el HashSet "eventosConsecuenciasYaImpresos" para evitar que se imprima más de una vez
	 */
	private void addEventoConsecuenciaYaImpreso(Integer idEvento) {
		this.eventosConsecuenciaYaImpresos.add(idEvento);
	}
	
	/**
	 * Incluye en el evento introducido como parámetro el Id del evento actual, para que quede registrado que ya ha sido impreso previamente y que no se debe volver a imprimir
	 * @param e	El evento en el que se quiere introducir el Id del evento actual como evento ya impreso
	 */
	private void incluirEventosCausaYaImpresos(Evento e) {
		e.setEventosCausaYaImpresos(this.eventosCausaYaImpresos);
		e.addEventoCausaYaImpreso(e.IdEvento);
	}

	/**
	 * Incluye en el HashSet "eventosCausaYaImpresos" el id del evento que se le pasa como parámetro. Tiene el objetivo de que este evento no se imprima más de una vez en las causas
	 *  y evitar así los problemas de impresión asociados a los ciclos que se pueden producir en los grafos
	 * @param idEvento	Id del evento que se quiere incluir en el HashSet "eventosCausaYaImpresos" para evitar que se imprima más de una vez
	 */
	private void addEventoCausaYaImpreso(Integer idEvento) {
		this.eventosCausaYaImpresos.add(idEvento);
	}

	/**
	 * Imprime el listado de MCR asociadas al evento causa que se indica como parámetro IdCausa
	 * @param sb	El StringBuffer que se va a utilizar para generar todo el cuerpo del archivo FreeMind, excepto la cabecera y el fin
	 * @param IdCausa	Es el Id del evento causa asociado al cual están se van a imprimir todas las MCR entre el evento actual y este evento causa
	 * @param imprimeCausas 	Booleano que indica si se quieren imprimir las causas en el archivo FreeMind
	 * @param imprimeConsecuencias		Booleano que indica si se quieren imprimir las consecuencias del nodo en el archivo FreeMind
	 * @param nivelNodo Representa el nivel de números de nodos de separación entre el nodo que se imprime (puede ser un evento o una mcr) y el evento crítico
	 */
	private void imprimirListadoMCRcausas(StringBuffer sb, int IdCausa, Boolean imprimeCausas, boolean imprimeConsecuencias, NivelNodo nivelNodo) {
		
		//Iterator<MCR> it = MCR_Causas.get(IdCausa).iterator();		//Creamos un iterador de la lista de MCR que están asociadas al evento consecuencia
		Iterator<MCR_procesada> it = MCR_Causas_procesadas.get(IdCausa).iterator();
		
		while( it.hasNext()) {
			MCR_procesada mcr_p = it.next();
			if( mcr_p != null) {
				mcr_p.imprimirNodoMCR_FreeMind(sb, imprimeCausas, imprimeConsecuencias, nivelNodo);
				//nivelNodo++;
			}
		}
	}

	
	/**
	 * Imprime el listado de MCR asociadas al evento consecuencia que se indica como parámetro IdConsecuencia
	 * @param sb	El StringBuffer que se va a utilizar para generar todo el cuerpo del archivo FreeMind, excepto la cabecera y el fin
	 * @param IdConsecuencia	Es el Id del evento consecuencia asociado al cual están se van a imprimir todas las MCR entre el evento actual y este evento consecuencia
	 * @param imprimeCausas 	Booleano que indica si se quieren imprimir las causas en el archivo FreeMind
	 * @param imprimeConsecuencias		Booleano que indica si se quieren imprimir las consecuencias del nodo en el archivo FreeMind
	 * @param nivelNodo Representa el nivel de números de nodos de separación entre el nodo que se imprime (puede ser un evento o una mcr) y el evento crítico
	 */
	private void imprimirListadoMCRConsecuencias(StringBuffer sb, int IdConsecuencia, Boolean imprimeCausas, Boolean imprimeConsecuencias, NivelNodo nivelNodo) {
		
		Iterator<MCR_procesada> it = MCR_Consecuencias_procesadas.get(IdConsecuencia).iterator();

		while( it.hasNext()) {
			MCR_procesada mcr_p = it.next();
			if( mcr_p != null) {
				mcr_p.imprimirNodoMCR_FreeMind(sb, imprimeCausas, imprimeConsecuencias, nivelNodo);
				//nivelNodo++;
			}
		}
	}

	/**
	 * Devuelve un StringBuffer con los errores generados durante la ejecución de esta clase
	 * @return un StringBuffer con los errores generados durante la ejecución de esta clase
	 */
	public StringBuffer imprimirErrores() {
		if (this.logErrores.isEmpty()) {
			return null;
		}
		else {
			StringBuffer encabezado_errores = new StringBuffer();
			encabezado_errores.append("*** Errores Evento: "+ this.getIdEvento() + "-" + this.getNombreEvento() + " ***");
			encabezado_errores.append(logErrores);
			return encabezado_errores;

		}
	}
	
	
	/**
	 * Escribe los errores correspondientes a la ejecución de esta clase en el log de errores
	 * @param error El error lógico que se ha producido al ejecutar esta clase 
	 */
	public void escribirError( String error) {
		this.logErrores.append(error + "\n");
	}

	
	/**
	 * Introduce las mcrs reactivas a este evento ordenadas para que se impriman correctamente en el orden correspondiente
	 * @param IdConsecuencia El ID del evento consecuencia a este al que se asocian las MCRs reactivas
	 * @param lista_mcrs_reactivas Lista de MCRS reactivas ordenadas en sentido de izquierda a derecha
	 */
	public void introduce_mcrs_reactivas(Integer IdConsecuencia, ArrayList<MCR> lista_mcrs_reactivas) {

		MCR_Consecuencias.put(IdConsecuencia, lista_mcrs_reactivas);		
	}

	/**
	 * Introduce las mcrs preventivas a este evento ordenadas para que se impriman correctamente en el orden correspondiente
	 * @param IdCausa El ID del evento causa de este al que se asocian las MCRs preventivas
	 * @param lista_mcrs_preventivas Lista de MCRS reactivas ordenadas en sentido derecha a izquierda según el orden de impresión (desde el evento actual hacia el evento causa)
	 */
	public void introduce_mcrs_preventivas(Integer IdCausa, ArrayList<MCR> lista_mcrs_preventivas) {

		MCR_Causas.put(IdCausa, lista_mcrs_preventivas);
		
	}

	
	/**
	 * Introduce las mcrs reactivas a este evento ordenadas para que se impriman correctamente en el orden correspondiente, almacenando
	 * en cada una de ellas el Id de la consecuencia-el Id de la Causa y el Id de la propia MCR, constituyendo esta terna el id de la mcr procesada
	 * @param idConsecuencia	El ID del evento consecuencia del actual con el cual está relacionado la mcr
	 * @param mcrs_procesadas_reactivas 	Es un ArrayList que almacena todas las MCR procesadas reactivas ordenadas por nº de orden de menor a mayor
	 */
	public void introduce_mcrs_procesadas_reactivas(Integer idConsecuencia, ArrayList<MCR_procesada> mcrs_procesadas_reactivas) {
			
			this.MCR_Consecuencias_procesadas.put(idConsecuencia, mcrs_procesadas_reactivas);
	}

	
	/**
	 * Introduce las mcrs preventivas a este evento ordenadas para que se impriman correctamente en el orden correspondiente, almacenando
	 * en cada una de ellas el Id de la consecuencia-el Id de la Causa y el Id de la propia MCR, constituyendo esta terna el id de la mcr procesada	 
	 * @param idCausa	El ID del evento causa del actual con el cual está relacionado la mcr
	 * @param mcrs_procesadas_preventivas	Es un ArrayList que almacena todas las MCR procesadas preventivas ordenadas por nº de orden de mayor a menor
	 */
	public void introduce_mcrs_procesadas_preventivas(Integer idCausa, ArrayList<MCR_procesada> mcrs_procesadas_preventivas) {

		this.MCR_Causas_procesadas.put(idCausa, mcrs_procesadas_preventivas);
	}

	/**
	 * Inserta en el HashMap de "Estados" el estado de este evento. El estado del evento depende de la consecuencia con la que se relacione este evento,
	 * por eso se pasa como parámetro el Id, para que se pueda realizar correctamente la asociación y no existan problemas a la hora de imprimirlos.
	 * La impresión del estado solo funcionará en los ficheros freemind cuando se impriman las causas, partiendo del evento crítico, pero no se imprimirán 
	 * los estados de los eventos consecuencia del evento crítico, ya que podría haber varias consecuencias para las cuales este evento causa tuviese distintos
	 * estados y no sería posible elegir uno solo. Por este motivo los eventos consecuencia del evento crítico se imprimirán todos en azul, como si estuviesen consolidados.
	 * @param idConsecuencia El Id del evento consecuencia con el que se relaciona este evento. El estado del evento depende del evento consecuencia con el que se relacione 
	 * el evento actual
	 * @param estadoCausa El estado del evento actual, que depende del evento consecuencia con el cual se relacione el evento actual
	 */
	public void insertarEstado(Integer idConsecuencia, String estadoCausa) {
		estados.put(idConsecuencia, estadoCausa);
		
	}

	public HashSet<Integer> getEventosCausaYaImpresos() {
		return eventosCausaYaImpresos;
	}

	@SuppressWarnings("unchecked")
	public void setEventosCausaYaImpresos(HashSet<Integer> eventosCausaYaImpresos) {
		this.eventosCausaYaImpresos = (HashSet<Integer>) eventosCausaYaImpresos.clone();
	}

	public HashSet<Integer> getEventosConsecuenciaYaImpresos() {
		return eventosConsecuenciaYaImpresos;
	}

	@SuppressWarnings("unchecked")
	public void setEventosConsecuenciaYaImpresos(HashSet<Integer> eventosConsecuenciaYaImpresos) {
		this.eventosConsecuenciaYaImpresos = (HashSet<Integer>) eventosConsecuenciaYaImpresos.clone();
	}


	

}
