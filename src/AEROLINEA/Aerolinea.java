package AEROLINEA;

import java.util.Arrays;
import java.util.Date;
import EXCEPCIONES.aeropuertoNoEncontrado;
import EXCEPCIONES.arregloVacio;
import EXCEPCIONES.avionNoEncontrado;
import EXCEPCIONES.empleadosNoDisponibles;
import EXCEPCIONES.malDigitadoNulo;
import EXCEPCIONES.personaNoEncontrada;
import EXCEPCIONES.reservaNoEncontrada;
import EXCEPCIONES.vueloLleno;
import EXCEPCIONES.vueloNoEncontrado;
import EXCEPCIONES.vueloVigente;
import Ficheros.ManejoFicheros;

public class Aerolinea {
	private Avion[] aviones;
	private Aeropuerto[] aeropuertos;
	private Vuelo[] vuelos;
	private Reservacion[] reservaciones;
	private Cliente[] clientes;
	private Empleado[] empleados;
	private ManejoFicheros f;
	
	public Aerolinea(Avion[] aviones, Aeropuerto[] aeropuertos, Vuelo[] vuelos, Reservacion[] reservaciones, 
	Cliente[] clientes, Empleado[] empleados, ManejoFicheros f) {
		this.aviones = aviones;
		this.aeropuertos = aeropuertos;
		this.vuelos = vuelos;
		this.reservaciones = reservaciones;
		this.clientes= clientes;
		this.empleados= empleados;
		this.f = f;
	}
	
	public Avion[] getAviones() {
		return aviones;
	}

	public void setAviones(Avion[] aviones) {
		this.aviones = aviones;
	}

	public Aeropuerto[] getAeropuertos() {
		return aeropuertos;
	}

	public void setAeropuertos(Aeropuerto[] aeropuertos) {
		this.aeropuertos = aeropuertos;
	}

	public Vuelo[] getVuelos() {
		return vuelos;
	}

	public void setVuelos(Vuelo[] vuelos) {
		this.vuelos = vuelos;
	}

	public Reservacion[] getReservaciones() {
		return reservaciones;
	}

	public void setReservaciones(Reservacion[] reservaciones) {
		this.reservaciones = reservaciones;
	}
	
	public Cliente[] getClientes(){
		return clientes;
	}
	
	public void setClientes(Cliente[] clientes){
		this.clientes= clientes;
	}
	
	public Empleado[] getEmpleados(){
		return empleados;
	}
	
	public void setEmpleados(Empleado[] empleados){
		this.empleados= empleados;
	}
	
	// Aeropuertos

	public void addAeropuerto(String nombre, String codigo, String pais, String ciudad, boolean esInternacional) throws malDigitadoNulo {
		if (nombre == null || nombre.isEmpty() || codigo == null || codigo.isEmpty() ||
		    pais == null || pais.isEmpty() || ciudad == null || ciudad.isEmpty()) {
			throw new malDigitadoNulo("Los datos del aeropuerto no pueden estar vacíos o nulos.");
		}

		Aeropuerto aeropuerto = new Aeropuerto(nombre, codigo, pais, ciudad, esInternacional);

		if (aeropuertos != null) {
			for (Aeropuerto a : aeropuertos) {
				if (a.getCodigo().equalsIgnoreCase(codigo)) {
					throw new malDigitadoNulo("El aeropuerto ya existe.");
				}
			}
			aeropuertos = Arrays.copyOf(aeropuertos, aeropuertos.length + 1);
		} else {
			aeropuertos = new Aeropuerto[1];
		}

		aeropuertos[aeropuertos.length - 1] = aeropuerto;
		f.addAeropuerto(aeropuerto);
	}
	
	public void eliminarAeropuerto(String codigo) throws aeropuertoNoEncontrado, arregloVacio {
		if (aeropuertos == null || aeropuertos.length == 0) {
			throw new arregloVacio("aeropuertos");
		}

		int indexAEliminar = devolverIndexAeropuerto(codigo);
		if (indexAEliminar == -1) {
			throw new aeropuertoNoEncontrado(codigo);
		}

		Aeropuerto[] newAeropuertos = new Aeropuerto[aeropuertos.length - 1];
		System.arraycopy(aeropuertos, 0, newAeropuertos, 0, indexAEliminar);
		System.arraycopy(aeropuertos, indexAEliminar + 1, newAeropuertos, indexAEliminar, aeropuertos.length - indexAEliminar - 1);
		aeropuertos = newAeropuertos;
		f.deleteAeropuerto(codigo);
	}
	
