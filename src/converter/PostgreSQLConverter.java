package converter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.jdbc.PgSQLXML;

import functions.MainWindowFunctions;
import graphicalinterface.ConversionWindow;

/**
 * Class used to convert PostgreSQL database to XML format
 * 
 * @author Jarek
 *
 */
class PostgreSQLConverter implements XmlConverter {

	private Connection con = MainWindowFunctions.getCon();

	// array of illegal characters in filenames
	// char[] illegalChars = { '\\', '/', ':', '?', '"', '<', '>', '|' };

	public PostgreSQLConverter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String convertTable(String tablename) {
		String nulls = "true";
		String tableforest = "false";
		String targetns;
		String sql;
		// if true, adds NAME.xsd as target namespace for NAME.xml
		if (ConversionWindow.isBindToXsd()) {
			targetns = tablename + ".xsd";
			targetns = targetns.replace("\"", "");
			// example: SELECT table_to_xml('false', 'true', 'example_table.xsd');
			sql = "SELECT table_to_xml('" + tablename + "', '" + nulls + "', '" + tableforest + "', '" + targetns
					+ "');";
		} else {
			targetns = "";
			// example: SELECT table_to_xml('false', 'true', 'n');
			sql = "SELECT table_to_xml('" + tablename + "', '" + nulls + "', '" + tableforest + "', '" + targetns
					+ "');";
		}

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				PgSQLXML xml = (PgSQLXML) rs.getObject(1);
				return xml.getString();
			} else {
				System.out.println("Conversion failed! PostgreSQLConverter.convertTable()");
			}
			st.close();
		} catch (SQLException e) {
			// convWin.alFail();
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String convertTableMetaData(String tablename) {
		String nulls = "true";
		String tableforest = "false";
		String targetns = "n";
		String sql = "SELECT table_to_xmlschema('" + tablename + "', '" + nulls + "', '" + tableforest + "', '"
				+ targetns + "');";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				PgSQLXML xml = (PgSQLXML) rs.getObject(1);
				return xml.getString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String convertSchemaMetadata(String schemaName) {
		// schema_to_xml(schema name, nulls boolean, tableforest boolean, targetns text)
		try {
			String sql = "SELECT schema_to_xmlschema('" + schemaName + "', 'false', 'true', 'n');";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				PgSQLXML xml = (PgSQLXML) rs.getObject(1);
				return xml.getString();
			} else {
				System.out.println("Conversion failed! PostgreSQLConverter.convertSchema()");
			}
			st.close();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return null;
	}

	@Override
	public String convertDatabaseMetadata(String database) {

		try {
			String sql = "SELECT database_to_xmlschema('true', 'false', 'n');";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				PgSQLXML xml = (PgSQLXML) rs.getObject(1);
				return xml.getString();
			} else {
				System.out.println("Conversion failed! PostgreSQLConverter.convertDatabaseMetadata()");
			}
			st.close();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return null;
	}

}
