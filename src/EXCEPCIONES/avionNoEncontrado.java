package EXCEPCIONES;

public class avionNoEncontrado extends Exception {

	public avionNoEncontrado(String s) {
		super("El avion: " + s + "no fue encontrado");
	}
}
