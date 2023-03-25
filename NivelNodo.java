package Inicio;

/**
 * Esta clase se crea para poder guardar el nivel de los nodos y pasarlos como referencia, de forma que se pueda conocer y actualizar en cada momento
 * el nivel del nodo, tanto de los nodos mcr como el nivel del evento de seguridad, para imprimir solamente hasta las causas o consecuencias de un determinado nivel
 * @author AFB
 *
 */
public class NivelNodo {

	private Integer i;
	
	public NivelNodo(Integer i) {
		this.i = i;
	}
	
	public void aumentarNivelNodo() {
		this.i++;
	}

	public void disminuirNivelNodo() {
		this.i--;
	}
	
	public int getNivelNodo() {
		return this.i;
	}
}
