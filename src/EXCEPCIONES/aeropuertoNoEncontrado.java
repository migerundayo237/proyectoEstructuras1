package EXCEPCIONES;

public class aeropuertoNoEncontrado extends Exception {

	public aeropuertoNoEncontrado(String s) {
		super("El aeropuerto: " + s + " no fue encontrado");
	}
	

}
