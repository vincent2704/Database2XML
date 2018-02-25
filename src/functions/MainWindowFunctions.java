package functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import converter.Database;
import main.App;

/**
 * Manages connection to database based on the input values in the GUI.
 * 
 * @author Jarek
 *
 */
public class MainWindowFunctions {

	private static Connection con;
	private ResultSet rsSchemas;
	private ResultSet rsTables;
	private static Database databaseType;
	private static String databaseName;

	/**
	 * Connects the client to the PostgreSQL database.
	 * 
	 * @param databaseType
	 * 
	 * @param address
	 *            Server address given in the text field
	 * @param port
	 *            Server port given in the text field
	 * @param database
	 *            Database name given in the text field
	 * @param user
	 *            Username given in the text field we want to connect as
	 * @param password
	 *            User's password given in the password field
	 */
	public static Connection connectToDb(Database databaseType, String address, String port, String database,
			String user, String password) {
		MainWindowFunctions.databaseType = databaseType;
		databaseName = database;
		try {
			switch (databaseType) {
			case POSTGRES:
				String urlPostgres = "jdbc:postgresql://" + address + ":" + port + "/" + database;
				con = DriverManager.getConnection(urlPostgres, user, password);
				break;
			// ones below are yet to be implemented
			case MYSQL:
				// database implementation in progress
				String urlMySQL = "jdbc:mysql://" + address + ":" + port + "/" + database;
				con = DriverManager.getConnection(urlMySQL, user, password);
				break;
			case JAVADB:
				con = null;
				break;
			case MSSQL:
				con = null;
				break;
			}
		} catch (SQLException e) {
			App app = new App();
			app.moveToMainWindow();
			return null;
		}
		return con;
	}

	public static Connection getCon() {
		return con;
	}

	public static Database getDbType() {
		return databaseType;
	}

	public static String getDatabaseName() {
		return databaseName;
	}

}
