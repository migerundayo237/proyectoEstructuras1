package AEROLINEA;

import java.io.Serializable;

public class Aeropuerto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2765104232870086094L;
	private String nombre;
	private String codigo;
	private String pais;
	private String ciudad;
	private boolean esInternacional;
	
	public Aeropuerto(String nombre, String codigo,  String pais, String ciudad, boolean esInternacional) {
		this.nombre = nombre;
		this.codigo = codigo;
		this.pais = pais;
		this.ciudad = ciudad;
		this.esInternacional = esInternacional;
	}

	public String getNombre() {
		return nombre;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public String getPais() {
		return pais;
	}

	public String getCiudad() {
		return ciudad;
	}

	public boolean isEsInternacional() {
		return esInternacional;
	}

	@Override
	public String toString() {
		String tipo = esInternacional? "internacional" : "local";
		return "Aeropuerto " + tipo + " " + nombre + " (" + codigo + " - " + ciudad + ", " + pais + ")";
	}
}
