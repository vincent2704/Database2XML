package functions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileManager {

	private String path, readmeContent;
	private CalendarManager cm;
	private List<String> lstReadmeStartingContent, lstReadmeFinishedContent;

	@Deprecated
	public void saveToFile(String filename, String content) throws URISyntaxException {
		Path f = Paths.get(new URI(filename));
		Charset charset = Charset.forName("UTF-8");
		try (BufferedWriter writer = Files.newBufferedWriter(f, charset)) {
			writer.write(content, 0, content.length());
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

	}

	/**
	 * Inputs String element into a new XML file
	 * 
	 * @param filename
	 *            Name of a desired XML file as prefix of .xml.<br>
	 *            For example, if filename equals Table, the created file's name is
	 *            Table.xml.<br>
	 * @param content
	 *            String representing content of the created file.
	 */
	public void inputToFile(String fileName, String content) {
		// changes table name if it contains illegal Windows filename characters
		for (int i = 0; i < fileName.length(); i++) {
			if (fileName.charAt(i) == '/' || fileName.charAt(i) == '\\' || fileName.charAt(i) == ':'
					|| fileName.charAt(i) == '?' || fileName.charAt(i) == '"' || fileName.charAt(i) == '<'
					|| fileName.charAt(i) == '>' || fileName.charAt(i) == '|') {
				fileName = fileName.replace(fileName.charAt(i), '_');
			}
		}
		try {
			// needed for saving file if directory is specified by the user
			path = ConversionWindowFunctions.strTargetDirectory;
			FileUtils.writeStringToFile(new File(path + "\\" + fileName), content, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates Readme.txt file containing data about the conversion start and finish
	 * time
	 */
	public void createReadmeFile(List<String> startTimeData, List<String> finishTimeData) {
		readmeContent = "";
		startTimeData.forEach(v -> {
			readmeContent += v + "\n";
		});
		readmeContent += "\n";
		finishTimeData.forEach(v -> {
			readmeContent += v + "\n";
		});
		// deletes last empty line
		readmeContent.substring(0, readmeContent.length() - 1);

		try {
			path = ConversionWindowFunctions.strTargetDirectory;
			if (path.length() > 1) {
				path = path.replace("/", "\\");
				FileUtils.writeStringToFile(new File(path + "\\" + "Readme.txt"), readmeContent,
						StandardCharsets.UTF_8);
			} else {
				FileUtils.writeStringToFile(new File("Readme.txt"), readmeContent, StandardCharsets.UTF_8);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates Readme.txt file content at the beginning of conversion which contains
	 * operation start time.
	 */
	public List<String> createReadmeStartContent(String database, String schema, String table) {
		cm = new CalendarManager();
		lstReadmeStartingContent = new ArrayList<>();
		// conditional printing for elements
		schema = schema != null ? ", schema: " + schema : "";
		table = table != null ? ", table: " + table : "";
		lstReadmeStartingContent.add("Conversion start time for database: " + database + schema + table);
		lstReadmeStartingContent.add(cm.getCurrentDay() + ", hour: " + cm.getCurrentTimeWithSeconds());

		return lstReadmeStartingContent;
	}

	/**
	 * Creates Readme.txt file content at the end of conversion which contains
	 * operation finish time.
	 */
	public List<String> createReadmeFinishedContent(String database, String schema, String table) {
		cm = new CalendarManager();
		lstReadmeFinishedContent = new ArrayList<>();
		// conditional printing for elements
		schema = schema != null ? ", schema: " + schema : "";
		table = table != null ? ", table: " + table : "";
		lstReadmeFinishedContent.add("Conversion end time for database: " + database + schema + table);
		lstReadmeFinishedContent.add(cm.getCurrentDay() + ", hour: " + cm.getCurrentTimeWithSeconds());

		return lstReadmeFinishedContent;
	}
}
