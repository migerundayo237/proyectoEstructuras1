package AEROLINEA;

import java.io.Serializable;
import java.util.Arrays;

public class Cliente extends Persona implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2181026042384026709L;
	private boolean EsVIP;
	private  long MillasAcumuladas;
	private  Reservacion[] Reservaciones;
	public Cliente(String nombreP, String tipoDocumento, String numDocumento, String nacionalidad, 
			char sexo, boolean esVIP, long millasAcumuladas) {
		super(nombreP, tipoDocumento, numDocumento, nacionalidad, sexo);
		EsVIP = esVIP;
		MillasAcumuladas = millasAcumuladas;
		Reservaciones = new Reservacion[0];
	}
	public boolean isEsVIP() {
		return EsVIP;
	}
	public long getMillasAcumuladas() {
		return MillasAcumuladas;
	}
	public Reservacion[] getReservaciones() {
		return Reservaciones;
	}
	public void setEsVIP(boolean esVIP) {
		EsVIP = esVIP;
	}
	public void setMillasAcumuladas(long millasAcumuladas) {
		MillasAcumuladas = millasAcumuladas;
	}
	public void setReservaciones(Reservacion[] reservaciones) {
		Reservaciones = reservaciones;
	}
	
	public void addReservacionACliente(Reservacion reservacion) {
		if (Reservaciones != null) Reservaciones = Arrays.copyOf(Reservaciones, Reservaciones.length + 1);
	    else Reservaciones = new Reservacion[1];
	    Reservaciones[Reservaciones.length - 1] = reservacion;
	}
	
	@Override
	public String toString() {
		StringBuilder reservasString = new StringBuilder("");
		if(Reservaciones.length != 0) {
			for(Reservacion r : Reservaciones) {
				reservasString.append("\n");
				reservasString.append(r.getCodigo());
			}
		} else {
			reservasString.append("ninguna");
		}
		String tipo = EsVIP? "VIP" : "regular";
		return super.getNombreP() + " (" + super.getSexo() + ")\n"
				+ super.getTipoDocumento() + " " + super.getNumDocumento()
				+ "\nNacionalidad: " + super.getNacionalidad()
				+ "\nTipo: cliente " + tipo
				+ "\nMillas acumuladas: " + MillasAcumuladas
				+ "\nReservaciones: " + reservasString;
	}
	
	
}
