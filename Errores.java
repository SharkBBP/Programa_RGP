package Inicio;




public class Errores {

	StringBuffer logErrores = new StringBuffer();
	EscritorDeFicheros ef = new EscritorDeFicheros();
	
	public Errores() {
		logErrores.append("#####################################\n");
		logErrores.append("#                                   #\n");
		logErrores.append("#  LOG DE ERRORES DE LA APLICACIÓN  #\n");
		logErrores.append("#                                   #\n");
		logErrores.append("#####################################\n\n");
	}
	
	
	public void anexar_errores(StringBuffer sb) {
		logErrores.append(sb);
	}
	
	public void imprimirErrores(String nombreFichero) {
		this.ef.escribir_en_fichero(nombreFichero, logErrores);
	}
}
