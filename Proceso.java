package Inicio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Proceso {

	private Integer fila;
	private String idProceso; 
	private String nombreProceso;
	private String estadoProceso;
	private HashMap<String,Responsable_procesado> responsables_p = new HashMap<String,Responsable_procesado>();
	private RegistroCCM r;
	private StringBuffer logErrores = new StringBuffer();
	
	
	public Proceso(String idProceso_largo, RegistroCCM r2) {
		this.fila = r2.getNumero_fila();
		this.idProceso = idProceso_largo;
		this.nombreProceso = r2.getProceso();
		this.estadoProceso = r2.getEstadoProceso();
		this.r = r2;
		
	}

	
	/**
	 * Asocia a este Proceso los distintos responsables con los que está relacionado, conforme a la información incluida en la tabla CausaConsecuenciaMCR
	 * @param r2 el registro correspondiente nº de fila en el que aparece este proceso en la tabla
	 * @param responsables2 Es el objeto que contiene todos los metadatos de la tabla de responsables, para poder asociar los responsables que existan a esta MCR procesada (Se usa en una fase posterior
	 * y se procesa esta información después de haber asociado los procesos
	 * @param documentos Es el objeto que contiene todos los documentos de la tabla Documentos, para poder asociar esos documentos a las MCR procesadas, tras haber asociado a la 
	 * misma los responsables. Los responsables tendrán que cumplir los requisitos establecidos en estos documentos
	 */
	public void incorporarResponsable(RegistroCCM r2, Responsables responsables2, Documentos documentos) {
		if(this.existeCoherencia(r2)) {
			String IdResponsable_largo = this.idProceso + "-" + r2.getIdAreaResponsable();
			
			if( this.responsables_p.containsKey(IdResponsable_largo)) {
				Responsable_procesado responsable_p = this.responsables_p.get(IdResponsable_largo);
				responsable_p.incorporarDocumento(r2, documentos);
			}
			else {
				Responsable_procesado responsable_p = new Responsable_procesado(IdResponsable_largo, r2, responsables2);
				this.responsables_p.put(IdResponsable_largo, responsable_p);
				responsable_p.incorporarDocumento(r2, documentos);
			}
		}
	}
	
	
	/**
	 * Existe coherencia cuando el proceso actual y el del registro que se le pasa como parámetro son iguales y el estado de ambos también es coherente. 
	 * Esta comprobación es necesaria ya que existen muchos registros que deben tener esta misma información (Proceso y EstadoProceso), y en todos los registros 
	 * la información para estos dos parámetros debería ser la misma. Si es incoherente, hay que detectar el punto en el que se produce la incoherencia y corregir 
	 * la base de datos con la información que arroje esta comprobación en el log de errores	 
	 * @param r2	El registro de la tabla CausaConsecuenciaMCR que se quiere comprobar
	 * @return	True si los datos de este objeto son coherentes con los datos que están almacenados en el registro "r2"
	 */
	private boolean existeCoherencia(RegistroCCM r2) {
		Boolean coherencia = true;
		
		//Comprobamos que existe coherencia entre el nombre del 'Proceso' de este objeto y el que está incluido en el registro 'r2'
		if( this.nombreProceso.compareTo(r2.getProceso()) != 0) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un Proceso: " 
					+ this.nombreProceso + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un EstadoProceso: " + r2.getProceso() );
			coherencia = false;
		}
		if( this.estadoProceso.compareTo(r2.getEstadoProceso()) != 0 ) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un EstadoProceso: " 
					+ this.estadoProceso + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un EstadoProceso: " + r2.getEstadoProceso() );
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
	 * Escribe en el StringBuffer que se pasa como parámetro, la información necesaria para escribir la información de este "Proceso"
	 * como "atributo" en el fichero FreeMind que se va a generar
	 * @param sb	StringBuffer que almacena todo el texto del archivo del grafo que se quiere imprimir en formato FreeMind
	 */
	public void imprimirAtributoFreeMind(StringBuffer sb) {
			
		VariablesGlobales vg = VariablesGlobales.getInstance();
		
		if( vg.getImprimirProcesos())
			sb.append("<attribute NAME=\"PROCESO\" VALUE=\"" + this.nombreProceso.toUpperCase() + "\"/>\n");	
		
		this.imprimirResponsablesFreeMind(sb);		
	}
	
	
	/**
	 * Dispara la impresión de la información de los responsables asociados a esta MCR. Los Procesos son la segunda información que cuelga de las MCR, tras los Susbsistemas,
	 *  posteriormente cuelgan los responsables, documentos y artículos
	 * @param sb StringBuffer que se va a utilizar para generar el fichero FreeMind que luego se usará para imprimir el grafo que queremos representar
	 */
	private void imprimirResponsablesFreeMind(StringBuffer sb) {

		Collection<Responsable_procesado> resps = this.responsables_p.values();

		Iterator<Responsable_procesado> it = resps.iterator();
		
		while( it.hasNext()) {
			Responsable_procesado r = it.next();
			r.imprimirAtributoFreeMind(sb);
		}
	}


}
