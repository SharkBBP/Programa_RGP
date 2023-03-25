package Inicio;


public final class VariablesGlobales {

	private static final VariablesGlobales variablesGlobales  = new VariablesGlobales();
	private Boolean imprimirSubsistemas = false;
	private Boolean imprimirProcesos = false;
	private Boolean imprimirResponsables = false;
	private Boolean imprimirDocumentos = false;
	
	private VariablesGlobales() {
		System.err.println("Singleton variablesGlobales creado");
	}
	
	public static VariablesGlobales getInstance() {
		return variablesGlobales;
	}

	public Boolean getImprimirSubsistemas() {
		return imprimirSubsistemas;
	}

	public void setImprimirSubsistemas(Boolean imprimirSubsistemas) {
		this.imprimirSubsistemas = imprimirSubsistemas;
	}

	public Boolean getImprimirProcesos() {
		return imprimirProcesos;
	}

	public void setImprimirProcesos(Boolean imprimirProcesos) {
		this.imprimirProcesos = imprimirProcesos;
	}

	public Boolean getImprimirResponsables() {
		return imprimirResponsables;
	}

	public void setImprimirResponsables(Boolean imprimirResponsables) {
		this.imprimirResponsables = imprimirResponsables;
	}

	public Boolean getImprimirDocumentos() {
		return imprimirDocumentos;
	}

	public void setImprimirDocumentos(Boolean imprimirDocumentos) {
		this.imprimirDocumentos = imprimirDocumentos;
	}
}
