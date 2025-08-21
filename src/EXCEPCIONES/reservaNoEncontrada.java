package EXCEPCIONES;

public class reservaNoEncontrada extends Exception {

	public reservaNoEncontrada(String s) {
		super("La reserva " + s + "no fue encontrada");
	}
}
