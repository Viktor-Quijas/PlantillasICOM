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
	
	private int operacion;
	private int valorX;
	private List<Proceso> procesosBloqueados = new ArrayList<>();
	
	Timer reloj;
	
	public TablaBloqueados(Timer Tabla_reloj) {
		setTitle("Procesos Bloqueados");
		setSize(300,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(525,300);
		
		String[] columnas = {"ID", "Ope", "Tiempo Transcurrido en Bloqueado"};
		modelo = new DefaultTableModel(null, columnas);
		
		tablaTerminados = new JTable(modelo);
		
		add(new JScrollPane(tablaTerminados), BorderLayout.CENTER);
		
		reloj = new Timer(1000, e -> {
			if (Tabla_reloj.isRunning())
				ActualizarTabla();			// <-- Función que refresca la tabla
		});
		reloj.start();
	}
	
	public void AgregarATablaBloqueados(Proceso p) {
		procesosBloqueados.add(p);
		
		operacion = p.getOperacion();
		p.setBloqueado(true);
		switch (operacion) {
		case 0:
			modelo.addRow(new Object[] {p.getId(), 
					p.getX() + " + " + p.getY(), 
					8});			
			break;
		case 1:
			modelo.addRow(new Object[] {p.getId(), 
					p.getX() + " - " + p.getY(), 
					8});			
			break;
		case 2:
			modelo.addRow(new Object[] {p.getId(), 
					p.getX() + " * " + p.getY(), 
					8});			
			break;
		case 3:
			modelo.addRow(new Object[] {p.getId(), 
					p.getX() + " / " + p.getY(), 
					8});			
			break;
		case 4:
			modelo.addRow(new Object[] {p.getId(), 
					p.getX() + " ^ " + p.getY(), 
					8});			
			break;
		case 5:
			modelo.addRow(new Object[] {p.getId(), 
					p.getX() + " % " + p.getY(), 
					8});			
			break;
		}
		
	}
	
	private void ActualizarTabla() {
		for (int i = 0; i < modelo.getRowCount(); i++) {
			valorX = (int) modelo.getValueAt(i,2);
			if (valorX == 0) {
				procesosBloqueados.get(i).setBloqueado(false);
				procesosBloqueados.remove(i);
				modelo.removeRow(i);
			} else {
				modelo.setValueAt(valorX - 1, i, 2);
			}
		}
	}
}
