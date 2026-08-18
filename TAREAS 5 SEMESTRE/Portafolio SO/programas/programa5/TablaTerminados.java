package lote;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TablaTerminados extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JTable tablaTerminados;
	private DefaultTableModel modelo;
	
	public TablaTerminados() {
		setTitle("Procesos Terminados");
		setSize(1050,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(825,300);
		
		String[] columnas = {"ID", "Ope", "Res", "TLlegada", "TFinalización", "TRetorno", "TRespuesa", "TEspera", "TServicio", "TTranscurrido", "TMaximoEst"};
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
				p.tiempoLlegada,
				p.tiempoFinalizacion,
				p.tiempoRetorno,
				p.tiempoRespuesta,
				p.tiempoRetorno - p.tiempoTranscurrido,
				tServicio,
				p.tiempoTranscurrido,
				p.tiempoMax
			};
		
		modelo.addRow(fila);

		}
}
