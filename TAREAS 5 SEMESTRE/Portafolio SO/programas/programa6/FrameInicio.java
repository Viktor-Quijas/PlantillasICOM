package productorConsumidor;

import javax.swing.JOptionPane;

public class FrameInicio {

	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "Simulador de Productor y Consumidor", "Bienvenidos", JOptionPane.INFORMATION_MESSAGE);
		
		FramePrincipal frame= new FramePrincipal();
		frame.iniciarVentana();
	}

}
