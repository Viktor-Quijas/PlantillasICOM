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
	
	public Proceso GetProceso(int i) {	return procesos.get(i); }
	
	public int size() {	return procesos.size(); }
}