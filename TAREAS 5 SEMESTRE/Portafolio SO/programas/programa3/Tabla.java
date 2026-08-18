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
import javax.swing.Timer;
import java.awt.*;

public class Tabla extends JFrame{

	private static final long serialVersionUID = 1L;
	
	//	-- Variables de la tabla --
	private JTable tabla;
	private DefaultTableModel modelo;
	private TablaTerminados tablaTerminados;
	private TablaBloqueados tablaBloqueados;
	
	//	-- Variables para acceder a la lista --
	private List<Proceso> listaProcesos = new ArrayList<>();
	private List<Proceso> procesosBloqueados = new ArrayList<>();
	private int i = 0;	//	<-- i apunta a lotes, j apunta a procesos
	private Lote procesosListos = new Lote();		//	<-- Guarda un lote
	private char operacion;	
	
	//	-- Variables para llevar el conteo --
	Timer reloj;
	private int contadorGlobal = 0;
	private int tiempoTranscurrido = 0;
	private int procesosLlegados = 0;
	
	public Tabla(List<Proceso> L) {
		//	-- Inicializa los valores en la tabla --
		setTitle("Tabla de procesos");
		setSize(500, 290);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(25,300);
		
		String[] columnas = {"_______________", "_____", "_____"};
		modelo = new DefaultTableModel(null, columnas);
		
		modelo.addRow(new Object[] {"Procesos pendientes: ", "0"});
		modelo.addRow(new Object[] {"Procesos en ejecucion: "});
		modelo.addRow(new Object[] {"ID", "TME", "TR"});
		modelo.addRow(new Object[] {"", "", ""});
		modelo.addRow(new Object[] {"", "", ""});
		modelo.addRow(new Object[] {"", "", ""});
		modelo.addRow(new Object[] {"", "", ""});
		modelo.addRow(new Object[] {"Proceso ejecutandose: "});
		modelo.addRow(new Object[] {"ID", ""});
		modelo.addRow(new Object[] {"Ope", ""});
		modelo.addRow(new Object[] {"TME", ""});
		modelo.addRow(new Object[] {"TT", ""});
		modelo.addRow(new Object[] {"TR", ""});
		modelo.addRow(new Object[] {"Contador global: ", "0"});
		
		tabla = new JTable(modelo);
		
		add(new JScrollPane(tabla), BorderLayout.CENTER);
		
		//	-- Inicializa las variables y el reloj --
		listaProcesos = L;	
		InicializarTabla();
		
		//	-- Objeto contador que controla el flujo de la tabla de procesos --
		reloj = new Timer(1000, e -> {
			this.setFocusable(true);	// <-- Escucha si las teclas fueron usadas. Es necesario que se refresque cada vez por si se desactivo con una interrupción o error
			this.requestFocusInWindow();// <-- Imnediatamente obtiene el foco de las ventanas
			contadorGlobal++;			// <-- Reloj global
			RegresarAListos();
			if (procesosListos.tamanio() > 0) {
				tiempoTranscurrido++;		// <-- Reloj de cada proceso
				ActualizarLista();			// <-- Función que refresca la tabla
			}
			if (reloj.isRunning()) 	modelo.setValueAt(contadorGlobal, 13, 1);	//	<-- Modifica el apartado de contador global
		});
			
		tablaTerminados = new TablaTerminados();
		tablaTerminados.setVisible(true);
		tablaBloqueados = new TablaBloqueados(reloj);
		tablaBloqueados.setVisible(true);
		
//		-- Encargado de escuchar si una tecla fue presionada --
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_P:
						if (reloj.isRunning()) {
							reloj.stop();
							System.out.println("Pausa");
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
							if (procesosListos.tamanio() > 0)	Interrupcion();
							
						}
						break;
					case KeyEvent.VK_E:
						if (reloj.isRunning()) {
							System.out.println("Error");
							setTitle("Cambiando de contexto");
							if (procesosListos.tamanio() > 0)	Error();
						}
						break;
					}
				}
			});
			
			this.setFocusable(true);
			this.requestFocusInWindow();
		
		reloj.start();
	}
	
