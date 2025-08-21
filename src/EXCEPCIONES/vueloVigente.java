package EXCEPCIONES;

public class vueloVigente extends Exception {
	public vueloVigente() {
		super("No es posible eliminar un vuelo que no haya finalizado o no se haya cancelado");
	}
}