	public int devolverIndexAeropuerto(String codigo) {
		if (aeropuertos == null) return -1;
		int index = 0;
		while(index < aeropuertos.length && !codigo.equalsIgnoreCase(aeropuertos[index].getCodigo())) index++;
		return (index == aeropuertos.length)? -1 : index;
	}
	
	public Aeropuerto buscarAeropuerto(String codigo) throws aeropuertoNoEncontrado, arregloVacio {
		if (aeropuertos == null || aeropuertos.length == 0) {
			throw new arregloVacio("aeropuertos");
		}
		
		int indexAeropuerto = devolverIndexAeropuerto(codigo);
		if (indexAeropuerto == -1) {
			throw new aeropuertoNoEncontrado(codigo);
		}
		
		return aeropuertos[indexAeropuerto];
	}
	
	
	// Aviones
	
	public void addAvion(String modelo, int capacidad) throws malDigitadoNulo {
		if(modelo == null || modelo.isEmpty()) {
			throw new malDigitadoNulo("Los datos del avión no pueden estar vacíos");
		}
		
		Avion avion = new Avion(modelo, capacidad);
		if (aviones != null) aviones = Arrays.copyOf(aviones, aviones.length + 1); 
		else aviones = new Avion[1];
		aviones[aviones.length - 1] = avion;
		f.addAvion(avion);
	}
	
	public void eliminarAvion(String codigo) throws avionNoEncontrado, arregloVacio {
		if(aviones == null || aviones.length == 0) {
			throw new arregloVacio("aviones");
		}
		
		int indexAEliminar = devolverIndexAvion(codigo);
		if(indexAEliminar == -1) {
			throw new avionNoEncontrado(codigo);
		}
		
		Avion[] newAviones = new Avion[aviones.length - 1];
		System.arraycopy(aviones, 0, newAviones, 0, indexAEliminar);
		System.arraycopy(aviones, indexAEliminar + 1, newAviones, indexAEliminar, aviones.length - indexAEliminar - 1);
		aviones = newAviones;
		f.deleteAvion(codigo);
	}
	
	public int devolverIndexAvion(String codigo) {
		if(aviones == null) return -1;
		int index = 0;
		while(index < aviones.length && !codigo.equalsIgnoreCase(aviones[index].getCodigoA())) index++;
		return (index == aviones.length)? -1 : index;
	}
	
	public Avion buscarAvion(String codigo) throws avionNoEncontrado, arregloVacio {
		if(aviones == null || aviones.length == 0) {
			throw new arregloVacio("aviones");
		}
		
		int indexAEliminar = devolverIndexAvion(codigo);
		if(indexAEliminar == -1) {
			throw new avionNoEncontrado(codigo);
		}
		int index = devolverIndexAvion(codigo);
		return aviones[index];
	}
	
	// Reservaciones
	
	
	public void addReservacion(Cliente[] clientes, String codigoVuelo, long precio)
	        throws malDigitadoNulo, vueloNoEncontrado, vueloLleno, arregloVacio {
	    
	    if (clientes == null || clientes.length == 0) {
	        throw new malDigitadoNulo("Debe haber al menos un cliente en la reservación.");
	    }

	    if (codigoVuelo == null) {
	        throw new malDigitadoNulo("El vuelo no puede ser nulo.");
	    }

	    if (precio <= 0) {
	        throw new malDigitadoNulo("El precio debe ser un valor positivo.");
	    }

	    Vuelo vuelo = buscarVuelo(codigoVuelo); 
	    
	    Reservacion reservacion = new Reservacion(clientes, vuelo, precio);
	    vuelo.asignarReserva(reservacion);
	    
	    if (reservaciones != null) reservaciones = Arrays.copyOf(reservaciones, reservaciones.length + 1);
	    else reservaciones = new Reservacion[1];
	    reservaciones[reservaciones.length - 1] = reservacion;
	    f.addReservacion(reservacion);
	}
	
