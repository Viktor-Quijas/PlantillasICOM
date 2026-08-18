package lote;

import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;

import java.awt.*;

public class Tabla extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public static final int DURACION_DE_SEGUNDO = 1000;	//	--> Cambia la velocidad de cada iteracion.
	public static final int LONGITUD_MEMORIA_FISICA = 48;
	
	/*	-- PENDEJADAS PARA LOS MARGENES ASHH -- */
	public final int MARGEN_X = 150;
	public final int MARGEN_Y = 60;
	
	public final int ANCHO = 0;
	public final int ALTO = 1;
	
	
	private static final int INTERCAMBIO_DE_PROCESOS = 0;
	private static final int PAUSA = 1;
	private static final int CONTINUAR = 2;
	private static final int INTERRUPCION = 3;
	private static final int ERROR = 4;
	private static final int PROCESO_NUEVO = 5;
	private static final int TABLA_BCP = 6;
	private static final int REGRESAR_DE_BLOQUEADO_A_MEMORIA = 7;
	private static final int CONTINUAR_EJECUCION = 8;
	private static final int TERMINAR_PROGRAMA = 9;
	private static final int ROUND_ROBIN = 10;
	private static final int MEMORY_FRAMES = 11;
	
	
	//	-- Variables para los paneles --
	private JPanel panelProcesosPendientes;
	private JPanel panelProcesosEjecucion;
	private JPanel panelMemoria;
	private JPanel panelTiempoGlobal;
	
	private JLabel lblProcesosPendientes = new JLabel("");
	
	private JLabel lblId = new JLabel ("");
	private JLabel lblOperacion = new JLabel ("");
	private JLabel lblTiempoMaximo = new JLabel ("");
	private JLabel lblTiempoTranscurrido = new JLabel ("");
	private JLabel lblTiempoRestante = new JLabel ("");
	private JLabel lblLongitud = new JLabel ("");
	
	private JLabel[] lblIdMemoria = new JLabel[LONGITUD_MEMORIA_FISICA];
	private JLabel[] lblTMEMemoria = new JLabel[LONGITUD_MEMORIA_FISICA];
	private JLabel[] lblTRMemoria = new JLabel[LONGITUD_MEMORIA_FISICA];
	private JLabel[] lblLongitudMemoria = new JLabel[LONGITUD_MEMORIA_FISICA];
	private int procesosEnMemoria = 0;
	
	private JLabel lblTiempoGlobal = new JLabel("");
	
	//	-- Variables para acceder a la lista --
	private List<Integer> colaEventos = new ArrayList<>();
	private List<Integer> eventos = new ArrayList<>();
	private List<Proceso> procesos = new ArrayList<>();
	private List<Proceso> memoriaVirtual = new ArrayList<>();
	private Proceso[] memoriaFisica = new Proceso[48];
	private List<Proceso> bloqueados = new ArrayList<>();
	private List<Proceso> procesador = new ArrayList<>();
	private List<Proceso> terminados = new ArrayList<>();
	private List<List<Proceso>> registroListas = new ArrayList<>();
	
	//	-- Variables para llevar el conteo --
	public Timer rafaga;
	private long segundoAnterior;
	private long segundoActual;
	private boolean relojLogico = true;
	private int quantum;
	private int contadorQuantum = 0;
	
	private int procesoId;
	private int contadorGlobal = 0;
	
	//	-- Variables para las tablas --
	
	private TablaTerminados tablaTerminados;
	private TablaBloqueados tablaBloqueados;
	private TablaBCP tablaBCP;
	private MemoryFrames tablaMemoryFrames;
	
	public Tabla(List<Proceso> L, int quantum) {
	//	-- Inicializa los valores en la tabla --
		
		int[] area = areaUtil();
		
		setTitle("Tabla de Ejecución");
		pack();
		setSize(area[ANCHO], area[ALTO]);
		setLocation(MARGEN_X, MARGEN_Y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10,10));
		
		
		IngresarLista(L);
		InicializarMemoriaFisica();
		InicializarVariables(quantum);
		IntegrarListas();
		InicializarPaneles();
		InicializarTabla();
		InicializarTablasAdicionales();
		
		
	//	-- Encargado de escuchar si una tecla fue presionada --
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_P:
						if (relojLogico) {
							colaEventos.add(PAUSA);
							System.out.println("Pausa");
						}
						break;
					case KeyEvent.VK_C:
						if (!relojLogico) {
							colaEventos.add(CONTINUAR);
							System.out.println("Continuar");
						}
						break;
					case KeyEvent.VK_I:
						if (relojLogico) {
							colaEventos.add(INTERRUPCION);
						}
						break;
					case KeyEvent.VK_E:
						if (relojLogico) {
							colaEventos.add(ERROR);
						}
						break;
					case KeyEvent.VK_N:
						if (relojLogico) {
							colaEventos.add(PROCESO_NUEVO);
						}
						break;
					case KeyEvent.VK_B:
						if(relojLogico) {
							colaEventos.add(TABLA_BCP);
						}
					case KeyEvent.VK_T:
						if (relojLogico) {
							colaEventos.add(MEMORY_FRAMES);
						}
					}
				}
			});
			
			this.setFocusable(true);
			this.requestFocusInWindow();
		
		rafaga.start();
	}
	
	/*	-- INICIALIZACION --	*/
	
	public int[] areaUtil() {
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

		int x = pantalla.width;
		int y = pantalla.height;
		
		int[] areaUtil = {(x - (MARGEN_X * 2)) / 2, (y - (MARGEN_Y * 2)) / 2};
		
		return areaUtil;
	}
	
	public void IngresarLista(List<Proceso> L) {
		if (L != null && L.size() > 0) 
			procesos.addAll(L);
	}
	
	public void InicializarMemoriaFisica() {
		for (int i = 1; i <= 4; i++) {
			memoriaFisica[LONGITUD_MEMORIA_FISICA - i] = new Proceso("SO");
		}
	}
	
	private void InicializarVariables(int quantum) {
		this.quantum = quantum;
		relojLogico = true;
		procesoId = procesos.size() + 1;
		segundoAnterior = System.currentTimeMillis();
		rafaga = new Timer(1, e -> {										//	--> Ejecuta los eventos de la manera más rápida posible
			segundoActual = System.currentTimeMillis();
			
			if (segundoActual - segundoAnterior >= DURACION_DE_SEGUNDO && relojLogico) {	//	--> Itera cada segundo
				contadorGlobal++;			// <-- Reloj global
				Ejecucion();
				contadorQuantum++;
				segundoAnterior += DURACION_DE_SEGUNDO;
			} else if (!relojLogico)
				segundoAnterior = segundoActual;
			
			EjecutarColaEventos();
			EventosProximos();
		});
	}
	
	private void InicializarPaneles() {
	    // --- Fuentes personalizadas para mejorar la lectura ---
	    Font fuenteTitulo = new Font("SansSerif", Font.BOLD, 14);
	    Font fuenteResaltado = new Font("SansSerif", Font.BOLD, 16);
	    
	    JPanel panelAuxCentral = new JPanel(new GridLayout(1, 2, 10, 10));
	    JPanel panelAuxNorte = new JPanel(new GridLayout(1, 2, 10, 10));
	    //JScrollPane panelAuxScrollMemoria = new JScrollPane();
	    
	    panelAuxNorte.setBorder(BorderFactory.createLineBorder(Color.BLACK));

	    // --- Panel de Procesos Pendientes (EAST) ---
	    // Usamos un borde con título en lugar de un JLabel suelto
	    panelProcesosPendientes = new JPanel(new GridLayout(1, 2));
	    panelProcesosPendientes.add(new JLabel(" Procesos pendientes:"));
	    panelProcesosPendientes.add(lblProcesosPendientes);
	    
	    panelAuxNorte.add(panelProcesosPendientes, BorderLayout.WEST);
	    panelAuxNorte.add(new JLabel(" Quantum: " + quantum), BorderLayout.EAST);


	    // --- Panel de Ejecución (CENTER) ---
	    // Reducimos una fila porque el título ahora es parte del borde
	    panelProcesosEjecucion = new JPanel(new GridLayout(6, 2, 10, 10));
	    panelProcesosEjecucion.setBorder(BorderFactory.createTitledBorder(null, "Proceso en Ejecución", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, fuenteTitulo));
	    
	    panelProcesosEjecucion.add(new JLabel(" ID:"));
	    panelProcesosEjecucion.add(lblId);
	    panelProcesosEjecucion.add(new JLabel(" Operación: "));
	    panelProcesosEjecucion.add(lblOperacion);
	    panelProcesosEjecucion.add(new JLabel(" Tiempo Máximo Estimado: "));
	    panelProcesosEjecucion.add(lblTiempoMaximo);
	    panelProcesosEjecucion.add(new JLabel(" Tiempo Transcurrido: "));
	    panelProcesosEjecucion.add(lblTiempoTranscurrido);
	    panelProcesosEjecucion.add(new JLabel(" TRestante: ")); // Corregido el typo "Timepo"
	    panelProcesosEjecucion.add(lblTiempoRestante);
	    panelProcesosEjecucion.add(new JLabel(" Tamaño: ")); // Corregido el typo "Timepo"
	    panelProcesosEjecucion.add(lblLongitud);


	    // --- Panel de Memoria (WEST) ---
	    panelMemoria = new JPanel(new GridLayout(0, 4, 10, 10)); // 6 filas (1 de encabezados + 5 de datos)
	    panelMemoria.setBorder(BorderFactory.createTitledBorder(null, "Procesos en Memoria", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, fuenteTitulo));
	    
	    panelMemoria.add(new JLabel("ID"));
	    panelMemoria.add(new JLabel("TME"));
	    panelMemoria.add(new JLabel("TR"));
	    panelMemoria.add(new JLabel("Tamaño"));


	    // --- Panel de Tiempo Global (SOUTH) ---
	    // Un FlowLayout alineado a la derecha se ve mejor para contadores globales
	    panelTiempoGlobal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
	    
	    JLabel lblTituloTiempo = new JLabel("Tiempo Global:");
	    lblTituloTiempo.setFont(fuenteTitulo);
	    
	    lblTiempoGlobal.setFont(fuenteResaltado);
	    lblTiempoGlobal.setForeground(new Color(0, 102, 204)); // Un color azul para destacarlo
	    lblTiempoGlobal.setText("0"); // Valor inicial por defecto
	    
	    panelTiempoGlobal.add(lblTituloTiempo);
	    panelTiempoGlobal.add(lblTiempoGlobal);


	    // --- Márgenes Generales de la Ventana ---
	    // Esto evita que los paneles peguen completamente con los bordes de la ventana
	    ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
		panelAuxCentral.add(panelProcesosEjecucion);
		panelAuxCentral.add(panelMemoria);

	    // Agregamos los paneles al JFrame con la nueva distribución
	    add(panelAuxNorte, BorderLayout.NORTH);
	    add(panelAuxCentral, BorderLayout.CENTER);
	    add(panelTiempoGlobal, BorderLayout.SOUTH);
	}
	
	private void InicializarTabla() {
		IngresarAMemoria();
		RefrescarTabla();
	}
	
	private void InicializarTablasAdicionales() {
		tablaTerminados = new TablaTerminados(this);
		tablaTerminados.setVisible(true);
		tablaBloqueados = new TablaBloqueados(this);
		tablaBloqueados.setVisible(true);
		tablaBCP = new TablaBCP(this);
		tablaMemoryFrames = new MemoryFrames(this, MemoryFrames.PRINCIPAL);
		tablaMemoryFrames.setVisible(true);
	}
	
	private void IntegrarListas() {
		registroListas.add(procesador);
		registroListas.add(memoriaVirtual);
		registroListas.add(bloqueados);
		registroListas.add(terminados);
		registroListas.add(procesos);
	}
	
	/*	-- EVENTOS --	*/
	
	private void EjecutarColaEventos() {
		eventos.addAll(colaEventos);
		colaEventos.clear();
		int indexEventos = 0, cantidadEventos = eventos.size();
		while(indexEventos < cantidadEventos) {
			switch(eventos.get(indexEventos)) {
			case INTERCAMBIO_DE_PROCESOS:
				Termino();
				break;
			case PAUSA:
				Pausa();
				break;
			case CONTINUAR:
				Continuar();
				break;
			case INTERRUPCION:
				Interrupcion();
				break;
			case ERROR:
				Error();
				break;
			case PROCESO_NUEVO:
				AgregarProcesoNuevo();
				break;
			case TABLA_BCP:
				MostrarBCP();
				break;
			case REGRESAR_DE_BLOQUEADO_A_MEMORIA:
				BloqueadoAMemoria();
				break;
			case CONTINUAR_EJECUCION:
				ContinuarEjecucion();
				break;
			case TERMINAR_PROGRAMA:
				TerminarPrograma();
				break;
			case ROUND_ROBIN:
				RR();
				break;
			case MEMORY_FRAMES:
				MostrarMemoryFrames();
				break;
			}
			indexEventos++;
		}
		eventos.clear();
	}
	
	/*	-- METODOS COMPLEMENTARIOS --	*/
	
	private boolean hayMarcosDisponibles(int paginasNecesarias) {
		int marcosDisponibles = 0;
		
		for  (int i = 0; i < LONGITUD_MEMORIA_FISICA && marcosDisponibles < paginasNecesarias; i++) {
			if (memoriaFisica[i] == null)
				marcosDisponibles++;
		}
		return marcosDisponibles >= paginasNecesarias;
	}
	
	private boolean ingresarMemoriaFisica(Proceso p) {
		int paginasNecesarias = p.getCantidadPaginas();
		
		if (!hayMarcosDisponibles(paginasNecesarias))
			return false;
		
		int paginasIngresadas = 0;

		for (int i = 0; i < LONGITUD_MEMORIA_FISICA && paginasIngresadas < paginasNecesarias; i++) {
			if (memoriaFisica[i] == null) {
				memoriaFisica[i] = p.getPagina(paginasIngresadas);
				paginasIngresadas++;
			}
		}
		
		return paginasIngresadas == paginasNecesarias;
	}
	
	private void eliminarDeMemoriaFisica(Proceso p) {
		int id = p.id, paginasRestantes = p.getCantidadPaginas();
		
		for (int i = 0; i < LONGITUD_MEMORIA_FISICA && paginasRestantes > 0; i++) {
			if (memoriaFisica[i] != null && memoriaFisica[i].id == id) {
				memoriaFisica[i] = null;
				paginasRestantes--;
			}
		}
	}
	
	private Proceso SacarDeEjecucion() {
		if (procesador.isEmpty())
			return null;
		Proceso enEjecucion;
		enEjecucion = procesador.get(0);
		procesador.remove(0);
		contadorQuantum = 0;
		return enEjecucion;
	}
	
	private void IngresarAEjecucion() {
		if (memoriaVirtual.isEmpty() || !procesador.isEmpty())
			return;
		Proceso siguienteAEjecutar;
		siguienteAEjecutar = memoriaVirtual.get(0);
		procesador.add(siguienteAEjecutar);
		memoriaVirtual.remove(0);
		if (!procesador.get(0).seEjecuto) {
			procesador.get(0).seEjecuto = true;
			procesador.get(0).tiempoRespuesta = contadorGlobal - procesador.get(0).tiempoLlegada;
		}
	}
	
	private void IngresarAMemoria() {
		if (procesos.isEmpty())
			return;
		
		Proceso siguienteEnMemoria;
		while (!procesos.isEmpty() && hayMarcosDisponibles(procesos.get(0).getCantidadPaginas())) {
			siguienteEnMemoria = procesos.get(0);
			
			if (ingresarMemoriaFisica(siguienteEnMemoria)) {
				memoriaVirtual.add(siguienteEnMemoria);
				procesos.remove(0);
				memoriaVirtual.get(memoriaVirtual.size() - 1).tiempoLlegada = contadorGlobal;
				agregarFilaParaMemoria();
				
				if (procesador.isEmpty()) {
					IngresarAEjecucion();
				}
			} else {
				eliminarDeMemoriaFisica(siguienteEnMemoria);
			}
		}
	}
	
	private void IngresarATerminados(Proceso terminado) {
		if (terminado == null)
			return;
		eliminarDeMemoriaFisica(terminado);
		terminados.add(terminado);
		terminado.tiempoFinalizacion = contadorGlobal;
		terminado.tiempoRetorno = terminado.tiempoFinalizacion - terminado.tiempoLlegada;
		tablaTerminados.AgregarATablaTerminados(terminado);
	}
	
	private void IngresarABloqueados(Proceso bloqueado) {
		if (bloqueado == null)
			return;
		bloqueado.bloqueado = true;
		bloqueado.tiempoBloqueado = 7;
		bloqueados.add(bloqueado);
		tablaBloqueados.AgregarATablaBloqueados(bloqueado);
	}
	
	private void BloqueadoAMemoria() {
		if (!bloqueados.isEmpty()) {
			for (int i = 0; i < bloqueados.size(); i++) {
				if (!bloqueados.get(i).bloqueado) {
					memoriaVirtual.add(bloqueados.get(i));
					bloqueados.remove(i);
					i--;
				}
				if (procesador.isEmpty()) {
					IngresarAEjecucion();
				}
			}
		}
	}
	
	private Proceso CrearProceso() {
		Proceso p = new Proceso(
			"Proceso-" + procesoId,
			procesoId,
			(int) (Math.random() * 6),   // Operación (0 a 5)
            (int) (Math.random() * 50),  // X
            (int) (Math.random() * 50) + 1, // Y (evitamos división por 0)
            (int) (Math.random() * 15) + 6
		);
		procesoId++;
		return p;
	}
	
	private void RefrescarTabla() {
		lblTiempoGlobal.setText(String.valueOf(contadorGlobal));
		lblProcesosPendientes.setText(String.valueOf(procesos.size()));
		
		if (memoriaVirtual.isEmpty() && procesador.isEmpty()) {
			LimpiarVentana();
			return;
		}
		
		lblId.setText(String.valueOf(procesador.get(0).id));
		lblOperacion.setText(String.valueOf(procesador.get(0).x + procesador.get(0).getOperacionChar() + procesador.get(0).y));
		lblTiempoMaximo.setText(String.valueOf(procesador.get(0).tiempoMax));
		lblTiempoTranscurrido.setText(String.valueOf(procesador.get(0).tiempoTranscurrido));
		lblTiempoRestante.setText(String.valueOf(procesador.get(0).tiempoMax - procesador.get(0).tiempoTranscurrido));
		lblLongitud.setText(String.valueOf(procesador.get(0).getLongitud()));
		
		
		for (int i = 0; i < procesosEnMemoria; i++) {
			if (i < memoriaVirtual.size()) {				
				lblIdMemoria[i].setText(String.valueOf(memoriaVirtual.get(i).id));
				lblTMEMemoria[i].setText(String.valueOf(memoriaVirtual.get(i).tiempoMax));
				lblTRMemoria[i].setText(String.valueOf(memoriaVirtual.get(i).tiempoMax - memoriaVirtual.get(i).tiempoTranscurrido));
				lblLongitudMemoria[i].setText(String.valueOf(memoriaVirtual.get(i).getLongitud()));
			} else {																				
				lblIdMemoria[i].setText("-");
				lblTMEMemoria[i].setText("-");
				lblTRMemoria[i].setText("-");
				lblLongitudMemoria[i].setText("-");
			}
		}
	}
	
	private void agregarFilaParaMemoria() {
		if (memoriaVirtual.size() <= procesosEnMemoria)
			return;
		
		lblIdMemoria[procesosEnMemoria] = new JLabel("-");
        lblTMEMemoria[procesosEnMemoria] = new JLabel("-");
        lblTRMemoria[procesosEnMemoria] = new JLabel("-");
        lblLongitudMemoria[procesosEnMemoria] = new JLabel("-");
        // Centrar el texto en la memoria se ve más ordenado
        lblIdMemoria[procesosEnMemoria].setHorizontalAlignment(SwingConstants.CENTER);
        lblTMEMemoria[procesosEnMemoria].setHorizontalAlignment(SwingConstants.CENTER);
        lblTRMemoria[procesosEnMemoria].setHorizontalAlignment(SwingConstants.CENTER);
        lblLongitudMemoria[procesosEnMemoria].setHorizontalAlignment(SwingConstants.CENTER);
        
        panelMemoria.add(lblIdMemoria[procesosEnMemoria]);
        panelMemoria.add(lblTMEMemoria[procesosEnMemoria]);
        panelMemoria.add(lblTRMemoria[procesosEnMemoria]);
        panelMemoria.add(lblLongitudMemoria[procesosEnMemoria]);
        
        procesosEnMemoria++;
	}
	
	private void LimpiarVentana() {
		lblId.setText("");
		lblOperacion.setText("");
		lblTiempoMaximo.setText("");
		lblTiempoTranscurrido.setText("");
		lblTiempoRestante.setText("");
		lblLongitud.setText("");
	}
	
	private void LlamadaATerminar() {
		if (procesador.isEmpty() && memoriaVirtual.isEmpty() && bloqueados.isEmpty() && procesos.isEmpty())
			colaEventos.add(TERMINAR_PROGRAMA);
	}
	
	private void EventosProximos() {
		colaEventos.add(REGRESAR_DE_BLOQUEADO_A_MEMORIA);
		if (contadorQuantum >= quantum) {
			colaEventos.add(ROUND_ROBIN);
		}
	}

	/*	-- EVENTOS PRINCIPALES --	*/
	
	private void Ejecucion() {
		if (!colaEventos.contains(INTERCAMBIO_DE_PROCESOS) && !colaEventos.contains(INTERRUPCION) && !colaEventos.contains(ERROR)) {
			colaEventos.add(CONTINUAR_EJECUCION);
		}
	}
	
	private void Termino() {
		Proceso terminado;
		terminado = SacarDeEjecucion();
		IngresarATerminados(terminado);
		IngresarAEjecucion();
		IngresarAMemoria();
		RefrescarTabla();
		LlamadaATerminar();
	}
	
	private void Interrupcion() {
		if (procesador.isEmpty())
			return;
		Proceso bloqueado;
		bloqueado = SacarDeEjecucion();
		IngresarABloqueados(bloqueado);
		IngresarAEjecucion();
		RefrescarTabla();
	}
	
	private void Error() {
		if (procesador.isEmpty())
			return;
		Proceso erroneo;
		erroneo = SacarDeEjecucion();
		erroneo.error = true;
		IngresarATerminados(erroneo);
		IngresarAEjecucion();
		IngresarAMemoria();
		RefrescarTabla();
		LlamadaATerminar();
	}
	
	private void RR() {
		if (procesador.isEmpty())
			return;
		Proceso loSaca;
		loSaca = SacarDeEjecucion();
		memoriaVirtual.add(loSaca);
		IngresarAEjecucion();
		RefrescarTabla();
	}
	
	public void Continuar() { 
		relojLogico = true; 
		tablaBloqueados.ContinuarBloqueados();
	}
	
	public void Pausa() { 
		relojLogico = false;
		tablaBloqueados.PausaBloqueados();
	}
	
	private void TerminarPrograma() {
		this.setFocusable(false);
		rafaga.stop();
		LimpiarVentana();
		JOptionPane.showMessageDialog(null, "Simulación Finalizada");
		System.exit(0);
		return;
	}
	
	private void ContinuarEjecucion() {
		if (procesador.isEmpty()) {
			RefrescarTabla();
			return;
		}
		procesador.get(0).tiempoTranscurrido++;
		if (procesador.get(0).tiempoTranscurrido == procesador.get(0).tiempoMax) {
			colaEventos.add(INTERCAMBIO_DE_PROCESOS);
		}
		
		RefrescarTabla();
	}
	
	private void AgregarProcesoNuevo() { 
		procesos.add(CrearProceso());
		IngresarAMemoria();
		IngresarAEjecucion();
		RefrescarTabla();
	}
	
	private void MostrarBCP() {
		tablaBCP.IngresarDatosBCP(registroListas, contadorGlobal);
		Pausa();
		SwingUtilities.invokeLater(() -> {
			tablaBCP.setVisible(true);
		});
	}
	
	private void MostrarMemoryFrames() {
		MemoryFrames tablaFuncion = new MemoryFrames(this, MemoryFrames.FUNCION);
		Pausa();
		SwingUtilities.invokeLater(() -> {
			tablaFuncion.setVisible(true);
		});
	}
	
	/*Getters Y Setters*/
	public Proceso[] getMemoriaFisica() {
		return memoriaFisica;
	}
	
	public List<List<Proceso>> getRegistroListas(){
		return registroListas;
	}
}
