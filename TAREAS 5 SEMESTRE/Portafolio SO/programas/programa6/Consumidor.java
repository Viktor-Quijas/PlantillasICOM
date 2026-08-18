package productorConsumidor;
import listaSimple.*;

public class Consumidor extends Entidad{
	
	Consumidor(Lista l){
		this.l = l;
		this.actual = l.getCabeza();
		this.longitudLista = l.getLongitud();
		this.entidad = CONSUMIDOR;
		this.estado = DESPIERTO;
	}
	
	@Override
	public void accion() {
		if (anterior != null && anterior.entidad  == CONSUMIDOR)
			anterior.entidad = -1;
		
		actual.consumible = false;
		actual.entidad = entidad;
		anterior = actual;
		actual = actual.sig;
	}
	
	@Override
	public boolean ingresar() {
		if (puedeIngresar(l.cantidadConsumible()) && estado == DESPIERTO) {
			estado = INGRESANDO;
			cantidadAccion = obtenerNumeroAleatorioAccion();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean puedeSeguir() {
		return l.cantidadConsumible() > 0;
	}
}
