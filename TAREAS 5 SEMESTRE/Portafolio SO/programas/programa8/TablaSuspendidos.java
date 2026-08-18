package lote;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class TablaSuspendidos extends JFrame{

	private static final long serialVersionUID = 2266654252359696316L;
	
	private JLabel[] lblEncabezado;
	private JLabel[] lblIdSuspendido = new JLabel[Tabla.LONGITUD_MEMORIA_FISICA];
	private JLabel[] lblLongSuspendido = new JLabel[Tabla.LONGITUD_MEMORIA_FISICA];
	
	private JPanel panelSuspendidos;
	
	private Tabla tabla;
	private Timer reloj;
	
	
	public TablaSuspendidos(Tabla tabla) {
		int[] area = Tabla.areaUtil();
		
		setTitle("Procesos Suspendidos");
		setSize(area[tabla.ANCHO] / 2, area[tabla.ALTO]);
		setLocation(Tabla.MARGEN_X + area[tabla.ANCHO] / 2, Tabla.MARGEN_Y + area[tabla.ALTO]);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		this.tabla = tabla;
		inicializarPaneles();
		
		reloj = new Timer(100, e -> {
			refrescarTabla();
		});
		
		reloj.start();
	}
	
	private void inicializarPaneles() {
	    // panelPrincipal con BorderLayout para encabezado arriba y datos abajo
	    JPanel panelPrincipal = new JPanel(new BorderLayout(0, 4));
	    panelPrincipal.setBorder(BorderFactory.createTitledBorder(getTitle()));

	    // Panel encabezado — GridLayout para que las columnas alineen con panelSuspendidos
	    JPanel panelEncabezado = new JPanel(new GridLayout(1, 2));
	    panelEncabezado.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));

	    lblEncabezado = new JLabel[2];
	    lblEncabezado[0] = new JLabel("ID");
	    lblEncabezado[1] = new JLabel("Tamaño");
	    lblEncabezado[0].setHorizontalAlignment(SwingConstants.CENTER);
	    lblEncabezado[1].setHorizontalAlignment(SwingConstants.CENTER);

	    panelEncabezado.add(lblEncabezado[0]);  // GridLayout no usa constraints
	    panelEncabezado.add(lblEncabezado[1]);  // BorderLayout.EAST/WEST no funciona en GridLayout

	    // Panel de datos
	    panelSuspendidos = new JPanel(new GridLayout(0, 2));
	    inicializarLabelesSuspendidos();

	    // ScrollPane para cuando hay muchos suspendidos
	    JScrollPane scroll = new JScrollPane(panelSuspendidos);
	    scroll.setBorder(null);

	    panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
	    panelPrincipal.add(scroll, BorderLayout.CENTER);  // CENTER estira el scroll

	    add(panelPrincipal, BorderLayout.CENTER);  // el JFrame también necesita BorderLayout

	    ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
	
	private void inicializarLabelesSuspendidos() {
		for(int cantSuspendidos = 0; cantSuspendidos < Tabla.LONGITUD_MEMORIA_FISICA; cantSuspendidos++) {
			lblIdSuspendido[cantSuspendidos] = new JLabel("-");
			lblLongSuspendido[cantSuspendidos] = new JLabel("-");
			
			lblIdSuspendido[cantSuspendidos].setHorizontalAlignment(SwingConstants.CENTER);
			lblLongSuspendido[cantSuspendidos].setHorizontalAlignment(SwingConstants.CENTER);
		}
	}
	
	private void refrescarTabla() {
		panelSuspendidos.removeAll();
		
		List<Proceso> procesosSuspendidos = new ArrayList<>(tabla.getRegistroListas().get(TablaBCP.A_REGRESAR));
		procesosSuspendidos.addAll(tabla.getRegistroListas().get(TablaBCP.SUSPENDIDOS));
		
		
		for (int i = 0; i < Tabla.LONGITUD_MEMORIA_FISICA; i++) {
			
			if (i < procesosSuspendidos.size()) {
				lblIdSuspendido[i].setText(String.valueOf(procesosSuspendidos.get(i).id));
				lblLongSuspendido[i].setText(String.valueOf(procesosSuspendidos.get(i).getLongitud()));
				
				panelSuspendidos.add(lblIdSuspendido[i]);
				panelSuspendidos.add(lblLongSuspendido[i]);
				
				if (tabla.getRegistroListas().get(TablaBCP.A_REGRESAR).contains(procesosSuspendidos.get(i))) {
					
					lblIdSuspendido[i].setForeground(new Color(204, 188, 41));
					lblLongSuspendido[i].setForeground(new Color(204, 188, 41));
					
				} else {
					
					lblIdSuspendido[i].setForeground(Color.BLACK);
					lblLongSuspendido[i].setForeground(Color.BLACK);
					
				}
				
			} else {
				
				lblIdSuspendido[i].setText("-");
				lblLongSuspendido[i].setText("-");
				lblIdSuspendido[i].setForeground(Color.BLACK);
				lblLongSuspendido[i].setForeground(Color.BLACK);
				
			}
		}
		
		panelSuspendidos.revalidate();  // ← recalcula el layout
	    panelSuspendidos.repaint();     // ← redibuja
	}
}
