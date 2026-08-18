package lote;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Tabla extends JFrame{

	private static final long serialVersionUID = 1L;
	
	//	-- Variables de la tabla --
	private JTable tabla;
	private DefaultTableModel modelo;
	
	//	-- Variables para acceder a la lista --
	private List<Lote> lotes = new ArrayList<>();
	private int i = 0, j = 0;
	private Lote loteIndex;
	private int operacion;
	
	//	-- Variables para llevar el conteo --
	Timer reloj;
	private int contadorGlobal = 0;
	private int tiempoTranscurrido = 0;
	private int lotesProcesados = 0;
	
	public Tabla( List<Lote> L) {
		
		//	-- Inicializa los valores en la tabla --
		setTitle("Tabla de procesos");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		String[] columnas = {"_____", "_____", "_____", "_____"};
		modelo = new DefaultTableModel(null, columnas);
		
		modelo.addRow(new Object[] {"Lotes pendientes: ", "0"});
		modelo.addRow(new Object[] {"Lote Actual"});
		modelo.addRow(new Object[] {"ID", "TME"});
		modelo.addRow(new Object[] {"", ""});
		modelo.addRow(new Object[] {"", ""});
		modelo.addRow(new Object[] {"", ""});
		modelo.addRow(new Object[] {"", ""});
		modelo.addRow(new Object[] {"Proceso en ejecucion"});
		modelo.addRow(new Object[] {"ID", "00"});
		modelo.addRow(new Object[] {"Ope", "00"});
		modelo.addRow(new Object[] {"TME", "00"});
		modelo.addRow(new Object[] {"TT", "00"});
		modelo.addRow(new Object[] {"TR", "00"});
		modelo.addRow(new Object[] {"Contador global: ", "0"});
		modelo.addRow(new Object[] {"Terminados"});
		modelo.addRow(new Object[] {"ID", "Ope", "Res", "N. Lote"});
		
		tabla = new JTable(modelo);
		
		add(new JScrollPane(tabla), BorderLayout.CENTER);
		
		this.lotes.addAll(L);
		
		//	-- Inicializa las variables y el reloj --
		InicializarLotes();
		InicializarTabla();
		
		reloj = new Timer(1000, e -> {
			contadorGlobal++;			// <-- Reloj global
			tiempoTranscurrido++;		// <-- Reloj de cada proceso
			ActualizarTabla();			// <-- Función que refresca la tabla
			if (reloj.isRunning()) modelo.setValueAt(contadorGlobal, 13, 1);	//	<-- Si el tiempo se detuvo, el reloj global no se actualiza una última vez
		});
		reloj.start();
	}
	
	private void ActualizarTabla() {
		//	-- Si el tiempo transcurrido del proceso llega a su valor maximo entonces: 
		if (tiempoTranscurrido > loteIndex.GetProceso(j).getTiempoMax()) {
			
			ProcesosTerminados();		// <-- Añade al final el proceso que se terminó
			tiempoTranscurrido = 0;		// <-- Reinicia el reloj para el sig proceso
			j++;						// <-- Apunta al siguiente proceso
			
			//	-- Si ya no existen procesos en ese lote entonces:
			if (j >= loteIndex.size()) {
				j = 0;							// <-- Inicializa el apuntador a procesos
				i++;							// <-- Apunta al sig lote
				lotesProcesados++;				// <-- Aumenta el contador de procesos
				
				//	-- Si existen más lotes entones: carga el sigueinte lote, si no: termina --
				if (i < lotes.size())
					loteIndex = lotes.get(i);
				else {
					reloj.stop();
	                JOptionPane.showMessageDialog(null, "Simulación Finalizada");
					return;
				}
			} 
			
			//	-- Bloque encargado de actualizar los datos en la tabla --
			modelo.setValueAt(lotes.size() - lotesProcesados, 0, 1);
			
			//	-- Actualiza la lista de pendientes --
			for (int ind = 3; ind - 2 < loteIndex.size(); ind++) {									// 				
				if (loteIndex.size() > ind - 2 + j) {												//
					modelo.setValueAt(loteIndex.GetProceso(ind - 2 + j).getId() , ind, 0);			//
					modelo.setValueAt(loteIndex.GetProceso(ind - 2 + j).getTiempoMax() , ind, 1);	//
				} else {
					modelo.setValueAt( "" , ind, 0);
					modelo.setValueAt( "" , ind, 1);
				}
			}
			
			modelo.setValueAt(loteIndex.GetProceso(j).getId() , 8, 1);
			operacion = loteIndex.GetProceso(j).getOperacion();
			switch (operacion) {
			case 0:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " + " + loteIndex.GetProceso(j).getY(), 9, 1);
				break;
			case 1:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " - " + loteIndex.GetProceso(j).getY(), 9, 1);
				break;
			case 2:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " * " + loteIndex.GetProceso(j).getY(), 9, 1);
				break;
			case 3:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " / " + loteIndex.GetProceso(j).getY(), 9, 1);
				break;
			case 4:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " ^ " + loteIndex.GetProceso(j).getY(), 9, 1);
				break;
			case 5:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " % " + loteIndex.GetProceso(j).getY(), 9, 1);
			}
			modelo.setValueAt(loteIndex.GetProceso(j).getTiempoMax() , 10, 1);
			modelo.setValueAt(tiempoTranscurrido , 11, 1);
			modelo.setValueAt(loteIndex.GetProceso(j).getTiempoMax() , 12, 1);
			
		} else {
			modelo.setValueAt(tiempoTranscurrido , 11, 1);
			modelo.setValueAt(loteIndex.GetProceso(j).getTiempoMax() - tiempoTranscurrido , 12, 1);
			
		}
	}
	
	// -- Inicializa el índice de los lotes --
	private void InicializarLotes() {
		loteIndex = lotes.get(i);
	}
	
	//	-- Inicializa la tabla --
	private void InicializarTabla() {
		
		lotesProcesados++;
		
		modelo.setValueAt(lotes.size() - lotesProcesados, 0, 1);
		
		for (int ind = 3; ind - 2 < loteIndex.size(); ind++) {
			modelo.setValueAt(loteIndex.GetProceso(ind - 2).getId() , ind, 0);
			modelo.setValueAt(loteIndex.GetProceso(ind - 2).getTiempoMax() , ind, 1);
		}
		
		modelo.setValueAt(loteIndex.GetProceso(0).getId() , 8, 1);
		operacion = loteIndex.GetProceso(j).getOperacion();
		switch (operacion) {
		case 0:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " + " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 1:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " - " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 2:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " * " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 3:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " / " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 4:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " ^ " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 5:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " % " + loteIndex.GetProceso(j).getY(), 9, 1);
		}
		modelo.setValueAt(loteIndex.GetProceso(0).getTiempoMax() , 10, 1);
		modelo.setValueAt( "00" , 11, 1);
		modelo.setValueAt(loteIndex.GetProceso(0).getTiempoMax() , 12, 1);
	}
	
	//	-- Coloca el proceso terminado al final de la tabla -- 
	private void ProcesosTerminados() {
		operacion = loteIndex.GetProceso(j).getOperacion();
		switch (operacion) {
		case 0:
			modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
					loteIndex.GetProceso(j).getX() + " + " + loteIndex.GetProceso(j).getY(), 
					loteIndex.GetProceso(j).getResultado(),
					lotesProcesados});
			break;
		case 1:
			modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
					loteIndex.GetProceso(j).getX() + " - " + loteIndex.GetProceso(j).getY(), 
					loteIndex.GetProceso(j).getResultado(),
					lotesProcesados});			break;
		case 2:
			modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
					loteIndex.GetProceso(j).getX() + " * " + loteIndex.GetProceso(j).getY(), 
					loteIndex.GetProceso(j).getResultado(),
					lotesProcesados});			break;
		case 3:
			modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
					loteIndex.GetProceso(j).getX() + " / " + loteIndex.GetProceso(j).getY(), 
					loteIndex.GetProceso(j).getResultado(),
					lotesProcesados});			break;
		case 4:
			modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
					loteIndex.GetProceso(j).getX() + " ^ " + loteIndex.GetProceso(j).getY(), 
					loteIndex.GetProceso(j).getResultado(),
					lotesProcesados});			break;
		case 5:
			modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
					loteIndex.GetProceso(j).getX() + " % " + loteIndex.GetProceso(j).getY(), 
					loteIndex.GetProceso(j).getResultado(),
					lotesProcesados});			break;
		}

	}
}
