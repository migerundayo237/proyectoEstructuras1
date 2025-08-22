package AEROLINEA;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class Reservacion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3718929503820529561L;
	private String codigo;
	private Cliente[] clientes;
	private Vuelo vuelo;
	private long precio;
	
	public Reservacion(Cliente[] clientes, Vuelo vuelo, long precio) {
		codigo = generarCodigo();
		this.clientes = clientes;
		this.vuelo = vuelo;
		this.precio = precio;
		aumentarMillas();
	}

	public String getCodigo() {
		return codigo;
	}

	public Cliente[] getClientes() {
		return clientes;
	}

	public Vuelo getVuelo() {
		return vuelo;
	}

	public long getPrecio() {
		return precio;
	}
	
	public String generarCodigo() {
		Random random = new Random();
        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));
        int numero = random.nextInt(10000);
        String numeroStr = String.format("%04d", numero);
        return "" + letra1 + letra2 + numeroStr;
	}
	
	public void aumentarMillas() {
		for(int i = 0; i < clientes.length; i++) {
			long millasCliente = clientes[i].getMillasAcumuladas();
			clientes[i].setMillasAcumuladas(millasCliente += precio/2);
		}
	}

	@Override
	public String toString() {
		StringBuilder clientesString = new StringBuilder("");
		for(Cliente c : clientes) {
			clientesString.append("\n");
			clientesString.append(c.getNombreP());
		}
		return "Reservación " + codigo 
				+ "\nClientes: " + clientesString 
				+ "\nVuelo: " + vuelo.getCodigo()
				+ "\nPrecio: $" + precio + "\n";
	}
}
