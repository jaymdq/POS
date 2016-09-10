package negocio.vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import database.DataBaseManager;
import negocio.logica.Producto;
import negocio.vista.filtros.UppercaseDocumentFilter;

public abstract class AbsOperacionProductoDialog extends AbsDialog{

	// Variables

	protected static final long serialVersionUID = 1L;
	protected final JPanel contentPanel = new JPanel();
	protected JTextField txtCodigo;
	protected JComboBox<String> rubrosComboBox;
	protected JCheckBox check2;
	protected JCheckBox check1;
	protected JCheckBox check3;
	protected JTextField txtDescripcion;
	protected ArrayList<String> rubros;
	protected ArrayList<Producto> productos;
	protected JLabel lblNewLabel;
	protected JButton operacionBtn;
	private JLabel lblCdigo;
	private JLabel lblRubro;
	private JLabel lblDescripcin;
	private JLabel lblPrecioDeCosto;
	private JLabel lblPrecioDeVenta;
	private JPanel buttonPane;
	protected JFormattedTextField txtPrecioCosto;
	protected JFormattedTextField txtPrecioVenta;


	// Constructors

	public AbsOperacionProductoDialog(TableDialog tableDialog) {
		super(tableDialog);
		setFont(new Font("Dialog", Font.PLAIN, 16));
		setResizable(false);
		setTitle("");
		setModal(true);
		setBounds(100, 100, 780, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("100dlu"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("100dlu:grow"),
				ColumnSpec.decode("30dlu"),
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
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC,}));
		{
			lblCdigo = new JLabel("C\u00F3digo");
			lblCdigo.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPanel.add(lblCdigo, "2, 2, left, default");
		}
		{
			txtCodigo = new JTextField();
			txtCodigo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rubrosComboBox.requestFocusInWindow();
				}
			});
			txtCodigo.setFont(new Font("Tahoma", Font.PLAIN, 16));
			contentPanel.add(txtCodigo, "4, 2, 3, 1, fill, default");
			txtCodigo.setColumns(10);
			txtCodigo.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					check1.setSelected(!txtCodigo.getText().isEmpty());
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					check1.setSelected(!txtCodigo.getText().isEmpty());
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
				}
			});

			{
				check1 = new JCheckBox("");
				check1.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						controlarBotonAgregar();
					}
				});
				check1.setEnabled(false);
				contentPanel.add(check1, "7, 2, center, center");
			}
			{
				lblRubro = new JLabel("Rubro");
				lblRubro.setFont(new Font("Tahoma", Font.BOLD, 16));
				contentPanel.add(lblRubro, "2, 4, left, default");
			}
			{
				rubrosComboBox = new JComboBox<String>();
				rubrosComboBox.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						if (e.getKeyChar() == '\n'){
							txtDescripcion.requestFocusInWindow();
						}
					}
				});
				rubrosComboBox.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						check2.setSelected(rubrosComboBox.getSelectedIndex() > 0);
					}
				});
				rubrosComboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
				contentPanel.add(rubrosComboBox, "4, 4, 3, 1, fill, default");
			}
			{
				check2 = new JCheckBox("");
				check2.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						controlarBotonAgregar();
					}
				});
				check2.setEnabled(false);
				contentPanel.add(check2, "7, 4, center, center");
			}
			{
				lblDescripcin = new JLabel("Descripci\u00F3n");
				lblDescripcin.setFont(new Font("Tahoma", Font.BOLD, 16));
				contentPanel.add(lblDescripcin, "2, 6, left, default");
			}
			{
				txtDescripcion = new JTextField();
				txtDescripcion.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						txtPrecioCosto.requestFocusInWindow();
					}
				});
				txtDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 16));
				contentPanel.add(txtDescripcion, "4, 6, 3, 1, fill, default");
				txtDescripcion.setColumns(10);
				((AbstractDocument) txtDescripcion.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
				txtDescripcion.getDocument().addDocumentListener(new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						check3.setSelected(!txtDescripcion.getText().isEmpty());
					}
					@Override
					public void removeUpdate(DocumentEvent e) {
						check3.setSelected(!txtDescripcion.getText().isEmpty());
					}
					@Override
					public void changedUpdate(DocumentEvent e) {
					}
				});
			}
			{
				check3 = new JCheckBox("");
				check3.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						controlarBotonAgregar();
					}
				});
				check3.setEnabled(false);
				contentPanel.add(check3, "7, 6, center, center");
			}
			{
				lblPrecioDeCosto = new JLabel("Precio de Costo");
				lblPrecioDeCosto.setFont(new Font("Tahoma", Font.BOLD, 16));
				contentPanel.add(lblPrecioDeCosto, "2, 8, right, default");
			}
			{
				txtPrecioCosto = new JFormattedTextField( NumberFormat.getNumberInstance(Locale.US) );
				txtPrecioCosto.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								txtPrecioCosto.selectAll();
							}
						});
					}
				});
				txtPrecioCosto.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						txtPrecioVenta.requestFocusInWindow();
					}
				});
				txtPrecioCosto.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						char ch = e.getKeyChar();
						String txt = txtPrecioCosto.getText();
						if (!isNumber(ch) && !isValidSignal(ch, txt) && !validatePoint(ch,txt)  && ch != '\b') {
							e.consume();
						}
					}

					private boolean isNumber(char ch){
						return ch >= '0' && ch <= '9';
					}

					private boolean isValidSignal(char ch, String txt){
						if( (txt == null || "".equals(txt.trim()) ) && ch == '-'){
							return true;
						}

						return false;
					}

					private boolean validatePoint(char ch, String txt){
						if(ch != '.' || txt.contains(".")){
							return false;
						}

						if(txt == null || "".equals(txt.trim())){
							txtPrecioCosto.setText("0.");
							return false;
						}else if("-".equals(txt)){
							txtPrecioCosto.setText("-0");
						}

						return true;
					}

				});
				txtPrecioCosto.setHorizontalAlignment(SwingConstants.RIGHT);
				txtPrecioCosto.setValue(new Double("0.00"));
				txtPrecioCosto.setColumns(10);
				txtPrecioCosto.setFont(new Font("Tahoma", Font.PLAIN, 16));
				contentPanel.add(txtPrecioCosto, "4, 8, fill, default");
			}
			{
				lblNewLabel = new JLabel("( Este Item es OPCIONAL )");
				lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 16));
				contentPanel.add(lblNewLabel, "6, 8");
			}
			{
				lblPrecioDeVenta = new JLabel("Precio de Venta");
				lblPrecioDeVenta.setFont(new Font("Tahoma", Font.BOLD, 16));
				contentPanel.add(lblPrecioDeVenta, "2, 10, right, default");
			}
			{
				//FIXME sacar la coma de mierda
				txtPrecioVenta = new JFormattedTextField( NumberFormat.getNumberInstance(Locale.US) );
				txtPrecioVenta.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								txtPrecioVenta.selectAll();
							}
						});
					}
				});
				txtPrecioVenta.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						operacionBtn.requestFocusInWindow();
					}
				});
				txtPrecioVenta.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						char ch = e.getKeyChar();
						String txt = txtPrecioVenta.getText();
						if (!isNumber(ch) && !isValidSignal(ch, txt) && !validatePoint(ch,txt)  && ch != '\b') {
							e.consume();
						}
					}

					private boolean isNumber(char ch){
						return ch >= '0' && ch <= '9';
					}

					private boolean isValidSignal(char ch, String txt){
						if( (txt == null || "".equals(txt.trim()) ) && ch == '-'){
							return true;
						}

						return false;
					}

					private boolean validatePoint(char ch, String txt){
						if(ch != '.' || txt.contains(".")){
							return false;
						}

						if(txt == null || "".equals(txt.trim())){
							txtPrecioVenta.setText("0.");
							return false;
						}else if("-".equals(txt)){
							txtPrecioVenta.setText("-0");
						}

						return true;
					}

				});
				txtPrecioVenta.setHorizontalAlignment(SwingConstants.RIGHT);
				txtPrecioVenta.setValue(new Double("0.00"));
				txtPrecioVenta.setColumns(10);
				txtPrecioVenta.setFont(new Font("Tahoma", Font.PLAIN, 16));
				contentPanel.add(txtPrecioVenta, "4, 10, fill, default");
			}
			contentPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtCodigo, rubrosComboBox, txtDescripcion}));
			{
				buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				{
					operacionBtn = new JButton("");
					operacionBtn.setEnabled(false);
					operacionBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
					buttonPane.add(operacionBtn);
					getRootPane().setDefaultButton(operacionBtn);
				}
			}	
		}
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtCodigo, rubrosComboBox, txtDescripcion, txtPrecioCosto, txtPrecioVenta, operacionBtn}));

		// Obtener los rubros disponibles y los productos disponibles
		getRubrosFromDataBase();
		getProductosFromDataBase();
		establecerOperacion();
	}

	// Getters and Setters

	// Methods

	protected abstract void establecerOperacion();

	// REV 1.0
	protected void getRubrosFromDataBase() {
		rubros = DataBaseManager.getInstancia().getRubrosList();

		rubrosComboBox.addItem("Seleccionar Rubro");

		for (String rubro : rubros)
			rubrosComboBox.addItem(rubro);
	}

	// REV 2.0
	protected void getProductosFromDataBase() {
		productos = DataBaseManager.getInstancia().getProductosList();
	}

	// REV 1.0
	protected void chequearQueElCodigoNoExista() {
		String codigo = txtCodigo.getText();

		for (Producto producto : productos){
			if (producto.getCodigo().equals(codigo)){
				check1.setSelected(false);
				break;
			}
		}
	}

	// REV 1.0
	protected void controlarBotonAgregar() {
		if (check1.isSelected() && check2.isSelected() && check3.isSelected())
			operacionBtn.setEnabled(true);
		else
			operacionBtn.setEnabled(false);

	}

}
