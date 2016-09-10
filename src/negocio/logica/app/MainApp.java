package negocio.logica.app;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import database.DataBaseManager;
import negocio.logica.Item;
import negocio.logica.Producto;
import negocio.logica.Venta;
import negocio.vista.AdministrarRubrosDialog;
import negocio.vista.AutoCompletion;
import negocio.vista.ButtonColumn;
import negocio.vista.CambioDialog;
import negocio.vista.RestaDialog;
import negocio.vista.SumaDialog;
import negocio.vista.TableDialog;
import negocio.vista.VentasTableModel;
import negocio.vista.filtros.Size;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BoxLayout;

public class MainApp {

	private JFrame frmTotosVersin;
	private JTabbedPane tabbedPane;
	private JComboBox<String> productosComboBox;
	public ArrayList<Producto> productos;
	public ArrayList<String> rubros;
	private JLabel labelDescripcionProducto;
	private JLabel labelRubroProducto;
	private JLabel labelPrecioProducto;
	private JComboBox<String> productosComboBoxVenta;
	private WebTable tablaVenta;
	public Venta venta = new Venta();
	private JLabel lblTotalVenta;
	public boolean terminaVenta = false;
	public JLabel lblTotalCambio;
	private CambioDialog cambioDialog = new CambioDialog(this);
	private SumaDialog sumaDialog = new SumaDialog(this);
	private RestaDialog restaDialog = new RestaDialog(this);
	private JButton btnNuevaVenta;
	private ButtonColumn buttonColumn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					// Se instala el Look and Feel
					WebLookAndFeel.install();

