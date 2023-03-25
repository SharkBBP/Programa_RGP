package Inicio;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Esta clase tiene como funci�n principal analizar los argumentos introducidos por el usuario al ejecutar el programa y generar, a partir 
 * de los citados argumentos, la consulta apropiada a realizar en la base de datos, para generar posteriormente el grafo que corresponda 
 * representar en funci�n de lo que quiera visualizar el usuario
 * @author Aitor Fajardo
 *
 */
public class AnalizadorArgumentos {
	
	String[] args;
	//HashMap<String, String> argumentos;
	//Boolean argumentosCorrectos = true;
	Boolean imprimeFM = false;						//Si es "true" Indica que se quiere generar una consulta para un fichero FreeMind 
	Boolean imprimeGV = false;						//Si es "true" Indica que se quiere generar una consulta para un fichero GraphViz 
	Integer eventoCritico = null;					//Almacena el Id del evento cr�tico a partir del cual se va a generar el grafo
	Boolean imprimeSoloEventos = false;				//si es "true" indica que se impriman �nicamente los eventos de seguridad, no las MCR
	
	Boolean imprimeSubsistemas = false;				//Si es "true" indica que se imprima la informaci�n relativa a los Subsistemas
	Boolean imprimeSbInfraestructura = false;		//Si es "true" indica que se imprima la informaci�n relativa al subsistema Infraestructura
	Boolean imprimeSbEnergia = false;				//Si es "true" indica que se imprima la informaci�n relativa al subsistema Energ�a
	Boolean imprimeSbCMStierra= false;				//Si es "true" indica que se imprima la informaci�n relativa al subsistema CMStierra
	Boolean imprimeSbCMSABordo = false;				//Si es "true" indica que se imprima la informaci�n relativa al subsistema CMS a bordo
	Boolean imprimeSbExplotacion = false;			//Si es "true" indica que se imprima la informaci�n relativa al subsistema Explotaci�n y gesti�n del tr�fico
	Boolean imprimeSbMantenimiento = false;			//Si es "true" indica que se imprima la informaci�n relativa al subsistema Mantenimiento
	Boolean imprimeSbMaterialRodante = false;		//Si es "true" indica que se imprima la informaci�n relativa al subsistema MaterialRodante
	
	Boolean imprimeProcesos = false;				//Si es "true" indica que se imprima la informaci�n relativa a los procesos
	Boolean imprimePrDiseno = false;				//Si es "true" indica que se imprima la informaci�n relativa a los procesos de dise�o
	Boolean imprimePrConstruccion = false;			//Si es "true" indica que se imprima la informaci�n relativa a los procesos de construcci�n
	Boolean imprimePrPuestaEnServicio = false;		//Si es "true" indica que se imprima la informaci�n relativa a los procesos de puesta en servicio
	Boolean imprimePrExplotacion = false;			//Si es "true" indica que se imprima la informaci�n relativa a los procesos de explotaci�n y gesti�n del tr�fico
	Boolean imprimePrMantenimiento = false;			//Si es "true" indica que se imprima la informaci�n relativa a los procesos de mantenimiento
	
	Boolean imprimeResponsables = false;			//Si es "true" indica que se imprima la informaci�n relativa a las �reas Responsables
	ArrayList<Integer> areasResponsablesSeleccionadas = new ArrayList<Integer>();		//Almacena el listado de �reas Responsables
	
	Boolean imprimeDocumentos = false;				//Si es "true" indica que se imprima la informaci�n relativa a los documentos que dan soporte a los requisitos
	ArrayList<Integer> documentosSeleccionados = new ArrayList<Integer>();				//Almacena el listado de documentos a utilizar
	
	Responsables responsables;
	Documentos documentos;
	VariablesGlobales vg = VariablesGlobales.getInstance();
	
