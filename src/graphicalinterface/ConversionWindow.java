package graphicalinterface;

import java.sql.SQLException;
import java.util.List;

import functions.ConversionWindowFunctions;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.App;

public class ConversionWindow extends BorderPane {
	private ConversionWindowFunctions convFunc;

	private ObservableList<String> olSchemas = FXCollections.observableArrayList();
	private ObservableList<String> olTables = FXCollections.observableArrayList();

	private ListView<String> lvSchemas;
	private ListView<String> lvTables;
	private Button btnCancel, btnSubmit, btnDir;
	private RadioButton rbtnMetaData;
	private RadioButton rbtnData;
	private RadioButton rbtnOnlySelected;
	private RadioButton rbtnOnlySelectedSchema;
	private RadioButton rbtnAllFromSchema;
	private RadioButton rbtnAllSchemas;
	private RadioButton rbtnSelectedSchemaFull;
	private RadioButton rbtnTablesAndSchemas;
	private RadioButton rbtnDatabase;
	private CheckBox chbDisplayViews;
	private CheckBox chbOneFile;
	private CheckBox chbDisplayDbName;
	private CheckBox chbCreateReadme;
	private static CheckBox chbBindToXsd;
	private Label lbDir;

	public ConversionWindow(App app) {
		// allows access to functions for this gui created in
		// ConversionWindowFunctions class
		convFunc = new ConversionWindowFunctions(this, app);

		setTop(createTopHBox());
		setBottom(createBottomHBox());
		setLeft(createGridPaneLeft());
		setCenter(createCenterList());
		setRight(createRightVBox());

		setPadding(new Insets(15, 15, 15, 15));
		refreshSchemas();
	}

	private HBox createTopHBox() {
		HBox hbox = new HBox(20);
		Label lbServ = new Label("Server URL: " + convFunc.getServerUrl());
		hbox.getChildren().addAll(lbServ);
		return hbox;
	}

	private GridPane createCenterList() {
		GridPane gp = new GridPane();
		lvTables = new ListView<>(olTables);
		gp.add(lvTables, 0, 0);
		return gp;
	}

