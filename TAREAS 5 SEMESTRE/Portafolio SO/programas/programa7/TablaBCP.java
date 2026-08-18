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
	
	private static final int PROCESADOR = 0;
	private static final int MEMORIA = 1;
	private static final int BLOQUEADOS = 2;
	private static final int TERMINADOS = 3;
	private static final int PROCESOS = 4;
	
	
	private JTable tablaBCP;
	private DefaultTableModel modelo;
	
	private Tabla ventanaOriginal; 
	
	private String estado;
	private String resultado;
	private String TRnCPU;
	private String TLlegada;
	private String TFinalizacion;
	private String TRetorno;
	private String TEspera;
	private String TRespuesta;
	private String TServicio;
	private String TTenBloqueado;
	
	private Object[] fila;
	
	
	public TablaBCP(Tabla principal) {
		setTitle("Tabla de bloques de control de procesos");
		setSize(1050,500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(this);
		
		this.ventanaOriginal = principal;
		
		String[] columnas = {"Estado", "ID", "Ope", "TME", "Tamaño", "Res", "TRnCPU", "TLlegada", "TFinalizacion", "TRetorno", "TRespuesta", "TEspera", "TServ", "TT en bloqueado"};
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
		if (L == null)
			return;
		
		for (int i = 0; i < L.size(); i++) {
			for (int j = 0; j < L.get(i).size(); j++) {
				
				resultado = "---";
				TRnCPU = String.valueOf(L.get(i).get(j).tiempoMax - L.get(i).get(j).tiempoTranscurrido);
				TLlegada = String.valueOf(L.get(i).get(j).tiempoLlegada);
				TEspera = String.valueOf(contadorGlobal - L.get(i).get(j).tiempoTranscurrido - L.get(i).get(j).tiempoLlegada);
				TRespuesta = String.valueOf(L.get(i).get(j).tiempoRespuesta);
				TServicio = String.valueOf(L.get(i).get(j).tiempoTranscurrido);
				TFinalizacion = "---";
				TRetorno = "---";
				TTenBloqueado = "---";
				
				switch (i) {
				case PROCESADOR:
					estado = "En Procesador";
					break;
					
				case MEMORIA:
					estado = "En Memoria";
					break;
					
				case BLOQUEADOS:
					estado = "Bloqueado";
					TTenBloqueado = String.valueOf(7 - L.get(i).get(j).tiempoBloqueado);
					break;
					
				case TERMINADOS:
					estado = "Terminados";
					if (!L.get(i).get(j).error)
						resultado = String.valueOf(L.get(i).get(j).getResultado());
					else
						resultado = "Error";
					TRnCPU = "---";
					TEspera = String.valueOf(L.get(i).get(j).tiempoRetorno - L.get(i).get(j).tiempoTranscurrido);
					TFinalizacion = String.valueOf(L.get(i).get(j).tiempoFinalizacion);
					TRetorno = String.valueOf(L.get(i).get(j).tiempoRetorno);
					break;
					
				case PROCESOS:
					estado = "Nuevo";
					TRnCPU = "---";
					TLlegada = "---";
					TEspera = "---";
					TRespuesta = "---";
					TServicio = "---";
					break;
				}
				
				fila = new Object[] {
					    estado,                                    // Estado
					    L.get(i).get(j).id,                        // ID
					    L.get(i).get(j).x + L.get(i).get(j).getOperacionChar() + L.get(i).get(j).y, // Ope
					    L.get(i).get(j).tiempoMax,                 // TME
					    L.get(i).get(j).getLongitud(),			   // Tamaño
					    resultado,                                 // Res
					    TRnCPU,                                    // TRnCPU
					    TLlegada,                                  // TLlegada
					    TFinalizacion,  // ✅ antes estaba TRespuesta aquí
					    TRetorno,       // ✅ antes estaba TEspera aquí
					    TRespuesta,     // ✅ antes estaba TServicio aquí
					    TEspera,        // ✅ antes estaba TFinalizacion aquí
					    TServicio,      // ✅ antes estaba TRetorno aquí
					    TTenBloqueado                              // TT en bloqueado
					};
				
				modelo.addRow(fila);
				
			}
		}
	}
	
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