//	-- Inicializa la tabla, inserta los primeros 5 procesos en el sistema y los muestra --
	private void InicializarTabla() {
		for (i = 0; i < 5; i++) {
			if (listaProcesos.size() > i) {
				procesosListos.agregarProceso(listaProcesos.get(i));
				procesosListos.getProceso(i).setTiempoLlegada(0);
			} else {
				break;
			}
		}
		procesosListos.getProceso(0).setTiempoRespuesta(0);
		procesosLlegados = i;
		RefrescarTabla();
	}
	
	private void ActualizarLista() {
		setTitle("Tabla de procesos");	//	<-- Recarga el nombre de la tabla de procesos, por si fue modificado.
		//	-- Si el tiempo transcurrido del proceso llega a su valor maximo entonces:
		if (tiempoTranscurrido + procesosListos.getProceso(0).getTiempoTranscurrido() > procesosListos.getProceso(0).getTiempoMax() || procesosListos.getProceso(0).getError() || procesosListos.getProceso(0).getBloqueado()) {
			
			procesosListos.getProceso(0).setTiempoTranscurrido(procesosListos.getProceso(0).getTiempoTranscurrido() + tiempoTranscurrido - 1);
			
			//	-- Si no se detuvo por interrupción: 
			if (!procesosListos.getProceso(0).getBloqueado()) {
				procesosListos.getProceso(0).setTiempoFinalizacion(contadorGlobal - 1);
				tablaTerminados.AgregarATablaTerminados(procesosListos.getProceso(0));		// <-- Añade al final el proceso que se terminó
				
				//	-- Si existen procesos en la lista entonces:
				if (i < listaProcesos.size()) {
					procesosListos.agregarProceso(listaProcesos.get(i));	//	<-- Añade un nuevo proceso en la lista
					listaProcesos.get(i).setTiempoLlegada(contadorGlobal - 1);
					procesosLlegados++; //	<-- Aumenta la cantidad de procesos que pasaron por el núcleo del sistema
					i++;
					
				//	-- Si no, termina
				} else if (procesosListos.tamanio() == 1 && procesosBloqueados.size() == 0) {
					this.setFocusable(false);	//	<-- La tabla deja de recibir señal del teclado para evitar errores.
					reloj.stop();
					LimpiarVentana();
	                JOptionPane.showMessageDialog(null, "Simulación Finalizada");
					return;
				}
			}
				
			procesosListos.popProceso(0);	// <-- Elimina el proceso que terminó
			tiempoTranscurrido = 0;			// <-- Reinicia el reloj para el sig proceso
			
			//	-- Asigna el tiempo de respuesta a el proceso nuevo que entró --
			if (procesosListos.tamanio() > 0 && procesosListos.getProceso(0).getTiempoRespuesta() < 0)
				procesosListos.getProceso(0).setTiempoRespuesta(contadorGlobal - procesosListos.getProceso(0).getTiempoLlegada());	
			
			
		} 
		
		if (procesosListos.tamanio() > 0)
			RefrescarTabla();
		else 
			LimpiarVentana();
	}
	
	// 	-- Función encargada de actualizar los datos de la tabla --
	private void RefrescarTabla() {
		modelo.setValueAt(listaProcesos.size() - procesosLlegados, 0, 1);	//	<-- Actualiza los procesos restantes
		
		//	-- Actualiza la lista de pendientes --
		for (int ind = 3; ind - 2 < 5; ind++) {														// 	ind - 2 <-- Se enfoca en el sig proceso y no en el actual. J es la variable que se encarga de avanzar la lista	
			if (procesosListos.tamanio() > ind - 2) {												//	Si el indice del siguiente proceso es válido, entonces avanza.
				modelo.setValueAt(procesosListos.getProceso(ind - 2).getId() , ind, 0);				//	<-- Actualiza el id de la cola de procesos
				modelo.setValueAt(procesosListos.getProceso(ind - 2).getTiempoMax() , ind, 1);		//	<-- Actualiza el Tiempo Máximo Estimado de la cola de procesos
				modelo.setValueAt(procesosListos.getProceso(ind - 2).getTiempoMax() - procesosListos.getProceso(ind - 2).getTiempoTranscurrido(), ind, 2);	//	<-- Actualiza el Tiempo Restante de la cola de procesos
			} else {																				//
				modelo.setValueAt( "" , ind, 0);													//	<-- Si no existe deja vacío ese registro.
				modelo.setValueAt( "" , ind, 1);
				modelo.setValueAt( "" , ind, 2);
			}
		}
		
		//	-- Muestra la información del proceso actual --
		modelo.setValueAt(procesosListos.getProceso(0).getId() , 8, 1);		//	<-- Su id
		//	Imprimir la operación a ejecutar dependiendo de la variable que tenga operación	--
		switch (procesosListos.getProceso(0).getOperacion()) {
		case 0:
			operacion = '+';	//	<-- Suma si es 0
			break;
		case 1:
			operacion = '-';	//	<-- Resta si es 1
			break;
		case 2:
			operacion = '*';	//	<-- Multiplicación si es 2
			break;
		case 3:
			operacion = '/';	//	<-- División si es 3
			break;
		case 4:
			operacion = '^';	//	<-- Potencia si es 4
			break;
		case 5:
			operacion = '%';	//	<-- Módulo si es 5
			break;
		}
		modelo.setValueAt(procesosListos.getProceso(0).getX() + " " + operacion + " " + procesosListos.getProceso(0).getY(), 9, 1);	//	<-- Imprime en pantalla
		//	-- Actualiza la información del tiempo que lleva este proceso --
		modelo.setValueAt(procesosListos.getProceso(0).getTiempoMax() , 10, 1);		//	<-- Muestra el Tiempo Max Estimado del proceso
		modelo.setValueAt(procesosListos.getProceso(0).getTiempoTranscurrido() + tiempoTranscurrido , 11, 1);							//	<-- Muestra el Tiempo Transcurrido del proceso
		modelo.setValueAt(procesosListos.getProceso(0).getTiempoMax() - procesosListos.getProceso(0).getTiempoTranscurrido() - tiempoTranscurrido, 12, 1);	//	<-- Muestra el Tiempo Restante del proceso
	}
	
	private void LimpiarVentana() {
		for (int i = 8; i < 13; i++) {
			modelo.setValueAt("", i, 1);
		}
	}
	
	private void Interrupcion() {
		//	-- Deja de escuchar al teclado para evitar múltiples usos simultaneos --
		this.setFocusable(false);
		//	-- El proceso guardado lo encola en la lista original--
		procesosListos.getProceso(0).setBloqueado(true);
		tablaBloqueados.AgregarATablaBloqueados(procesosListos.getProceso(0));
		procesosBloqueados.add(procesosListos.getProceso(0));
	}
	
	//	-- Igual que Interrupción excepto:  
	private void Error() {
		this.setFocusable(false);
		//	-- Asigna a la variable error del proceso como true (pues el proceso generó un error) --
		procesosListos.getProceso(0).setError(true);
	}
	
	private void RegresarAListos() {
		if (procesosBloqueados.size() > 0) {
			for (int i = 0; i < procesosBloqueados.size(); i++) {
				if (!procesosBloqueados.get(i).getBloqueado()) {
					procesosListos.agregarProceso(procesosBloqueados.get(i));
					procesosBloqueados.remove(i);
				}
			}
		}
	}
}