	//Envolvemos la consulta base d�nde lo que buscamos son datos no nulos en otra consulta en d�nde vamos a filtrar los datos pasados por par�metro		
	private String consulta = "SELECT *"		//Consulta que se quiere hacer a la tabla "CausaConsecuenciaMCR", para representar posteriormente la informaci�n indicada en la misma en el fichero freemind
			+ " FROM CausaConsecuenciaMCR"
			//Solamente procesamos los registros que tienen todos los datos completos (Excepto 'Componente' y 'Elemento')
			+ " WHERE  CausaConsecuenciaMCR.IdConsecuenciaCausa IS NOT NULL AND CausaConsecuenciaMCR.IdConsecuencia IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdConsecuencia IS NOT NULL AND CausaConsecuenciaMCR.IdCausa IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdCausa IS NOT NULL AND CausaConsecuenciaMCR.IdMCR IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdMCR IS NOT NULL AND CausaConsecuenciaMCR.OrdenMCR IS NOT NULL AND CausaConsecuenciaMCR.SubsistemaMCR IS NOT NULL AND CausaConsecuenciaMCR.EstadoSubsistema IS NOT NULL AND CausaConsecuenciaMCR.Proceso IS NOT NULL AND CausaConsecuenciaMCR.EstadoProceso IS NOT NULL AND CausaConsecuenciaMCR.IdDocumento_de_requisitos IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdDocumento IS NOT NULL AND CausaConsecuenciaMCR.Articulos IS NOT NULL AND CausaConsecuenciaMCR.EstadoArticulos IS NOT NULL AND CausaConsecuenciaMCR.IdAreaResponsable IS NOT NULL AND CausaConsecuenciaMCR.EstadoIdAreaResponsable IS NOT NULL";
			
	
		
	/**
	 * Constructor de la clase. Inicia tambi�n el proceso de an�lisis de los argumentos introducidos
	 * @param args Lista de argumentos introducidos al ejecutar el programa
	 * @param responsables Conjunto de responsables considerados en la base de datos (Es la informaci�n obtenida de la tabla Areas Responsables de la BD)
	 * @param documentos Conjunto de documentos establecidos en la tabla "Documentos" de la BD
	 *
	public AnalizadorArgumentos(String[] args, Responsables responsables, Documentos documentos) {
		this.args = args;								//Lista de argumentos introducidos al ejecutar el programa
		this.responsables = responsables;				//Conjunto de responsables considerados en la base de datos (Es la informaci�n obtenida de la tabla Areas Responsables de la BD)
		this.documentos = documentos;					//Conjunto de documentos establecidos en la tabla "Documentos" de la BD
		procesarArgumentos();							//Inicia el proceso de an�lisis de los argumentos introducidos por el usuario al ejecutar el programa
		//this.consulta = this.generarConsulta();			//Genera la consulta adecuada tras haber procesado los argumentos que ha introducido el usuario al ejecutar el programa
		
	}*/

	/**
	 * Constructor de la clase. Inicia tambi�n el proceso de an�lisis de los argumentos introducidos
	 * @param args Lista de argumentos introducidos al ejecutar el programa
	 */
	public AnalizadorArgumentos(String[] args) {

		this.args = args;								//Lista de argumentos introducidos al ejecutar el programa
		//procesarArgumentos();							//Inicia el proceso de an�lisis de los argumentos introducidos por el usuario al ejecutar el programa
		//establecerVariablesDeImpresion();
		//this.consulta = this.generarConsulta();			//Genera la consulta adecuada tras haber procesado los argumentos que ha introducido el usuario al ejecutar el programa
		
	}


	private void establecerVariablesDeImpresion() {
		vg.setImprimirSubsistemas(imprimeSubsistemas);
		vg.setImprimirDocumentos(imprimeDocumentos);
		vg.setImprimirProcesos(imprimeProcesos);
		vg.setImprimirResponsables(imprimeResponsables);
	}


