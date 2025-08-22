package Interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.*;
import AEROLINEA.Aerolinea;
import AEROLINEA.Aeropuerto;
import AEROLINEA.Avion;
import AEROLINEA.Cliente;
import AEROLINEA.Empleado;
import AEROLINEA.Reservacion;
import AEROLINEA.Vuelo;
import EXCEPCIONES.aeropuertoNoEncontrado;
import EXCEPCIONES.avionNoEncontrado;
import EXCEPCIONES.empleadosNoDisponibles;
import EXCEPCIONES.arregloVacio;
import EXCEPCIONES.malDigitadoNulo;
import EXCEPCIONES.personaNoEncontrada;
import EXCEPCIONES.reservaNoEncontrada;
import EXCEPCIONES.vueloLleno;
import EXCEPCIONES.vueloNoEncontrado;
import EXCEPCIONES.vueloVigente;
import Ficheros.ManejoFicheros;

public class InterfazAerolinea {
    private JTextArea outputArea;
    private ManejoFicheros f = new ManejoFicheros();
    private Aerolinea a = f.getAerolinea();
    private String[] tipoDoc = { "CC", "CE", "TI", "Pasaporte" };
    private String[] sexos = {"Masculino", "Femenino", "Otro"};
    private String[] cargos = {"Piloto", "Copiloto"};
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new InterfazAerolinea().crearInterfaz());
	}
	
	private void crearInterfaz() {
		JFrame frame = new JFrame("Aerolínea");
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Aviones", crearPanelAviones());
        tabbedPane.add("Aeropuertos", crearPanelAeropuertos());
        tabbedPane.add("Clientes", crearPanelClientes());
        tabbedPane.add("Empleados", crearPanelEmpleados());
        tabbedPane.add("Vuelos", crearPanelVuelos());
        tabbedPane.add("Reservaciones", crearPanelReservaciones());

        frame.add(tabbedPane, BorderLayout.CENTER);

        outputArea = new JTextArea(5, 40); // small visible size
        outputArea.setEditable(false);
        frame.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        frame.setVisible(true);
	}
	
	private JPanel crearPanelAviones() {
		JPanel panel = new JPanel(new BorderLayout());

	    // --- Top: action buttons ---
	    JPanel actionsPanel = new JPanel(new FlowLayout());
	    JButton addButton = new JButton("Agregar Avión");
	    JButton deleteButton = new JButton("Eliminar Avión");
	    JButton searchButton = new JButton("Buscar Avión");
	    JButton showButton = new JButton("Mostrar Aviones");
	    actionsPanel.add(addButton);
	    actionsPanel.add(deleteButton);
	    actionsPanel.add(searchButton);
	    actionsPanel.add(showButton);

	    // --- Center: forms (hidden until needed) ---
	    JPanel formsPanel = new JPanel(new CardLayout());

	    // Añadir avión
	    JPanel addForm = new JPanel(new GridLayout(4, 2, 5, 5));
	    JTextField modeloField = new JTextField();
	    JTextField capacidadField = new JTextField();
	    JButton confirmarAdd = new JButton("Confirmar");

	    addForm.add(new JLabel("Modelo:")); addForm.add(modeloField);
	    addForm.add(new JLabel("Capacidad:")); addForm.add(capacidadField);
	    addForm.add(new JLabel("")); addForm.add(confirmarAdd);

	    formsPanel.add(addForm, "ADD");

	    // Eliminar avión
	    JPanel deleteForm = new JPanel(new FlowLayout());
	    JTextField deleteCodigo = new JTextField(10);
	    JButton confirmarDelete = new JButton("Eliminar");
	    deleteForm.add(new JLabel("Código del avión:"));
	    deleteForm.add(deleteCodigo);
	    deleteForm.add(confirmarDelete);

	    formsPanel.add(deleteForm, "DELETE");
	    
	    // Buscar avión
	    JPanel searchForm = new JPanel(new FlowLayout());
	    JTextField buscarCodigo = new JTextField(10);
	    JButton confirmarBuscar = new JButton("Buscar");
	    searchForm.add(new JLabel("Código del avión:"));
	    searchForm.add(buscarCodigo);
	    searchForm.add(confirmarBuscar);

	    formsPanel.add(searchForm, "SEARCH");

	    // Initially hide forms
	    ((CardLayout) formsPanel.getLayout()).show(formsPanel, ""); 

	    // --- Action listeners ---
	    addButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "ADD");
	    });

	    deleteButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "DELETE");
	    });
	    
	    searchButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "SEARCH");
	    });

	    confirmarAdd.addActionListener(e -> {
	        try {
	            a.addAvion(
	                modeloField.getText(),
	                Integer.parseInt(capacidadField.getText())
	            );
	            outputArea.append("Avión agregado: " + modeloField.getText() + 
	            				" (" + a.getAviones()[a.getAviones().length - 1].getCodigoA() + ")" + "\n");
	            modeloField.setText("");
	            capacidadField.setText("");
	        } catch (malDigitadoNulo | NumberFormatException ex) {
	            outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });

	    confirmarDelete.addActionListener(e -> {
	        String codigo = deleteCodigo.getText();
	        try {
	        	a.eliminarAvion(codigo);
	        	outputArea.append("Avión eliminado: " + codigo + "\n");
	        	deleteCodigo.setText("");
	        }
	        catch(arregloVacio | avionNoEncontrado | vueloVigente ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    confirmarBuscar.addActionListener(e -> {
	    	String codigo = buscarCodigo.getText();
	    	try {
	    		Avion avion = a.buscarAvion(codigo);
	    		outputArea.append("Avión encontrado: " + avion.toString() + "\n");
	    		buscarCodigo.setText("");
	    	}
	    	catch(arregloVacio | avionNoEncontrado ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    showButton.addActionListener(e -> {
	        if (a.getAviones() == null || a.getAviones().length == 0) {
	            outputArea.append("No hay aviones registrados.\n");
	        } else {
	            outputArea.append("Lista de aviones:\n");
	            for (Avion av : a.getAviones()) {
	                outputArea.append(av.toString());
	                outputArea.append("\n");
	            }
	        }
	    });

	    // --- Assemble panel ---
	    panel.add(actionsPanel, BorderLayout.NORTH);
	    panel.add(formsPanel, BorderLayout.CENTER);

	    return panel;
	}
	
	private JPanel crearPanelAeropuertos() {
	    JPanel panel = new JPanel(new BorderLayout());

	    // --- Top: action buttons ---
	    JPanel actionsPanel = new JPanel(new FlowLayout());
	    JButton addButton = new JButton("Agregar Aeropuerto");
	    JButton deleteButton = new JButton("Eliminar Aeropuerto");
	    JButton searchButton = new JButton("Buscar Aeropuerto");
	    JButton showButton = new JButton("Mostrar Aeropuertos");
	    actionsPanel.add(addButton);
	    actionsPanel.add(deleteButton);
	    actionsPanel.add(searchButton);
	    actionsPanel.add(showButton);

	    // --- Center: forms (hidden until needed) ---
	    JPanel formsPanel = new JPanel(new CardLayout());

	    // Añadir aeropuerto
	    JPanel addForm = new JPanel(new GridLayout(6, 2, 5, 5));
	    JTextField nombreField = new JTextField();
	    JTextField codigoField = new JTextField();
	    JTextField paisField = new JTextField();
	    JTextField ciudadField = new JTextField();
	    JCheckBox internacionalCheck = new JCheckBox("Internacional");
	    JButton confirmarAdd = new JButton("Confirmar");

	    addForm.add(new JLabel("Nombre:")); addForm.add(nombreField);
	    addForm.add(new JLabel("Código:")); addForm.add(codigoField);
	    addForm.add(new JLabel("País:")); addForm.add(paisField);
	    addForm.add(new JLabel("Ciudad:")); addForm.add(ciudadField);
	    addForm.add(new JLabel("Tipo:")); addForm.add(internacionalCheck);
	    addForm.add(new JLabel("")); addForm.add(confirmarAdd);

	    formsPanel.add(addForm, "ADD");

	    // Eliminar aeropuerto
	    JPanel deleteForm = new JPanel(new FlowLayout());
	    JTextField deleteCodigo = new JTextField(10);
	    JButton confirmarDelete = new JButton("Eliminar");
	    deleteForm.add(new JLabel("Código del aeropuerto:"));
	    deleteForm.add(deleteCodigo);
	    deleteForm.add(confirmarDelete);

	    formsPanel.add(deleteForm, "DELETE");
	    
	    // Buscar aeropuerto
	    JPanel searchForm = new JPanel(new FlowLayout());
	    JTextField buscarCodigo = new JTextField(10);
	    JButton confirmarBuscar = new JButton("Buscar");
	    searchForm.add(new JLabel("Código del aeropuerto:"));
	    searchForm.add(buscarCodigo);
	    searchForm.add(confirmarBuscar);

	    formsPanel.add(searchForm, "SEARCH");

	    // Initially hide forms
	    ((CardLayout) formsPanel.getLayout()).show(formsPanel, ""); 

	    // --- Action listeners ---
	    addButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "ADD");
	    });

	    deleteButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "DELETE");
	    });
	    
	    searchButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "SEARCH");
	    });

	    confirmarAdd.addActionListener(e -> {
	        try {
	            a.addAeropuerto(
	                nombreField.getText(),
	                codigoField.getText(),
	                paisField.getText(),
	                ciudadField.getText(),
	                internacionalCheck.isSelected()
	            );
	            outputArea.append("Aeropuerto agregado: " + nombreField.getText() + "\n");

	            // Clear fields
	            nombreField.setText("");
	            codigoField.setText("");
	            paisField.setText("");
	            ciudadField.setText("");
	            internacionalCheck.setSelected(false);

	        } catch (malDigitadoNulo ex) {
	            outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });

	    confirmarDelete.addActionListener(e -> {
	        String codigo = deleteCodigo.getText();
	        try {
	        	a.eliminarAeropuerto(codigo);
	        	outputArea.append("Aeropuerto eliminado: " + codigo + "\n");
	        	deleteCodigo.setText("");
	        }
	        catch(arregloVacio | aeropuertoNoEncontrado ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    confirmarBuscar.addActionListener(e -> {
	    	String codigo = buscarCodigo.getText();
	    	try {
	    		Aeropuerto aeropuerto = a.buscarAeropuerto(codigo);
	    		outputArea.append("Aeropuerto encontrado: " + aeropuerto.toString() + "\n");
	    		buscarCodigo.setText("");
	    	}
	    	catch(arregloVacio | aeropuertoNoEncontrado ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    showButton.addActionListener(e -> {
	        if (a.getAeropuertos() == null || a.getAeropuertos().length == 0) {
	            outputArea.append("No hay aeropuertos registrados.\n");
	        } else {
	            outputArea.append("Lista de aeropuertos:\n");
	            for (Aeropuerto ae : a.getAeropuertos()) {
	                outputArea.append(ae.toString());
	                outputArea.append("\n");
	            }
	        }
	    });

	    // --- Assemble panel ---
	    panel.add(actionsPanel, BorderLayout.NORTH);
	    panel.add(formsPanel, BorderLayout.CENTER);

	    return panel;
	}
	
	private JPanel crearPanelClientes() {
		JPanel panel = new JPanel(new BorderLayout());

		// --- Top: action buttons ---
		JPanel actionsPanel = new JPanel(new FlowLayout());
		JButton addButton = new JButton("Agregar Cliente");
		JButton deleteButton = new JButton("Eliminar Cliente");
		JButton searchButton = new JButton("Buscar Cliente");
		JButton showButton = new JButton("Mostrar Clientes");
		actionsPanel.add(addButton);
		actionsPanel.add(deleteButton);
		actionsPanel.add(searchButton);
		actionsPanel.add(showButton);

		// --- Center: forms (hidden until needed) ---
		JPanel formsPanel = new JPanel(new CardLayout());

		// Añadir cliente
		JPanel addForm = new JPanel(new GridLayout(10, 2, 5, 5));
		JTextField nombreField = new JTextField();
		JComboBox<String> tipoDocBoxAdd = new JComboBox<>(tipoDoc);
		JTextField numDocField = new JTextField();
		JTextField nacionalidadField = new JTextField();
		JComboBox<String> sexoBox = new JComboBox<>(sexos);
		JCheckBox vipCheck = new JCheckBox("Cliente VIP");
		JTextField millasField = new JTextField();
		JButton confirmarAdd = new JButton("Confirmar");

		addForm.add(new JLabel("Nombre:")); addForm.add(nombreField);
		addForm.add(new JLabel("Tipo de Documento:")); addForm.add(tipoDocBoxAdd);
		addForm.add(new JLabel("Número de Documento:")); addForm.add(numDocField);
		addForm.add(new JLabel("Nacionalidad:")); addForm.add(nacionalidadField);
		addForm.add(new JLabel("Sexo:")); addForm.add(sexoBox);
		addForm.add(new JLabel("Tipo:")); addForm.add(vipCheck);
		addForm.add(new JLabel("Millas acumuladas:")); addForm.add(millasField);
		addForm.add(new JLabel("")); addForm.add(confirmarAdd);

		formsPanel.add(addForm, "ADD");

		// Eliminar cliente
		JPanel deleteForm = new JPanel(new FlowLayout());
		JComboBox<String> tipoDocBoxDelete = new JComboBox<>(tipoDoc);
		JTextField deleteNumDoc = new JTextField(10);
		JButton confirmarDelete = new JButton("Eliminar");
		deleteForm.add(new JLabel("Tipo de documento:"));
		deleteForm.add(tipoDocBoxDelete);
		deleteForm.add(new JLabel("Número de documento:"));
		deleteForm.add(deleteNumDoc);
		deleteForm.add(confirmarDelete);

		formsPanel.add(deleteForm, "DELETE");

		// Buscar cliente
		JPanel searchForm = new JPanel(new FlowLayout());
		JComboBox<String> tipoDocBoxSearch = new JComboBox<>(tipoDoc);
		JTextField buscarNumDoc = new JTextField(10);
		JButton confirmarBuscar = new JButton("Buscar");
		searchForm.add(new JLabel("Tipo de documento:"));
		searchForm.add(tipoDocBoxSearch);
		searchForm.add(new JLabel("Número de documento:"));
		searchForm.add(buscarNumDoc);
		searchForm.add(confirmarBuscar);

		formsPanel.add(searchForm, "SEARCH");

		// Initially hide forms
		((CardLayout) formsPanel.getLayout()).show(formsPanel, ""); 

		// --- Action listeners ---
		addButton.addActionListener(e -> {
		    ((CardLayout) formsPanel.getLayout()).show(formsPanel, "ADD");
		});

		deleteButton.addActionListener(e -> {
		    ((CardLayout) formsPanel.getLayout()).show(formsPanel, "DELETE");
		});

		searchButton.addActionListener(e -> {
		    ((CardLayout) formsPanel.getLayout()).show(formsPanel, "SEARCH");
		});

		confirmarAdd.addActionListener(e -> {
		    char sexo = ((String) sexoBox.getSelectedItem()).charAt(0);
			
		    try {
		        a.addCliente(
		            nombreField.getText(),
		            (String) tipoDocBoxAdd.getSelectedItem(),
		            numDocField.getText(),
		            nacionalidadField.getText(),
		            sexo,
		            vipCheck.isSelected(),
		            Long.parseLong(millasField.getText())
		        );
		        outputArea.append("Cliente agregado: " + nombreField.getText() + "\n");

		        // Clear fields
		        nombreField.setText("");
		        numDocField.setText("");
		        nacionalidadField.setText("");
		        vipCheck.setSelected(false);
		        millasField.setText("");

		    } catch (malDigitadoNulo | NumberFormatException ex) {
		        outputArea.append("Error: " + ex.getMessage() + "\n");
		    }
		});

		confirmarDelete.addActionListener(e -> {
		    String numDoc = deleteNumDoc.getText();
		    try {
		    	a.eliminarCliente((String) tipoDocBoxDelete.getSelectedItem(), numDoc);
		    	outputArea.append("Cliente eliminado: " + (String) tipoDocBoxDelete.getSelectedItem() + " " + numDoc + "\n");
		    	deleteNumDoc.setText("");
		    }
		    catch(arregloVacio | personaNoEncontrada | malDigitadoNulo ex) {
		    	outputArea.append("Error: " + ex.getMessage() + "\n");
		    }
		});

		confirmarBuscar.addActionListener(e -> {
		    String numDoc = buscarNumDoc.getText();
		    try {
		    	Cliente cliente = a.buscarCliente((String) tipoDocBoxSearch.getSelectedItem(), numDoc);
		    	outputArea.append("Cliente encontrado: " + cliente.getNombreP() + " " + numDoc + "\n");
		    	buscarNumDoc.setText("");
		    }
		    catch(arregloVacio | malDigitadoNulo | personaNoEncontrada ex) {
		    	outputArea.append("Error: " + ex.getMessage() + "\n");
		    }
		});

		showButton.addActionListener(e -> {
		    if (a.getClientes() == null || a.getClientes().length == 0) {
		        outputArea.append("No hay clientes registrados.\n");
		    } else {
		        outputArea.append("Lista de clientes:\n");
		        for (Cliente c : a.getClientes()) {
		            outputArea.append(c.toString());
		            outputArea.append("\n");
		        }
		    }
		});

		// --- Assemble panel ---
		panel.add(actionsPanel, BorderLayout.NORTH);
		panel.add(formsPanel, BorderLayout.CENTER);

		return panel;

	}
	
	private JPanel crearPanelEmpleados() {
		JPanel panel = new JPanel(new BorderLayout());

	    // --- Top: action buttons ---
	    JPanel actionsPanel = new JPanel(new FlowLayout());
	    JButton addButton = new JButton("Agregar Empleado");
	    JButton deleteButton = new JButton("Eliminar Empleado");
	    JButton searchButton = new JButton("Buscar Empleado");
	    JButton showButton = new JButton("Mostrar Empleados");
	    actionsPanel.add(addButton);
	    actionsPanel.add(deleteButton);
	    actionsPanel.add(searchButton);
	    actionsPanel.add(showButton);

	    // --- Center: forms (hidden until needed) ---
	    JPanel formsPanel = new JPanel(new CardLayout());
	    
	    // --- Common values ---

	    // Añadir empleado
	    JPanel addForm = new JPanel(new GridLayout(8, 2, 5, 5));
	    JTextField nombreField = new JTextField();
	    JComboBox<String> tipoDocBoxAdd = new JComboBox<>(tipoDoc);
	    JTextField numDocField = new JTextField();
	    JTextField nacionalidadField = new JTextField();
	    JComboBox<String> sexoBox = new JComboBox<>(sexos);
	    JComboBox<String> cargoBox = new JComboBox<>(cargos);
	    JButton confirmarAdd = new JButton("Confirmar");

	    addForm.add(new JLabel("Nombre:")); addForm.add(nombreField);
	    addForm.add(new JLabel("Tipo de Documento:")); addForm.add(tipoDocBoxAdd);
	    addForm.add(new JLabel("Número de Documento:")); addForm.add(numDocField);
	    addForm.add(new JLabel("Nacionalidad:")); addForm.add(nacionalidadField);
	    addForm.add(new JLabel("Sexo:")); addForm.add(sexoBox);
	    addForm.add(new JLabel("Cargo:")); addForm.add(cargoBox);
	    addForm.add(new JLabel("")); addForm.add(confirmarAdd);

	    formsPanel.add(addForm, "ADD");

	    // Eliminar empleado
	    JPanel deleteForm = new JPanel(new FlowLayout());
	    JComboBox<String> tipoDocBoxDelete = new JComboBox<>(tipoDoc);
	    JTextField deleteNumDoc = new JTextField(10);
	    JButton confirmarDelete = new JButton("Eliminar");
	    deleteForm.add(new JLabel("Tipo de documento:"));
	    deleteForm.add(tipoDocBoxDelete);
	    deleteForm.add(new JLabel("Número de documento:"));
	    deleteForm.add(deleteNumDoc);
	    deleteForm.add(confirmarDelete);

	    formsPanel.add(deleteForm, "DELETE");
	    
	    // Buscar empleado
	    JPanel searchForm = new JPanel(new FlowLayout());
		JComboBox<String> tipoDocBoxSearch = new JComboBox<>(tipoDoc);
	    JTextField buscarNumDoc = new JTextField(10);
	    JButton confirmarBuscar = new JButton("Buscar");
	    searchForm.add(new JLabel("Tipo de documento:"));
	    searchForm.add(tipoDocBoxSearch);
	    searchForm.add(new JLabel("Número de documento:"));
	    searchForm.add(buscarNumDoc);
	    searchForm.add(confirmarBuscar);

	    formsPanel.add(searchForm, "SEARCH");

	    // Initially hide forms
	    ((CardLayout) formsPanel.getLayout()).show(formsPanel, ""); 

	    // --- Action listeners ---
	    addButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "ADD");
	    });

	    deleteButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "DELETE");
	    });
	    
	    searchButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "SEARCH");
	    });

	    confirmarAdd.addActionListener(e -> {
	        char sexo = ((String) sexoBox.getSelectedItem()).charAt(0);
	    	
	    	try {
	            a.addEmpleado(
	                nombreField.getText(),
	                (String) tipoDocBoxAdd.getSelectedItem(),
	                numDocField.getText(),
	                nacionalidadField.getText(),
	                sexo,
	                (String) cargoBox.getSelectedItem()
	            );
	            outputArea.append("Empleado agregado: " + nombreField.getText() + "\n");

	            // Clear fields
	            nombreField.setText("");
                numDocField.setText("");
                nacionalidadField.setText("");

	        } catch (malDigitadoNulo ex) {
	            outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });

	    confirmarDelete.addActionListener(e -> {
	        String numDoc = deleteNumDoc.getText();
	        try {
	        	a.eliminarEmpleado((String) tipoDocBoxDelete.getSelectedItem(), numDoc);
	        	outputArea.append("Empleado eliminado: " + tipoDoc + " " + numDoc + "\n");
	        	deleteNumDoc.setText("");
	        }
	        catch(arregloVacio | personaNoEncontrada | malDigitadoNulo | vueloVigente ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    confirmarBuscar.addActionListener(e -> {
	        String numDoc = buscarNumDoc.getText();
	        try {
	        	a.buscarEmpleado((String) tipoDocBoxSearch.getSelectedItem(), numDoc);
	        	outputArea.append("Empleado encontrado: " + tipoDoc + " " + numDoc + "\n");
	        	buscarNumDoc.setText("");
	        }
	        catch(arregloVacio | malDigitadoNulo | personaNoEncontrada ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    showButton.addActionListener(e -> {
	        if (a.getEmpleados() == null || a.getEmpleados().length == 0) {
	            outputArea.append("No hay empleados registrados.\n");
	        } else {
	            outputArea.append("Lista de empleados:\n");
	            for (Empleado em : a.getEmpleados()) {
	                outputArea.append(em.toString());
		            outputArea.append("\n");
	            }
	        }
	    });

	    // --- Assemble panel ---
	    panel.add(actionsPanel, BorderLayout.NORTH);
	    panel.add(formsPanel, BorderLayout.CENTER);

	    return panel;
    }

	private JPanel crearPanelVuelos() {
		JPanel panel = new JPanel(new BorderLayout());

	    // --- Top: action buttons ---
	    JPanel actionsPanel = new JPanel(new FlowLayout());
	    JButton addButton = new JButton("Agregar Vuelo");
	    JButton deleteButton = new JButton("Eliminar Vuelo");
	    JButton searchButton = new JButton("Buscar Vuelo");
	    JButton updateButton = new JButton("Actualizar Vuelo");
	    JButton showButton = new JButton("Mostrar Vuelos");
	    actionsPanel.add(addButton);
	    actionsPanel.add(deleteButton);
	    actionsPanel.add(searchButton);
	    actionsPanel.add(updateButton);
	    actionsPanel.add(showButton);

	    // --- Center: forms (hidden until needed) ---
	    JPanel formsPanel = new JPanel(new CardLayout());

	    // Añadir vuelo
	    JPanel addForm = new JPanel(new GridLayout(8, 2, 5, 5));
	    JTextField avionField = new JTextField();
	    JTextField tripulacionField = new JTextField();
	    JTextField salidaField = new JTextField();
	    JTextField llegadaField = new JTextField();
	    SpinnerDateModel salidaModel = new SpinnerDateModel();
	    JSpinner salidaSpinner = new JSpinner(salidaModel);
	    salidaSpinner.setEditor(new JSpinner.DateEditor(salidaSpinner, "dd/MM/yyyy HH:mm"));
	    SpinnerDateModel llegadaModel = new SpinnerDateModel();
	    JSpinner llegadaSpinner = new JSpinner(llegadaModel);
	    llegadaSpinner.setEditor(new JSpinner.DateEditor(llegadaSpinner, "dd/MM/yyyy HH:mm"));
	    JButton confirmarAdd = new JButton("Confirmar");

	    addForm.add(new JLabel("Código del avión:")); addForm.add(avionField);
	    addForm.add(new JLabel("Cantidad de miembros de la tripulación:")); addForm.add(tripulacionField);
	    addForm.add(new JLabel("Código del aeropuerto de salida:")); addForm.add(salidaField);
	    addForm.add(new JLabel("Código del aeropuerto de llegada:")); addForm.add(llegadaField);
	    addForm.add(new JLabel("Fecha/hora de salida:")); addForm.add(salidaSpinner);
	    addForm.add(new JLabel("Fecha/hora de llegada:")); addForm.add(llegadaSpinner);
	    addForm.add(new JLabel("")); addForm.add(confirmarAdd);

	    formsPanel.add(addForm, "ADD");

	    // Eliminar vuelo
	    JPanel deleteForm = new JPanel(new FlowLayout());
	    JTextField deleteCodigo = new JTextField(10);
	    JButton confirmarDelete = new JButton("Eliminar");
	    deleteForm.add(new JLabel("Código del vuelo:"));
	    deleteForm.add(deleteCodigo);
	    deleteForm.add(confirmarDelete);

	    formsPanel.add(deleteForm, "DELETE");
	    
	    // Buscar vuelo
	    JPanel searchForm = new JPanel(new FlowLayout());
	    JTextField buscarCodigo = new JTextField(10);
	    JButton confirmarBuscar = new JButton("Buscar");
	    searchForm.add(new JLabel("Código del vuelo:"));
	    searchForm.add(buscarCodigo);
	    searchForm.add(confirmarBuscar);

	    formsPanel.add(searchForm, "SEARCH");
	    
	    // Actualizar vuelo
	    JPanel updateForm = new JPanel(new FlowLayout());
	    JTextField actualizarCodigo = new JTextField(10);
	    JButton confirmarActualizar = new JButton("Cancelar o marcar como finalizado");
	    updateForm.add(new JLabel("Código del vuelo:"));
	    updateForm.add(actualizarCodigo);
	    updateForm.add(confirmarActualizar);

	    formsPanel.add(updateForm, "UPDATE");


	    // Initially hide forms
	    ((CardLayout) formsPanel.getLayout()).show(formsPanel, ""); 

	    // --- Action listeners ---
	    addButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "ADD");
	    });

	    deleteButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "DELETE");
	    });
	    
	    searchButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "SEARCH");
	    });
	    
	    updateButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "UPDATE");
	    });

	    confirmarAdd.addActionListener(e -> {
	        try {
	            a.addVuelo(
	                avionField.getText(),
	                Integer.parseInt(tripulacionField.getText()),
	                salidaField.getText(),
	                llegadaField.getText(),
	                (Date) salidaSpinner.getValue(),
	                (Date) llegadaSpinner.getValue()
	            );
	            outputArea.append("Vuelo agregado: " + a.getVuelos()[a.getVuelos().length - 1].getCodigo() + "\n");
	            avionField.setText("");
                tripulacionField.setText("");
                salidaField.setText("");
                llegadaField.setText("");
	        } catch (malDigitadoNulo | aeropuertoNoEncontrado | NumberFormatException | 
	        		empleadosNoDisponibles | avionNoEncontrado | arregloVacio ex) {
	            outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });

	    confirmarDelete.addActionListener(e -> {
	        String codigo = deleteCodigo.getText();
	        try {
	        	a.eliminarVuelo(codigo);
	        	outputArea.append("Vuelo eliminado: " + codigo + "\n");
	        	deleteCodigo.setText("");
	        }
	        catch(arregloVacio | vueloNoEncontrado | malDigitadoNulo | vueloVigente ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    confirmarBuscar.addActionListener(e -> {
	    	String codigo = buscarCodigo.getText();
	    	try {
	    		Vuelo vuelo = a.buscarVuelo(codigo);
	    		outputArea.append("Vuelo encontrado: " + vuelo.toString() + "\n");
	    		buscarCodigo.setText("");
	    	}
	    	catch(arregloVacio | vueloNoEncontrado | malDigitadoNulo ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    confirmarActualizar.addActionListener(e -> {
	    	String codigo = actualizarCodigo.getText();
	    	try {
	    		a.actualizarVuelo(codigo);
	    		outputArea.append("Vuelo actualizado: " + codigo + "\n");
	    	}
	    	catch(arregloVacio | vueloNoEncontrado | malDigitadoNulo ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    showButton.addActionListener(e -> {
	        if (a.getVuelos() == null || a.getVuelos().length == 0) {
	            outputArea.append("No hay vuelos registrados.\n");
	        } else {
	            outputArea.append("Lista de vuelos:\n");
	            for (Vuelo v : a.getVuelos()) {
	                outputArea.append(v.toString());
	                outputArea.append("\n");
	            }
	        }
	    });

	    // --- Assemble panel ---
	    panel.add(actionsPanel, BorderLayout.NORTH);
	    panel.add(formsPanel, BorderLayout.CENTER);

	    return panel;
	}
	
	private JPanel crearPanelReservaciones() {
		JPanel panel = new JPanel(new BorderLayout());

	    // --- Top: action buttons ---
	    JPanel actionsPanel = new JPanel(new FlowLayout());
	    JButton addButton = new JButton("Agregar Reservación");
	    JButton deleteButton = new JButton("Eliminar Reservación");
	    JButton searchButton = new JButton("Buscar Reservación");
	    JButton showButton = new JButton("Mostrar Reservaciones");
	    actionsPanel.add(addButton);
	    actionsPanel.add(deleteButton);
	    actionsPanel.add(searchButton);
	    actionsPanel.add(showButton);

	    // --- Center: forms (hidden until needed) ---
	    JPanel formsPanel = new JPanel(new CardLayout());

	    // Añadir reservación
	    JPanel addForm = new JPanel(new GridLayout(5, 2, 5, 5));
	    JTextField clientesField = new JTextField();
	    JTextField vueloField = new JTextField();
	    JTextField precioField = new JTextField();
	    JButton confirmarAdd = new JButton("Confirmar");

	    addForm.add(new JLabel("Cantidad de clientes:")); addForm.add(clientesField);
	    addForm.add(new JLabel("Código del vuelo:")); addForm.add(vueloField);
	    addForm.add(new JLabel("Precio:")); addForm.add(precioField);
	    addForm.add(new JLabel("")); addForm.add(confirmarAdd);

	    formsPanel.add(addForm, "ADD");

	    // Eliminar reservación
	    JPanel deleteForm = new JPanel(new FlowLayout());
	    JTextField deleteCodigo = new JTextField(10);
	    JButton confirmarDelete = new JButton("Eliminar");
	    deleteForm.add(new JLabel("Código de la reservación:"));
	    deleteForm.add(deleteCodigo);
	    deleteForm.add(confirmarDelete);

	    formsPanel.add(deleteForm, "DELETE");
	    
	    // Buscar reservación
	    JPanel searchForm = new JPanel(new FlowLayout());
	    JTextField buscarCodigo = new JTextField(10);
	    JButton confirmarBuscar = new JButton("Buscar");
	    searchForm.add(new JLabel("Código de la reservación:"));
	    searchForm.add(buscarCodigo);
	    searchForm.add(confirmarBuscar);

	    formsPanel.add(searchForm, "SEARCH");

	    // Initially hide forms
	    ((CardLayout) formsPanel.getLayout()).show(formsPanel, ""); 

	    // --- Action listeners ---
	    addButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "ADD");
	    });

	    deleteButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "DELETE");
	    });
	    
	    searchButton.addActionListener(e -> {
	        ((CardLayout) formsPanel.getLayout()).show(formsPanel, "SEARCH");
	    });

	    confirmarAdd.addActionListener(e -> {
	        try {
	            int numClientes = Integer.parseInt(clientesField.getText());
	            Cliente[] clientes = new Cliente[numClientes];

	            for (int i = 0; i < numClientes; i++) {
	                // Use combo box style dialog for tipoDoc
	                String tipoDocSeleccionado = (String) JOptionPane.showInputDialog(
	                        null,
	                        "Cliente " + (i + 1) + " - Tipo de documento:",
	                        "Seleccionar tipo de documento",
	                        JOptionPane.QUESTION_MESSAGE,
	                        null,
	                        tipoDoc,   // array of options
	                        tipoDoc[0] // default option
	                );

	                String numDoc = JOptionPane.showInputDialog(
	                        "Cliente " + (i + 1) + " - Número de documento:"
	                );

	                clientes[i] = a.buscarCliente(tipoDocSeleccionado, numDoc);
	            }

	            a.addReservacion(
	            		clientes, 
	            		vueloField.getText(), 
	            		Long.parseLong(precioField.getText())
	            );
	            outputArea.append("Reservación agregada: " 
	            			+ a.getReservaciones()[a.getReservaciones().length - 1].getCodigo() + "\n");
	            
	            clientesField.setText("");
	            vueloField.setText("");
	            precioField.setText("");

	        } catch (malDigitadoNulo | NumberFormatException | vueloNoEncontrado | vueloLleno | arregloVacio | personaNoEncontrada ex) {
	            outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });

	    confirmarDelete.addActionListener(e -> {
	        String codigo = deleteCodigo.getText();
	        try {
	        	a.eliminarReservacion(codigo);
	        	outputArea.append("Reservación eliminada: " + codigo + "\n");
	        	deleteCodigo.setText("");
	        }
	        catch(arregloVacio | reservaNoEncontrada ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    confirmarBuscar.addActionListener(e -> {
	    	String codigo = buscarCodigo.getText();
	    	try {
	    		Reservacion reservacion = a.buscarReservacion(codigo);
	    		outputArea.append("Reservación encontrada: " + reservacion.toString() + "\n");
	    		buscarCodigo.setText("");
	    	}
	    	catch(arregloVacio | reservaNoEncontrada ex) {
	        	outputArea.append("Error: " + ex.getMessage() + "\n");
	        }
	    });
	    
	    showButton.addActionListener(e -> {
	        if (a.getReservaciones() == null || a.getReservaciones().length == 0) {
	            outputArea.append("No hay reservaciones registradas.\n");
	        } else {
	            outputArea.append("Lista de reservaciones:\n");
	            for (Reservacion r : a.getReservaciones()) {
	                outputArea.append(r.toString());
	                outputArea.append("\n");
	            }
	        }
	    });

	    // --- Assemble panel ---
	    panel.add(actionsPanel, BorderLayout.NORTH);
	    panel.add(formsPanel, BorderLayout.CENTER);

	    return panel;
	}
}
