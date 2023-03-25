package Inicio;


public class MCR {

	private Integer IdMCR;
	private String NombreMCR;
	//private String Descripcion;
	//private ArrayList<String> Subsistemas = new ArrayList<String>();
	private StringBuffer logErrores = new StringBuffer();		//StringBuffer que almacena los errores de ejecución de la clase 

	
	public MCR(Integer IdMCR, String NombreMCR) {
		this.IdMCR = IdMCR;
		this.NombreMCR = NombreMCR;
		//this.Descripcion = Descripcion;
	}
	
	/**
	 * Devuelve un String con la información de la MCR
	 */
	public String toString() {
		return "MCR: " + this.IdMCR + "-" + this.NombreMCR;
	}

	
	/**
	 * Imprime en el StringBuffer que se le pasa como parámetro la información del nodo que constituye la MCR procesada
	 * @param sb StringBuffer que se va a utilizar para generar el fichero FreeMind que luego se usará para imprimir el grafo que queremos representar
	 * @param imprimeCausas	Indica si se quiere que se impriman la información asociada a las causas del evento crítico
	 * @param imprimeConsecuencias Indica si se quiere que se impriman la información asociada a las consecuencias del evento crítico
	 * @param nivelNodo	Indica cuál es el nivel del nodo actual (distancia entre el nodo actual y el nodo raíz-evento crítico) 
	 */
	public void imprimirNodoMCR_FreeMind(StringBuffer sb, Boolean imprimeCausas, boolean imprimeConsecuencias, NivelNodo nivelNodo) {
		//Añadimos la información nuclear del nodo al StringBuffer
		sb.append("<node ");
		nivelNodo.aumentarNivelNodo();

		if (nivelNodo.getNivelNodo() == 1 && imprimeConsecuencias) {
			sb.append("POSITION=\"right\" ");		//Si el nivel del nodo es 1 y hay que imprimir las causas se indica que este nodo se representa a la izquierda
		}
		else if (nivelNodo.getNivelNodo() == 1 && imprimeCausas) {
			sb.append("POSITION=\"left\" ");		//Si el nivel del nodo es 1 y hay que imprimir las consecuencias se indica que este nodo se representa a la derecha
		}
		sb.append("STYLE=\"bubble\" TEXT=\"" + NombreMCR + "\">\n");
		
		//Añadimos la información de los atributos del nodo al StringBuffer
		sb.append("<attribute NAME=\"ID\" VALUE=\"" + IdMCR + "\"/>\n");
		
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

	public Integer getIdMCR() {
		return this.IdMCR;
	}

	public String getNombre() {
		
		return this.NombreMCR;
	}

}
