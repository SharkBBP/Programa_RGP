package Inicio;


/**
 * Esta clase sirve para almacenar todos los datos de un registro (fila) de la tabla CausaConsecuenciaMCR y poder extraer posteriormente la misma con facilidad
 * @author shark1
 *
 */
public class RegistroCCM {

	private Integer numero_fila;			//Almacena el ID del registro que se está leyendo en cada momento en el Recordset
	private Integer IdConsecuencia;			//Almacena el ID del evento consecuencia que se está leyendo de la tabla "CausaConsecuenciaMCR"
	private String EstadoIdConsecuencia;
	private Integer IdCausa;				//Almacena el ID del evento causa que se está leyendo de la tabla "CausaConsecuenciaMCR"
	private String EstadoIdCausa;
	private Integer IdMCR;					//Almacena el ID de la medida de control del riesgo que se está leyendo de la tabla "CausaConsecuenciaMCR"
	private String EstadoIdMCR;
	private Integer OrdenMCR;
	private String SubsistemaMCR;
	private String EstadoSubsistema;
	private String Componente;
	private String Elemento;
	private String Proceso;
	private String EstadoProceso;
	private Integer IdDocumento_de_requisitos;
	private String EstadoIdDocumento;
	private String Articulos;
	private String EstadoArticulos;
	private Integer IdAreaResponsable;
	private String EstadoIdAreaResponsable;
	private Integer IdMCRsiguiente;			//Almacena el ID de la medida de control del riesgo que existe a continuación de la actual, en la tabla "CausaConsecuenciaMCR", entendida como siguiente si consideramos que avanzamos hacia el accidente
	private String MCRInicialOFinal;		//Indica si la MCR es inicial (I), final (F) o única (U) entre la Causa y la consecuencia

	public RegistroCCM(Integer numero_fila, Integer idConsecuencia, String estadoIdConsecuencia, Integer idCausa,
			String estadoIdCausa, Integer idMCR, String estadoIdMCR, Integer ordenMCR, String subsistemaMCR,
			String estadoSubsistema, String proceso, String estadoProceso, Integer idDocumento_de_requisitos,
			String estadoIdDocumento, String articulos, String estadoArticulos, Integer idAreaResponsable,
			String estadoIdAreaResponsable, String componente, String elemento, Integer idMCRsiguiente,
			String mCRInicialOFinal) {
		
		this.setNumero_fila(numero_fila);
		this.setIdConsecuencia(idConsecuencia);
		this.setEstadoIdConsecuencia(estadoIdConsecuencia);
		this.setIdCausa(idCausa);
		this.setEstadoIdCausa(estadoIdCausa);
		this.setIdMCR(idMCR);
		this.setEstadoIdMCR(estadoIdMCR);
		this.setOrdenMCR(ordenMCR);
		this.setSubsistemaMCR(subsistemaMCR);
		this.setEstadoSubsistema(estadoSubsistema);
		this.setProceso(proceso);
		this.setEstadoProceso(estadoProceso);
		this.setIdDocumento_de_requisitos(idDocumento_de_requisitos);
		this.EstadoIdDocumento = estadoIdDocumento;
		this.setArticulos(articulos);
		this.setEstadoArticulos(estadoArticulos);
		this.setIdAreaResponsable(idAreaResponsable);
		this.setEstadoIdAreaResponsable(estadoIdAreaResponsable);
		this.setComponente(componente);
		this.setElemento(elemento);
		this.setIdMCRsiguiente(idMCRsiguiente);
		this.setMCRInicialOFinal(mCRInicialOFinal);
			
	}

	public Integer getNumero_fila() {
		return numero_fila;
	}

	public void setNumero_fila(Integer numero_fila) {
		this.numero_fila = numero_fila;
	}

	public Integer getIdConsecuencia() {
		return IdConsecuencia;
	}

	public void setIdConsecuencia(Integer idConsecuencia) {
		IdConsecuencia = idConsecuencia;
	}

	public String getEstadoIdConsecuencia() {
		return EstadoIdConsecuencia;
	}

	public void setEstadoIdConsecuencia(String estadoIdConsecuencia) {
		EstadoIdConsecuencia = estadoIdConsecuencia;
	}

	public Integer getIdCausa() {
		return IdCausa;
	}

	public void setIdCausa(Integer idCausa) {
		IdCausa = idCausa;
	}

	public String getEstadoIdCausa() {
		return EstadoIdCausa;
	}

	public void setEstadoIdCausa(String estadoIdCausa) {
		EstadoIdCausa = estadoIdCausa;
	}

	public Integer getIdMCR() {
		return IdMCR;
	}

	public void setIdMCR(Integer idMCR) {
		IdMCR = idMCR;
	}

	public String getEstadoIdMCR() {
		return EstadoIdMCR;
	}

	public void setEstadoIdMCR(String estadoIdMCR) {
		EstadoIdMCR = estadoIdMCR;
	}

	public Integer getOrdenMCR() {
		return OrdenMCR;
	}

	public void setOrdenMCR(Integer ordenMCR) {
		OrdenMCR = ordenMCR;
	}

	public String getSubsistemaMCR() {
		return SubsistemaMCR;
	}

	public void setSubsistemaMCR(String subsistemaMCR) {
		SubsistemaMCR = subsistemaMCR;
	}

	public String getEstadoSubsistema() {
		return EstadoSubsistema;
	}

	public void setEstadoSubsistema(String estadoSubsistema) {
		EstadoSubsistema = estadoSubsistema;
	}

	public String getComponente() {
		return Componente;
	}

	public void setComponente(String componente) {
		Componente = componente;
	}

	public String getElemento() {
		return Elemento;
	}

	public void setElemento(String elemento) {
		Elemento = elemento;
	}

	public String getProceso() {
		return Proceso;
	}

	public void setProceso(String proceso) {
		Proceso = proceso;
	}

	public String getEstadoProceso() {
		return EstadoProceso;
	}

	public void setEstadoProceso(String estadoProceso) {
		EstadoProceso = estadoProceso;
	}

	public Integer getIdDocumento_de_requisitos() {
		return IdDocumento_de_requisitos;
	}

	public void setIdDocumento_de_requisitos(Integer idDocumento_de_requisitos) {
		IdDocumento_de_requisitos = idDocumento_de_requisitos;
	}

	public String getArticulos() {
		return Articulos;
	}

	public void setArticulos(String articulos) {
		Articulos = articulos;
	}

	public String getEstadoArticulos() {
		return EstadoArticulos;
	}

	public void setEstadoArticulos(String estadoArticulos) {
		EstadoArticulos = estadoArticulos;
	}

	public Integer getIdAreaResponsable() {
		return IdAreaResponsable;
	}

	public void setIdAreaResponsable(Integer idAreaResponsable) {
		IdAreaResponsable = idAreaResponsable;
	}

	public String getEstadoIdAreaResponsable() {
		return EstadoIdAreaResponsable;
	}

	public void setEstadoIdAreaResponsable(String estadoIdAreaResponsable) {
		EstadoIdAreaResponsable = estadoIdAreaResponsable;
	}

	public Integer getIdMCRsiguiente() {
		return IdMCRsiguiente;
	}

	public void setIdMCRsiguiente(Integer idMCRsiguiente) {
		IdMCRsiguiente = idMCRsiguiente;
	}

	public String getMCRInicialOFinal() {
		return MCRInicialOFinal;
	}

	public void setMCRInicialOFinal(String mCRInicialOFinal) {
		MCRInicialOFinal = mCRInicialOFinal;
	}

	public String getEstadoIdDocumento() {
		return this.EstadoIdDocumento;
	}
}
