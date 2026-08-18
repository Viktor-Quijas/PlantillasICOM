package listaSimple;

public class Lista {
	private Nodo h; 	//	--> head
	private Nodo t;	//	--> tail
	private int longitud;
	
	public Lista(){
		this.longitud = 0;
		CrearLista();
	}
	
	public void CrearLista() {
		while (longitud < 18) {
			InsertarFinal();
		}
	}
	
	private void InsertarFinal() {
	    Nodo temp = new Nodo(longitud); 
	    longitud++;
	    if (h == null) {
	        h = temp;
	        t = temp;
	        t.sig = h;
	    } else {
	        t.sig = temp;
	        t = temp;
	        t.sig = h;
	    }
	}

	
	public int cantidadConsumible() {
		Nodo n = h;
		int i, cant = 0;
		
		for (i = 0; i < longitud; i++, n = n.sig) {
			if (n.consumible)
				cant++;
		}
		return cant;
	}
	
	public boolean listaLlena() {
		return cantidadConsumible() >= longitud;
	}
	
	public void limpiarEntidad() {
		Nodo n = h;
		int i;
		
		for (i = 0; i < longitud; i++, n = n.sig) {
			if (n.entidad >= 0)
				n.entidad = -1;
				
		}
	}
	
	public Nodo getCabeza() {
		return h;
	}
	
	public int getLongitud() {
		return longitud;
	}
}