	public void eliminarReservacion(String codigo) throws reservaNoEncontrada, arregloVacio {
		if(reservaciones == null || reservaciones.length == 0) {
			throw new arregloVacio("reservaciones");
		}
		
		int indexAEliminar = devolverIndexReservacion(codigo);
		if(indexAEliminar == -1) {
			throw new reservaNoEncontrada(codigo);
		}
		
		Reservacion[] newReservaciones = new Reservacion[reservaciones.length - 1];
		System.arraycopy(reservaciones, 0, newReservaciones, 0, indexAEliminar);
		System.arraycopy(reservaciones, indexAEliminar + 1, newReservaciones, indexAEliminar, reservaciones.length - indexAEliminar - 1);
		reservaciones = newReservaciones;
		f.deleteReservacion(codigo);
	}
	
	public int devolverIndexReservacion(String codigo) {
		if(reservaciones == null) return -1;
		int index = 0;
		while(index < reservaciones.length && !codigo.equalsIgnoreCase(reservaciones[index].getCodigo())) index++;
		return (index == reservaciones.length)? -1 : index;
	}
	
	public Reservacion buscarReservacion(String codigo) throws reservaNoEncontrada, arregloVacio {
		if(reservaciones == null || reservaciones.length == 0) {
			throw new arregloVacio("reservaciones");
		}
		
		int index = devolverIndexReservacion(codigo);
		if(index == -1) {
			throw new reservaNoEncontrada(codigo);
		}
		
		return reservaciones[index];
	}
	
	// Clientes-Empleados
	public int devolverIndexCliente(String tipoDocumento, String numDocumento) {
	    if (clientes == null || numDocumento == null || tipoDocumento == null) return -1;
	    int index = 0;
	    while (index < clientes.length &&
	          (!tipoDocumento.equalsIgnoreCase(clientes[index].getTipoDocumento()) ||
	           !numDocumento.equalsIgnoreCase(clientes[index].getNumDocumento()))) {
	        index++;
	    }
	    return (index == clientes.length) ? -1 : index;
	}

	public Cliente buscarCliente(String tipoDocumento, String numDocumento) 
	        throws personaNoEncontrada, arregloVacio, malDigitadoNulo {
	    if (clientes == null || clientes.length == 0) {
	        throw new arregloVacio("clientes");
	    }
	    if (numDocumento == null || tipoDocumento == null) {
	        throw new malDigitadoNulo("Número o tipo de documento no puede ser nulo");
	    }
	    
	    int index = devolverIndexCliente(tipoDocumento, numDocumento);
	    if (index == -1) {
	        throw new personaNoEncontrada(tipoDocumento + " " + numDocumento);
	    }
	    
	    return clientes[index];
	}

	public int devolverIndexEmpleado(String tipoDocumento, String numDocumento) {
	    if (empleados == null || numDocumento == null || tipoDocumento == null) return -1;
	    int index = 0;
	    while (index < empleados.length &&
	          (!tipoDocumento.equalsIgnoreCase(empleados[index].getTipoDocumento()) ||
	           !numDocumento.equalsIgnoreCase(empleados[index].getNumDocumento()))) {
	        index++;
	    }
	    return (index == empleados.length) ? -1 : index;
	}

	public Empleado buscarEmpleado(String tipoDocumento, String numDocumento) 
	        throws personaNoEncontrada, arregloVacio, malDigitadoNulo {
	    if (empleados == null || empleados.length == 0) {
	        throw new arregloVacio("empleados");
	    }
	    if (numDocumento == null || tipoDocumento == null) {
	        throw new malDigitadoNulo("Número o tipo de documento no puede ser nulo");
	    }
	    
	    int index = devolverIndexEmpleado(tipoDocumento, numDocumento);
	    if (index == -1) {
	        throw new personaNoEncontrada(tipoDocumento + " " + numDocumento);
	    }
	    
	    return empleados[index];
	}

	public void addEmpleado(String nombreP, String tipoDocumento, String numDocumento, String nacionalidad,
	                        char sexo, String cargo)
	        throws malDigitadoNulo {
	
	    if (nombreP == null || tipoDocumento == null || numDocumento == null || nacionalidad == null || sexo == '\u0000' || cargo == null) {
	        throw new malDigitadoNulo("Ningún dato del empleado puede ser nulo.");
	    }
	
	    if (nombreP.isBlank() || tipoDocumento.isBlank() || numDocumento.isBlank() || nacionalidad.isBlank() || Character.isWhitespace(sexo) || cargo.isBlank()) {
	        throw new malDigitadoNulo("Ningún dato del empleado puede estar vacío.");
	    }
	    Empleado empleado = new Empleado(nombreP, tipoDocumento, numDocumento, nacionalidad, sexo, cargo);
	
	    
	    if (empleados != null) {
	        empleados = Arrays.copyOf(empleados, empleados.length + 1);
	    } else {
	        empleados = new Empleado[1];
	    }
	
	    empleados[empleados.length - 1] = empleado;
	    f.addEmpleado(empleado);
	}

