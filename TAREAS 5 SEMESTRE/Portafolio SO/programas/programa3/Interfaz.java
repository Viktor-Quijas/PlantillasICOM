/*
 * 			CAMBIOS VERSION 2.0
 * 
 */

package lote;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class Interfaz extends JFrame {

	private static final long serialVersionUID = 1L;
	
	//Variable para obtener los lotes del formulario.
	private List<Proceso> ListaProcesos = new ArrayList<>();
	private Formato formulario;
	
	public Interfaz() {}
	
	public void IniciarPrograma() {
		//	-- Llama a la ventena del formulario --
		formulario = new Formato(this, true);
		formulario.setVisible(true);
		
		// -- Si el proceso terminó y guardó todo entonces: 
		if (formulario.GetFinalizado() && formulario.GetLista().size() > 0) {
			
			// 	-- Se obtiene la lista de lotes --
			ListaProcesos = formulario.GetLista();
			
			//	--	Llama a la ventana de la tabla --
			Tabla tabla = new Tabla(ListaProcesos); 
			SwingUtilities.invokeLater(() -> {
				tabla.setVisible(true);
        	});
			
			//	-- Imprime en el CMD los datos de la tabla (para depuración) --
			Proceso indexProceso;
			
			for (int i = 0; i < ListaProcesos.size(); i++) {
				indexProceso = ListaProcesos.get(i);
				System.out.println("Proceso num: " + (i + 1) + "\n");
				System.out.println(indexProceso.getNombre());
				System.out.println(indexProceso.getOperacion());
				System.out.println(indexProceso.getId());
				System.out.println("TM: " + indexProceso.getTiempoMax());
				System.out.println(indexProceso.getX());
				System.out.println(indexProceso.getY());
			}
			
		//	Si no: truena --
		} else {
			JOptionPane.showMessageDialog(this, "Algo salió mal, los procesos no se guardaron con éxito","Error Fatal", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		
	}

}
