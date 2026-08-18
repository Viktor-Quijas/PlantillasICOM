package productorConsumidor;
import java.util.Random;
import listaSimple.*;

public abstract class Entidad {
	/*
	 * Constantes para cada uno de los tres posibles estados que pueden llegar a tener
	 * las entidades. 
	 * 
	 * DORMIDO: va a permanecer así durante un periodo.
	 * TRABAJANDO: se encuentra en la lista consumiendo/produciendo.
	 * INGRESANDO: se encuentra intentando ingresar a la lista.
	 * 
	 * */
	protected static final int DORMIDO = 0;
	protected static final int TRABAJANDO = 1;
	protected static final int DESPIERTO = 2;
	protected static final int INGRESANDO = 3;
	
	/*
	 * El productor se encarga de llenar el contenedor.
	 * El consumidor es quien elimina consumibles del contenedor
	 * 
	 * Estas constantes son identificadores para esas dos entidades.
	 * */
	public static final int PRODUCTOR = 0;
	public static final int CONSUMIDOR = 1;
	
	/*
	 * Variables que específican los valores de la identidad.
	 * 
	 * entidad: define si es productor o consumidor.
	 * estado: si se encuentra dormido, trabajando o intentando ingresar.
	 * tiempoDescanso: la cantidad de tiempo que van a tardar antes de querer volver a ingresar.
	 * cantidadAccion: la cantidad de veces que realizará su acción.
	 * actual: nodo actual en el que se encuentra.
	 * anterior: nodo anterior, sirve para desmarcar el camino de la entidad.
	 * longitudLista: almacena el tamaño de la lista.
	 * yaIngresoUnaVez: un tick extra para que aparezca el estado de INGRESANDO en el Frame.
	 * estaPorSalir: similar al anterior pero cuando ya terminó su trabajo.
	 * */
	
	private Random rand = new Random();
	
	protected Lista l;
	protected int entidad;
	protected int estado;
	protected int cantidadAccion;
	protected int tiempoDescanso = 0;
	protected Nodo actual = null;
	protected Nodo anterior = null;
	protected int longitudLista;
	protected boolean yaIngresoUnaVez = false;
	protected boolean estaPorSalir = false;
	
	/*
	 * Métodos pertenecientes a los dos tipos de entidad.
	 * Aquellos en los cuales ambos tengan comportamientos totalmente distinos
	 * se deja como método abstracto.
	 * */
	
	
	//	-- Metodos --
	
	/*
	 * Me lo pasó el cloude.
	 * Basicamente determina si la entidad terminó
	 * Se encarga de manejar todas las variables de conteó
	 * para que el FramePrincipal no se apendeje con los tiempos.
	 */
	
	public boolean tick() {
		reducirElSueno();
		if (estado == DORMIDO)
			return false;
		
		if (cantidadAccion > 0 && estado == TRABAJANDO && puedeSeguir()) {
			accion();
			cantidadAccion--;
		} else if (estado == TRABAJANDO)
			cantidadAccion = 0;
		
		if (estado == INGRESANDO && yaIngresoUnaVez)
			estado = TRABAJANDO;
		else if (estado == INGRESANDO)
			yaIngresoUnaVez = true;
		
		if (cantidadAccion <= 0 && estado == TRABAJANDO && (estaPorSalir || !puedeSeguir())) {	//Me mamé, esto lo hice yo y me mamé
			dormitar();
		} else if (cantidadAccion <= 0 && estado == TRABAJANDO) {
			estaPorSalir = true;
		}
		
		return cantidadAccion == 0 && estado == DORMIDO;
	}
	
	public boolean estaListo() {
		return cantidadAccion == 0 && estado == DESPIERTO;
	}
		
	
	/*
	 * Hace la siguiente pregunta: ¿Hay alguien?.
	 * Devuelve true si existe alguna entidad en la lista.
	 * */
	
	protected boolean HayAlguien() {
		if (actual == null)
			return true;
		
		Nodo temp = actual;
		
		for (int indexLongitud = 0; indexLongitud < longitudLista && temp != null; indexLongitud++) {
			if (temp.entidad >= 0)
				return true;
			temp = temp.sig;
		}
		
		return false;
	}
	
	/*
	 * Determina si la entidad puede ingresar
	 * Devuelve true si puede.
	 * */
	
	public boolean puedeIngresar(boolean listaLlena) {
		if (estado != DESPIERTO || HayAlguien() || listaLlena) 
			return false;
		return true;
	}
	
	public boolean puedeIngresar(int cantidadConsumible) {
		if (estado != DESPIERTO || HayAlguien() || cantidadConsumible <= 0) 
			return false;
		return true;
	}
	
	/*
	 * Pone a dormmir a la entidad
	 * cambia su estado.
	 * */
	
	protected void dormitar() {
		tiempoDescanso = obtenerNumeroAleatorioDormir();
		cantidadAccion = 0;
		yaIngresoUnaVez = false;
		estaPorSalir = false;
		estado = DORMIDO;
	}
	
	/*
	 * Ve si puede despertar a la entidad
	 * */
	
	protected void intentarDespertar() {
		if (tiempoDescanso <= 0) 
			estado = DESPIERTO;
	}
	
	/*
	 * Da un número aleatorio dentro de un rango dado.
	 * RANGO DEFAULT = 3 - 6
	 * */
	
	public int obtenerNumeroAleatorioAccion() { return rand.nextInt(10) + 3; }
	public int obtenerNumeroAleatorioDormir() { return rand.nextInt(4) + 3; }
	
	/*
	 * Funcion para poder reducir el sueño de la entidad y despertarla posteriormente.
	 * */
	
	protected void reducirElSueno() {
		if (estado == DORMIDO) {
			tiempoDescanso--;
			intentarDespertar();
		}
		
	}
	
	/*
	 * Devuelve el estado acutal de la entidad.
	 * */
	
	public int getEstado() { return estado; }
	
	/*
	 * Por definirse.
	 * Método abstracto que define la acción de cada tipo de entidad.
	 * */
	abstract void accion();
	
	/*
	 * Metodo abstracto que define las condiciones para entrar a las cajas.
	 * */
	
	abstract boolean ingresar();
	
	/*
	 * Pregunta si se puede seguir ejecutando
	 * */
	abstract boolean puedeSeguir();
}
