/*
 * 			CAMBIOS VERSION 2.0
 * - Se agregaron los atributos de error y tiempo restante
 * - Tiempo restante por default es igual a tiempo máximo estimado hasta que alguna interrupción ocurra.
 * -
 */

package lote;
import java.util.ArrayList;
import java.util.List;

public class Lote {
	private List<Proceso> procesos = new ArrayList<>();
	
	public Lote(List<Proceso> nuevosProcesos){
		if (nuevosProcesos.size() <= 5) {
			this.procesos.addAll(nuevosProcesos);
		}
	}
	
	public void InterrumpirProceso(int i) {
		Proceso p = GetProceso(i);
		procesos.remove(i);
		procesos.add(p);
	}
	
	public Proceso GetProceso(int i) {	return procesos.get(i); }
	
	public int size() {	return procesos.size(); }
}