	private GridPane createGridPaneLeft() {
		GridPane gp = new GridPane();
		lvSchemas = new ListView<>(olSchemas);
		lvSchemas.setMaxWidth(140);
		lvSchemas.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
					refreshTables();
				});
		gp.add(lvSchemas, 0, 0);
		return gp;
	}

	private VBox createRightVBox() {
		VBox vbox = new VBox(20);
		Label lbDataType = new Label("Data Type:");
		Label lbElement = new Label("Element to convert:");

		rbtnMetaData = new RadioButton("Metadata");
		rbtnData = new RadioButton("Data");

		rbtnOnlySelected = new RadioButton("Generate only selected table"); // zrobione
		rbtnAllFromSchema = new RadioButton("Generate all tables from selected schema"); // zrobione
		rbtnOnlySelectedSchema = new RadioButton("Generate selected schema structure"); // zrobione
		// rbtnSelectedSchemaFull = new RadioButton("Generate selected schema structure
		// and tables (to do)");
		rbtnAllSchemas = new RadioButton("Generate all schemas structure");
		// rbtnTablesAndSchemas = new RadioButton("Generate all schemas and tables (to
		// do)");
		rbtnDatabase = new RadioButton("Generate database metadata");

		btnDir = new Button("Change directory...");
		btnDir.setOnAction(e -> {
			convFunc.changeTargetDirectory();
		});

		btnSubmit = new Button("Generate");
		btnSubmit.setOnAction(e -> {
			try {
				convFunc.generate(getSelectedSchema(), getSelectedTable());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

		chbBindToXsd = new CheckBox("Bind XML to XSD");

		chbDisplayViews = new CheckBox("Include views");
		chbDisplayViews.setOnAction(e -> {
			refreshTables();
		});

		chbDisplayDbName = new CheckBox("Add database name to file");

		// currently hidden, possibly it will be removed
		chbOneFile = new CheckBox("Save in one file (to do)");
		chbOneFile.setOnAction(e -> {
			System.out.println("Do zaimplementowania");
		});

		chbCreateReadme = new CheckBox("Create Readme file");

		ToggleGroup group = new ToggleGroup();
		rbtnOnlySelected.setToggleGroup(group);
		rbtnAllFromSchema.setToggleGroup(group);
		rbtnOnlySelectedSchema.setToggleGroup(group);
		rbtnAllSchemas.setToggleGroup(group);
		rbtnDatabase.setToggleGroup(group);

		vbox.getChildren().addAll(lbDataType, rbtnMetaData, rbtnData); // data type part
		vbox.getChildren().addAll(lbElement, rbtnOnlySelected, rbtnAllFromSchema, rbtnOnlySelectedSchema,
				rbtnAllSchemas, rbtnDatabase, btnDir, btnSubmit, chbBindToXsd, chbDisplayViews, chbDisplayDbName,
				chbCreateReadme);

		return vbox;
	}

	private HBox createBottomHBox() {
		HBox hbox = new HBox(20);
		btnCancel = new Button("Cancel");
		btnCancel.setOnAction(e -> {
			convFunc.navigateToMain();
		});
		lbDir = new Label("");
		hbox.getChildren().addAll(btnCancel, lbDir);
		return hbox;
	}

	/**
	 * updates schemas list
	 */
	private void refreshSchemas() {
		convFunc.fillSchemaList();
	}

	/**
	 * updates table list whenever checkbox state is changed
	 */
	private void refreshTables() {
		int selected = lvTables.getSelectionModel().getSelectedIndex();
		String schema = getSelectedSchema();
		if (schema != null && !schema.isEmpty()) {
			convFunc.fillTablesList(schema);
		}
		lvTables.getSelectionModel().select(selected);
	}

	/**
	 * Adds schemas to ListView
	 * 
	 * @param schemas
	 *            schemas to add
	 * @param clear
	 *            if true, clears String elements in the olSchemas ObservableList
	 */
	public void addSchemas(List<String> schemas, boolean clear) {
		if (!olSchemas.isEmpty() && clear) {
			olSchemas.clear();
		}
		olSchemas.addAll(schemas);
	}

	/**
	 * Adds tables to ListView
	 * 
	 * @param tables
	 *            tables to add
	 * @param clear
	 *            if true, clears String elements in the olTables ObservableList
	 */
	public void addTables(List<String> tables, boolean clear) {
		if (!olTables.isEmpty() && clear) {
			olTables.clear();
		}
		olTables.addAll(tables);
		System.out.println("Liczba zaladowanych tabel: " + olTables.size());
	}

	public void updateDirLabel(String labelText) {
		lbDir.setText(labelText);
	}

	// currently never used
	public Alert alFail() {
		Alert a = new Alert(AlertType.ERROR);
		a.setTitle("Conversion failed");
		a.setHeaderText("Check if proper elements are selected");
		a.showAndWait();

		return a;
	}

	public Alert alSelectPath() {
		Alert a = new Alert(AlertType.WARNING);
		a.setTitle("Folder not selected");
		a.setHeaderText("");
		a.setContentText("Select destination folder.");
		a.showAndWait();

		return a;
	}

	public String getSelectedSchema() {
		return lvSchemas.getSelectionModel().getSelectedItem();
	}

	public String getSelectedTable() {
		return lvTables.getSelectionModel().getSelectedItem();
	}

	public String getDirectoryPath() {
		return lbDir.getText();
	}

	public boolean isOnlySelected() {
		return rbtnOnlySelected.isSelected();
	}

	public boolean isOnlySelectedSchema() {
		return rbtnOnlySelectedSchema.isSelected();
	}

	public boolean isAllFromSchema() {
		return rbtnAllFromSchema.isSelected();
	}

	public boolean isAllSchemas() {
		return rbtnAllSchemas.isSelected();
	}

	public boolean isOnlyTables() {
		return rbtnSelectedSchemaFull.isSelected();
	}

	public boolean isTablesAndSchemas() {
		return rbtnTablesAndSchemas.isSelected();
	}

	public boolean isIncludeViews() {
		return chbDisplayViews.isSelected();
	}

	public boolean isDisplayDatabaseName() {
		return chbDisplayDbName.isSelected();
	}

	public boolean isSaveInOneFile() {
		return chbOneFile.isSelected();
	}

	public boolean isDatabase() {
		return rbtnDatabase.isSelected();
	}

	public boolean isMetaData() {
		return rbtnMetaData.isSelected();
	}

	public boolean isData() {
		return rbtnData.isSelected();
	}

	public static boolean isBindToXsd() {
		return chbBindToXsd.isSelected();
	}

	public boolean isCreateReadme() {
		return chbCreateReadme.isSelected();
	}

}