	/**
	 * Llama a los distintos subprocesos para establecer los distintos criterios de filtrado que haya introducido el usuario e ir configurando la consulta a la BD conforme a los criterios
	 * de filtrado que ha introducido el usuario
	 * @return La consulta que se va a realizar a la BD, conforme a los criterios de filtrado empleados por el usuario
	 */
	private String generarConsulta() {
		
		if( this.imprimeSubsistemas) {
			filtrarSubsistemas();
		}
		if( this.imprimeProcesos) {
			filtrarProcesos();
		}
		if( this.imprimeResponsables) {
			filtrarResponsables();
		}
		if( this.imprimeDocumentos) {
			filtrarDocumentos();
		}
		
		return this.consulta;
	}


	/**
	 * A�ade a la consulta un AND con los distintos subsistemas que se quiere que se representen en el grafo
	 */
	private void filtrarSubsistemas() {
		
		int contador = 0;
		
		consulta = consulta + " AND ( ";		//Incluimos las condiciones del filtrado de subsistemas en la consulta
		
		if( this.imprimeSbCMSABordo) {
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.SubsistemaMCR = \"Control, Mando y Se�alizaci�n a bordo\" ";
			contador++;
		}
		if( this.imprimeSbCMStierra) {
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.SubsistemaMCR = \"Control, Mando y Se�alizaci�n en tierra\" ";
			contador++;
		}
		if( this.imprimeSbEnergia) {
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.SubsistemaMCR = \"Energ�a\" ";
			contador++;
		}
		if( this.imprimeSbExplotacion) {
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.SubsistemaMCR = \"Explotaci�n y gesti�n del tr�fico\" ";
			contador++;
		}
		if( this.imprimeSbInfraestructura) {
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.SubsistemaMCR = \"Infraestructura\" ";
			contador++;
		}
		if( this.imprimeSbMantenimiento) {
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.SubsistemaMCR = \"Mantenimiento\" ";
			contador++;
		}
		if( this.imprimeSbMaterialRodante) {
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.SubsistemaMCR = \"Material Rodante\" ";
			contador++;
		}
		
		consulta = consulta + " ) ";		//Cerramos el condicionado de filtrado de Subsistemas en la consulta
	}

	
	/**
	 * A�ade a la consulta un AND con los distintos responsables que se quiere que se representen en el grafo
	 */
	private void filtrarResponsables() {
	
		consulta = consulta + " AND ( ";		//Incluimos las condiciones del filtrado de responsables en la consulta
		
		int contador = 0;
		Integer idResponsable = null;
		Iterator<Integer> it = this.areasResponsablesSeleccionadas.iterator();
		
		while( it.hasNext()) {
			idResponsable = it.next();
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.IdAreaResponsable = " + idResponsable;
			contador++;
		}

		
		consulta = consulta + " ) ";		//Cerramos el condicionado de filtrado de responsables en la consulta

	}

	/**
	 * A�ade a la consulta un AND con los distintos documentos que se quiere que se representen en el grafo
	 */
	private void filtrarDocumentos() {
		
		consulta = consulta + " AND ( ";		//Incluimos las condiciones del filtrado de documentos en la consulta
		
		int contador = 0;
		Integer idDocumento = null;
		Iterator<Integer> it = this.documentosSeleccionados.iterator();
		
		while( it.hasNext()) {
			idDocumento = it.next();
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.IdDocumento_de_requisitos = " + idDocumento;	
			contador++;
		}

		consulta = consulta + " ) ";		//Cerramos el condicionado de filtrado de responsables en la consulta

	}

