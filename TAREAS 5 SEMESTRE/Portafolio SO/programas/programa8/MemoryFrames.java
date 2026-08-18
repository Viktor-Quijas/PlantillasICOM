package lote;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class MemoryFrames extends JFrame{
	private static final long serialVersionUID = 4805413963207134088L;
	
	private static final int PROCESADOR = 0;
	private static final int MEMORIA = 1;
	private static final int BLOQUEADOS = 2;
	
	private static final int LONGITUD_ENCABEZADO = 3;
	
	//private static final int LONGITUD_MEMORIA_FISICA = 48;
	
	public static final int PRINCIPAL = 0;
	public static final int FUNCION = 1;
	
	Tabla tabla;
	private JLabel[][] lblMemoriaFisica = new JLabel[8][6];
	private JPanel panelMemoriaFisica = new JPanel();
	
	//private JLabel[] lblMarcos = new JLabel[LONGITUD_MEMORIA_FISICA];
	private JPanel panelMarcos = new JPanel();
	
	private List<List<Proceso>> registroListas;
	
	Timer reloj;
	
	public MemoryFrames(Tabla tabla, int definicion) {
		this.tabla = tabla;
		
		switch (definicion) {
		case PRINCIPAL:
			tablaPrincipal();
			add(panelMemoriaFisica);
			reloj.start();
			break;
		case FUNCION:
			add(tablaDePaginas());
			break;
		default:
			System.out.println("Acceso Inválido a MemoryFrame");
			break;
		}
	}
	
	private void tablaPrincipal() {
		int[] area = Tabla.areaUtil();
		
		setTitle("Memoria Física");
		setSize(area[tabla.ANCHO],area[tabla.ALTO]);
		setLocation(Tabla.MARGEN_X + area[tabla.ANCHO], Tabla.MARGEN_Y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		registroListas = tabla.getRegistroListas();
		inicializarPanelesPrincipal();
		
		reloj = new Timer(100, e -> {
			actualizarPanelPrincipal();
		});
		
	}
	
	private void inicializarPanelesPrincipal() {
		panelMemoriaFisica.setBorder(BorderFactory.createTitledBorder("Memoria Física"));
		panelMemoriaFisica.setLayout(new GridLayout(6, 8, 5, 5));
		((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		inicializarlblMemoriaFisica();
		actualizarPanelPrincipal();
	}
	
	private void inicializarlblMemoriaFisica() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 6; j++) {
				lblMemoriaFisica[i][j] = new JLabel("--");
			}
		}
			
	}
	
	private void actualizarPanelPrincipal() {
		Proceso[] memoriaFisica = tabla.getMemoriaFisica();
		int contMemoria = 0;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 6; j++) {
				if (memoriaFisica[contMemoria] != null) {
					lblMemoriaFisica[i][j].setText(contMemoria + " [ " + memoriaFisica[contMemoria].getPaginaInfo()[Proceso.LONGITUD_PAG] + "/5 ]");
					
					if (buscarProcesoEnMemoriaLogica(MEMORIA, memoriaFisica[contMemoria])) {
						lblMemoriaFisica[i][j].setForeground(new Color(72, 64, 207));
					} else if (buscarProcesoEnMemoriaLogica(BLOQUEADOS, memoriaFisica[contMemoria])) {
						lblMemoriaFisica[i][j].setForeground(new Color(218, 10, 255));
					} else if (buscarProcesoEnMemoriaLogica(PROCESADOR, memoriaFisica[contMemoria])) {
						lblMemoriaFisica[i][j].setForeground(new Color(204, 37, 62));
					} else {
						lblMemoriaFisica[i][j].setText(contMemoria + " SO");
						lblMemoriaFisica[i][j].setForeground(Color.LIGHT_GRAY);
					}
				} else {
					lblMemoriaFisica[i][j].setText(contMemoria + " [ 0/5 ]");
					lblMemoriaFisica[i][j].setForeground(Color.BLACK);
				}
				
				lblMemoriaFisica[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				panelMemoriaFisica.add(lblMemoriaFisica[i][j]);
				contMemoria++;
			}
		}
		
	}
	
	private boolean buscarProcesoEnMemoriaLogica(int indexLista, Proceso p) {
		if (p == null)
			return false;
		
		int id = p.id;
		
		for (int i = 0; i < registroListas.get(indexLista).size(); i++) {
			if (id == registroListas.get(indexLista).get(i).id)
				return true;
		}
		return false;
	}

	
	private JScrollPane tablaDePaginas() {
		setTitle("Tabla de Paginas");
		setSize(750,700);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(this);
		
		inicializarPanelTablapaginas();
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_C == e.getKeyCode()) {
					dispose(); 
					tabla.Continuar();
				}
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose(); 
				tabla.Continuar();
			}
		});
		
		JScrollPane panelFinal = new JScrollPane(panelMarcos);
		panelFinal.setBorder(BorderFactory.createTitledBorder("Marcos de Memoria"));
		((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return panelFinal;
	}
	
	private void inicializarPanelTablapaginas() {
		panelMarcos.setLayout(new GridLayout(0, 1, 5, 5));
		
		JPanel panelPaginasProceso;
		JLabel lblPag;
		JLabel lblMarco;
		JLabel lblLong;
		
		List<Integer> procesosFormados = new ArrayList<>();
		Proceso[] memoriaFisica = tabla.getMemoriaFisica();
		List<int[]> paginasYMarcos = empatarMarcos(memoriaFisica);
		
		Font fuenteResaltado = new Font("SansSerif", Font.BOLD, 16);
		
		for (int i = 0; i < paginasYMarcos.size(); i++) {
			if (procesosFormados.contains(paginasYMarcos.get(i)[Proceso.ID]))
				continue;
			
			panelPaginasProceso = new JPanel(new GridLayout(0, LONGITUD_ENCABEZADO));
			panelPaginasProceso.setBorder(BorderFactory.createTitledBorder("ID: " + paginasYMarcos.get(i)[Proceso.ID]));
			
			JLabel pag = new JLabel("Pag");
			JLabel marco = new JLabel("Marco");
			JLabel longitud = new JLabel("Tamaño");
			
			pag.setHorizontalAlignment(SwingConstants.CENTER);
			marco.setHorizontalAlignment(SwingConstants.CENTER);
			longitud.setHorizontalAlignment(SwingConstants.CENTER);
			
			pag.setFont(fuenteResaltado);
			marco.setFont(fuenteResaltado);
			longitud.setFont(fuenteResaltado);
			
			panelPaginasProceso.add(pag);
			panelPaginasProceso.add(marco);
			panelPaginasProceso.add(longitud);
			
			
			for ( int j = 0; j < paginasYMarcos.size(); j++) {
				if (paginasYMarcos.get(j)[Proceso.ID] == paginasYMarcos.get(i)[Proceso.ID]) {
					lblPag = new JLabel(String.valueOf(paginasYMarcos.get(j)[Proceso.PAG]));
					lblMarco = new JLabel(String.valueOf(paginasYMarcos.get(j)[Proceso.MARCO]));
					lblLong = new JLabel(paginasYMarcos.get(j)[Proceso.LONG] + " / 5");
					
					lblPag.setHorizontalAlignment(SwingConstants.CENTER);
					lblMarco.setHorizontalAlignment(SwingConstants.CENTER);
					lblLong.setHorizontalAlignment(SwingConstants.CENTER);
					
					panelPaginasProceso.add(lblPag);
					panelPaginasProceso.add(lblMarco);
					panelPaginasProceso.add(lblLong);
				}
			}
			
			procesosFormados.add(paginasYMarcos.get(i)[Proceso.ID]);
			panelMarcos.add(panelPaginasProceso);
		}
	}
	
	private List<int[]> empatarMarcos(Proceso[] memoriaFisica) {
	    List<int[]> paginasYMarcos = new ArrayList<>();

	    for (int indexMemoriaFisica = 0; indexMemoriaFisica < Tabla.LONGITUD_MEMORIA_FISICA; indexMemoriaFisica++) {
	        if (memoriaFisica[indexMemoriaFisica] == null || memoriaFisica[indexMemoriaFisica].nombre == "SO")
	            continue;

	        int[] empatarMarco = new int[4];
	        empatarMarco[Proceso.ID]    = memoriaFisica[indexMemoriaFisica].id;
	        empatarMarco[Proceso.PAG]   = memoriaFisica[indexMemoriaFisica].pagina[Proceso.NUMERO_PAG];
	        empatarMarco[Proceso.MARCO] = indexMemoriaFisica;
	        empatarMarco[Proceso.LONG]  = memoriaFisica[indexMemoriaFisica].pagina[Proceso.LONGITUD_PAG];

	        paginasYMarcos.add(empatarMarco);
	    }

	    paginasYMarcos.sort(Comparator
	        .comparingInt((int[] e) -> e[Proceso.ID])
	        .thenComparingInt(e -> e[Proceso.PAG])
	    );

	    return paginasYMarcos;
	}
	

	
	public void PausaMemoryFrames() {
		reloj.stop();
	}
	
	public void ContinuarMemoryFrames() {
		reloj.start();
	}
}