	public void addCliente(String nombreP, String tipoDocumento, String numDocumento, String nacionalidad,
	                       char sexo, boolean esVIP, long millasAcumuladas)
	        throws malDigitadoNulo {
	
	    
	    if (nombreP == null || tipoDocumento == null || numDocumento == null || sexo == '\u0000' || nacionalidad == null) {
	        throw new malDigitadoNulo("Ningún dato del cliente puede ser nulo.");
	    }
	
	    if (nombreP.isBlank() || tipoDocumento.isBlank() || numDocumento.isBlank() || nacionalidad.isBlank() || Character.isWhitespace(sexo)) {
	        throw new malDigitadoNulo("Ningún dato del cliente puede estar vacío.");
	    }
	    
	    if (millasAcumuladas < 0) {
	    	throw new malDigitadoNulo("Las millas acumuladas no pueden ser negativas");
	    }
	
	    Cliente cliente = new Cliente(nombreP, tipoDocumento, numDocumento, nacionalidad,
                sexo, esVIP, millasAcumuladas);
	   
	    if (clientes != null) {
	        clientes = Arrays.copyOf(clientes, clientes.length + 1);
	    } else {
	        clientes = new Cliente[1];
	    }
	
	    clientes[clientes.length - 1] = cliente;
	    f.addCliente(cliente);
	}

	public void eliminarEmpleado(String numDocumento, String tipoDocumento) 
	        throws personaNoEncontrada, arregloVacio, malDigitadoNulo {
	    
	    if (empleados == null || empleados.length == 0) {
	        throw new arregloVacio("empleados");
	    }
	    
	    if (numDocumento == null || numDocumento.isEmpty() || 
	        tipoDocumento == null || tipoDocumento.isEmpty()) {
	        throw new malDigitadoNulo("El número y tipo de documento no pueden ser nulos o vacíos");
	    }
	    
	    int indexAEliminar = devolverIndexEmpleado(numDocumento, tipoDocumento);
	    if (indexAEliminar == -1) {
	        throw new personaNoEncontrada(numDocumento + " - " + tipoDocumento);
	    }
	    
	    Empleado[] newEmpleados = new Empleado[empleados.length - 1];
	    System.arraycopy(empleados, 0, newEmpleados, 0, indexAEliminar);
	    System.arraycopy(empleados, indexAEliminar + 1, newEmpleados, indexAEliminar, 
	                     empleados.length - indexAEliminar - 1);
	    empleados = newEmpleados;
	}
	
	public void eliminarCliente(String numDocumento, String tipoDocumento) 
	        throws personaNoEncontrada, arregloVacio, malDigitadoNulo {
	    
	    if (clientes == null || clientes.length == 0) {
	        throw new arregloVacio("clientes");
	    }
	    
	    if (numDocumento == null || numDocumento.isEmpty() || 
	        tipoDocumento == null || tipoDocumento.isEmpty()) {
	        throw new malDigitadoNulo("El número y tipo de documento no pueden ser nulos o vacíos");
	    }
	    
	    int indexAEliminar = devolverIndexCliente(numDocumento, tipoDocumento);
	    if (indexAEliminar == -1) {
	        throw new personaNoEncontrada(numDocumento + " - " + tipoDocumento);
	    }
	    
	    Cliente[] newClientes = new Cliente[clientes.length - 1];
	    System.arraycopy(clientes, 0, newClientes, 0, indexAEliminar);
	    System.arraycopy(clientes, indexAEliminar + 1, newClientes, indexAEliminar, 
	                     clientes.length - indexAEliminar - 1);
	    clientes = newClientes;
	}

