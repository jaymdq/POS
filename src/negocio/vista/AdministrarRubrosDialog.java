package negocio.vista;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import database.DataBaseManager;
import negocio.vista.filtros.UppercaseDocumentFilter;

public class AdministrarRubrosDialog extends JDialog {

	// Variables

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField rubroTxt;
	private JTable table;
	private ArrayList<String> rubros;
	private JButton btnAgregar;
	private JButton btnEliminar;

	// Constructors

	public AdministrarRubrosDialog() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setTitle("Administrar Rubros");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
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
				RowSpec.decode("8dlu"),
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,}));
		{
			JLabel lblRubro = new JLabel("Rubro");
			lblRubro.setFont(new Font("Tahoma", Font.BOLD, 18));
			contentPanel.add(lblRubro, "2, 2, left, default");
		}
		{
			rubroTxt = new JTextField();
			rubroTxt.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					manageButtons();
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					manageButtons();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
				}
			});
			rubroTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
			contentPanel.add(rubroTxt, "4, 2, 5, 1, fill, default");
			rubroTxt.setColumns(10);
			((AbstractDocument) rubroTxt.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
		}

		{
			btnAgregar = new JButton("Agregar");
			btnAgregar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addRubroToDataBase();
				}
			});
			btnAgregar.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPanel.add(btnAgregar, "4, 4");
		}
		{
			btnEliminar = new JButton("Eliminar");
			btnEliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteRubro();
				}
			});
			btnEliminar.setEnabled(false);
			btnEliminar.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPanel.add(btnEliminar, "6, 4");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "2, 6, 7, 1, fill, fill");
			table = new JTable();
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					moveRubroToTxtField();
				}
			});
			table.setFont(new Font("Tahoma", Font.PLAIN, 16));
			table.setFillsViewportHeight(true);
			scrollPane.setViewportView(table);
			table.setBorder(new LineBorder(new Color(0, 0, 0)));
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setModel(new DefaultTableModel(
					new Object[][] {
						{new Integer(1), "CIGARRILLOS"},
					},
					new String[] {
							"ID", "RUBRO"
					}
					) {

				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
						Integer.class, String.class
				};
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			table.getColumnModel().getColumn(0).setResizable(false);
			table.getColumnModel().getColumn(0).setMaxWidth(75);
			table.getColumnModel().getColumn(1).setResizable(false);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}

		// Obtenemos los rubros de la base de datos
		getRubrosFromDataBase();
	}
	
	private void moveRubroToTxtField() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0){
			rubroTxt.setText(rubros.get(selectedRow));
		}		
	}

	private void manageButtons() {
		if (rubros.contains(rubroTxt.getText())){
			btnAgregar.setEnabled(false);
			btnEliminar.setEnabled(true);
		}else{
			btnAgregar.setEnabled(true);
			btnEliminar.setEnabled(false);
		}
	}

	private void getRubrosFromDataBase(){

		rubros = DataBaseManager.getInstancia().getRubrosList();

		DefaultTableModel model = new DefaultTableModel(new String[] { "ID", "RUBRO" },0) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
			    return false;
			}
		};
		
		table.setModel(model);
		for (int indice = 0; indice < rubros.size(); indice++) {
			model.addRow(new Object[]{ indice, rubros.get(indice) });
		}

		// Table renderer
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		table.getColumnModel().getColumn(0).setMaxWidth(75);
		table.setRowHeight(30);
	}

	private void addRubroToDataBase(){
		String rubroToAdd = rubroTxt.getText();

		if (rubroToAdd.equals("") || rubros.contains(rubroToAdd))
			return;

		DataBaseManager.getInstancia().insertRubro(rubroToAdd);

		getRubrosFromDataBase();
		manageButtons();
	}

	private void deleteRubro() {

		String rubroToDelete = rubroTxt.getText();

		if (rubroToDelete.equals("") || !rubros.contains(rubroToDelete))
			return;

		rubros.remove(rubroToDelete);

		DataBaseManager.getInstancia().deleteRubro(rubroToDelete);

		getRubrosFromDataBase();
		manageButtons();
	}
}
