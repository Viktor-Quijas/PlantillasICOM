package lote;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TablaTerminados extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JTable tablaTerminados;
	private DefaultTableModel modelo;
	
	private char operacion;
	
	public TablaTerminados() {
		setTitle("Procesos Terminados");
		setSize(1050,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(825,300);
		
		String[] columnas = {"ID", "Ope", "Res", "Tiempo de Llegada", "Tiempo de Finalización", "Tiempo de Retorno", "Tiempo de Respuesta", "Tiempo de Espera", "Tiempo de Servicio"};
		modelo = new DefaultTableModel(null, columnas);
		
		tablaTerminados = new JTable(modelo);
		
		add(new JScrollPane(tablaTerminados), BorderLayout.CENTER);
	}
	
	public void AgregarATablaTerminados(Proceso p) {
		
		switch (p.getOperacion()) {
		case 0:
			operacion = '+';	//	<-- Suma si es 0
			break;
		case 1:
			operacion = '-';	//	<-- Resta si es 1
			break;
		case 2:
			operacion = '*';	//	<-- Multiplicación si es 2
			break;
		case 3:
			operacion = '/';	//	<-- División si es 3
			break;
		case 4:
			operacion = '^';	//	<-- Potencia si es 4
			break;
		case 5:
			operacion = '%';	//	<-- Módulo si es 5
			break;
		}
		
		if (!p.getError()) {
			
			modelo.addRow(new Object[] {p.getId(), 
					p.getX() + " " + operacion + " " + p.getY(), 
					p.getResultado(),
					p.getTiempoLlegada(),
					p.getTiempoFinalizacion(),
					p.getTiempoFinalizacion() - p.getTiempoLlegada(),
					p.getTiempoRespuesta(),
					p.getTiempoFinalizacion() - p.getTiempoLlegada() - p.getTiempoTranscurrido(),
					p.getTiempoMax()});
		} else {
			modelo.addRow(new Object[] {p.getId(), 
					p.getX() + " " + operacion + " " + p.getY(), 
					"ERROR",
					p.getTiempoLlegada(),
					p.getTiempoFinalizacion(),
					p.getTiempoFinalizacion() - p.getTiempoLlegada(),
					p.getTiempoRespuesta(),
					p.getTiempoFinalizacion() - p.getTiempoLlegada() - p.getTiempoTranscurrido(),
					p.getTiempoTranscurrido()});
		}
	}
}
