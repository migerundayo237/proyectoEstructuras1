package AEROLINEA;

import java.io.Serializable;
import java.util.Random;

public class Avion implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8177612996758536639L;
	private String codigoA;       // Código único del avión
    private String modelo;        // Modelo del avión
    private int capacidad;        // Capacidad de pasajeros
    private boolean disponibilidad; // 

    // Constructor
    public Avion(String modelo, int capacidad) {
        codigoA = generarCodigo();
        this.modelo = modelo;
        this.capacidad = capacidad;
        disponibilidad = true;
    }

    // Getters y Setters
    public String getCodigoA() {
        return codigoA;
    }

    public void setCodigoA(String codigoA) {
        this.codigoA = codigoA;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
    
    public String generarCodigo() {
		Random random = new Random();
        char letra1 = (char) ('A' + random.nextInt(26));
        int numero = random.nextInt(1000);
        String numeroStr = String.format("%03d", numero);
        return "" + letra1 + numeroStr;
	}

	@Override
	public String toString() {
		String disponible = disponibilidad? "disponible" : "no disoponible";
		return "Avión " + codigoA + ": " + modelo + ", capacidad " + capacidad + " (" + disponible + ")";
	}
    
    
}