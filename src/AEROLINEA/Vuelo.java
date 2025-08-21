package AEROLINEA;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import EXCEPCIONES.empleadosNoDisponibles;
import EXCEPCIONES.malDigitadoNulo;
import EXCEPCIONES.vueloLleno;

public class Vuelo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4999884171434390028L;
	private String codigo;
    private Avion avion;
    private Empleado[] tripulacion;
    private Cliente[] clientes;
    private Aeropuerto aeropuertoSalida;
    private Aeropuerto aeropuertoLlegada;
    private Date horaSalida;
    private Date horaLlegada;
    private boolean finalizadoOCancelado;

    // Constructor
    public Vuelo(Avion avion, int cantidadTripulacion, Empleado[] empleadosAerolinea, Aeropuerto aeropuertoSalida,
    		Aeropuerto aeropuertoLlegada, Date horaSalida, Date horaLlegada) throws empleadosNoDisponibles {
        codigo = generarCodigo();
        this.avion = avion;
        tripulacion = asignarTripulacion(cantidadTripulacion, empleadosAerolinea);
        clientes = new Cliente[avion.getCapacidad()];
        this.aeropuertoSalida = aeropuertoSalida;
        this.aeropuertoLlegada = aeropuertoLlegada;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        finalizadoOCancelado = false;
    }

    // Método para calcular el tiempo estimado de vuelo como texto
    public String tiempoEstimadoTexto()  {
        long duracionMs = horaLlegada.getTime() - horaSalida.getTime();
        long minutosTotales = duracionMs / (1000 * 60);
        long horas = minutosTotales / 60;
        long minutos = minutosTotales % 60;

        return horas + " horas y " + minutos + " minutos";
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Empleado[] getTripulacion() {
        return tripulacion;
    }

    public void setTripulacion(Empleado[] tripulacion) {
        this.tripulacion = tripulacion;
    }

    public Cliente[] getClientes() {
        return clientes;
    }

    public void setClientes(Cliente[] clientes) {
        this.clientes = clientes;
    }

    public Aeropuerto getAeropuertoSalida() {
        return aeropuertoSalida;
    }

    public void setAeropuertoSalida(Aeropuerto aeropuertoSalida) {
        this.aeropuertoSalida = aeropuertoSalida;
    }

    public Aeropuerto getAeropuertoLlegada() {
        return aeropuertoLlegada;
    }

    public void setAeropuertoLlegada(Aeropuerto aeropuertoLlegada) {
        this.aeropuertoLlegada = aeropuertoLlegada;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Date getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(Date horaLlegada) {
        this.horaLlegada = horaLlegada;
    }
    
    public boolean isFinalizadoOCancelado() {
		return finalizadoOCancelado;
	}

	public void setFinalizadoOCancelado(boolean finalizadoOCancelado) {
		this.finalizadoOCancelado = finalizadoOCancelado;
	}

	public String generarCodigo() {
		Random random = new Random();
        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));
        char letra3 = (char) ('A' + random.nextInt(26));
        int numero = random.nextInt(1000);
        String numeroStr = String.format("%03d", numero);
        return "" + letra1 + letra2 + letra3 + numeroStr;
	}
    
    public Empleado[] asignarTripulacion(int cantidad, Empleado[] empleados) throws empleadosNoDisponibles {
        Empleado[] tripulacion = new Empleado[cantidad];
        int indexEmpleadoAsignado = 0;

        for (int i = 0; i < empleados.length && indexEmpleadoAsignado < cantidad; i++) {
            if (empleados[i].isDisponibilidad()) {
                tripulacion[indexEmpleadoAsignado] = empleados[i];
                empleados[i].setDisponibilidad(false);
                indexEmpleadoAsignado++;
            }
        }

        if (indexEmpleadoAsignado < cantidad) {
            throw new empleadosNoDisponibles();
        }

        return tripulacion;
    }

    public void asignarReserva(Reservacion reserva) throws vueloLleno {
	    Cliente[] clientesReserva = reserva.getClientes();
	    
	    int asientosDisponibles = 0;
	    for (Cliente c : clientes) {
	        if (c == null) asientosDisponibles++;
	    }
	    
	    if (asientosDisponibles == 0) {
	        throw new vueloLleno("No hay asientos disponibles en el vuelo " + codigo);
	    }
	    
	    if (clientesReserva.length > asientosDisponibles) {
	        throw new vueloLleno("No hay suficientes asientos disponibles para todos los clientes de la reservación " + reserva.getCodigo());
	    }
	    
	    int asignados = 0;
	    for (int i = 0; i < clientes.length && asignados < clientesReserva.length; i++) {
	        if (clientes[i] == null) {
	            clientes[i] = clientesReserva[asignados++];
	        }
	    }
    }
    
    public void marcarfinalizadoOCancelado() {
    	avion.setDisponibilidad(true);
    	for(Empleado e : tripulacion) {
    		e.setDisponibilidad(true);
    	}
    	finalizadoOCancelado = true;
    }

	@Override
	public String toString() {
		String estado = finalizadoOCancelado? "finalizado o cancelado" : "vigente";
		return "Vuelo " + codigo + "\nAvión: " + avion.toString() 
				+ "\nTripulación: " + Arrays.toString(tripulacion)
				+ "\nAeropuerto de salida: " + aeropuertoSalida.toString()
				+ "\nAeropuerto de llegada: " + aeropuertoLlegada.toString() 
				+ "\nTiempo de vuelo estimado: " + tiempoEstimadoTexto()
				+ "\nEstado: " + estado + "\n";
	}
}
