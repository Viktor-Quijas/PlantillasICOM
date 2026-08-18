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
	
	public Lote() {}
	
	public void agregarProceso(Proceso p) {
		if (procesos.size() <= 5) {
			this.procesos.add(p);
		}
		else {
			System.out.println("No se pueden añadir más procesos a la ejecución");
		}
	}
	
	public void interrumpirProceso(int i) {
		Proceso p = getProceso(i);
		procesos.remove(i);
		procesos.add(p);
	}
	
	public void popProceso(int i) {
		procesos.remove(i);
	}
	
	public Proceso getProceso(int i) {	return procesos.get(i); }
	
	public int tamanio() {	return procesos.size(); }
}