package functions;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import converter.XmlConverter;
import converter.XmlConverterFactory;
import graphicalinterface.ConversionWindow;
import javafx.stage.DirectoryChooser;
import main.App;

public class ConversionWindowFunctions {
	private App app;
	private ConversionWindow convWin;
	private Connection con = MainWindowFunctions.getCon();

	public static String strTargetDirectory = System.getProperty("user.dir");
	private String dbname = MainWindowFunctions.getDatabaseName();
	private final String[] arrayTV = new String[] { "TABLE", "VIEW" };
	private final String[] arrayT = new String[] { "TABLE" };
	private FileManager fm;
	private List<String> conversionStartList, conversionFinishList;

	public ConversionWindowFunctions(ConversionWindow conversionWindow, App app) {
		convWin = conversionWindow;
		this.app = app;
		fm = new FileManager();
	}

	/**
	 * TODO: write smth
	 */
	public void fillSchemaList() {
		try {
			List<String> schs = getSchemas();
			convWin.addSchemas(schs, true);
		} catch (SQLException e) {
			System.err.println("Nie dziala fillschema");
			e.printStackTrace();
		}
	}

	/**
	 * TODO: write smth
	 * 
	 * @param schematype
	 *            a nie write xd
	 */
	public void fillTablesList(String schema) {
		String[] array;
		try {
			if (convWin.isIncludeViews()) {
				array = arrayTV;
			} else {
				array = arrayT;
			}
			List<String> tables = getTablesFromSchema(schema, array);
			convWin.addTables(tables, true);
		} catch (SQLException e) {
			System.err.println("DEBUG: nie dziala pobieranie tabel");
			e.printStackTrace();
		}
	}

	public void changeTargetDirectory() {
		DirectoryChooser dc = new DirectoryChooser();
		dc.setInitialDirectory(new File(strTargetDirectory));
		dc.setTitle("Choose XML files location");
		File dir = dc.showDialog(app.getStage());
		if (dir != null) {
			strTargetDirectory = dir.getAbsolutePath();
			convWin.updateDirLabel("Current target directory: " + strTargetDirectory);
		}
	}

	public void navigateToMain() {
		app.moveToMainWindow();
	}

	/**
	 * Depending on selected option, this overloaded uses convert() method to
	 * indicate proper way of database to XML file conversion.
	 * 
	 * @param schema
	 *            Schema containing given elements
	 * @param table
	 *            Table and/or view being converted and exported into XML file
	 * @throws SQLException
	 */
	public void generate(String schema, String table) throws SQLException {
		// doesnt allow to generate files without path selection anymore
		if (convWin.getDirectoryPath().length() < 1) {
			convWin.alSelectPath();
		} else {
			initializeReadme(schema, table);
			if (convWin.isOnlySelected()) { // Generate only selected table
				if (convWin.isMetaData() && !convWin.isData()) { // metadata
					convertTableMetaData(schema, table);
				}
				if (!convWin.isMetaData() && convWin.isData()) { // data
					convertTableData(schema, table);
				}
				if (convWin.isData() && convWin.isMetaData()) { // metadata and data
					// convertTableDataAndMetadata(schema, table); - deprecated method
					convertTableMetaData(schema, table);
					convertTableData(schema, table);
				}
			}

			if (convWin.isAllFromSchema()) { // Generate all tables from selected schema
				if (convWin.isMetaData() && !convWin.isData()) { // metadata
					convertAllTablesMetadataInSchema(schema);
				}
				if (!convWin.isMetaData() && convWin.isData()) { // data
					convertAllTablesDataInSchema(schema);
				}
				if (convWin.isData() && convWin.isMetaData()) { // metadata and data
					convertAllTablesDataAndMetadataInSchema(schema);
				}

			}

			if (convWin.isOnlySelectedSchema()) { // Generate selected schema structure
				convertSchemaMetadata(schema);
			}

			if (convWin.isAllSchemas()) { // Generate all schemas structure
				convertAllSchemasStructure();
			}

			if (convWin.isDatabase()) { // Generate database metadata
				convertDatabaseMetadata(dbname);
			}

			finishCreateReadme(schema, table);
		}

	}

