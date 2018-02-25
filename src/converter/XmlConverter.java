package converter;

public interface XmlConverter {

	/**
	 * Method converting table content to an XML file.
	 * 
	 * @param tablename
	 * @return String representing the content of generated XML file
	 */
	String convertTable(String tablename);

	/**
	 * Converts table metadata into an XSD file.
	 * 
	 * @param tablename
	 * @return String representing the content of generated XSD file
	 */
	String convertTableMetaData(String tablename);

	/**
	 * Method converting schema definition to an XSD file.
	 * 
	 * @param schemaname
	 * @return String representing the content of generated XSD file
	 */
	String convertSchemaMetadata(String schemaName);

	/**
	 * Method converting database definition into an XSD file.
	 * 
	 * @param database
	 * @return String representing the content of generated XSD file
	 */
	String convertDatabaseMetadata(String database);

}
