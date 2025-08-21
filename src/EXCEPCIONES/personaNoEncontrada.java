package EXCEPCIONES;

public class personaNoEncontrada extends Exception {

	public personaNoEncontrada(String s) {
		super("La persona con el documento " + s + " no fue encontrada");
	}
}
