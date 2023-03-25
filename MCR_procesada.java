package Inicio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class MCR_procesada {
	
	private String idMCR_procesada;
	private MCR mcr;
	private Integer fila;
	private String estadoIdMCR;
	private Integer ordenMCR;
	//private Integer idMCRsiguiente;
	//private String mCRInicialOFinal;
	private RegistroCCM r;
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 
	private HashMap<String, Subsistema> subsistemas = new HashMap<String, Subsistema>();


	public MCR_procesada(String idMCR_procesada, MCR mcr, RegistroCCM r) {

		this.idMCR_procesada = idMCR_procesada;
		this.mcr = mcr;
		this.fila = r.getNumero_fila();
		this.setR(r);
		this.estadoIdMCR = r.getEstadoIdMCR();
		this.ordenMCR = r.getOrdenMCR();
		//this.idMCRsiguiente = r.getIdMCRsiguiente();
		//this.mCRInicialOFinal = r.getMCRInicialOFinal();
		this.r = r;
	
	}


	/**
	 * Devuelve el registro (fila) de la tabla CausaConsecuenciaMCR a la que está asociada esta MCR procesada (el registro correspondiente nº de fila en el que aparece esta MCR en la tabla)
	 * @return	el registro correspondiente nº de fila en el que aparece esta MCR en la tabla
	 */
	public RegistroCCM getR() {
		return r;
	}

	/**
	 * Devuelve el registro (fila) de la tabla CausaConsecuenciaMCR a la que está asociada esta MCR procesada (el registro correspondiente nº de fila en el que aparece esta MCR en la tabla)
	 * @param r el registro correspondiente nº de fila en el que aparece esta MCR en la tabla
	 */
	public void setR(RegistroCCM r) {
		this.r = r;
	}


	/**
	 * Asocia a esta MCR procesada los distintos subsistemas con los que está relacionada, conforme a la información incluida en la tabla CausaConsecuenciaMCR
	 * @param r	el registro correspondiente nº de fila en el que aparece esta MCR en la tabla
	 * @param responsables Es el objeto que contiene todos los metadatos de la tabla de responsables, para poder asociar los responsables que existan a esta MCR procesada (Se usa en una fase posterior
	 * y se procesa esta información después de haber asociado los procesos
	 * @param documentos Es el objeto que contiene todos los documentos de la tabla Documentos, para poder asociar esos documentos a las MCR procesadas, tras haber asociado a la 
	 * misma los responsables. Los responsables tendrán que cumplir los requisitos establecidos en estos documentos
	 */
	public void incorporarSubsistema(RegistroCCM r, Responsables responsables, Documentos documentos) {
		if (this.existeCoherencia( r )) {
			String IdSubsistema_largo = this.idMCR_procesada + "-" + r.getSubsistemaMCR();
			
			if( this.subsistemas.containsKey(IdSubsistema_largo)) {
				Subsistema sbs = this.subsistemas.get(IdSubsistema_largo);
				sbs.incorporarProceso(r, responsables, documentos);
			}
			else {
				Subsistema subsistema = new Subsistema(IdSubsistema_largo, r);
				this.subsistemas.put(IdSubsistema_largo, subsistema);
				subsistema.incorporarProceso(r, responsables, documentos);
			}
		}
	}


	
	/**
	 * Comprueba que los datos que están incluido en el registro r2 son coherentes con los de este objeto
	 * @param r2	el registro correspondiente nº de fila en el que aparece esta MCR en la tabla
	 * @return True en caso de que los datos entre el registro r2 y este objeto sean coherentes entre sí
	 */
	private Boolean existeCoherencia(RegistroCCM r2) {
		Boolean coherencia = true;
		//Comprobamos que los IdMCR previo e incluido en el nuevo registro son iguales
		String idMcrLargo = r2.getIdConsecuencia() + "-" + r2.getIdCausa() + "-" + r2.getIdMCR();
		if (this.idMCR_procesada.compareTo(idMcrLargo) != 0) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un IdMCR_procesado: " 
					+ this.idMCR_procesada + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un IdMCR_procesado: " + idMcrLargo );
			coherencia = false;
		}
		
		//Comprobamos que la mcr almacenada coincide con la indicada en el registro r2
		if(this.mcr.getIdMCR()-r.getIdMCR() != 0) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un IdMCR: " 
					+ this.mcr.getIdMCR() + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un IdMCR: " + r.getIdMCR() );
			coherencia = false;
		}
		
		//Comprobamos que el estadoIdMCR es coherente
		if( this.estadoIdMCR.compareTo(r.getEstadoIdMCR()) != 0) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un EstadoIdMCR: " 
					+ this.estadoIdMCR + " distinto del de la fila: " + r.getEstadoIdMCR() + " que tiene un EstadoIdMCR: " + r.getIdMCR() );
			coherencia = false;
		}
		
		//Comprobamos que el ordenIdMCR es coherente
		if( this.ordenMCR != r.getOrdenMCR()) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un OrdenMCR: " 
					+ this.ordenMCR + " distinto del de la fila: " + r.getEstadoIdMCR() + " que tiene un OrdenMCR: " + r.getOrdenMCR() );
			coherencia = false;			
		}
	
		return coherencia;
		
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
	 * Imprime la información asociada a esta MCR en una cadena de texto
	 */
	public String toString() {
		return "MCR: " + this.mcr.toString();
	}


	/**
	 * Imprime en el StringBuffer que se le pasa como parámetro la información del nodo que constituye la MCR procesada
	 * @param sb StringBuffer que se va a utilizar para generar el fichero FreeMind que luego se usará para imprimir el grafo que queremos representar
	 * @param imprimeCausas	Indica si se quiere que se impriman la información asociada a las causas del evento crítico
	 * @param imprimeConsecuencias Indica si se quiere que se impriman la información asociada a las consecuencias del evento crítico
	 * @param nivelNodo	Indica cuál es el nivel del nodo actual (distancia entre el nodo actual y el nodo raíz-evento crítico) 
	 */
	public void imprimirNodoMCR_FreeMind(StringBuffer sb, Boolean imprimeCausas, boolean imprimeConsecuencias, NivelNodo nivelNodo) {
		
		String etiquetaFormatoPrefijo = null;		//Es la etiqueta que se va a utilizar antes del texto del nodo para dar formato al mismo. Depende del estado de la misma
		String etiquetaFormatoSufijo = null;		//Es la etiqueta que se va a utilizar después del texto del nodo para dar formato al mismo. Depende del estado de la misma

		//Añadimos la información nuclear del nodo al StringBuffer
		sb.append("\n<node ");
		nivelNodo.aumentarNivelNodo();
		
		//Dependiendo del estado de la MCR se imprime con un color u otro y se seleccionan las etiquetas prefijo y sufijo que van a dar formato al texto del 
		//nodo, en función del estado del mismo
		switch( this.estadoIdMCR) {
			case "Consolidado": 
				sb.append("BACKGROUND_COLOR=\"#FFFFFF\" ");
				etiquetaFormatoPrefijo = "";
				etiquetaFormatoSufijo = "";
				break;
			case "Eliminado": 
				sb.append("BACKGROUND_COLOR=\"#FF9999\" ");
				etiquetaFormatoPrefijo = "<u>";
				etiquetaFormatoSufijo = "</u>";
				break;
			case "Modificado": 
				sb.append("BACKGROUND_COLOR=\"#FFCC99\" ");
				etiquetaFormatoPrefijo = "<i><b>";
				etiquetaFormatoSufijo = "</b></i>";
				break;
			case "Nuevo": 
				sb.append("BACKGROUND_COLOR=\"#CCFFCC\" ");
				etiquetaFormatoPrefijo = "<b>";
				etiquetaFormatoSufijo = "</b>";
				break;
			case "Revision_GT":
				sb.append("BACKGROUND_COLOR=\"#9999FF\" ");
				etiquetaFormatoPrefijo = "<b>";
				etiquetaFormatoSufijo = "</b>";
				break;
			case "Eliminacion_Consolidada":
				sb.append("BACKGROUND_COLOR=\"#000000\" ");
				break;
			default:
				sb.append("BACKGROUND_COLOR=\"#A020F0\" ");
				System.out.println("Formato MCR - Opción no prevista");
				break;				
		}

		if (nivelNodo.getNivelNodo() == 1 && imprimeConsecuencias) {
			sb.append("POSITION=\"right\" ");		//Si el nivel del nodo es 1 y hay que imprimir las causas se indica que este nodo se representa a la izquierda
		}
		else if (nivelNodo.getNivelNodo() == 1 && imprimeCausas) {
			sb.append("POSITION=\"left\" ");		//Si el nivel del nodo es 1 y hay que imprimir las consecuencias se indica que este nodo se representa a la derecha
		}

		sb.append("STYLE=\"bubble\" >\n");
		sb.append("<icon BUILTIN=\"closed\"/>");
		sb.append("<richcontent TYPE=\"NODE\"><html>");
		sb.append("<head>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<p>");
		
		//Formateamos el texto en varias líneas con los formatos requeridos
		formatearTexto( sb, this.mcr.getNombre(), etiquetaFormatoPrefijo, etiquetaFormatoSufijo);

		sb.append("\n</body>");
		sb.append("\n</html>");
		sb.append("\n</richcontent>\n");

		sb.append("<attribute_layout NAME_WIDTH=\"60\" VALUE_WIDTH=\"60\"/>");

		//Añadimos la información de los atributos del nodo al StringBuffer
		sb.append("<attribute NAME=\"ID\" VALUE=\"" + this.mcr.getIdMCR() + "\"/>\n");	
		
		this.imprimirSubsistemasFreeMind(sb);
	}

	
	/**
	 * Permite imprimir el contenido de cada nodo en varias líneas, de forma que sea mucho más cuadrada la celda del nodo y se pueda visualizar merjor el diagrama Freemind
	 * @param sb	El StringBuffer que almacena la información del archivo FreeMind
	 * @param nombreMCR		El nombre asignado a la MCR en la base de datos
	 * @param etiquetaFormatoSufijo 	La etiqueta de apertura de formato del texto para adaptarlo a daltónicos (Negrita, Subrayado, Cursiva, etc) correspondiente a cada estado 
	 * 									Nuevo, Eliminado, Modificado o Consolidado
	 * @param etiquetaFormatoPrefijo 	La etiqueta de apertura de formato del texto para adaptarlo a daltónicos (Negrita, Subrayado, Cursiva, etc) correspondiente a cada estado 
	 * 									Nuevo, Eliminado, Modificado o Consolidado
	 */
	private void formatearTexto(StringBuffer sb, String nombreMCR, String etiquetaFormatoPrefijo, String etiquetaFormatoSufijo) {
			
		Integer longitudMaximaLinea = 20;
		String[] palabras = nombreMCR.trim().split(" ");
		Integer longitudLinea = 0;
		Integer numeroPalabras = palabras.length;
		String palabra;
		Boolean anadirPalabra = true;
		
			
		//sb.append("STYLE=\"bubble\" >\n");
		sb.append("\n<richcontent TYPE=\"NODE\">\n<html>");
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
			sb.append("\n" + etiquetaFormatoPrefijo + linea + etiquetaFormatoSufijo);
			sb.append("\n</p>");
		}
		
		/*
		 * 		//sb.append("STYLE=\"bubble\" >\n");
		sb.append("\n<richcontent TYPE=\"NODE\">\n<html>");
		sb.append("\n\t<head>\n\t</head>");	
		sb.append("\n\t<body>");
		
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
			sb.append("\n\t\t<p>");
			sb.append("\n\t\t\t" + etiquetaFormatoPrefijo + linea + etiquetaFormatoSufijo);
			sb.append("\n\t\t</p>");
		}
		 */
	}

	/**
	 * Dispara la impresión de la información de los subsistemas asociados a esta MCR. Los Subsistemas es la primera información que cuelga de las MCR, posteriormente
	 * cuelgan los procesos, responsables, documentos y artículos
	 * @param sb StringBuffer que se va a utilizar para generar el fichero FreeMind que luego se usará para imprimir el grafo que queremos representar
	 */
	private void imprimirSubsistemasFreeMind(StringBuffer sb) {
		Collection<Subsistema> subs = this.subsistemas.values();
		
		Iterator<Subsistema> it = subs.iterator();
		
		while( it.hasNext()) {
			Subsistema s = it.next();
			s.imprimirAtributoFreeMind(sb);
		}
		
	}
}
