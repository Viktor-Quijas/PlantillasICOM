package lote;

public class Proceso {	
	private int x;
	private int y;
	private int operacion;
	private int id;
	private int tiempoMax;
	private String nombre;
	
	public int getResultado() {
		switch (operacion) {
		case 0:
			return x + y;
		case 1:
			return x - y;
		case 2: 
			return x * y;
		case 3:
			return x / y;
		case 4:
			return (int) Math.pow(x,y);
		case 5:
			return x % y;
		default:
			return -1111111111;
		}
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getOperacion() {
		return operacion;
	}
	public void setOperacion(int operacion) {
		this.operacion = operacion;
	}
	public int getId() {
		return id;
	}
	public void setId(int numID) {
		this.id = numID;
	}
	public int getTiempoMax() {
		return tiempoMax;
	}
	public void setTiempoMax(int tiempoMax) {
		this.tiempoMax = tiempoMax;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Proceso (String nombre, int id, int operacion, int x, int y, int tiempoMax) {
		this.nombre = nombre;
		this.id = id;
		this.operacion = operacion;
		this.x = x;
		this.y = y;
		this.tiempoMax = tiempoMax;
	}
	public Proceso() {}
	
}
