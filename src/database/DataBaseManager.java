package database;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import negocio.logica.Producto;

public class DataBaseManager {

	// Variables

	private static DataBaseManager instance = null;
	private Connection connection;
	private String dataBaseName;
	private ArrayList<String> rubros;
	private ArrayList<Producto> productos;
	private ArrayList<Integer> rubrosID;
	private ArrayList<Integer> productosID;

	// Constructors

	private DataBaseManager(){
		this.dataBaseName = "negocio.db";
		this.connection = null;
		this.rubros = new ArrayList<String>();
		this.productos = new ArrayList<Producto>();
		this.rubrosID = new ArrayList<Integer>();
		this.productosID = new ArrayList<Integer>();
	}

	// Getters and Setters

	public static DataBaseManager getInstancia(){
		if (instance == null){
			instance = new DataBaseManager();
		}
		return instance;
	}

	public String getDataBaseName() {
		return dataBaseName;
	}

	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	// Methods

	// REV. 1.0
	public boolean openConnection(){
		boolean out = false;
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.dataBaseName);
			out = true;
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return out;
	}

	// REV. 1.0
	public void closeConnection(){
		try {
			if (this.connection != null && ! this.connection.isClosed())
				this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// REV. 1.0
	public void startDataBase(){

		this.openConnection();
		Statement statement = null;

		try {

			// PRODUCTO  PRIMARY KEY
			statement = connection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS producto " + 
					"(id 			INTEGER PRIMARY KEY," +
					"codigo			CHAR(40)		NOT NULL," +
					"descripcion	TEXT			NOT NULL," +
					"rubro			INT				NOT NULL," +
					"precioCompra	REAL," +
					"precioVenta	REAL			NOT NULL)";
			statement.executeUpdate(sql);

			// RUBRO

			sql = "CREATE TABLE IF NOT EXISTS rubro " + 
					"(id 			INTEGER PRIMARY KEY," +
					"nombre			CHAR(40)	NOT NULL)";
			statement.executeUpdate(sql);
			statement.close();

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally{
			this.closeConnection();
		}
		
		// Cargamos la base de datos
		refreshData();
	}

	private void refreshData(){
		
		this.rubros = new ArrayList<String>();
		this.productos = new ArrayList<Producto>();
		this.rubrosID = new ArrayList<Integer>();
		this.productosID = new ArrayList<Integer>();
		
		this.openConnection();
		Statement statement = null;

		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery( "SELECT * FROM rubro;" );
			while ( resultSet.next() ) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("nombre");
				rubros.add(name);
				rubrosID.add(id);
			}

			resultSet.close();
			statement.close();
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery( "SELECT * FROM producto;" );
			while ( resultSet.next() ) {
				Integer id = resultSet.getInt("id");
				String descripcion = resultSet.getString("descripcion");
				String codigo = resultSet.getString("codigo");
				Integer rubro  = resultSet.getInt("rubro");
				Double precioCompra = resultSet.getDouble("precioCompra");
				Double precioVenta = resultSet.getDouble("precioVenta");
				Producto producto = new Producto(descripcion, codigo, rubro, precioCompra, precioVenta);
				productos.add(producto);
				productosID.add(id);
			}
			resultSet.close();
			statement.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally{
			this.closeConnection();
		}
		
	}
	
	// REV. 1.0
	public boolean insertProducto(Producto producto){

		this.openConnection();
		Statement statement = null;
		boolean out = false;

		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String sql = "INSERT INTO producto (codigo, descripcion, rubro, precioCompra, precioVenta) " +
					"VALUES (" + producto.getSQLValues() + ");"; 
			statement.executeUpdate(sql);
			statement.close();
			connection.commit();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()){
				int id = generatedKeys.getInt(1);
				productos.add(producto);
				productosID.add(id);
			}
			out = true;
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally{
			this.closeConnection();
		}

		return out;
	}

	// REV. 1.0
	public boolean insertRubro(String rubro){

		this.openConnection();
		Statement statement = null;
		boolean out = false;

		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String sql = "INSERT INTO rubro (id, nombre) " +
					"VALUES (NULL, '" + rubro + "');"; 
			statement.executeUpdate(sql);
			statement.close();
			connection.commit();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()){
				int id = generatedKeys.getInt(1);
				rubros.add(rubro);
				rubrosID.add(id);
			}
			out = true;
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return out;
	}

	// REV. 1.0
	public boolean deleteRubro(String rubro){

		this.openConnection();
		Statement statement = null;
		boolean out = false;

		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String sql = "DELETE FROM rubro " +
					"WHERE nombre='" + rubro + "';"; 
			statement.executeUpdate(sql);
			statement.close();
			connection.commit();

			int index = rubros.indexOf(rubro);
			rubros.remove(index);
			rubrosID.remove(index);

			out = true;
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return out;
	}

	// REV. 1.0
	public ArrayList<String> getRubrosList() {

		if (rubros.isEmpty()){
			refreshData();
		}

		return rubros;
	}

	// REV. 1.0
	public ArrayList<Producto> getProductosList() {

		if (productos.isEmpty()){
			refreshData();
		}

		return productos;
	}

	public String getRubroByID(Integer id){
		String out = null;

		for (int i = 0; i < rubrosID.size(); i++){
			if (rubrosID.get(i).equals(id)){
				out = rubros.get(i);
			}
		}

		return out;
	}

	// REV 1.0
	public Integer getRubroID(String rubroName) {
		int index = rubros.indexOf(rubroName);
		return rubrosID.get(index);
	}

	// REV 1.0
	public boolean existeProductoConID(String codigo) {

		boolean out = false;
		Statement statement;
		String query;
		this.openConnection();

		try {
			statement = connection.createStatement();
			query = "SELECT id FROM producto "+
					"WHERE codigo='"+codigo+"';";
			ResultSet results = statement.executeQuery(query);
			if (results.next()){
				out = true;
			}
			results.close();
			statement.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			this.closeConnection();
		}

		return out;
	}
	
	public Integer getProductoIDByAppearence(int index) {
		return productosID.get(index);
	}

	public boolean updateProducto(Integer productoID, Producto producto) {
		this.openConnection();
		Statement statement = null;
		boolean out = false;

		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			String sql = "UPDATE producto " +
					"SET codigo='" + producto.getCodigo() + "', " +
					"descripcion='" + producto.getDescripcion() + "', " +
					"rubro=" + producto.getRubro() + ", " + 
					"precioCompra=" + producto.getPrecioCosto() + ", " +
					"precioVenta=" +producto.getPrecioVenta() + " " +
					"WHERE id="+ productoID + ";"; 
			statement.executeUpdate(sql);
			statement.close();
			connection.commit();
			out = true;
			
			// Refrescamos nuestros datos
			refreshData();
			
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally{
			this.closeConnection();
		}

		return out;
	}
	
	public boolean deleteProducto(Integer productoID, Producto producto) {
		this.openConnection();
		Statement statement = null;
		boolean out = false;

		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			
			String sql = "DELETE FROM producto " +
					"WHERE id='" + productoID + "';"; 
			statement.executeUpdate(sql);
			statement.close();
			connection.commit();
			out = true;
			
			// Refrescamos nuestros datos
			refreshData();
			
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally{
			this.closeConnection();
		}

		return out;
	}


	// IMPORTACION Y EXPORTACION

	public void exportData(String filename) {

		Statement statement;
		String query;
		FileWriter fileWriter = null;
		this.openConnection();

		try {
			statement = connection.createStatement();
			query = "SELECT id,nombre FROM rubro";
			ResultSet results = statement.executeQuery(query);

			Vector<String> toAdd = new Vector<String>();
			while (results.next()){
				int id = results.getInt(1);
				String nombre = results.getString(2);
				toAdd.add(id + "," + nombre);
			}
			results.close();
			statement.close();

			fileWriter = new FileWriter(filename);
			fileWriter.append("ID,NOMBRE");
			fileWriter.append("\n");

			for (String lineToAdd : toAdd){
				fileWriter.append(lineToAdd + "\n");				
			}


		} catch(Exception e) {
			e.printStackTrace();
			statement = null;
		} finally{
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.closeConnection();
		}
	}

	

}
