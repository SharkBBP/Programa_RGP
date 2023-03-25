package Inicio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Responsable_procesado {

	private Integer fila;
	private String IdAreaResponsable_largo;
	private Integer IdAreaResponsable;
	private Responsable responsable;
	private String EstadoIdAreaResponsable;
	private HashMap<String,Documento_procesado> documentos = new HashMap<String,Documento_procesado>();
	private RegistroCCM r;
	private StringBuffer logErrores = new StringBuffer();		//AÑADIR A INFORME DE ERRORES
	
	
	public Responsable_procesado(String idResponsable_largo, RegistroCCM r2, Responsables responsables) {
		
		this.fila = r2.getNumero_fila();
		this.IdAreaResponsable_largo = idResponsable_largo;
		this.EstadoIdAreaResponsable = r2.getEstadoIdAreaResponsable();
		this.IdAreaResponsable = r2.getIdAreaResponsable();
		this.EstadoIdAreaResponsable = r2.getEstadoIdAreaResponsable();
		this.responsable = responsables.getResponsable(IdAreaResponsable);
		this.r = r2;
		
	}

	
	/*
	 * Devuelve este objeto y la información asociada al responsable actual, sin procesar (es decir, sin Id largo, sino el id que le corresponde únicamente en la
	 * tabla de responsables
	 * @param idAreaResponsable	Es el Id del área de la cual queremos obtener el objeto responsable
	 * @return	El objeto responsable correspondiente al id introducido como parámetro
	 *
	private Responsable getResponsable(Integer idAreaResponsable) {
		return this.responsable;
	}

	
	/**
	 * Obtiene el nombre del responsable del objeto actual
	 * @return el nombre del responsable del objeto actual
	 *
	private String getNombreResponsable() {
		return this.responsable.getNombreResponsable();
	}
*/
	
	/**
	 * Asocia a este Responsable procesado los distintos documentos con los que está relacionada, conforme a la información incluida en la tabla CausaConsecuenciaMCR
	 * @param r2 el registro correspondiente nº de fila en el que aparece esta MCR en la tabla
	 * @param documentos2 Es el objeto que contiene todos los documentos de la tabla Documentos, para poder asociar esos documentos a las MCR procesadas, tras haber asociado a la 
	 * misma los responsables. Los responsables tendrán que cumplir los requisitos establecidos en estos documentos
	 */
	public void incorporarDocumento(RegistroCCM r2, Documentos documentos2) {
		if( this.existeCoherencia(r2)) {
			String IdDocumento_largo = this.IdAreaResponsable_largo + "-" + r2.getIdDocumento_de_requisitos();
			
			if( this.documentos.containsKey(IdDocumento_largo)) {
				Documento_procesado documento_p = this.documentos.get(IdDocumento_largo);
				documento_p.incorporarDocumento(r2, documentos2);
			}
			else {
				Documento_procesado documento = new Documento_procesado(IdDocumento_largo, r2, documentos2);
				this.documentos.put(IdDocumento_largo, documento);
				documento.incorporarDocumento(r2, documentos2);
			}
		}
	}


	/**
	 * Existe coherencia cuando el id del responsable actual y el del registro que se le pasa como parámetro son iguales y el estado de ambos también es coherente. 
	 * Esta comprobación es necesaria ya que existen muchos registros que deben tener esta misma información (IdResponsable y EstadoResponsable), y en todos los registros 
	 * la información para estos dos parámetros debería ser la misma. Si es incoherente, hay que detectar el punto en el que se produce la incoherencia y corregir 
	 * la base de datos con la información que arroje esta comprobación en el log de errores	 
	 * 
	 * @param r2	El registro de la tabla CausaConsecuenciaMCR que se quiere comprobar
	 * @return	True si los datos de este objeto son coherentes con los datos que están almacenados en el registro "r2"
	 */
	private boolean existeCoherencia(RegistroCCM r2) {
		
		Boolean coherencia = true;
		
		if( this.IdAreaResponsable != r2.getIdAreaResponsable()) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un IdResponsable: " 
					+ this.IdAreaResponsable + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un IdResponsable: " + r2.getIdAreaResponsable() );
			coherencia = false;
		}
		if( this.EstadoIdAreaResponsable.compareTo(r2.getEstadoIdAreaResponsable()) != 0 ) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un IdEstadoAreaResponsable: " 
					+ this.EstadoIdAreaResponsable + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un EstadoProceso: " + r2.getEstadoIdAreaResponsable() );
			coherencia = false;
		}
		
		return coherencia;

	}
	
	
	/**
	 * Escribe los errores correspondientes a la ejecución de esta clase en el log de errores
	 * @param error El error lógico que se ha producido al ejecutar esta clase 
	 */
	public void escribirError( String error) {
		this.logErrores.append(error + "\n");
	}
	
	/**
	 * Devuelve un StringBuffer con los errores generados durante la ejecución de esta clase
	 * @return un StringBuffer con los errores generados durante la ejecución de esta clase
	 */
	public StringBuffer imprimirErrores() {

		return this.logErrores;
	}

	
	/**
	 * Escribe en el StringBuffer que se pasa como parámetro, la información necesaria para escribir la información de este objeto "Documento"
	 * como "atributo" en el fichero FreeMind que se va a generar
	 * @param sb	StringBuffer que almacena todo el texto del archivo del grafo que se quiere imprimir en formato FreeMind
	 */
	public void imprimirAtributoFreeMind(StringBuffer sb) {

		VariablesGlobales vg = VariablesGlobales.getInstance();

		if( vg.getImprimirResponsables())
			sb.append("<attribute NAME=\"AREA RESPONSABLE\" VALUE=\"" + this.responsable.getNombreResponsable() + "\"/>\n");	
		
		this.imprimirDocumentosFreeMind(sb);
	}

	
	/**
	 * Escribe en el StringBuffer que se pasa como parámetro, la información necesaria para escribir la información de los documentos que están relacionados
	 * con este objeto "Responsable", de forma que puedan visualizarse en el fichero freemind que se va a generar para dibujar el grafo. Los artículos se escriben 
	 * como atributos que se representan asociado a la MCR
	 * @param sb 	StringBuffer que almacena todo el texto del archivo del grafo que se quiere imprimir en formato FreeMind
	 */
	private void imprimirDocumentosFreeMind(StringBuffer sb) {
		
		Collection<Documento_procesado> documentos_p = this.documentos.values();
		
		Iterator<Documento_procesado> it = documentos_p.iterator();
		
		while( it.hasNext()) {
			Documento_procesado dp = it.next();
			dp.imprimirAtributoFreeMind(sb);
		}		
	}
}
