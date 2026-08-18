package lote;

public class Proceso {	
	public int x;
	public int y;
	public int operacion;
	public int id;
	public int tiempoMax;
	public String nombre;
	
	public int tiempoLlegada = 0;
	public int tiempoFinalizacion = 0;
	public int tiempoRetorno = 0;
	public int tiempoRespuesta = 0;
	public int tiempoBloqueado = 0;
	public int tiempoTranscurrido;		
	
	public boolean error = false;	
	public boolean bloqueado = false;
	
	public boolean seEjecuto = false;
	
	//	-- Setters y Getters --
	
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
	
	public String getOperacionChar() {
		switch (operacion) {
		case 0:
			return " + ";
		case 1:
			return " - ";
		case 2:
			return " * ";
		case 3:
			return " / ";
		case 4:
			return " ^ ";
		case 5:
			return " % ";
		default:
			return " naco ";
			
		}
	}
	
	//	-- Constructores --
	
	public Proceso (String nombre, int id, int operacion, int x, int y, int tiempoMax) {
		this.nombre = nombre;
		this.id = id;
		this.operacion = operacion;
		this.x = x;
		this.y = y;
		this.tiempoMax = tiempoMax;
		this.tiempoTranscurrido = 0;
	}
	
	public Proceso() {}
	
}
