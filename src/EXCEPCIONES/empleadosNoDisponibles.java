package EXCEPCIONES;

public class empleadosNoDisponibles extends Exception {
	public empleadosNoDisponibles() {
		super("No hay suficientes empleaods disponibles para el vuelo");
	}
}
