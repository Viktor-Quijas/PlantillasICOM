package lote;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import java.util.List;
import java.util.ArrayList;

public class TablaBloqueados extends JFrame{
private static final long serialVersionUID = 1L;
	
	private JTable tablaTerminados;
	private DefaultTableModel modelo;
	
	private int valorX;
	private List<Proceso> procesosBloqueados = new ArrayList<>();
	
	Timer reloj;
	
	public TablaBloqueados(Tabla tabla) {
		
		int[] area = tabla.areaUtil();
		
		setTitle("Procesos Bloqueados");
		setSize(area[tabla.ANCHO], area[tabla.ALTO]);
		setLocation(tabla.MARGEN_X, tabla.MARGEN_Y + area[tabla.ALTO]);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String[] columnas = {"ID", "Operacion", "Tamaño", "Tiempo Restante en CPU", "TR en Bloqueado"};
		modelo = new DefaultTableModel(null, columnas);
		
		tablaTerminados = new JTable(modelo);
		
		add(new JScrollPane(tablaTerminados), BorderLayout.CENTER);
		
		reloj = new Timer(1000, e -> {
			ActualizarTabla();			// <-- Función que refresca la tabla
		});
		reloj.start();
	}
	
	public void AgregarATablaBloqueados(Proceso p) {
		procesosBloqueados.add(p);
		modelo.addRow(new Object[] {
				p.id, 
				p.x + p.getOperacionChar() + p.y,
				p.getLongitud(),
				p.tiempoMax - p.tiempoTranscurrido,
				p.tiempoBloqueado});
		
	}
	
	private void ActualizarTabla() {
		for (int i = 0; i < modelo.getRowCount(); i++) {
			valorX = (int) modelo.getValueAt(i,4);
			if (valorX == 0) {
				procesosBloqueados.get(i).bloqueado = false;
				procesosBloqueados.remove(i);
				modelo.removeRow(i);
				i--;
			} else {
				procesosBloqueados.get(i).tiempoBloqueado--;
				modelo.setValueAt(valorX - 1, i, 4);
			}
		}
	}
	
	public void PausaBloqueados() { reloj.stop(); }
	public void ContinuarBloqueados() { reloj.start(); }
}
