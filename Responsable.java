package Inicio;


public class Responsable {

	private Integer idResponsable;
	private String nombreResponsable;
	
	public Responsable(Integer id, String nombreResponsable) {
		this.setIdResponsable(id);
		this.setNombreResponsable(nombreResponsable);
		
	}

	/**
	 * @return the idResponsable
	 */
	public Integer getIdResponsable() {
		return idResponsable;
	}

	/**
	 * @param idResponsable the idResponsable to set
	 */
	public void setIdResponsable(Integer idResponsable) {
		this.idResponsable = idResponsable;
	}

	/**
	 * @return the nombreResponsable
	 */
	public String getNombreResponsable() {
		return nombreResponsable;
	}

	/**
	 * @param nombreResponsable the nombreResponsable to set
	 */
	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

}
