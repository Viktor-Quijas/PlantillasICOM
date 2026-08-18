package lote;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TablaTerminados extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JTable tablaTerminados;
	private DefaultTableModel modelo;
	
	public TablaTerminados(Tabla tabla) {
		int[] area = tabla.areaUtil();
		
		setTitle("Procesos Terminados");
		setSize(area[tabla.ANCHO],area[tabla.ALTO]);
		setLocation(tabla.MARGEN_X + area[tabla.ANCHO], tabla.MARGEN_Y + area[tabla.ALTO]);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String[] columnas = {"ID", "Ope", "Res", "TME", "TLlegada", "TFinalización", "TRetorno", "TRespuesa", "TEspera", "TServicio"};
		modelo = new DefaultTableModel(null, columnas);
		
		tablaTerminados = new JTable(modelo);
		
		add(new JScrollPane(tablaTerminados), BorderLayout.CENTER);
	}
	
	public void AgregarATablaTerminados(Proceso p) {
		String resultado;
		int tServicio;
		
		if (p.error) {
			resultado = "Error";
			tServicio = p.tiempoTranscurrido;
		} else {
			resultado = String.valueOf(p.getResultado());
			tServicio = p.tiempoMax;
		}
		
		Object[] fila = new Object[] {
				p.id, 
				p.x + p.getOperacionChar() + p.y,
				resultado,
				p.tiempoMax,
				p.tiempoLlegada,
				p.tiempoFinalizacion,
				p.tiempoRetorno,
				p.tiempoRespuesta,
				p.tiempoRetorno - p.tiempoTranscurrido,
				tServicio,
			};
		
		modelo.addRow(fila);

		}
}
