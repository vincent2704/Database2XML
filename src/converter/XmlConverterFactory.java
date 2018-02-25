package converter;

import functions.MainWindowFunctions;

public class XmlConverterFactory {
	public static XmlConverter createConverter() {
		Database database = MainWindowFunctions.getDbType();
		switch (database) {
		case MSSQL:
		case MYSQL:
		case POSTGRES:
			return new PostgreSQLConverter();
		default:
			throw new IllegalArgumentException("Wrong database " + database);
		}
	}
}
