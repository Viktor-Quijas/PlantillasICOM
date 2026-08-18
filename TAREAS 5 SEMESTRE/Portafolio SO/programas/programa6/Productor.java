package productorConsumidor;
import listaSimple.*;

public class Productor extends Entidad{
	
	Productor(Lista l){
		this.l = l;
		this.actual = l.getCabeza();
		this.longitudLista = l.getLongitud();
		this.entidad = PRODUCTOR;
		this.estado = DESPIERTO;
	}
	
	@Override
	public void accion() {
		if (anterior != null && anterior.entidad == PRODUCTOR)
			anterior.entidad = -1;

		actual.consumible = true;
		actual.entidad = entidad;
		anterior = actual;
		actual = actual.sig;
	}
	
	@Override
	public boolean ingresar() {
		if (puedeIngresar(l.listaLlena()) && estado == DESPIERTO) {
			estado = INGRESANDO;
			cantidadAccion = obtenerNumeroAleatorioAccion();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean puedeSeguir() {
		return !l.listaLlena();
	}
}