	/**
	 * A�ade a la consulta un AND con los distintos procesos que se quiere que se representen en el grafo
	 */
	private void filtrarProcesos() {
		
		consulta = consulta + " AND ( ";		//Incluimos las condiciones del filtrado de procesos en la consulta
		
		int contador = 0;
		
		if( this.imprimePrConstruccion) {
			consulta = consulta + imprimeOr(contador) + " CausaConsecuenciaMCR.Proceso = \"Construcci�n\" ";
			contador++;
		}
		if( this.imprimePrDiseno) {
			consulta = consulta + imprimeOr(contador) +" CausaConsecuenciaMCR.Proceso = \"Dise�o\" ";
			contador++;
		}
		if( this.imprimePrExplotacion) {
			consulta = consulta + imprimeOr(contador) +" CausaConsecuenciaMCR.Proceso = \"Explotaci�n y gesti�n del tr�fico\" ";
			contador++;
		}
		if( this.imprimePrMantenimiento) {
			consulta = consulta + imprimeOr(contador) +" CausaConsecuenciaMCR.Proceso = \"Mantenimiento\" ";
			contador++;
		}		
		if( this.imprimePrPuestaEnServicio) {
			consulta = consulta + imprimeOr(contador) +" CausaConsecuenciaMCR.Proceso = \"Puesta en Servicio\" ";
			contador++;
		}
		
		consulta = consulta + " ) ";		//Cerramos el condicionado de filtrado de procesos en la consulta

	}

	/**
	 * Funci�n auxiliar que en funci�n del valor del contador introducido como par�metro a�ade un OR a la cadena de consulta o no
	 * @param contador Si es !=0 a�ade un "OR" a la cadena de consulta
	 * @return
	 */
	private String imprimeOr(int contador) {
		String cadena = "";
		if( contador != 0) {
			cadena = " OR ";
		}
		
		return cadena;
	}

	
	/**
	 * M�todo que permite procesar los argumentos recibidos para poder asignarlos a las variables correspondientes y poder generar, en conjunci�n con el resto de m�todos de la clase, la consulta comentada.
	 */
	public void procesarArgumentos() {
		
		String argumento = null;
		String opcion = null;
		String parametro = null;
		
		for( int i = 0; i < this.args.length; i++) {
			argumento = this.args[i];
			
			if (esOpcionValida(argumento)){
				opcion = argumento;
				
				if( noNecesitaParametrosAdicionales(opcion) ) {
					configurarOpciones( opcion, null);
				}
			}
			else if ( !esOpcionValida(argumento) && esParametroValido( opcion, argumento )) {
				parametro = argumento;
				configurarOpciones( opcion, parametro);
			}
			else if ( !esOpcionValida(argumento) && !esParametroValido( opcion, argumento )) {
					System.out.println("El par�metro: " + argumento + " utilizado en la opci�n " + opcion + " no es un par�metro v�lido");
					imprimirAyuda();
			}
			else {
				System.out.println("La opci�n " + opcion + " no es un par�metro v�lido");
				imprimirAyuda();
			}
		}
		
		
		establecerVariablesDeImpresion();
	}

	
	/**
	 * Devuelve un booleano indicando, si en funci�n del par�metro analizado ser�an necesarios par�metros adicionales que complementasen el par�metro introducido
	 * @param opcion
	 * @return
	 */
	private boolean noNecesitaParametrosAdicionales(String opcion) {
		switch (opcion) {
			case "-e":
			case "-gv":
				return true;
			default:
				return false;
		}
	}

