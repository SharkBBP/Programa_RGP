package Inicio;


public class Documento {

	private Integer idDocumento;
	private String codigo;
	private String titulo;
	private String enlaces;
	private String version;
	private String fecha;
	private String tipo;
	
	public Documento(Integer idDocumento,String codigo, String titulo, String enlaces, String version, String fecha, String tipo) {
		this.setIdDocumento(idDocumento);
		this.setCodigo(codigo);
		this.setTitulo(titulo);
		this.setEnlaces(enlaces);
		this.setVersion(version);
		this.setFecha(fecha);
		this.setTipo(tipo);
	}

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return the enlaces
	 */
	public String getEnlaces() {
		return enlaces;
	}

	/**
	 * @param enlaces the enlaces to set
	 */
	public void setEnlaces(String enlaces) {
		this.enlaces = enlaces;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
