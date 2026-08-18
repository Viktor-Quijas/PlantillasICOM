package lote;

import javax.swing.JOptionPane;

public class Menu {
	
	public static void main(String[] args) {
		
		JOptionPane.showMessageDialog(null, "Simulador de Procesos en Java", "Bienvenidos", JOptionPane.INFORMATION_MESSAGE);
		
		Interfaz ventana = new Interfaz();
		ventana.IniciarPrograma();
	}
}