	/**
	 * Configura el valor de las variables necesarias para generar las consultas y establecer las opciones de impresi�n del grafo
	 * @param opcion Las opciones son todas las que est�n indicadas con un gui�n "-" como prefijo delante del argumento introducido
	 * @param argumento son los argumentos que no van precedidos de gui�n "-" delante del mismo. Sirven generalmente para particularizar las opciones introducidas, estableciendo, por ejemplo, criterios de filtrado
	 */
	private void configurarOpciones(String opcion, String argumento) {
		switch ( opcion ) {
			case "-fm":
				this.imprimeFM = true;
				this.eventoCritico = Integer.valueOf(argumento);
				break;
			case "-e":
				this.imprimeSoloEventos = true;
				break;
			case "-s":
				this.imprimeSubsistemas = true;
				switch ( argumento ) {
					case "i":
						this.imprimeSbInfraestructura = true;
						break;
					case "e":
						this.imprimeSbEnergia = true;
						break;
					case "ct":
						this.imprimeSbCMStierra = true;
						break;
					case "cb":
						this.imprimeSbCMSABordo = true;
						break;
					case "ex":
						this.imprimeSbExplotacion = true;
						break;
					case "m":
						this.imprimeSbMantenimiento = true;
						break;
					case "mr":
						this.imprimeSbMaterialRodante = true;
						break;
					case "t":
						this.imprimeSbInfraestructura = true;
						this.imprimeSbEnergia = true;
						this.imprimeSbCMStierra = true;
						this.imprimeSbCMSABordo = true;
						this.imprimeSbExplotacion = true;
						this.imprimeSbMantenimiento = true;
						this.imprimeSbMaterialRodante = true;
						break;
					case "n":
						this.imprimeSubsistemas = false;
						break;
					default:
						break;
				}
				break;
			case "-p":
				this.imprimeProcesos = true;
				switch ( argumento ) {
					case "d":
						this.imprimePrDiseno = true;
						break;
					case "c":
						this.imprimePrConstruccion = true;
						break;
					case "pes":
						this.imprimePrPuestaEnServicio = true;
						break;
					case "ex":
						this.imprimePrExplotacion = true;
						break;
					case "m":
						this.imprimePrMantenimiento = true;
						break;
					case "t":
						this.imprimePrDiseno = true;
						this.imprimePrConstruccion = true;
						this.imprimePrPuestaEnServicio = true;
						this.imprimePrExplotacion = true;
						this.imprimePrMantenimiento = true;
						break;
					case "n":
						this.imprimeProcesos = false;
						break;
					default:
						break;
				}
				break;
			case "-r":
				this.imprimeResponsables = true;
				if( argumento.compareTo("n") == 0 ){
					this.imprimeResponsables = false;
				}
				else if( argumento.compareTo("t") == 0) {
					this.areasResponsablesSeleccionadas = this.responsables.obtenerIds();
				}
				else if( esResponsableValido(argumento)) {
					this.areasResponsablesSeleccionadas.add(Integer.valueOf(argumento));
				}
				else {
					System.out.println("El IdResponsable indicado: " + argumento + " no es v�lido. No ser� tenido en cuenta en la representaci�n del �rbol");
				}
				break;
			case "-d":
				this.imprimeDocumentos = true;
				if( argumento.compareTo("n") == 0 ){
					this.imprimeDocumentos = false;
				}
				else if( argumento.compareTo("t") == 0) {
					this.documentosSeleccionados = this.documentos.obtenerIds();
				}
				else if( esDocumentoValido(argumento)) {
					this.documentosSeleccionados.add(Integer.valueOf(argumento));
				}
				else {
					System.out.println("El IdDocumento indicado: " + argumento + " no es v�lido. No ser� tenido en cuenta en la representaci�n del �rbol");
				}
				break;
			case "-gv":
				this.imprimeGV = true;
				break;

			default:
				break;
		}

	}		

	
	/**
	 * Indica si el Id del documento introducido como par�metro es un documento v�lido (Si hay un documento con esa Id dentro de la tabla de documentos en la BD 
	 * @param argumento Id del documento
	 * @return "true" si el id del documento es v�lido
	 */
	private boolean esDocumentoValido(String argumento) {
		return this.documentos.existeDocumento(Integer.valueOf(argumento));
	}

	
	/**
	 * Indica si el Id del responsable introducido como par�metro es un documento v�lido (Si hay un documento con esa Id dentro de la tabla de documentos en la BD 
	 * @param argumento Id del responsable
	 * @return "true" si el id del responsable es v�lido
	 */
	private boolean esResponsableValido(String argumento) {
		if ( !argumento.contentEquals("n") )
			return this.responsables.existeResponsable(Integer.valueOf(argumento));
		else
			return true;
	}