	/**
	 * Initializes creating Readme.txt content about the conversion starting time if
	 * Create readme option is selected.
	 * 
	 * @param schema
	 * @param table
	 */
	private void initializeReadme(String schema, String table) {
		if (convWin.isCreateReadme()) {
			if (convWin.isDatabase()) {
				conversionStartList = fm.createReadmeStartContent(dbname, null, table);
			} else {
				conversionStartList = fm.createReadmeStartContent(dbname, schema, table);
			}
		}
	}

	/**
	 * Finishes creating Readme.txt content about the conversion finish time if
	 * Create readme option is selected.
	 * 
	 * @param schema
	 * @param table
	 */
	private void finishCreateReadme(String schema, String table) {
		if (convWin.isCreateReadme()) {
			if (convWin.isDatabase()) {
				// schema and table may still be selected by user
				conversionFinishList = fm.createReadmeFinishedContent(dbname, null, null);
				fm.createReadmeFile(conversionStartList, conversionFinishList);
			} else {
				conversionFinishList = fm.createReadmeFinishedContent(dbname, schema, table);
				fm.createReadmeFile(conversionStartList, conversionFinishList);
			}
		}
	}

	/**
	 * Loops through all schemas and their tables and calls
	 * convertAllTablesDataInSchema() method for each one of them.
	 * 
	 * @throws SQLException
	 */
	private void convertAllTablesInAllSchemas() throws SQLException {
		List<String> schemas = getSchemas();
		for (String s : schemas) {
			convertAllTablesDataInSchema(s);
		}
	}

	/**
	 * Loops through all schemas and calls convertSchemaMetadata() method for each
	 * one of them.
	 * 
	 * @throws SQLException
	 */
	private void convertAllSchemasStructure() throws SQLException {
		List<String> schemas = getSchemas();
		for (String s : schemas) {
			convertSchemaMetadata(s);
		}
	}

	/**
	 * Converts all tables metadata in given schema into XSD files.
	 * 
	 * @throws SQLException
	 */
	private void convertAllTablesMetadataInSchema(String schema) throws SQLException {
		List<String> tables = getTablesFromSchema(schema, convWin.isIncludeViews() ? arrayTV : arrayT);
		for (String t : tables) {
			convertTableMetaData(schema, t);
		}
	}

	/**
	 * Converts all tables data to XML files in a given schema.
	 * 
	 * @param schema
	 * @throws SQLException
	 */
	private void convertAllTablesDataInSchema(String schema) throws SQLException {
		List<String> tables = getTablesFromSchema(schema, convWin.isIncludeViews() ? arrayTV : arrayT);
		for (String t : tables) {
			convertTableData(schema, t);
		}
	}

	/**
	 * Converts all tables data and metadata in a given schema into XML files.
	 * 
	 * @param schema
	 * @throws SQLException
	 */
	private void convertAllTablesDataAndMetadataInSchema(String schema) throws SQLException {
		List<String> tables = getTablesFromSchema(schema, convWin.isIncludeViews() ? arrayTV : arrayT);
		for (String t : tables) {
			convertTableData(schema, t);
			convertTableMetaData(schema, t);
		}
	}

	/**
	 * Converts table element into XML format and outputs it into XML file. Uses
	 * XmlConverter object to determine appropriate way of using the convertTable
	 * method.
	 * 
	 * @param schema
	 *            Schema containing the given table
	 * @param table
	 *            Table being converted to XML
	 * @return
	 */
	private String convertTableData(String schema, String table) {
		XmlConverter converter = XmlConverterFactory.createConverter();
		// Map<String, Object> params = new HashMap<>();
		String tablename = schema + ".\"" + table + "\"";
		// params.put("table", schema + ".\"" + table + "\"");
		// params.put("nulls", true);
		// params.put("tableforest", false);
		// params.put("targetns", "n");
		String xmlContent = converter.convertTable(tablename);
		String fileName;
		if (convWin.isDisplayDatabaseName()) {
			fileName = "[" + dbname + "]" + schema + "." + table;
		} else {
			fileName = schema + "." + table;
		}
		fileName += ".xml";

		fm.inputToFile(fileName, xmlContent);
		return fileName;
	}

