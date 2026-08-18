package lote;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Formato extends JDialog{
	/*		Atributos		*/
	
	private static final long serialVersionUID = 1L;
	
	//Para panel gráfico
	private JPanel panelEntrada;
	private JPanel panelBotones;
	
	private boolean finalizado = false; // Bandera para saber si terminó bien
	private int cantidadTotalProcesos;			// Cantidad de procesos que se van a generar.
	
	//Listas
	private List<Proceso> listaProcesos = new ArrayList<>();
	
	/*		Métodos		*/
	
	public Formato(Frame padre, boolean modal) {	//	Parametros necesarios para superponer la ventana
		super(padre,modal); // Esto hace que la ventana detenga la ejecución del padre
		//	-- Estilo de la venta --
		setTitle("Cantidad De Procesos");
		setSize(450,150);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 2));
		
		//	-- Estilo de las entradas de texto en el registro --
		panelEntrada = new JPanel(new GridLayout(1, 2, 1, 1));					//	<-- Se crea el panel para los campos con un márgen determinado				
		panelEntrada.setBorder(BorderFactory.createEmptyBorder(17, 15, 17, 15));
		
		JLabel lblProcesos = new JLabel("Cantidad de procesos a ingresar:");	//	<-- Label para el nombre del campo
		JTextField txtCantProcesos = new JTextField();							// 	<-- Cuadro de texto
		
		panelEntrada.add(lblProcesos);	panelEntrada.add(txtCantProcesos);		//	<-- Se añaden al panel de entrada de datos
		
		//	-- Estilo de los botones --
		panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));	// 	<-- Se crean los botones con un márgen determinado
		JButton btnContinuar = new JButton("Continuar");						//	<-- Objeto botón
        panelBotones.add(btnContinuar);											//	<-- Se añade el botón al panel de botones
		
		add(panelEntrada, BorderLayout.CENTER);									//	Se agregan ambos paneles a la 
		add(panelBotones, BorderLayout.SOUTH);									//	ventana creada.
		
		//	-- Función del botón Continuar --
		btnContinuar.addActionListener(e -> {			//Función lambda
			//	-- Guardar texto --
			String strCantProcesos = txtCantProcesos.getText();

			//	-- Verificaciones y paso de string a integers --
			if (!strCantProcesos.isEmpty()) {
				try {
					cantidadTotalProcesos = Integer.parseInt(strCantProcesos);
					// 	-- Verificaciones de entradas --
					if (cantidadTotalProcesos > 0) {
						// -- Crear y añadir el proceso en un lote --
						generarListaProcesos();
						finalizado = true;
						this.dispose();
					} else {
						JOptionPane.showMessageDialog(this, "Ingresó una cantidad NO valida", "Cuidado!", JOptionPane.WARNING_MESSAGE);		//	<-- (ventana de la cual emerge, mensaje a mostrar, título del mensaje, ícono del mensaje)
					}
					//	-- Atrapa la excepción del intento de convertir String a integer --
				} catch (NumberFormatException exception){
					JOptionPane.showMessageDialog(this, "Solo puedes ingresar número enteros", "Cuidado!", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Ingrese una cantidad para continuar", "Cuidado!", JOptionPane.WARNING_MESSAGE);
			}
		});
	}
	
	public void generarListaProcesos() {

        for (int i = 1; i <= cantidadTotalProcesos; i++) {
            // Creamos un proceso con datos de ejemplo
            // Usamos i para el ID y valores aleatorios para x, y, tiempo y operación
        	//int TM = (int) (Math.random() * 15) + 6;
            Proceso p = new Proceso(
                "Proceso-" + i,              // Nombre
                i,                           // ID
                (int) (Math.random() * 6),   // Operación (0 a 5)
                (int) (Math.random() * 50),  // X
                (int) (Math.random() * 50) + 1, // Y (evitamos división por 0)
                (int) (Math.random() * 15) + 6 // Tiempo Máximo
            );
            listaProcesos.add(p);
        }
    }
	
	public void generarNuevoProceso() {
		cantidadTotalProcesos++;
		Proceso p = new Proceso(
			"Proceso-" + cantidadTotalProcesos,
			cantidadTotalProcesos,
			(int) (Math.random() * 6),   // Operación (0 a 5)
            (int) (Math.random() * 50),  // X
            (int) (Math.random() * 50) + 1, // Y (evitamos división por 0)
            (int) (Math.random() * 15) + 6
		);
		listaProcesos.add(p);
	}
	
	public List<Proceso> GetLista()		{ return listaProcesos;	}		//	<-- La versión 1.0 retorna listaLotes.
	public boolean GetFinalizado()		{ return finalizado;	}
	
	//	-- Antiguo Constructor --			<-- NO BORRAR PORQUE ME SIRVE PARA VER LA LÓGICA DE LOS PANELES.
	/*
	public Formato(Frame padre, boolean modal) {	//	Parametros necesarios para superponer la ventana
		super(padre,modal); // Esto hace que la ventana detenga la ejecución del padre
		//	-- Estilo de la venta --
		setTitle("Registro");
		setSize(600,350);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));
		
		//	-- Estilo de las entradas de texto en el registro --
		panelEntrada = new JPanel(new GridLayout(7, 2, 10, 10));
		panelEntrada.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JLabel lblNombre = new JLabel("Nombre del autor:");
		JTextField txtNombre = new JTextField();
		
		JLabel lblID = new JLabel("ID:");
		JTextField txtID = new JTextField();
		
		String[] operaciones = {"Suma", "Resta", "Multiplicacion", "Division", "Potencia", "Modulo"};
		JLabel lblOperacion = new JLabel("Operacion: ");
		JComboBox<String> selectOperacion = new JComboBox<>(operaciones);
		
		JLabel lblX = new JLabel("Primer operando: ");
		JTextField txtX = new JTextField();
		
		JLabel lblY = new JLabel("Segundo Operando: ");
		JTextField txtY = new JTextField();
		
		JLabel lblTME = new JLabel("Tiempo maximo estimado: ");
		JTextField txtTME = new JTextField();
		
		panelEntrada.add(lblNombre);	panelEntrada.add(txtNombre);
		panelEntrada.add(lblID);		panelEntrada.add(txtID);
		panelEntrada.add(lblOperacion);	panelEntrada.add(selectOperacion);
		panelEntrada.add(lblX);			panelEntrada.add(txtX);
		panelEntrada.add(lblY);			panelEntrada.add(txtY);
		panelEntrada.add(lblTME);		panelEntrada.add(txtTME);
		
		//	-- Estilo de los botones --
		panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		JButton btnAgregar = new JButton("Añadir Proceso");
        JButton btnTerminar = new JButton("Guardar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnTerminar);
		
		add(panelEntrada, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		
		//	-- Función del botón Agregar --
		btnAgregar.addActionListener(e -> {			//Función lambda
			//	-- Guardar texto --
			nombre = txtNombre.getText();
			String strID = txtID.getText();
			operacion = selectOperacion.getSelectedIndex();
			String strX = txtX.getText();
			String strY = txtY.getText();
			String strTiempoMax = txtTME.getText();
			
			//	-- Verificaciones y paso de string a integers --
			if (!nombre.isEmpty() &&!strID.isEmpty() && !strX.isEmpty() &&
				!strY.isEmpty() && !strTiempoMax.isEmpty()) {
				try {
					id = Integer.parseInt(strID);
					x = Integer.parseInt(strX);
					y = Integer.parseInt(strY);
					tiempoMax = Integer.parseInt(strTiempoMax);
					
					// 	-- Verificaciones de entradas --
					if (!ids.contains(id) && id > 0) {
						
						if (!(tiempoMax < 1)) {
						
							if (!((operacion == 3 || operacion == 5) && y == 0)) {
							
								// -- Crear y añadir el proceso en un lote --
								Proceso nuevoProceso = new Proceso(nombre, id, operacion, x, y, tiempoMax);
								AgregarProceso(nuevoProceso);
								
								txtNombre.setText("");
								txtID.setText("");
								txtX.setText("");
								txtY.setText("");
								txtTME.setText("");
								
								ids.add(id);
								
								JOptionPane.showMessageDialog(this, "Proceso de " + nombre + " agregado correctamente");
								
							} else {
								
								JOptionPane.showMessageDialog(this, "Operación indefinida, verifique sus operandos", "Cuidado!", JOptionPane.WARNING_MESSAGE);
							
							}
						} else {
							
							JOptionPane.showMessageDialog(this, "Eliga un rango de tiempo válido", "Cuidado!", JOptionPane.WARNING_MESSAGE);
						}
						
					} else {
						
						JOptionPane.showMessageDialog(this, "Este ID no es válido o ya está en uso", "Cuidado!", JOptionPane.WARNING_MESSAGE);

					}
					
					//	-- Atrapa la excepción del intento de convertir String a integer --
				} catch (NumberFormatException exception){
					JOptionPane.showMessageDialog(this, "Recuerda que solo puedes ingresar numeros enteros");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Ventanas Vacías, por favor rellena todos los campos");
			}
		});
		
		//	-- Accion del boton guardar --		*Hay que cambiar este método para el generador automático
		btnTerminar.addActionListener(e -> {
			AgregarProceso(null);
			if (listaLotes.size() > 0) { 	
				finalizado = true;
				JOptionPane.showMessageDialog(this, "Registro terminado de manera satisfactoria");
				this.dispose();
			}
			else {
				JOptionPane.showMessageDialog(this, "No existen procesos en la lista", "Cuidado!", JOptionPane.WARNING_MESSAGE);
			}
		});
		
	}

	private void AgregarProceso(Proceso p) {
		
		//	-- Guarda el último lote en la lista, como esté	--
		if (p == null) {											
			if(!lote.isEmpty()) {
				Lote nuevoLote = new Lote(lote);
				listaLotes.add(nuevoLote);
				lote.clear();
			}
		//	-- Añade un proceso a un lote --
		} else {													
			lote.add(p);
		//	-- Guarda el lote completo en la lista --
			if (lote.size() == 5){									
				Lote nuevoLote = new Lote(new ArrayList<>(lote));
				listaLotes.add(nuevoLote);
				lote.clear();
			}
		}
	}
	*/
	
}