	private void imprimirAyuda() {
		System.out.println( "***  AYUDA  ***\n"
				+ "\tImprimir ficheros FreeMind:\n"
				+ "\t\t -fm 'IdEvento' [-e] | [-s {i, e, ct, cb, ex, m, mr, t} | -p { d, c, pes, ex, m, t} | -r {Ids Areas Responsables} | -d {Ids Documentos} ]");

		System.out.println("\n\n-fm  -  Imprime el arbol de eventos causa y consecuencia en formato FreeMind");
		System.out.println("\n\t'IdEvento'  -  Es el Id del evento cr�tico que va a ser la ra�z a partir de la cual se representan los eventos causantes y sus eventos consecuencia");

		System.out.println("\n-e  -  Imprime solamente el �rbol de eventos consecuencia y causa del evento indicado. Esta opci�n no requiere par�metros adicionales.");

		System.out.println("\n-s  -  Permite seleccionar los distintos subsistemas de las MCR que se quieren representar en el �rbol. Se debe indicar alguna o varias de las siguientes opciones:");
		System.out.println("\n\ti  -  Infraestructura");
		System.out.println("\n\te  -  Energ�a");
		System.out.println("\n\tct  -  CMS en tierra");
		System.out.println("\n\tcb  -  CMS a bordo");
		System.out.println("\n\tex  -  Explotaci�n y gesti�n del tr�fico");
		System.out.println("\n\tm  -  Mantenimiento");
		System.out.println("\n\tmr  -  Material Rodante");
		System.out.println("\n\tt  -  Todos los subsistemas");
		System.out.println("\n\tn  -  No imprimir la informaci�n relativa a los Subsistemas");

		System.out.println("\n-p  -  Permite seleccionar los distintos procesos que se desean filtrar para su representaci�n en el �rbol. Se deben indicar alguna o varias de las siguientes opciones");
		System.out.println("\n\td  -  Dise�o");
		System.out.println("\n\tc  -  Construcci�n");
		System.out.println("\n\tpes  -  Puesta en Servicio");
		System.out.println("\n\tex  -  Explotaci�n y gesti�n del tr�fico");
		System.out.println("\n\tm  -  Mantenimiento");
		System.out.println("\n\tt  -  Todos");
		System.out.println("\n\tn  -  No imprimir la informaci�n relativa a los Procesos");
		
		System.out.println("\n-r  -  Permite seleccionar las �reas responsables de aplicar las MCR. Se pueden seleccionar varias de estas �reas indicando sus ids");
		System.out.println("\n\t{Ids Areas Responsables}  -  Son los ids de las distintas �reas responsables cuyas MCR asociadas queremos representar en el Grafo");
		System.out.println("\n\tn  -  No imprimir la informaci�n relativa a los Responsables");

		System.out.println("\n-d  -  Permite seleccionar los documentos asociados que queremos filtrar para que se representen �nicamente las MCR que est�n relacionadas con los mismos");
		System.out.println("\n\t{Ids Documentos}  -  Son los ids de los distintos documentos asociados a las MCR que queremos representar en el grafo");
		System.out.println("\n\tn  -  No imprimir la informaci�n relativa a los Documentos");

	}

	
	/**
	 * Comprueba si los par�metros introducidos son par�metros v�lidos para el sistema y se puede procesar los argumentos introducidos por el usuario 
	 * @param opcion Es la opci�n indicada en los argumentos que se va a analizar (Las opciones son los argumentos que van precedidos por un gui�n "-")
	 * @param argumento Es el argumento introducido por el usuario que no va precedido por un gui�n. Generalmente sirven para filtrar el contenido que se quiere mostrar en los grafos que se exporten)
	 * @return "true" si el par�metro analizado es un par�metro v�lido que se puede interpretar correctamente por la aplicaci�n
	 */
	private boolean esParametroValido(String opcion, String argumento) {
		switch ( opcion) {
			case "-fm":
				try {
					Integer.valueOf(argumento);
					return true;
				}
			   catch (NumberFormatException ex){
				   System.out.println( "El par�metro: " + argumento + " no es un n�mero. Debe introducir un n�mero (IdEvento) v�lido para ejecutar la aplicaci�n");
				   ex.printStackTrace();
		           return false;
		        }
			case "-e":			//En caso de que se quiera imprimir solo los eventos no deber�a haber ning�n argumento detr�s
					return true;
			case "-s":
				switch ( argumento ) {
					case "i":
					case "e":
					case "ct":
					case "cb":
					case "ex":
					case "m":
					case "mr":
					case "t":
					case "n":
						return true;
					default: 
						return false;
				}
			case "-p":
				switch ( argumento ) {
					case "d":
					case "c":
					case "pes":
					case "ex":
					case "m":
					case "t":
					case "n":
						return true;
					default: 
						return false;
			}
			case "-r":
				try {
					if (argumento.contentEquals("n") ) {
						return true;
					}
					else if (argumento.contentEquals("t") ) {
						return true;
					}
					else {
						Integer.valueOf(argumento);
						return true;
					}
				}
			   catch (NumberFormatException ex){
				   System.out.println( "El par�metro: " + argumento + " no es un n�mero. Debe introducir un n�mero (IdAreaResponsable) v�lido para ejecutar la aplicaci�n");
				   ex.printStackTrace();
		           return false;
		        }
			case "-d":
				try {
					if (argumento.contentEquals("n")) {
						return true;
					}
					else if (argumento.contentEquals("t") ) {
						return true;
					}
					else {
						Integer.valueOf(argumento);
						return true;
					}
				}
			   catch (NumberFormatException ex){
				   System.out.println( "El par�metro: " + argumento + " no es un n�mero. Debe introducir un n�mero (IdDocumento) v�lido para ejecutar la aplicaci�n");
				   ex.printStackTrace();
		           return false;
		        }
		default:
				return false;
		
		}
	}