	/**
	 * Converts table metadata into a single XSD file.
	 * 
	 * @param schema
	 * @param table
	 */
	private void convertTableMetaData(String schema, String table) {
		XmlConverter conv = XmlConverterFactory.createConverter();
		String tablename = schema + ".\"" + table + "\"";
		String xmlContent = conv.convertTableMetaData(tablename);
		String fileName;
		if (convWin.isDisplayDatabaseName()) {
			fileName = "[" + dbname + "]" + schema + "." + table;
		} else {
			fileName = schema + "." + table;
		}
		fileName += ".xsd";
		fm.inputToFile(fileName, xmlContent);
	}

	/**
	 * @deprecated No longer used since generating XML and XSD content in a single
	 *             file had no use. When Metadata and Data radio buttons are
	 *             selected, it should generate two kinds of files at once in a
	 *             single folder. This method converts table's both metadata and
	 *             data content into a single XML file.<br>
	 *             This method concatenates output of converting table metadata and
	 *             data and has been left for legacy purposes.
	 * 
	 * @param schema
	 *            Schema containing the converted table
	 * @param table
	 *            Table being converted
	 */
	@Deprecated
	public void convertTableDataAndMetadata(String schema, String table) {
		XmlConverter conv = XmlConverterFactory.createConverter();
		String tablename = schema + ".\"" + table + "\"";
		String metaData = conv.convertTableMetaData(tablename);
		String data = conv.convertTable(tablename);
		String xmlContent = metaData + "\n" + data;
		String fileName;
		if (convWin.isDisplayDatabaseName()) {
			fileName = "[" + dbname + "]" + schema + "." + table;
		} else {
			fileName = schema + "." + table;
		}
		fileName += ".xml";
		fm.inputToFile(fileName, xmlContent);
	}

	/**
	 * Converts schema metadata into XSD format and outputs it into XSD file.
	 * 
	 * @param Schema
	 *            to get metadata from to convert
	 */
	public void convertSchemaMetadata(String schema) {
		XmlConverter converter = XmlConverterFactory.createConverter();
		String xmlContent = converter.convertSchemaMetadata(schema);
		String fileName;
		if (convWin.isDisplayDatabaseName()) {
			fileName = "[" + dbname + "]" + schema;
		} else {
			fileName = schema;
		}
		fileName += ".xsd";
		fm.inputToFile(fileName, xmlContent);
	}

	/**
	 * Converts database metadata into XSD file.
	 * 
	 * @param database
	 */
	public void convertDatabaseMetadata(String database) {
		XmlConverter converter = XmlConverterFactory.createConverter();
		String xmlContent = converter.convertDatabaseMetadata(database);
		String fileName = database + ".xsd";
		fm.inputToFile(fileName, xmlContent);
	}

	private List<String> getSchemas() throws SQLException {
		DatabaseMetaData metaData = con.getMetaData();
		ResultSet rsSchemas = metaData.getSchemas();
		List<String> schs = new ArrayList<>();
		while (rsSchemas.next()) {
			schs.add(rsSchemas.getString(1));
		}
		// usuwa z listy schematy niewidoczne
		schs.remove("pg_catalog");
		schs.remove("information_schema");
		return schs;
	}

	/**
	 * Gets all tables from selected schema and returns List containing their names.
	 * 
	 * @param schema
	 *            Schema containing returned tables
	 * @param array
	 *            Array determining whether returned List contains views or not
	 * @return List containing names of selected tables and/or views
	 * @throws SQLException
	 */
	private List<String> getTablesFromSchema(String schema, String[] array) throws SQLException {
		ResultSet rsTables = con.getMetaData().getTables(null, schema, "%", array);
		List<String> tables = new ArrayList<>();
		while (rsTables.next()) {
			tables.add(rsTables.getString(3));
		}
		return tables;
	}

	public String getServerUrl() {
		try {
			return con.getMetaData().getURL();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
