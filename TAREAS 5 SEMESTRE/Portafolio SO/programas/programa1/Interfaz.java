package lote;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class Interfaz extends JFrame {

	private static final long serialVersionUID = 1L;
	
	//Variable para obtener los lotes del formulario.
	private List<Lote> lotes = new ArrayList<>();
	private Formato formulario;
	
	public Interfaz() {}
	
	public void IniciarPrograma() {
		//	-- Llama a la ventena del formulario --
		formulario = new Formato(this, true);
		formulario.setVisible(true);
		
		// -- Si el proceso terminó y guardó todo entonces: 
		if (formulario.GetFinalizado() && formulario.GetLista().size() > 0) {
			
			// 	-- Se obtiene la lista de lotes --
			lotes = formulario.GetLista();
			
			//	--	Llama a la ventana de la tabla --
			Tabla tabla = new Tabla(lotes); 
			SwingUtilities.invokeLater(() -> {
				tabla.setVisible(true);
        	});
			
			//	-- Imprime en el CMD los datos de la tabla (para depuración) --
			Lote indexLote;
			Proceso indexProceso;
			
			for (int i = 0; i < lotes.size(); i++) {
				indexLote = lotes.get(i);
				for (int j = 0; j < indexLote.size(); j++) {
					indexProceso = indexLote.GetProceso(j);
					System.out.println("Lote num: " + (i + 1) + "\n");
					System.out.println(indexProceso.getNombre());
					System.out.println(indexProceso.getOperacion());
					System.out.println(indexProceso.getId());
					System.out.println(indexProceso.getTiempoMax());
					System.out.println(indexProceso.getX());
					System.out.println(indexProceso.getY());
				}
				
			}
			
		//	Si no: truena --
		} else {
			JOptionPane.showMessageDialog(this, "Algo salió mal, los procesos no se guardaron con éxito","Error Fatal", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		
	}

}
