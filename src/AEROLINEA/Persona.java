package AEROLINEA;

import java.io.Serializable;

public abstract class Persona implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2636555315858873148L;
	protected String NombreP;
	protected String  TipoDocumento;
	protected String NumDocumento ;
	protected String Nacionalidad ;
	protected char Sexo;
	
	public Persona(String nombreP, String tipoDocumento, String numDocumento, String nacionalidad, char sexo) {
		NombreP = nombreP;
		TipoDocumento = tipoDocumento;
		NumDocumento = numDocumento;
		Nacionalidad = nacionalidad;
		Sexo = sexo;
	}

	public String getNombreP() {
		return NombreP;
	}

	public void setNombreP(String nombreP) {
		NombreP = nombreP;
	}

	public String getTipoDocumento() {
		return TipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		TipoDocumento = tipoDocumento;
	}

	public String getNumDocumento() {
		return NumDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		NumDocumento = numDocumento;
	}

	public String getNacionalidad() {
		return Nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		Nacionalidad = nacionalidad;
	}

	public char getSexo() {
		return Sexo;
	}

	public void setSexo(char sexo) {
		Sexo = sexo;
	}
}