					MainApp window = new MainApp();
					window.frmTotosVersin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainApp() {	

		initialize();
		iniciarBaseDeDatos();
		refreshData();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTotosVersin = new JFrame();
		frmTotosVersin.setIconImage(Toolkit.getDefaultToolkit().getImage(MainApp.class.getResource("/resources/negocio_127x127.png")));
		frmTotosVersin.setTitle("Toto's    -    Versi\u00F3n 1.0");
		frmTotosVersin.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frmTotosVersin.setBounds(100, 100, 1024, 700);
		frmTotosVersin.setMinimumSize(new Dimension(760,560));
		frmTotosVersin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTotosVersin.setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		frmTotosVersin.setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		mnArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuBar.add(mnArchivo);

		JMenuItem mntmImportarBaseDe = new JMenuItem("Importar Base de Datos");
		mntmImportarBaseDe.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnArchivo.add(mntmImportarBaseDe);

		JMenuItem mntmExportarBaseDe = new JMenuItem("Exportar Base de Datos");
		mntmExportarBaseDe.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mntmExportarBaseDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DataBaseManager.getInstancia().exportData("./export.csv");
			}
		});
		mnArchivo.add(mntmExportarBaseDe);

		JSeparator separator = new JSeparator();
		mnArchivo.add(separator);

		JMenuItem mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mntmSalir.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnArchivo.add(mntmSalir);

		JMenu mnConfiguracin = new JMenu("Configuraci\u00F3n");
		mnConfiguracin.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuBar.add(mnConfiguracin);

		JMenuItem mntmAdministrarRubros = new JMenuItem("Administrar Rubros");
		mntmAdministrarRubros.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mntmAdministrarRubros.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmAdministrarRubros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				administrarRubros();
			}
		});
		mnConfiguracin.add(mntmAdministrarRubros);

		JSeparator separator_1 = new JSeparator();
		mnConfiguracin.add(separator_1);

		JMenuItem mntmAdministrarProductos = new JMenuItem("Administrar Productos");
		mntmAdministrarProductos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mntmAdministrarProductos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mntmAdministrarProductos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				administrarProductos();
			}
		});
		mnConfiguracin.add(mntmAdministrarProductos);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (productosComboBox != null && tabbedPane.getSelectedIndex() == 0)
					productosComboBox.requestFocusInWindow();
				if (productosComboBoxVenta != null && tabbedPane.getSelectedIndex() == 1)
					productosComboBoxVenta.requestFocusInWindow();
			}
		});
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		frmTotosVersin.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panelPrecios = new JPanel();
		panelPrecios.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Precios [F3]", null, panelPrecios, null);
		panelPrecios.setLayout(new BorderLayout(0, 0));

		JPanel precioPanel = new JPanel();
		precioPanel.setBorder(new LineBorder(new Color(0, 0, 0), 4));
		precioPanel.setBackground(SystemColor.menu);
		precioPanel.setForeground(SystemColor.menu);
		panelPrecios.add(precioPanel, BorderLayout.SOUTH);

		labelPrecioProducto = new JLabel("$");
		labelPrecioProducto.setForeground(Color.BLACK);
		labelPrecioProducto.setFont(new Font("Tahoma", Font.BOLD, 98));
		precioPanel.add(labelPrecioProducto);

		JPanel panel = new JPanel();
		panelPrecios.add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("50dlu"),
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("50dlu"),
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,}));

		JLabel lblNewLabel_1 = new JLabel("Buscar");
		panel.add(lblNewLabel_1, "2, 2, center, center");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, Size.getInstance().getValue(34)));

		productosComboBox = new JComboBox<String>();
		productosComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateVista();
			}
		});
		panel.add(productosComboBox, "4, 2, fill, center");
		productosComboBox.setFont(new Font("Tahoma", Font.PLAIN, 22));
		AutoCompletion.enable(productosComboBox);

		JSeparator separator_2 = new JSeparator();
		panel.add(separator_2, "1, 4, 4, 1");

		JLabel lblProducto = new JLabel("Producto");
		lblProducto.setHorizontalAlignment(SwingConstants.CENTER);
		lblProducto.setFont(new Font("Tahoma", Font.BOLD, Size.getInstance().getValue(48)));
		panel.add(lblProducto, "2, 6, 3, 1, center, default");

		JSeparator separator_3 = new JSeparator();
		panel.add(separator_3, "1, 8, 4, 1");

		labelDescripcionProducto = new JLabel("");
		labelDescripcionProducto.setHorizontalAlignment(SwingConstants.CENTER);
		labelDescripcionProducto.setFont(new Font("Tahoma", Font.PLAIN, Size.getInstance().getValue(36)));
		panel.add(labelDescripcionProducto, "2, 10, 3, 1, center, center");

		JSeparator separator_4 = new JSeparator();
		panel.add(separator_4, "1, 12, 4, 1");

		JLabel lblNewLabel_2 = new JLabel("Rubro");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, Size.getInstance().getValue(48)));
		panel.add(lblNewLabel_2, "2, 14, 3, 1, center, default");

		JSeparator separator_6 = new JSeparator();
		panel.add(separator_6, "1, 16, 4, 1");

		labelRubroProducto = new JLabel("");
		labelRubroProducto.setHorizontalAlignment(SwingConstants.CENTER);
		labelRubroProducto.setFont(new Font("Tahoma", Font.PLAIN, Size.getInstance().getValue(36)));
		panel.add(labelRubroProducto, "2, 18, 3, 1, center, center");

		JSeparator separator_5 = new JSeparator();
		panel.add(separator_5, "2, 20, 3, 1");

		JPanel panelVentas = new JPanel();
		tabbedPane.addTab("Ventas [F4]", null, panelVentas, null);
		panelVentas.setLayout(new BorderLayout(0, 0));

		JPanel panelBuscar = new JPanel();
		panelVentas.add(panelBuscar, BorderLayout.NORTH);
		panelBuscar.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
						FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:default"),
						FormSpecs.RELATED_GAP_ROWSPEC,}));

		JLabel lblBuscar = new JLabel("Buscar");
		lblBuscar.setFont(new Font("Tahoma", Font.BOLD, Size.getInstance().getValue(34)));
		panelBuscar.add(lblBuscar, "2, 2, center, center");

		productosComboBoxVenta = new JComboBox<String>();
		productosComboBoxVenta.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {


			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					if (productosComboBoxVenta.getSelectedIndex() > 0){
						if (terminaVenta){
							terminaVenta = false;
							limpiarVenta();
						}
						agregarProductoVenta();
					}else
						if (!terminaVenta)
							terminaVenta = true;
						else{
							cambioDialog.setVisible(true);
						}
				}

				if (e.getKeyChar() == '+'){
					// +
					// Mostrar dialogo que permita especificar el monto a agregar 
					sumaDialog.setVisible(true);
				}

				if (e.getKeyChar() == '-'){
					// -
					// Mostrar dialogo que permita especificar el monto a restar 
					restaDialog.setVisible(true);
				}
			}
		});

		productosComboBoxVenta.setFont(new Font("Tahoma", Font.PLAIN, 22));
		productosComboBoxVenta.setModel(productosComboBox.getModel());
		AutoCompletion.enable(productosComboBoxVenta);
		panelBuscar.add(productosComboBoxVenta, "4, 2, fill, center");

		JPanel panelCentral = new JPanel();
		panelVentas.add(panelCentral, BorderLayout.CENTER);

		Action delete = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				WebTable table = (WebTable) e.getSource();

				int modelRow = Integer.valueOf( e.getActionCommand() );
				((VentasTableModel)table.getModel()).removeRow(modelRow);
			}
		};


		// Table
		tablaVenta = new WebTable ( new VentasTableModel(venta) );
		buttonColumn = new ButtonColumn(tablaVenta, delete, 0);
		tablaVenta.setEditable(true);
		tablaVenta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				//TODO MEJORAR ESTE SISTEMA

				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					productosComboBoxVenta.requestFocusInWindow();	
				}
				e.consume();
			}			
		});

		// Columna del boton de borrar
		tablaVenta.getColumnModel().getColumn(0).setMaxWidth(50);
		tablaVenta.getColumnModel().getColumn(0).setMaxWidth(50);

		// Columna del producto
		//tablaVenta.getColumnModel().getColumn(1).setMaxWidth(150);
		//tablaVenta.getColumnModel().getColumn(1).setPreferredWidth(150);

		// Columna de cantidad
		tablaVenta.getColumnModel().getColumn(2).setMaxWidth(Size.getInstance().getValue(150));
		tablaVenta.getColumnModel().getColumn(2).setPreferredWidth(Size.getInstance().getValue(150));

		// Columna de precio
		tablaVenta.getColumnModel().getColumn(3).setMaxWidth(Size.getInstance().getValue(150));
		tablaVenta.getColumnModel().getColumn(3).setPreferredWidth(Size.getInstance().getValue(150));

		// Columna del total
		tablaVenta.getColumnModel().getColumn(4).setMaxWidth(Size.getInstance().getValue(150));
		tablaVenta.getColumnModel().getColumn(4).setPreferredWidth(Size.getInstance().getValue(150));

		tablaVenta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaVenta.setRowHeight(40);
		tablaVenta.setFillsViewportHeight(true);
		tablaVenta.setFontSize(24);
		tablaVenta.setFont(new Font("Tahoma", Font.PLAIN, 24));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tablaVenta.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		tablaVenta.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		tablaVenta.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );		
		panelCentral.setLayout(new BorderLayout(0, 0));
		WebScrollPane scrollPane = new WebScrollPane ( tablaVenta );

		panelCentral.add(scrollPane);

		JPanel panel_1 = new JPanel();
		panelVentas.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel lblTotal = new JLabel("TOTAL $ ");
		lblTotal.setFont(new Font("Tahoma", Font.BOLD, Size.getInstance().getValue(36)));
		panel_2.add(lblTotal, BorderLayout.CENTER);

		lblTotalVenta = new JLabel("0.00");
		lblTotalVenta.setPreferredSize(new Dimension(200, 45));
		lblTotalVenta.setMinimumSize(new Dimension(100, 14));
		lblTotalVenta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalVenta.setFont(new Font("Tahoma", Font.BOLD, Size.getInstance().getValue(36)));
		panel_2.add(lblTotalVenta, BorderLayout.EAST);

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.WEST);

		JLabel lblCambio = new JLabel("CAMBIO $");
		lblCambio.setFont(new Font("Tahoma", Font.BOLD, Size.getInstance().getValue(36)));
		panel_3.add(lblCambio);

		lblTotalCambio = new JLabel("0.00");
		lblTotalCambio.setFont(new Font("Tahoma", Font.BOLD, Size.getInstance().getValue(36)));
		panel_3.add(lblTotalCambio);

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnNuevaVenta = new JButton("Nueva Venta [F10]");
		btnNuevaVenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiarVenta();
			}
		});
		btnNuevaVenta.setFont(new Font("Tahoma", Font.BOLD, Size.getInstance().getValue(24)));
		panel_4.add(btnNuevaVenta);
		tabbedPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{productosComboBox}));
		frmTotosVersin.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{productosComboBox}));


		//Cambio de ventanas------------------------------------------------------------
		Action cambiarAPreciosAction = new AbstractAction("cambiarAPrecios") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				tabbedPane.setSelectedIndex(0);
			}
		};

		Action cambiarAVentasAction = new AbstractAction("cambiarAVentas") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				tabbedPane.setSelectedIndex(1);
				productosComboBoxVenta.requestFocusInWindow();
			}
		};

		Action nuevaVentaAction = new AbstractAction("nuevaVenta") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				if (tabbedPane.getSelectedIndex() == 1){
					limpiarVenta();
					productosComboBoxVenta.requestFocusInWindow();
				}
			}
		};

		String keyF3 = "cambiarAPrecios"; 
		tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), keyF3);
		tabbedPane.getActionMap().put(keyF3, cambiarAPreciosAction);

		String keyF4 = "cambiarAVentas"; 
		tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), keyF4);
		tabbedPane.getActionMap().put(keyF4, cambiarAVentasAction);

		String keyF10 = "nuevaVenta"; 
		btnNuevaVenta.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), keyF10);
		btnNuevaVenta.getActionMap().put(keyF10, nuevaVentaAction);

	}

	protected void limpiarVenta() {
		venta =((VentasTableModel) tablaVenta.getModel()).clear();
		lblTotalVenta.setText("0.00");
		lblTotalCambio.setText("0.00");
	}

	private void administrarProductos() {
		TableDialog dialog = new TableDialog(this);
		dialog.setVisible(true);			
	}

	private void administrarRubros() {
		AdministrarRubrosDialog dialog = new AdministrarRubrosDialog();
		dialog.setVisible(true);	
	}

	private void iniciarBaseDeDatos(){
		// Se inicia la base de datos
		DataBaseManager.getInstancia().startDataBase();

	}

	private void updateVista() {

		if (productosComboBox != null){
			int selectedIndex = productosComboBox.getSelectedIndex();
			if (selectedIndex > 0){
				Producto producto = productos.get(selectedIndex - 1);
				labelDescripcionProducto.setText(producto.getDescripcion());
				labelRubroProducto.setText(DataBaseManager.getInstancia().getRubroByID(producto.getRubro()));
				labelPrecioProducto.setText("$" + producto.getPrecioVenta().toString());
			}else{
				labelDescripcionProducto.setText("");
				labelRubroProducto.setText("");
				labelPrecioProducto.setText("$");
			}
		}
	}

	public void refreshData(){
		productos = DataBaseManager.getInstancia().getProductosList();
		rubros = DataBaseManager.getInstancia().getRubrosList();

		// Update combobox

		productosComboBox.removeAllItems();
		productosComboBox.addItem("");

		for (Producto producto : productos){
			productosComboBox.addItem(producto.toString());
		}
	}

	private void agregarProductoVenta() {

		if (productosComboBox != null){
			int selectedIndex = productosComboBox.getSelectedIndex();
			if (selectedIndex > 0){
				Producto producto = productos.get(selectedIndex - 1);
				venta.addToVenta(new Item(producto,1));

				actualizarPantallaVenta();
			}
		}

	}

	public void agregarMontoALaVenta(Double monto) {

		Producto productoSinCategorizar = new Producto("VARIOS", "-", -1, 0.0, monto);
		Item item = new Item(productoSinCategorizar,1);
		venta.addToVenta(item);

		actualizarPantallaVenta();
	}

	public void restarMontoALaVenta(Double monto) {
		if (monto == 0.0)
			return;
		if (monto > 0)
			monto = -1 * monto;
		Producto productoSinCategorizar = new Producto("RESTA PRODUCTO", "-", -1, 0.0, monto);
		Item item = new Item(productoSinCategorizar,1);
		venta.addToVenta(item);

		actualizarPantallaVenta();

	}



	private void actualizarPantallaVenta(){

		// Se actualiza el total
		lblTotalVenta.setText(String.format("%.2f", venta.getTotal()));

		// Se actualiza la tabla
		tablaVenta.repaint();

		// Borramos el comboBox
		if (productosComboBoxVenta != null){
			productosComboBoxVenta.setSelectedIndex(0);
		}
	}


}
