/*
 * 			CAMBIOS VERSION 2.0
 * - La tabla imprime una columna extra llamada Tiempo Restante
 * - La función encargada de recorrer los procesos en cola también fue actualizada mostrando ese valor.
 * - Se agregaron las bibliotecas para espíar si una tecla fue presionada: "java.awt.event.KeyAdapter" y "java.awt.event.KeyEvent".
 * - La comparación de la tabla que hacía que se cambiaran los datos pasa de comparar el tiempo máx a el tiempo restante.
 * - A esta comparación se agrega verificar si el proceso actual da error para terminarlo inmediatamente.
 * 
 * 
 * NOTA: El reloj del programa se ejecuta contando desde el segundo cero, por eso es que 
 * primero actualiza en la tabla y después se incrementa en la variable. El conteo real pertenece a lo mostrado en la tabla.
 */

package lote;

import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Tabla extends JFrame{

	private static final long serialVersionUID = 1L;
	
	//	-- Variables de la tabla --
	private JTable tabla;
	private DefaultTableModel modelo;
	
	//	-- Variables para acceder a la lista --
	private List<Lote> lotes = new ArrayList<>();
	private int i = 0, j = 0;	//	<-- i apunta a lotes, j apunta a procesos
	private Lote loteIndex;		//	<-- Guarda un lote
	private int operacion;	
	
	//	-- Variables para llevar el conteo --
	Timer reloj;
	private int contadorGlobal = 0;
	private int tiempoTranscurrido = 0;
	private int lotesProcesados = 0;
	
	public Tabla( List<Lote> L) {
		
		//	-- Inicializa los valores en la tabla --
		setTitle("Tabla de procesos");
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		String[] columnas = {"_______________", "_____", "_____", "_____"};
		modelo = new DefaultTableModel(null, columnas);
		
		modelo.addRow(new Object[] {"Lotes pendientes: ", "0"});
		modelo.addRow(new Object[] {"Lote Actual"});
		modelo.addRow(new Object[] {"ID", "TME", "TR"});
		modelo.addRow(new Object[] {"", "", ""});
		modelo.addRow(new Object[] {"", "", ""});
		modelo.addRow(new Object[] {"", "", ""});
		modelo.addRow(new Object[] {"", "", ""});
		modelo.addRow(new Object[] {"Proceso ejecutandose"});
		modelo.addRow(new Object[] {"ID", ""});
		modelo.addRow(new Object[] {"Ope", ""});
		modelo.addRow(new Object[] {"TME", ""});
		modelo.addRow(new Object[] {"TT", ""});
		modelo.addRow(new Object[] {"TR", ""});
		modelo.addRow(new Object[] {"Contador global: ", "0"});
		modelo.addRow(new Object[] {"Terminados"});
		modelo.addRow(new Object[] {"ID", "Ope", "Res", "N. Lote"});
		
		tabla = new JTable(modelo);
		
		add(new JScrollPane(tabla), BorderLayout.CENTER);
		
		this.lotes.addAll(L);
		
		//	-- Inicializa las variables y el reloj --
		InicializarLotes();
		InicializarTabla();
		
		//	-- Objeto contador que controla el flujo de la tabla de procesos --
		reloj = new Timer(1000, e -> {
			this.setFocusable(true);	// <-- Escucha si las teclas fueron usadas. Es necesario que se refresque cada vez por si se desactivo con una interrupción o error
			this.requestFocusInWindow();// <-- Imnediatamente obtiene el foco de las ventanas
			ActualizarTabla();			// <-- Función que refresca la tabla
			modelo.setValueAt(contadorGlobal, 13, 1);	//	<-- Modifica el apartado de contador global
			contadorGlobal++;			// <-- Reloj global
			tiempoTranscurrido++;		// <-- Reloj de cada proceso
		});
		reloj.start();
		
		//	-- Encargado de escuchar si una tecla fue presionada --
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_P:
					if (reloj.isRunning()) {
						reloj.stop();
						System.out.println("Pausa");
						JOptionPane.showMessageDialog(null, "Simulación en pausa");
					}
					break;
				case KeyEvent.VK_C:
					if (!reloj.isRunning()) {
						reloj.start();
						System.out.println("Continuar");
					}
					break;
				case KeyEvent.VK_I:
					if (reloj.isRunning()) {
						System.out.println("Interrupción");
						setTitle("Cambiando de contexto");
						Interrupcion();
						modelo.setValueAt(contadorGlobal, 13, 1);	//	<-- Actualiza de inmediato para simular un segundo que tarda en hacer el cambio de contexto
						contadorGlobal++;							// 	<-- Aumenta el segundo de cambio de contexto
						tiempoTranscurrido = 0;
						
					}
					break;
				case KeyEvent.VK_E:
					if (reloj.isRunning()) {
						System.out.println("Error");
						setTitle("Cambiando de contexto");
						Error();
						modelo.setValueAt(contadorGlobal, 13, 1);	//	<-- Modifica el apartado de contador global
						contadorGlobal++;			// <-- Reloj global
					}
					break;
				}
			}
		});
		
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	private void ActualizarTabla() {
		setTitle("Tabla de procesos");	//	<-- Recarga el nombre de la tabla de procesos, por si fue modificado.
		//	-- Si el tiempo transcurrido del proceso llega a su valor maximo entonces: 
		if (tiempoTranscurrido > loteIndex.GetProceso(j).getTiempoRestante() || loteIndex.GetProceso(j).getError()) {
			
			ProcesosTerminados();		// <-- Añade al final el proceso que se terminó
			tiempoTranscurrido = 0;		// <-- Reinicia el reloj para el sig proceso
			j++;						// <-- Apunta al siguiente proceso
			
			//	-- Si ya no existen procesos en ese lote entonces:
			if (j >= loteIndex.size()) {
				j = 0;							// <-- Inicializa el apuntador a procesos
				i++;							// <-- Apunta al sig lote
				lotesProcesados++;				// <-- Aumenta el contador de procesos
				
				//	-- Si existen más lotes entones: carga el sigueinte lote, si no: termina --
				if (i < lotes.size())
					loteIndex = lotes.get(i);
				else {
					this.setFocusable(false);	//	<-- La tabla deja de recir señal del teclado para evitar errores.
					reloj.stop();
	                JOptionPane.showMessageDialog(null, "Simulación Finalizada");
					return;
				}
			} 
			
			//	-- Bloque encargado de actualizar los datos en la tabla --
			modelo.setValueAt(lotes.size() - lotesProcesados, 0, 1);	//	<-- Actualiza los lotes restantes
			
			//	-- Actualiza la lista de pendientes --
			for (int ind = 3; ind - 2 < loteIndex.size(); ind++) {										// 	ind - 2 <-- Se enfoca en el sig proceso y no en el actual. J es la variable que se encarga de avanzar la lista	
				if (loteIndex.size() > ind - 2 + j) {													//	Si el indice del siguiente proceso es válido, entonces avanza.
					modelo.setValueAt(loteIndex.GetProceso(ind - 2 + j).getId() , ind, 0);				//	<-- Actualiza el id de la cola de procesos
					modelo.setValueAt(loteIndex.GetProceso(ind - 2 + j).getTiempoMax() , ind, 1);		//	<-- Actualiza el Tiempo Máximo Estimado de la cola de procesos
					modelo.setValueAt(loteIndex.GetProceso(ind - 2 + j).getTiempoRestante() , ind, 2);	//	<-- Actualiza el Tiempo Restante de la cola de procesos
				} else {																				//
					modelo.setValueAt( "" , ind, 0);													//	<-- Si no existe deja vacío ese registro.
					modelo.setValueAt( "" , ind, 1);
					modelo.setValueAt( "" , ind, 2);
				}
			}
			
			//	-- Muestra la información del proceso actual --
			modelo.setValueAt(loteIndex.GetProceso(j).getId() , 8, 1);	//	<-- Su id
			operacion = loteIndex.GetProceso(j).getOperacion();			//	<-- Obtenemos la operación para:
			//	Imprimir la operación a ejecutar dependiendo de la variable que tenga operación	--
			switch (operacion) {
			case 0:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " + " + loteIndex.GetProceso(j).getY(), 9, 1);	//	<-- Suma si es 0
				break;
			case 1:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " - " + loteIndex.GetProceso(j).getY(), 9, 1);	//	<-- Resta si es 1
				break;
			case 2:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " * " + loteIndex.GetProceso(j).getY(), 9, 1);	//	<-- Multiplicación si es 2
				break;
			case 3:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " / " + loteIndex.GetProceso(j).getY(), 9, 1);	//	<-- División si es 3
				break;
			case 4:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " ^ " + loteIndex.GetProceso(j).getY(), 9, 1);	//	<-- Potencia si es 4
				break;
			case 5:
				modelo.setValueAt(loteIndex.GetProceso(j).getX() + " % " + loteIndex.GetProceso(j).getY(), 9, 1);	//	<-- Módulo si es 5
			}
			//	-- Actualiza la información del tiempo que lleva este proceso --
			modelo.setValueAt(loteIndex.GetProceso(j).getTiempoMax() , 10, 1);		//	<-- Muestra el Tiempo Max Estimado del proceso
			modelo.setValueAt(tiempoTranscurrido , 11, 1);							//	<-- Muestra el Tiempo Transcurrido del proceso
			modelo.setValueAt(loteIndex.GetProceso(j).getTiempoRestante() , 12, 1);	//	<-- Muestra el Tiempo Restante del proceso
			
		} else {
			//	-- Sino, refresca la tabla. Porque si hubo una interrupción necesito que muestre los datos del nuevo proceso
			RefrescarTabla();
			
		}
	}
	
	// 	-- Igual que Actualizar tabla pero sin las verificaciones de esta (debido a que se llama a la función de manera segura) --
	private void RefrescarTabla() {
		modelo.setValueAt(lotes.size() - lotesProcesados, 0, 1);
		
		//	-- Actualiza la lista de pendientes --
		for (int ind = 3; ind - 2 < loteIndex.size(); ind++) {												
			if (loteIndex.size() > ind - 2 + j) {												
				modelo.setValueAt(loteIndex.GetProceso(ind - 2 + j).getId() , ind, 0);			
				modelo.setValueAt(loteIndex.GetProceso(ind - 2 + j).getTiempoMax() , ind, 1);	
				modelo.setValueAt(loteIndex.GetProceso(ind - 2 + j).getTiempoRestante() , ind, 2);
			} else {
				modelo.setValueAt( "" , ind, 0);
				modelo.setValueAt( "" , ind, 1);
				modelo.setValueAt( "" , ind, 2);
			}
		}
		
		modelo.setValueAt(loteIndex.GetProceso(j).getId() , 8, 1);
		operacion = loteIndex.GetProceso(j).getOperacion();
		switch (operacion) {
		case 0:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " + " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 1:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " - " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 2:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " * " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 3:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " / " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 4:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " ^ " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 5:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " % " + loteIndex.GetProceso(j).getY(), 9, 1);
		}
		modelo.setValueAt(loteIndex.GetProceso(j).getTiempoMax() , 10, 1);
		modelo.setValueAt(tiempoTranscurrido , 11, 1);
		modelo.setValueAt(loteIndex.GetProceso(j).getTiempoRestante(), 12, 1);
	}
	
	// -- Inicializa el índice de los lotes --
	private void InicializarLotes() {
		loteIndex = lotes.get(i);	//	<-- LoteIndex guarda la lista de procesos que se encuentra en ese lote de la lista de lotes.
	}
	
	//	-- Inicializa la tabla, funciona igual que Actualizar la tabla. Debería de ir al incio pero meh --
	private void InicializarTabla() {
		
		lotesProcesados++;
		
		modelo.setValueAt(lotes.size() - lotesProcesados, 0, 1);
		
		for (int ind = 3; ind - 2 < loteIndex.size(); ind++) {
			modelo.setValueAt(loteIndex.GetProceso(ind - 2).getId() , ind, 0);
			modelo.setValueAt(loteIndex.GetProceso(ind - 2).getTiempoMax() , ind, 1);
			modelo.setValueAt(loteIndex.GetProceso(ind - 2).getTiempoRestante() , ind, 2);
		}
		
		modelo.setValueAt(loteIndex.GetProceso(0).getId() , 8, 1);
		operacion = loteIndex.GetProceso(j).getOperacion();
		switch (operacion) {
		case 0:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " + " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 1:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " - " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 2:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " * " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 3:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " / " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 4:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " ^ " + loteIndex.GetProceso(j).getY(), 9, 1);
			break;
		case 5:
			modelo.setValueAt(loteIndex.GetProceso(j).getX() + " % " + loteIndex.GetProceso(j).getY(), 9, 1);
		}
		modelo.setValueAt(loteIndex.GetProceso(0).getTiempoMax() , 10, 1);
		modelo.setValueAt( "00" , 11, 1);
		modelo.setValueAt(loteIndex.GetProceso(0).getTiempoRestante() , 12, 1);
	}
	
	//	-- Coloca el proceso terminado al final de la tabla -- 
	private void ProcesosTerminados() {
		if (!loteIndex.GetProceso(j).getError()) {
			operacion = loteIndex.GetProceso(j).getOperacion();
			switch (operacion) {
			case 0:
				modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
						loteIndex.GetProceso(j).getX() + " + " + loteIndex.GetProceso(j).getY(), 
						loteIndex.GetProceso(j).getResultado(),
						lotesProcesados});
				break;
			case 1:
				modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
						loteIndex.GetProceso(j).getX() + " - " + loteIndex.GetProceso(j).getY(), 
						loteIndex.GetProceso(j).getResultado(),
						lotesProcesados});			break;
			case 2:
				modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
						loteIndex.GetProceso(j).getX() + " * " + loteIndex.GetProceso(j).getY(), 
						loteIndex.GetProceso(j).getResultado(),
						lotesProcesados});			break;
			case 3:
				modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
						loteIndex.GetProceso(j).getX() + " / " + loteIndex.GetProceso(j).getY(), 
						loteIndex.GetProceso(j).getResultado(),
						lotesProcesados});			break;
			case 4:
				modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
						loteIndex.GetProceso(j).getX() + " ^ " + loteIndex.GetProceso(j).getY(), 
						loteIndex.GetProceso(j).getResultado(),
						lotesProcesados});			break;
			case 5:
				modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
						loteIndex.GetProceso(j).getX() + " % " + loteIndex.GetProceso(j).getY(), 
						loteIndex.GetProceso(j).getResultado(),
						lotesProcesados});			break;
			}
		} else {
			modelo.addRow(new Object[] {loteIndex.GetProceso(j).getId(), 
				loteIndex.GetProceso(j).getX() + " + " + loteIndex.GetProceso(j).getY(), 
				"Error",
				lotesProcesados});
		}

	}

	private void Interrupcion() {
		//	-- Detiene al reloj para realizar el procedimiento (no se si sea necesario) --
		reloj.stop();
		//	-- Guarda el progreso que llevaba el proceso actual --
		loteIndex.GetProceso(j).setTiempoRestante(loteIndex.GetProceso(j).getTiempoRestante() - tiempoTranscurrido);
		//	-- El proceso guardado lo encola en la lista original--
		lotes.get(i).InterrumpirProceso(j);
		//	-- Actualiza la sublista con la que trabaja la tabla para no generar conflictos --
		loteIndex = lotes.get(i);
		//	-- Muestra un mensaje al usuario --
		JOptionPane.showMessageDialog(null, "Proceso Interrumpido");
		//	-- Deja de escuchar al teclado para evitar múltiples usos simultaneos --
		this.setFocusable(false);
		//	-- Comienza el conteo del reloj para continuar --
		reloj.start();
	}
	
	//	-- Igual que Interrupción excepto:  
	private void Error() {
		reloj.stop();
		//	-- Asigna a la variable error del proceso como true (pues el proceso generó un error) --
		lotes.get(i).GetProceso(j).setError(true);
		loteIndex = lotes.get(i);
		JOptionPane.showMessageDialog(null, "Proceso Lanzó una Excepción");
		this.setFocusable(false);
		reloj.start();
	}
}
