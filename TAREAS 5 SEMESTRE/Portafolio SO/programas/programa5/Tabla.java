package lote;

import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;

import java.awt.*;

public class Tabla extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static final int DURACION_DE_SEGUNDO = 1000;	//	--> Cambia la velocidad de cada iteracion.
	private static final int MAX_PROCESOS_EN_MEMORIA = 5;
	
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
	
	JLabel[] lblIdMemoria = new JLabel[4];
	JLabel[] lblTMEMemoria = new JLabel[4];
	JLabel[] lblTRMemoria = new JLabel[4];
	
	JLabel lblTiempoGlobal = new JLabel("");
	
	//	-- Variables para acceder a la lista --
	private List<Integer> colaEventos = new ArrayList<>();
	private List<Integer> eventos = new ArrayList<>();
	private List<Proceso> procesos = new ArrayList<>();
	private List<Proceso> memoria = new ArrayList<>();
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
	
	public Tabla(List<Proceso> L, int quantum) {
	//	-- Inicializa los valores en la tabla --
		setTitle("Tabla de procesos");
		setSize(500, 290);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(25,300);
		setLayout(new BorderLayout(10,10));
		
		IngresarLista(L);
		InicializarVariables(quantum);
		InicializarPaneles();
		InicializarTabla();
		InicializarTablasAdicionales();
		IntegrarListas();
		
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
							setTitle("Cambiando de contexto");
							colaEventos.add(INTERRUPCION);
						}
						break;
					case KeyEvent.VK_E:
						if (relojLogico) {
							setTitle("Cambiando de contexto");
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
					}
				}
			});
			
			this.setFocusable(true);
			this.requestFocusInWindow();
		
		rafaga.start();
	}
	
	/*	-- INICIALIZACION --	*/
	
	public void IngresarLista(List<Proceso> L) {
		if (L != null && L.size() > 0) 
			procesos.addAll(L);
	}
	
	private void InicializarPaneles() {
		panelProcesosPendientes = new JPanel(new GridLayout(2, 2));
		
		panelProcesosPendientes.add(new JLabel("Procesos pendientes:"));
		panelProcesosPendientes.add(lblProcesosPendientes);
		panelProcesosPendientes.add(new JLabel("Quantum: " + quantum));
		
		
		panelProcesosEjecucion = new JPanel(new GridLayout(6, 2));
		
		
		panelProcesosEjecucion.add(new JLabel("PROCESO EN EJECUCION"));
		panelProcesosEjecucion.add(new JLabel(""));
		panelProcesosEjecucion.add(new JLabel("ID:"));
		panelProcesosEjecucion.add(lblId);
		panelProcesosEjecucion.add(new JLabel("Operación:"));
		panelProcesosEjecucion.add(lblOperacion);
		panelProcesosEjecucion.add(new JLabel("Tiempo Máximo Estimado:"));
		panelProcesosEjecucion.add(lblTiempoMaximo);
		panelProcesosEjecucion.add(new JLabel("Tiempo Transcurrido:"));
		panelProcesosEjecucion.add(lblTiempoTranscurrido);
		panelProcesosEjecucion.add(new JLabel("Timepo Restante:"));
		panelProcesosEjecucion.add(lblTiempoRestante);
		
		
		panelMemoria = new JPanel(new GridLayout(5, 3, 10, 10));
		
		panelMemoria.add(new JLabel("ID"));
		panelMemoria.add(new JLabel("TME"));
		panelMemoria.add(new JLabel("TR"));
		
		for (int i = 0; i < 4; i++) {
			lblIdMemoria[i] = new JLabel("");
			lblTMEMemoria[i] = new JLabel("");
			lblTRMemoria[i] = new JLabel("");
			panelMemoria.add(lblIdMemoria[i]);
			panelMemoria.add(lblTMEMemoria[i]);
			panelMemoria.add(lblTRMemoria[i]);
		}
		
		
		panelTiempoGlobal = new JPanel(new GridLayout(1, 2));
		
		panelTiempoGlobal.add(new JLabel("Tiempo Global:"));
		panelTiempoGlobal.add(lblTiempoGlobal);
		
		add(panelProcesosEjecucion, BorderLayout.PAGE_START);
		add(panelProcesosPendientes, BorderLayout.EAST);
		add(panelMemoria, BorderLayout.WEST);
		add(panelTiempoGlobal, BorderLayout.SOUTH);
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
			
			EventosProximos();
			EjecutarColaEventos();
		});
	}
	
	private void InicializarTabla() {
		int i, maxProcesos = procesos.size();
		for (i = 0; i < 5 && i < maxProcesos; i++) {
			IngresarAMemoria();
			memoria.get(memoria.size() - 1).tiempoLlegada = 0;
		}
		IngresarAEjecucion();
		procesador.get(0).tiempoRespuesta = 0;
		RefrescarTabla();
	}
	
	private void InicializarTablasAdicionales() {
		tablaTerminados = new TablaTerminados();
		tablaTerminados.setVisible(true);
		tablaBloqueados = new TablaBloqueados();
		tablaBloqueados.setVisible(true);
		tablaBCP = new TablaBCP(this);
	}
	
	private void IntegrarListas() {
		registroListas.add(procesador);
		registroListas.add(memoria);
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
			}
			indexEventos++;
		}
		eventos.clear();
	}
	
	/*	-- EVENTOS COMPLEMENTARIOS --	*/
	
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
		if (memoria.isEmpty() || !procesador.isEmpty())
			return;
		Proceso siguienteAEjecutar;
		siguienteAEjecutar = memoria.get(0);
		procesador.add(siguienteAEjecutar);
		memoria.remove(0);
		if (!procesador.get(0).seEjecuto) {
			procesador.get(0).seEjecuto = true;
			procesador.get(0).tiempoRespuesta = contadorGlobal - procesador.get(0).tiempoLlegada;
		}
	}
	
	private void IngresarAMemoria() {
		if (procesos.isEmpty() || procesador.size() + memoria.size() + bloqueados.size() == MAX_PROCESOS_EN_MEMORIA)
			return;
		Proceso siguienteEnMemoria;
		siguienteEnMemoria = procesos.get(0);
		memoria.add(siguienteEnMemoria);
		procesos.remove(0);
		memoria.get(memoria.size() - 1).tiempoLlegada = contadorGlobal;
	}
	
	private void IngresarATerminados(Proceso terminado) {
		if (terminado == null)
			return;
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
					memoria.add(bloqueados.get(i));
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
		
		if (memoria.isEmpty() && procesador.isEmpty()) {
			LimpiarVentana();
			return;
		}
		
		lblId.setText(String.valueOf(procesador.get(0).id));
		lblOperacion.setText(String.valueOf(procesador.get(0).x + procesador.get(0).getOperacionChar() + procesador.get(0).y));
		lblTiempoMaximo.setText(String.valueOf(procesador.get(0).tiempoMax));
		lblTiempoTranscurrido.setText(String.valueOf(procesador.get(0).tiempoTranscurrido));
		lblTiempoRestante.setText(String.valueOf(procesador.get(0).tiempoMax - procesador.get(0).tiempoTranscurrido));
		
		for (int i = 0; i < 4; i++) {
			if (i < memoria.size()) {				
				lblIdMemoria[i].setText(String.valueOf(memoria.get(i).id));
				lblTMEMemoria[i].setText(String.valueOf(memoria.get(i).tiempoMax));
				lblTRMemoria[i].setText(String.valueOf(memoria.get(i).tiempoMax - memoria.get(i).tiempoTranscurrido));
			} else {																				
				lblIdMemoria[i].setText("");
				lblTMEMemoria[i].setText("");
				lblTRMemoria[i].setText("");
			}
		}
	}
	
	private void LimpiarVentana() {
		lblId.setText("");
		lblOperacion.setText("");
		lblTiempoMaximo.setText("");
		lblTiempoTranscurrido.setText("");
		lblTiempoRestante.setText("");
	}
	
	private void LlamadaATerminar() {
		if (procesador.isEmpty() && memoria.isEmpty() && bloqueados.isEmpty() && procesos.isEmpty())
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
		memoria.add(loSaca);
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
			LlamadaATerminar();
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
	
	
}
