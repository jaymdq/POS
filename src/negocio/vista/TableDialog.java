package negocio.vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;

import database.DataBaseManager;
import negocio.logica.Producto;
import negocio.logica.app.MainApp;
import negocio.vista.filtros.Size;

public class TableDialog extends JDialog {

	// Variables

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private WebTable table;
	private JButton eliminarProductoButton;
	private JButton modificarProductoButton;
	private JButton agregarProductoButton;
	private MainApp mainApp;

	// Constructors

	public TableDialog(MainApp mainApp) {
		this.mainApp = mainApp;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Productos");
		setBounds(100, 100, Size.getInstance().getMonitorWidth() - 20, Size.getInstance().getMonitorHeight() - 200);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			// Table
			table = new WebTable ( new ProductsTableModel() );
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setEditable(false);
			table.setRowHeight(40);
			table.setFillsViewportHeight(true);
			table.setFontSize(Size.getInstance().getValue(24));
			table.setFont(new Font("Tahoma", Font.PLAIN, Size.getInstance().getValue(24)));
			
			table.getColumnModel().getColumn(0).setPreferredWidth(Size.getInstance().getValue(175));
			table.getColumnModel().getColumn(0).setMaxWidth(Size.getInstance().getValue(175));
			table.getColumnModel().getColumn(2).setPreferredWidth(Size.getInstance().getValue(250));
			table.getColumnModel().getColumn(2).setMaxWidth(Size.getInstance().getValue(250));
			table.getColumnModel().getColumn(3).setPreferredWidth(Size.getInstance().getValue(125));
			table.getColumnModel().getColumn(3).setMaxWidth(Size.getInstance().getValue(125));
			table.getColumnModel().getColumn(4).setPreferredWidth(Size.getInstance().getValue(125));
			table.getColumnModel().getColumn(4).setMaxWidth(Size.getInstance().getValue(125));
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( JLabel.CENTER );
			table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
			
			WebScrollPane scrollPane = new WebScrollPane ( table );

			contentPanel.setLayout(new BorderLayout(0, 0));
			contentPanel.add(scrollPane);

			// Better column sizes
			//initColumnSizes ( table );

		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				agregarProductoButton = new JButton("Agregar Producto");
				agregarProductoButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						agregarProducto();
					}
				});
				agregarProductoButton.setFont(new Font("Tahoma", Font.BOLD, 16));
				buttonPane.add(agregarProductoButton);
				getRootPane().setDefaultButton(agregarProductoButton);
			}
			{
				modificarProductoButton = new JButton("Modificar Producto");
				modificarProductoButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						modificarProducto();
					}
				});
				modificarProductoButton.setFont(new Font("Tahoma", Font.BOLD, 16));
				buttonPane.add(modificarProductoButton);
			}
			{
				eliminarProductoButton = new JButton("Eliminar Producto");
				eliminarProductoButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						eliminarProducto();
					}
				});
				eliminarProductoButton.setFont(new Font("Tahoma", Font.BOLD, 16));
				buttonPane.add(eliminarProductoButton);
			}
		}
	}

	private void initColumnSizes ( JTable table )
	{
		ProductsTableModel model = ( ProductsTableModel ) table.getModel ();
		TableColumn column;
		Component comp;
		int headerWidth;
		int cellWidth;
		Object[] longValues = model.longValues;
		TableCellRenderer headerRenderer = table.getTableHeader ().getDefaultRenderer ();

		for ( int i = 0; i < model.getColumnCount (); i++ )
		{
			column = table.getColumnModel ().getColumn ( i );

			comp = headerRenderer.getTableCellRendererComponent ( null, column.getHeaderValue (), false, false, 0, 0 );
			headerWidth = comp.getPreferredSize ().width;

			comp = table.getDefaultRenderer ( model.getColumnClass ( i ) ).
					getTableCellRendererComponent ( table, longValues[ i ], false, false, 0, i );
			cellWidth = comp.getPreferredSize ().width;

			column.setPreferredWidth ( Math.max ( headerWidth, cellWidth ) );

		}
	}

	protected void agregarProducto() {
		AgregarProductoDialog dialog = new AgregarProductoDialog(this);
		dialog.setVisible(true);		
	}

	protected void modificarProducto() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow > -1){
			ModificarProductoDialog dialog = new ModificarProductoDialog(this, selectedRow);
			dialog.setVisible(true);	
		}
	}

	private void eliminarProducto(){
		int selectedRow = table.getSelectedRow();
		if (selectedRow > -1){
			ArrayList<Producto> productos = DataBaseManager.getInstancia().getProductosList();
			Integer productoID = DataBaseManager.getInstancia().getProductoIDByAppearence(selectedRow);
			Producto producto = productos.get(selectedRow);

			Object[] options = { "Si", "No" };
			int result = JOptionPane.showOptionDialog(null, "¿Estas seguro que querés borrar este producto??", "Importante", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
			if (result == JOptionPane.YES_OPTION){
				boolean resultado = DataBaseManager.getInstancia().deleteProducto(productoID, producto);
				refreshTable();
				if (resultado){

					// Se avisa que se elimino bien
					final WebNotification notificationPopup = new WebNotification ();
					notificationPopup.setIcon ( NotificationIcon.minus.getIcon() );
					notificationPopup.setDisplayTime ( 5000 );
					notificationPopup.setContent("Producto Eliminado");    
					NotificationManager.showNotification ( notificationPopup );
				}
			}

		}
	}

	public void refreshTable(){
		// se actualiza la tabla
		table.setModel( new ProductsTableModel() );
		table.getColumnModel().getColumn(0).setPreferredWidth(Size.getInstance().getValue(175));
		table.getColumnModel().getColumn(0).setMaxWidth(Size.getInstance().getValue(175));
		table.getColumnModel().getColumn(2).setPreferredWidth(Size.getInstance().getValue(250));
		table.getColumnModel().getColumn(2).setMaxWidth(Size.getInstance().getValue(250));
		table.getColumnModel().getColumn(3).setPreferredWidth(Size.getInstance().getValue(125));
		table.getColumnModel().getColumn(3).setMaxWidth(Size.getInstance().getValue(125));
		table.getColumnModel().getColumn(4).setPreferredWidth(Size.getInstance().getValue(125));
		table.getColumnModel().getColumn(4).setMaxWidth(Size.getInstance().getValue(125));
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		
		this.mainApp.refreshData();
	}

}
