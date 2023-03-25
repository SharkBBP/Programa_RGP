package Inicio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Subsistema {

	private Integer fila;
	private String idSubsistemaMCR;
	private String SubsistemaMCR;
	private String EstadoSubsistema;
//	private String Componente;		//En un futuro podrían ser HashMaps, que colgarían del subsistema. De componente colgaría 'Elemento' (el siguiente campo)
//	private String Elemento;		//En un futuro podría ser un HashMap que colgaría de Componente. A su vez, de Elemento colgaría el 'Proceso'.
	private HashMap<String,Proceso> procesos = new HashMap<String,Proceso>();
	private RegistroCCM r;
	private StringBuffer logErrores;
	
	
	public Subsistema(String idSubsistemaMCR, RegistroCCM r) {
		
		this.fila = r.getNumero_fila();
		this.idSubsistemaMCR = idSubsistemaMCR;
		this.SubsistemaMCR = r.getSubsistemaMCR();
		this.EstadoSubsistema = r.getEstadoSubsistema();
//		this.Componente = r.getComponente();
//		this.Elemento = r.getElemento();
		this.r = r;
		
	}
	
	/**
	 * Asocia a este Subsistema los distintos procesos con los que está relacionado, conforme a la información incluida en la tabla CausaConsecuenciaMCR
	 * @param r2 el registro correspondiente nº de fila en el que aparece este Subsistema en la tabla
	 * @param responsables2 Es el objeto que contiene todos los metadatos de la tabla de responsables, para poder asociar los responsables que existan a esta MCR procesada (Se usa en una fase posterior
	 * y se procesa esta información después de haber asociado los procesos
	 * @param documentos Es el objeto que contiene todos los documentos de la tabla Documentos, para poder asociar esos documentos a las MCR procesadas, tras haber asociado a la 
	 * misma los responsables. Los responsables tendrán que cumplir los requisitos establecidos en estos documentos
	 */
	public void incorporarProceso(RegistroCCM r2, Responsables responsables, Documentos documentos) {
		if(this.existeCoherencia(r2)) {
			String IdProceso_largo = this.idSubsistemaMCR.toString() + "-" + r2.getProceso();
			
			if( this.procesos.containsKey(IdProceso_largo)) {
				Proceso proceso = this.procesos.get(IdProceso_largo);
				proceso.incorporarResponsable(r2, responsables, documentos);
			}
			else {
				Proceso proceso = new Proceso(IdProceso_largo, r2);
				this.procesos.put(IdProceso_largo, proceso);
				proceso.incorporarResponsable(r2, responsables, documentos);
			}
		}
	}

	
	
	/**
	 * Existe coherencia cuando el Subsistema actual y el del registro que se le pasa como parámetro son iguales y el estado de ambos también es coherente. 
	 * Esta comprobación es necesaria ya que existen muchos registros que deben tener esta misma información (Subsistema y EstadoSubsistema), y en todos los registros 
	 * la información para estos dos parámetros debería ser la misma. Si es incoherente, hay que detectar el punto en el que se produce la incoherencia y corregir 
	 * la base de datos con la información que arroje esta comprobación en el log de errores	 
	 * @param r2	El registro de la tabla CausaConsecuenciaMCR que se quiere comprobar
	 * @return	True si los datos de este objeto son coherentes con los datos que están almacenados en el registro "r2"
	 */
	private boolean existeCoherencia(RegistroCCM r2) {
	
		Boolean coherencia = true;
		
		//Comprobamos que existe coherencia entre el nombre del 'Subsistema' de este objeto y el que está incluido en el registro 'r2'
		if( this.SubsistemaMCR.compareTo(r2.getSubsistemaMCR()) != 0) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un Subsistema: " 
					+ this.SubsistemaMCR + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un Subsistema: " + r2.getSubsistemaMCR() );
			coherencia = false;
		}
		
		
		//Comprobamos que existe coherencia con el Estado del Subsistema entre el objeto actual y el registro pasado como parámetro
		if( this.EstadoSubsistema.compareTo(r.getEstadoSubsistema()) != 0) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un EstadoSubsistema: " 
					+ this.EstadoSubsistema + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un EstadoSubsistema: " + r2.getEstadoSubsistema() );
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
	 * Escribe en el StringBuffer que se pasa como parámetro, la información necesaria para escribir la información de este objeto "Subsistema"
	 * como "atributo" en el fichero FreeMind que se va a generar
	 * @param sb	StringBuffer que almacena todo el texto del archivo del grafo que se quiere imprimir en formato FreeMind
	 */
	public void imprimirAtributoFreeMind(StringBuffer sb) {
		VariablesGlobales vg = VariablesGlobales.getInstance();
		
		if( vg.getImprimirSubsistemas())
			sb.append("<attribute NAME=\"SUBSISTEMA\" VALUE=\"" + this.SubsistemaMCR.toUpperCase() + "\"/>\n");	
		
		this.imprimirProcesosFreeMind(sb);
	}

	/**
	 * Dispara la impresión de la información de los procesos asociados a esta MCR. Los responsables son la tercera información que cuelga de las MCR, tras los responsables,
	 *  posteriormente cuelgan los documentos y artículos
	 * @param sb StringBuffer que se va a utilizar para generar el fichero FreeMind que luego se usará para imprimir el grafo que queremos representar
	 */
	private void imprimirProcesosFreeMind(StringBuffer sb) {

		Collection<Proceso> procs = this.procesos.values();
	
		Iterator<Proceso> it = procs.iterator();
		
		while( it.hasNext()) {
			Proceso p = it.next();
			p.imprimirAtributoFreeMind(sb);
		}
				
	}
	
	public String toString() {
		return this.idSubsistemaMCR;
	}

}
