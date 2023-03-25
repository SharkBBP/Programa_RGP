package Inicio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Documento_procesado {
	
	private Integer fila;
	private String idDocumento_largo;
	private Integer idDocumento;
	private String estadoDocumento;
	//private String articulos;
	private Documento documento;
	//private String estadoArticulos;
	private RegistroCCM r;
	private HashMap<String, String> articulos = new HashMap<String, String>();
	private StringBuffer logErrores = new StringBuffer();


	public Documento_procesado(String idDocumento_largo, RegistroCCM r2, Documentos documentos2) {
		this.fila = r2.getNumero_fila();
		this.idDocumento_largo = idDocumento_largo;
		this.idDocumento = r2.getIdDocumento_de_requisitos();
		//this.articulos = r2.getArticulos();
		//this.estadoArticulos = r2.getEstadoArticulos();
		this.documento = documentos2.getDocumento(idDocumento);
		this.r = r2;
		
	}
	
	

	/**
	 * Carga el id del documento indicado en la estructura de datos, de forma que se puedan imprimir los datos asociados al mismo cuando 
	 * se genere el grafo correspondiente
	 * @param r2 	Es el registro que contiene todos los datos de la tabla CausaConsecuenciaMCR
	 * @param documentos2	Es la "tabla" de datos de documentos, que contiene todos los documentos con sus metadatos, para que se puedan incluir en la estructura de datos 
	 * que se va a utilizar para imprimir
	 */
	public void incorporarDocumento(RegistroCCM r2, Documentos documentos2) {
		if( this.existeCoherencia(r2)) {
			String IdArticulo_largo = this.idDocumento_largo + "-" + r2.getArticulos();
			
			if( this.articulos.containsKey(IdArticulo_largo)) {
				String articulo = this.articulos.get(IdArticulo_largo);
				articulo.concat("\n" + r2.getArticulos());
			}
			else {
				String articulo = r2.getArticulos();
				this.articulos.put(IdArticulo_largo, articulo);
				articulo.concat( r2.getArticulos());
				//System.out.println(articulo);
			}
		}
	}	
	
	
	/**
	 * Existe coherencia cuando el id del documento actual y el del registro que se le pasa como parámetro son iguales y el estado de ambos también es coherente. 
	 * Esta comprobación es necesaria ya que existen muchos registros que deben tener esta misma información (IdDocumento y EstadoDocumento), y en todos los registros 
	 * la información para estos dos parámetros debería ser la misma. Si es incoherente, hay que detectar el punto en el que se produce la incoherencia y corregir 
	 * la base de datos con la información que arroje esta comprobación en el log de errores
	 * @param r2	El registro de la tabla CausaConsecuenciaMCR que se quiere comprobar
	 * @return	True si los datos de este objeto son coherentes con los datos que están almacenados en el registro "r2"
	 */
	private boolean existeCoherencia(RegistroCCM r2) {
		
		Boolean coherencia = true;
		
		if( this.idDocumento != r2.getIdDocumento_de_requisitos()) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un IdDocumento_de_requisitos: " 
					+ this.idDocumento + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un IdDocumento_de_requisitos: " + r2.getIdDocumento_de_requisitos() );
			coherencia = false;
		}
		if(this.estadoDocumento != null && this.estadoDocumento.compareTo(r2.getEstadoIdDocumento()) != 0 ) {
			this.escribirError("Error: Tabla: CausaConsecuenciaMCR - La fila " + this.fila + " tiene un EstadoIdDocumento: " 
					+ this.estadoDocumento + " distinto del de la fila: " + r.getNumero_fila() + " que tiene un EstadoIdDocumento: " + r2.getEstadoIdDocumento() );
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
		
		if (this.documento != null && vg.getImprimirDocumentos())
			sb.append("<attribute NAME=\"Documento\" VALUE=\"" + this.documento.getTitulo() + "\"/>\n");	
		
		this.imprimirDocumentosFreeMind(sb);		
	}


	/**
	 * Escribe en el StringBuffer que se pasa como parámetro, la información necesaria para escribir la información de los artículos que están relacionados
	 * con este objeto "Documento", de forma que puedan visualizarse en el fichero freemind que se va a generar para dibujar el grafo. Los artículos se escriben 
	 * como atributos que se representan asociado a la MCR
	 * @param sb 	StringBuffer que almacena todo el texto del archivo del grafo que se quiere imprimir en formato FreeMind
	 */
	private void imprimirDocumentosFreeMind(StringBuffer sb) {

		Collection<String> articulos_p = this.articulos.values();
		VariablesGlobales vg = VariablesGlobales.getInstance();

		Iterator<String> it = articulos_p.iterator();
		
		String cadena_articulos = "";
		
		if( vg.getImprimirDocumentos()) {
			while( it.hasNext()) {
				String c_a = it.next();
				cadena_articulos = cadena_articulos + c_a+ "\n";
				//cadena_articulos.concat("\n");
				sb.append("<attribute NAME=\"Artículos\" VALUE=\"" + cadena_articulos + "\"/>\n");	
			}	
		}
	}
	

}