	/**
	 * Permite comprobar si los argumentos introducidos como opciones por el usuario (argumentos precedidos por un gui�n "-") son v�lidos para poder ser interpretados por el programa
	 * @param argumento El argumento introducido por el usuario al ejecutar el programa, del cual se quiere comprobar si es una opci�n v�lida para ser procesada por el programa
	 * @return "true" si el argumento es un argumento v�lido
	 */
	private boolean esOpcionValida(String argumento) {
		switch ( argumento ) {
			case "-fm":
			case "-e":
			case "-s":
			case "-p":
			case "-r":
			case "-d":
			case "-gv":
				return true;
		default:
				return false;
		}
	}

	/**
	 * Devuelve la consulta SQL generada a partir del proceso de las opciones y argumentos utilizados por el usuario al ejecutar el programa
	 * @return La consulta SQL generada a partir del proceso de las opciones y argumentos utilizados por el usuario al ejecutar el programa
	 */
	public String obtenerConsulta() {
		this.generarConsulta();
		return this.consulta ;
	}

	/**
	 * Indica si se quiere imprimir solamente los eventos de seguridad o se quiere imprimir tambi�n las MCR en el grafo
	 * @return "true" si se quiere imprimir solamente los eventos de seguridad y no se quieren imprimir las MCR en el grafo
	 */
	public Boolean imprimirSoloEventos() {
		return this.imprimeSoloEventos;
	}

	
	/**
	 * Devuelve el Id del evento cr�tico a partir del cual se va a generar el grafo de eventos y mcrs 
	 * @return el Id del evento cr�tico a partir del cual se va a generar el grafo de eventos y mcrs
	 */
	public int getEventoCritico() {
		return this.eventoCritico;
	}


	public void setResponsables(Responsables responsables) {
		this.responsables = responsables;
	}


	public void setDocumentos(Documentos documentos) {
		this.documentos = documentos;
	}


	public Boolean getImprimirSubsistemas() {
		return this.imprimeSubsistemas;
	}


	public Boolean getImprimirProcesos() {
		return this.imprimeProcesos;
	}


	public Boolean getImprimirResponsables() {
		return this.imprimeResponsables;
	}


	public Boolean getImprimirDocumentos() {
		return this.imprimeDocumentos;
	}
}
