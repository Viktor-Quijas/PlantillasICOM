package lote;

import java.util.ArrayList;
import java.util.List;

public class Proceso {
	public final static int NUMERO_PAG = 0;
	public final static int LONGITUD_PAG = 1;
	
	public static final int ID = 0;
	public static final int PAG = 1;
	public static final int MARCO = 2;
	public static final int LONG = 3;
	
	public int x;
	public int y;
	public int operacion;
	public int id;
	public int tiempoMax;
	public String nombre;
	
	private int longitud;
	private int cantPaginas;
	
	public int[] pagina = new int[2];
	public List<Proceso> paginas = new ArrayList<>();
	
	
	public int tiempoLlegada;
	public int tiempoFinalizacion;
	public int tiempoRetorno;
	public int tiempoRespuesta;
	public int tiempoBloqueado;
	public int tiempoTranscurrido;		
	
	public boolean error;	
	public boolean bloqueado;
	
	public boolean seEjecuto;
	public boolean seIngresoAMemoria;
	
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
	
	private void crearPaginas() {
		cantPaginas = longitud / 5;
		
		if (longitud % 5 > 0)
			cantPaginas++;
		
		pagina[NUMERO_PAG] = 0;
		if (longitud > 5) 
			pagina[LONGITUD_PAG] = 5;
		else 
			pagina[LONGITUD_PAG] = longitud;
		
		paginas.add(this);
		Proceso pagActual = this;
		Proceso nuevaPag;
		
		for (int i = 1; i < cantPaginas; i++, pagActual = nuevaPag) {
			nuevaPag = new Proceso(pagActual);
			paginas.add(nuevaPag);
		}
	}
	
	public Proceso getPagina(int i) {
		return paginas.get(i);
	}
	
	public Proceso getPaginaPrincipal() {
		return paginas.get(0);
	}
	
	public int[] getPaginaInfo() {
		return pagina;
	}
	
	public int[] getPaginaInfo(int i) {
		return paginas.get(i).pagina;
	}
	
	public int getCantidadPaginas() {
		return cantPaginas;
	}
	
	public int getLongitud() {
		return longitud;
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
		error = false;	
		bloqueado = false;
		seEjecuto = false;
		
		longitud = (int) (Math.random() * 24) + 6;
		
		crearPaginas();
	}
	
	private Proceso (Proceso p) {
		
		this.nombre = p.nombre;
		this.id = p.id;
		this.operacion = p.operacion;
		this.x = p.x;
		this.y = p.y;
		this.longitud = p.getLongitud();
		this.tiempoMax = p.tiempoMax;
		this.tiempoTranscurrido = -1;
		this.cantPaginas = p.getCantidadPaginas();
		this.paginas = p.paginas;
		error = false;	
		bloqueado = false;
		seEjecuto = false;
		
		this.pagina[NUMERO_PAG] = p.pagina[NUMERO_PAG] + 1;
	
		if (cantPaginas == this.pagina[NUMERO_PAG] + 1 && longitud % 5 > 0)
			this.pagina[LONGITUD_PAG] = longitud % 5;
		else 
			this.pagina[LONGITUD_PAG] = 5;
		
	}
	
	public Proceso (String SO) {
		if (SO == "SO")
			this.nombre = SO;
		else 
			this.nombre = "Corrupto";
	}
	
}
