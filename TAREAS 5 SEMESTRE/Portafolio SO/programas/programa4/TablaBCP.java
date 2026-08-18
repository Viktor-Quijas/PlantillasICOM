package lote;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.*;

public class TablaBCP extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JTable tablaBCP;
	private DefaultTableModel modelo;
	
	private Tabla ventanaOriginal; 
	
	private int idnEjecucion;
	
	public TablaBCP(Tabla principal) {
		setTitle("Tabla de bloques de control de procesos");
		setSize(1050,500);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(this);
		
		this.ventanaOriginal = principal;
		
		String[] columnas = {"Estado", "ID", "Ope", "TME", "Res", "TRnCPU", "TFin", "TRet", "TEsp", "TResp", "TServ", "TT en bloqueado"};
		modelo = new DefaultTableModel(null, columnas);
		
		tablaBCP = new JTable(modelo);
		
		add(new JScrollPane(tablaBCP), BorderLayout.CENTER);
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_C == e.getKeyCode()) {
					CerrarBCP();
				}
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				CerrarBCP();
			}
		});
		
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	public void IngresarDatosBCP(List<List<Proceso>> L, int contadorGlobal) {
		/*if (L == null)
			return;
		
		String tiempoRespuesta;
		String resultado;
		
		for (int i = 0; i < L.size(); i++) {
			switch (getEstado(L,i)) {
			case 0:
				modelo.addRow(new Object[] {"Nuevo",
						L.get(i).getId(),
						L.get(i).getX() + L.get(i).getOperacionChar() + L.get(i).getY(),
						L.get(i).getTiempoMax(),
						});
				break;
			case 1:
				modelo.addRow(new Object[] {"Bloqueado",
						L.get(i).getId(),
						L.get(i).getX() + L.get(i).getOperacionChar() + L.get(i).getY(),
						L.get(i).getTiempoMax(),
						" ",
						L.get(i).getTiempoMax() - L.get(i).getTiempoTranscurrido(),
						" ",
						" ",
						contadorGlobal - 1 - L.get(i).getTiempoLlegada() - L.get(i).getTiempoTranscurrido(),
						L.get(i).getTiempoRespuesta(),
						L.get(i).getTiempoTranscurrido(),
						8 - L.get(i).tBloqueado
						});
				break;
			case 2:
				modelo.addRow(new Object[] {"En Ejecucion",
						L.get(i).getId(),
						L.get(i).getX() + L.get(i).getOperacionChar() + L.get(i).getY(),
						L.get(i).getTiempoMax(),
						" ",
						L.get(i).getTiempoMax() - L.get(i).getTiempoTranscurrido(),
						" ",
						" ",
						contadorGlobal - L.get(i).getTiempoLlegada() - L.get(i).getTiempoTranscurrido(),
						L.get(i).getTiempoRespuesta(),
						L.get(i).getTiempoTranscurrido(),
						});
				break;
			case 3:
				tiempoRespuesta = String.valueOf(L.get(i).getTiempoRespuesta());
				if (L.get(i).getTiempoRespuesta() < 0) 
					tiempoRespuesta = "";
				
				modelo.addRow(new Object[] {"En Memoria",
						L.get(i).getId(),
						L.get(i).getX() + L.get(i).getOperacionChar() + L.get(i).getY(),
						L.get(i).getTiempoMax(),
						" ",
						L.get(i).getTiempoMax() - L.get(i).getTiempoTranscurrido(),
						" ",
						" ",
						contadorGlobal - L.get(i).getTiempoLlegada() - L.get(i).getTiempoTranscurrido(),
						tiempoRespuesta,
						L.get(i).getTiempoTranscurrido(),
						});
				break;
			case 4:
				resultado = String.valueOf(L.get(i).getResultado());
				if (L.get(i).getError())
					resultado = "Error";
				
				modelo.addRow(new Object[] {"Terminado",
						L.get(i).getId(),
						L.get(i).getX() + L.get(i).getOperacionChar() + L.get(i).getY(),
						L.get(i).getTiempoMax(),
						resultado,
						"",
						L.get(i).getTiempoFinalizacion(),
						L.get(i).getTiempoFinalizacion() - L.get(i).getTiempoLlegada(),
						L.get(i).getTiempoFinalizacion() - L.get(i).getTiempoLlegada() - L.get(i).getTiempoTranscurrido(),
						L.get(i).getTiempoRespuesta(),
						L.get(i).getTiempoTranscurrido(),
						});
				break;
			}
		}
		*/
	}

	public int getEstado(List<Proceso> L, int posicion) {/*
		if (L == null || L.size() <= posicion)
			return -1;	//	<-- -1 = Error, nulo o OutOfBounds.
		
		if (L.get(posicion).getTiempoLlegada() < 0)
			return 0;	//	<-- 0 = Proceso Nuevo.
		
		if (L.get(posicion).getBloqueado())
			return 1;	//	<-- 1 = Bloqueado.
		
		if (posicion + 1 == idnEjecucion)
			return 2;	//	<-- 2 = Proceso en ejecucion.
		
		if (L.get(posicion).getTiempoFinalizacion() < 0 && !L.get(posicion).getBloqueado())
			return 3;	//	<-- 3 = Proceso en memoria.

		return 4; 		//	<-- 4 = Terminado.
	*/return -1;}
	
	private void ContinuarOriginal() {
		if (ventanaOriginal != null)
			ventanaOriginal.Continuar();
	}
	
	public void CerrarBCP() {
		modelo.setRowCount(0);
		ContinuarOriginal();
		dispose();
	}
	
}
