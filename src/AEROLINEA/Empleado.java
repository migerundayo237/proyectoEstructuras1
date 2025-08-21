package AEROLINEA;

import java.io.Serializable;

public class Empleado extends Persona implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5408912191381578318L;
	private String Cargo;
	private boolean Disponibilidad;
	
	public Empleado(String nombreP, String tipoDocumento, String numDocumento, String nacionalidad, char sexo, String cargo) {
		super(nombreP, tipoDocumento, numDocumento, nacionalidad, sexo);
		Cargo = cargo;
		Disponibilidad = true;
	}

	public String getCargo() {
		return Cargo;
	}

	public boolean isDisponibilidad() {
		return Disponibilidad;
	}

	public void setCargo(String cargo) {
		Cargo = cargo;
	}

	public void setDisponibilidad(boolean disponibilidad) {
		Disponibilidad = disponibilidad;
	}

	@Override
	public String toString() {
		String disponible = Disponibilidad? "disponible" : "no disponible";
		return Cargo + " " + super.getNombreP() + " (" + super.getSexo() + ")\n"
				+ super.getTipoDocumento() + " " + super.getNumDocumento()
				+ "\nNacionalidad: " + super.getNacionalidad()
				+ "\nEstado: " + disponible + "\n";
	}
}
