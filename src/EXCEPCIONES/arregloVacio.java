package EXCEPCIONES;

public class arregloVacio extends Exception {

	public arregloVacio(String s) {
		super("El arreglo " + s + " esta vacío");
	}
}
