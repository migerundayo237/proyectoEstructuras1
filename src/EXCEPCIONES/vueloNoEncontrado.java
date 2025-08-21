package EXCEPCIONES;

public class vueloNoEncontrado extends Exception {

	public vueloNoEncontrado(String s) {
		super("El vuelo: " + s + " no fue encontrado" );
	}
}