	// Vuelos 
	public void addVuelo(String codigoAvion, int cantidadTripulacion, String codAeropuertoSalida,
	                     String codAeropuertoLlegada, Date horaSalida, Date horaLlegada)
	        throws malDigitadoNulo, empleadosNoDisponibles, avionNoEncontrado, arregloVacio, aeropuertoNoEncontrado {
	
	    if (codigoAvion == null) {
	        throw new malDigitadoNulo("El avión no puede ser nulo.");
	    }
	    
	    if (cantidadTripulacion <= 0) {
	    	throw new malDigitadoNulo("Debe haber al menos un miembro en la tripulación.");
	    }

	    if (codAeropuertoSalida == null || codAeropuertoLlegada == null) {
	        throw new malDigitadoNulo("Los aeropuertos de salida y llegada no pueden ser nulos.");
	    }
	    
	    if (horaSalida == null || horaLlegada == null) {
	        throw new malDigitadoNulo("Las horas de salida y llegada no pueden ser nulas.");
	    }
	    
	    if (!horaSalida.before(horaLlegada)) {
	        throw new malDigitadoNulo("La hora de llegada no puede ser igual o anterior a la de salida.");
	    }
	    
	    Avion avion = buscarAvion(codigoAvion);
	    avion.setDisponibilidad(false);
	    Aeropuerto aeropuertoSalida = buscarAeropuerto(codAeropuertoSalida);
	    Aeropuerto aeropuertoLlegada = buscarAeropuerto(codAeropuertoLlegada);
	
	    Vuelo vuelo = new Vuelo(avion, cantidadTripulacion, empleados, aeropuertoSalida, aeropuertoLlegada, horaSalida, horaLlegada);
	
	    if (vuelos != null) {
	        vuelos = Arrays.copyOf(vuelos, vuelos.length + 1);
	    } else {
	        vuelos = new Vuelo[1];
	    }
	
	    vuelos[vuelos.length - 1] = vuelo;
	    f.addVuelo(vuelo);
	}

	public void eliminarVuelo(String codigo) throws vueloNoEncontrado, arregloVacio, malDigitadoNulo, vueloVigente {
	    if (vuelos == null || vuelos.length == 0) {
	        throw new arregloVacio("vuelos");
	    }
	    if (codigo == null || codigo.isEmpty()) {
	        throw new malDigitadoNulo("El código del vuelo no puede ser nulo o vacío.");
	    }
	
	    int indexAEliminar = devolverIndexVuelo(codigo);
	    if (indexAEliminar == -1) {
	        throw new vueloNoEncontrado(codigo);
	    }
	    
	    if (!vuelos[indexAEliminar].isFinalizadoOCancelado()) {
	    	throw new vueloVigente();
	    }
	
	    Vuelo[] newVuelos = new Vuelo[vuelos.length - 1];
	    System.arraycopy(vuelos, 0, newVuelos, 0, indexAEliminar);
	    System.arraycopy(vuelos, indexAEliminar + 1, newVuelos, indexAEliminar, vuelos.length - indexAEliminar - 1);
	    vuelos = newVuelos;
	    f.deleteVuelo(codigo);
	}

	public Vuelo buscarVuelo(String codigo) throws vueloNoEncontrado, arregloVacio, malDigitadoNulo {
	    if (vuelos == null || vuelos.length == 0) {
	        throw new arregloVacio("vuelos");
	    }
	    if (codigo == null || codigo.isEmpty()) {
	        throw new malDigitadoNulo("El código del vuelo no puede ser nulo o vacío.");
	    }
	
	    int index = devolverIndexVuelo(codigo);
	    if (index == -1) {
	        throw new vueloNoEncontrado(codigo);
	    }
	
	    return vuelos[index];
	}
	
	public void actualizarVuelo(String codigo) throws vueloNoEncontrado, arregloVacio, malDigitadoNulo {
		if (vuelos == null || vuelos.length == 0) {
	        throw new arregloVacio("vuelos");
	    }
	    if (codigo == null || codigo.isEmpty()) {
	        throw new malDigitadoNulo("El código del vuelo no puede ser nulo o vacío.");
	    }
	    
		int index = devolverIndexVuelo(codigo);
		if (index == -1) {
	        throw new vueloNoEncontrado(codigo);
	    }
		
		vuelos[index].marcarfinalizadoOCancelado();
	}

	public int devolverIndexVuelo(String codigo) {
		if(vuelos == null) return -1;
		int index = 0;
		while(index < vuelos.length && !codigo.equalsIgnoreCase(vuelos[index].getCodigo())) index++;
		return (index == vuelos.length)? -1 : index;
	}
}
