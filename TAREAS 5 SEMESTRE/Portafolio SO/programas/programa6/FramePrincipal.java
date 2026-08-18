package productorConsumidor;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;

import listaSimple.*;

public class FramePrincipal extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JPanel panelCajas;
	private JPanel panelInformacionProductor;
	private JPanel panelInformacionConsumidor;
	private JPanel panelInformacion;
	private JPanel panelContador;
	
	private JLabel lblContador;
	private JLabel[] lblCajas;
	private JLabel lblEstadoProductor;
	private JLabel lblEstadoConsumidor; 
	private JLabel lblTurnoProductor;
	private JLabel lblTurnoConsumidor;
	private JLabel lblTiempoRestanteProductor;
	private JLabel lblTiempoRestanteConsumidor;
	
	private ImageIcon pacManCerrado = new ImageIcon("img/PacMan_Cerrado.png");
	private ImageIcon pacManAbierto = new ImageIcon("img/PacMan_Abierto.png");
	private ImageIcon pacManFantasma = new ImageIcon("img/PacMan_Fantasma.png");
	private ImageIcon consumible = new ImageIcon("img/consumible.png");
	private ImageIcon checkMark = new ImageIcon("img/checkMark.png");
	private ImageIcon vacio = new ImageIcon();
	private ImageIcon pacManAnimacion = new ImageIcon();
	
	
	private Lista lista;
	private Productor productor;
	private Consumidor consumidor;
	
	private int turno = -1;

	private boolean tproductor;
	private boolean tconsumidor;
	private int dondeResideProductor;
	private int dondeResideConsumidor;
	
	private Timer reloj;
	private int contadorGlobal = 0;
	private Random rand;
	
	public FramePrincipal () {
		setTitle("Productor y Consumidor");
		setSize(1900, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10,10));
		/*
		 * Que disque para darle una fuente chida dice Gimini
		 * */
		
		try {
		    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            javax.swing.UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    System.out.println("No se pudo cargar el tema Nimbus.");
		}
		
		
		InicializarVariables();
		InicializarPaneles();
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_P:
					reloj.stop();
					break;
				case KeyEvent.VK_C:
					reloj.start();
					break;
				case KeyEvent.VK_ESCAPE:
					TerminarPrograma();
					break;
				}
			}
		});
		
		setFocusable(true);
		requestFocusInWindow();
		reloj.start();
	}
	
	private void InicializarVariables() {
		lista = new Lista();
		productor = new Productor(lista);
		consumidor = new Consumidor(lista);
		rand = new Random();
		reloj = new Timer(1000, e -> {
			Ejecucion();
		});
		
	}
	
	private void InicializarPaneles() {
		Font fuenteTitulo = new Font("SansSerif", Font.BOLD, 14);
		
		panelCajas = new JPanel(new GridLayout(1,lista.getLongitud(), 5, 0));
		panelContador = new JPanel(new GridLayout(1, 2));
		panelInformacionProductor = new JPanel(new GridLayout(3, 2));
		panelInformacionConsumidor = new JPanel(new GridLayout(3, 2));
		panelInformacion = new JPanel(new GridLayout(1, 2));
		
		lblContador = new JLabel("0");
		lblCajas = new JLabel[lista.getLongitud()];
		lblEstadoProductor = new JLabel("Despierto");
		lblEstadoConsumidor = new JLabel("Despierto");
		lblTiempoRestanteProductor = new JLabel("0");
		lblTiempoRestanteConsumidor = new JLabel("0");
		lblTurnoProductor = new JLabel("En disputa");
		lblTurnoConsumidor = new JLabel("En disputa");
		
		panelContador.add(new JLabel ("Tiempo Transcurrido: "));
		panelContador.add(lblContador);
		
		for (int i = 0; i < lista.getLongitud(); i++) {
			lblCajas[i] = new JLabel(new ImageIcon());
			panelCajas.add(lblCajas[i]);
		}
		
		panelInformacionProductor.add(new JLabel ("Estado: "));		panelInformacionProductor.add(lblEstadoProductor);
		panelInformacionProductor.add(new JLabel ("Tiempo restante: "));		panelInformacionProductor.add(lblTiempoRestanteProductor);
		panelInformacionProductor.add(new JLabel ("Turno: "));		panelInformacionProductor.add(lblTurnoProductor);
		
		panelInformacionConsumidor.add(new JLabel ("Estado: "));		panelInformacionConsumidor.add(lblEstadoConsumidor);
		panelInformacionConsumidor.add(new JLabel ("Tiempo restante: "));		panelInformacionConsumidor.add(lblTiempoRestanteConsumidor);
		panelInformacionConsumidor.add(new JLabel ("Turno: "));		panelInformacionConsumidor.add(lblTurnoConsumidor);
		
		panelInformacion.add(panelInformacionProductor);	panelInformacion.add(panelInformacionConsumidor);
		
		/*
		 * Todo esto me lo agrego papá Gemini
		 * */
		panelInformacionProductor.setBorder(BorderFactory.createTitledBorder(
			    BorderFactory.createLineBorder(Color.GRAY), "Información del Productor", 
			    TitledBorder.LEFT, TitledBorder.TOP, fuenteTitulo));
		
		panelInformacionConsumidor.setBorder(BorderFactory.createTitledBorder(
			    BorderFactory.createLineBorder(Color.GRAY), "Información del Consumidor", 
			    TitledBorder.LEFT, TitledBorder.TOP, fuenteTitulo));
		
		panelCajas.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		panelCajas.setBackground(Color.BLACK);
		
		panelInformacion.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		panelContador.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
		
		Font fuenteDatos = new Font("SansSerif", Font.BOLD, 18);
		lblContador.setFont(fuenteDatos);
		lblContador.setForeground(new Color(0, 102, 204)); // Un color azul vibrante
		
		add(panelContador, BorderLayout.NORTH);
		add(panelCajas, BorderLayout.CENTER);
		add(panelInformacion, BorderLayout.SOUTH);
	}
	
	public void iniciarVentana() {
		this.setVisible(true);
	}
	
	private void Ejecucion() {
		contadorGlobal++;
		
		if (turno < 0) 
			DeterminarBolado();
		
		tproductor = productor.tick();
		tconsumidor = consumidor.tick();
		
		if (tproductor || tconsumidor) {
			turno = -1;
			LimpiarEntidad();
		}
		
		ActualizarVariablesGraficas();
	}
	
	
	private void DeterminarBolado() {
		int resultadoBolado = rand.nextInt(2);
		switch (resultadoBolado) {
		case Entidad.PRODUCTOR:
			if(productor.ingresar())
				turno = Entidad.PRODUCTOR;
			else if (consumidor.ingresar()) 
				turno = Entidad.CONSUMIDOR;
			break;
		case Entidad.CONSUMIDOR:
			if(consumidor.ingresar())
				turno = Entidad.CONSUMIDOR;
			else if (productor.ingresar()) 
				turno = Entidad.PRODUCTOR;
			break;
		}
	}
	
	private void ActualizarVariablesInformacion() {
		String strEstadoProductor = "";
		String strEstadoConsumidor = ""; 
		
		switch(productor.getEstado()) {
		case Entidad.DESPIERTO:
			lblTiempoRestanteProductor.setText(String.valueOf(""));
			strEstadoProductor = "Despierto";
			break;
		case Entidad.DORMIDO:
			lblTiempoRestanteProductor.setText(String.valueOf(productor.tiempoDescanso));
			strEstadoProductor = "Dormido";
			break;
		case Entidad.TRABAJANDO:
			lblTiempoRestanteProductor.setText(String.valueOf(productor.cantidadAccion));
			strEstadoProductor = "Trabajando";
			break;
		case Entidad.INGRESANDO:
			lblTiempoRestanteProductor.setText(String.valueOf(""));
			strEstadoProductor = "Ingresando";
			break;
		}
		
		switch(consumidor.getEstado()) {
		case Entidad.DESPIERTO:
			lblTiempoRestanteConsumidor.setText(String.valueOf(""));
			strEstadoConsumidor = "Despierto";
			break;
		case Entidad.DORMIDO:
			lblTiempoRestanteConsumidor.setText(String.valueOf(consumidor.tiempoDescanso));
			strEstadoConsumidor = "Dormido";
			break;
		case Entidad.TRABAJANDO:
			lblTiempoRestanteConsumidor.setText(String.valueOf(consumidor.cantidadAccion));
			strEstadoConsumidor = "Trabajando";
			break;
		case Entidad.INGRESANDO:
			lblTiempoRestanteConsumidor.setText(String.valueOf(""));
			strEstadoConsumidor = "Ingresando";
			break;
		}
		
		lblEstadoProductor.setText(strEstadoProductor);
		lblEstadoConsumidor.setText(strEstadoConsumidor);
		
		if (turno == Entidad.PRODUCTOR) {
			lblTurnoProductor.setIcon(checkMark);
			lblTurnoConsumidor.setIcon(vacio);
			lblTurnoProductor.setText("");
			lblTurnoConsumidor.setText("");
		} else if (turno == Entidad.CONSUMIDOR) {
			lblTurnoProductor.setIcon(vacio);
			lblTurnoConsumidor.setIcon(checkMark);
			lblTurnoProductor.setText("");
			lblTurnoConsumidor.setText("");
		} else {
			lblTurnoProductor.setIcon(vacio);
			lblTurnoConsumidor.setIcon(vacio);
			lblTurnoProductor.setText("En disputa");
			lblTurnoConsumidor.setText("En disputa");
			lblTiempoRestanteProductor.setText(String.valueOf(""));
			lblTiempoRestanteConsumidor.setText(String.valueOf(""));
		}
		
		lblContador.setText(String.valueOf(contadorGlobal));
	}
	
	private void ActualizarVariablesGraficas() {
		ActualizarVariablesInformacion();
		AnimacionPacMan();
		
		int i = 0;
		Nodo actual = lista.getCabeza();
		while (i < lista.getLongitud()) {
			if (actual.entidad == Entidad.PRODUCTOR)
				lblCajas[i].setIcon(pacManFantasma);
			else if (actual.entidad == Entidad.CONSUMIDOR) 
				lblCajas[i].setIcon(pacManAnimacion);
			else if (actual.consumible == true) 
				lblCajas[i].setIcon(consumible);
			else 
				lblCajas[i].setIcon(vacio);
			actual = actual.sig;
			i++;
		}
	}
	
	private void LimpiarEntidad() {
		if (productor.anterior != null) {
			dondeResideProductor = productor.actual.index;
			lblCajas[dondeResideProductor].setIcon(consumible);
		}
		
		if (consumidor.anterior != null) {
			dondeResideConsumidor = consumidor.actual.index;
			lblCajas[dondeResideConsumidor].setIcon(vacio);
		}
		lista.limpiarEntidad();
	}
	
	private void TerminarPrograma() {
		this.setFocusable(false);
		reloj.stop();
		JOptionPane.showMessageDialog(null, "Simulación Finalizada");
		return;
	}
	
	private void AnimacionPacMan() {
		if (contadorGlobal % 2 == 0)
			pacManAnimacion = pacManAbierto;
		else
			pacManAnimacion = pacManCerrado;
	}
}
