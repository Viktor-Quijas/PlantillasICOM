package listaSimple;

public class Nodo {
	public int entidad;
	public boolean consumible;
	public Nodo sig;
	public int index;
	
	Nodo(){
		this.entidad = -1;
		this.consumible = false;
		this.sig = null;
	}
	
	Nodo(int index){
		this.entidad = -1;
		this.consumible = false;
		this.sig = null;
		this.index = index;
	}
	
	Nodo(Nodo sig, int index){
		this.entidad = -1;
		this.consumible = false;
		this.sig = sig;
		this.index = index;
	}
}
