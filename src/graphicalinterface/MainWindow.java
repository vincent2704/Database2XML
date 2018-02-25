package graphicalinterface;

import com.mysql.cj.core.util.StringUtils;

import converter.Database;
import functions.MainWindowFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import main.App;

public class MainWindow extends GridPane {

	private App app;
	private TextField tfAddr, tfDb, tfPort, tfUser;
	private PasswordField pfPassw;
	private Label lbAddr, lbDb, lbPort, lbUser, lbPassw, lbProduct;
	private Button btnConnect;
	private ComboBox<Database> cbProduct;
	private ObservableList<String> olProduct = FXCollections.observableArrayList();

	public MainWindow(App app) {
		this.app = app;
		olProduct.add("PostgreSQL");
		olProduct.add("MySQL");
		olProduct.add("Oracle DB");
		olProduct.add("Microsoft Server");

		createComponents();
		addComponents();

		setPadding(new Insets(15, 15, 15, 15));
		setVgap(25);
		setHgap(25);

	}

	private void createComponents() {
		tfAddr = new TextField("localhost");
		tfPort = new TextField("5432");
		tfDb = new TextField("bazatestowa");
		tfUser = new TextField("postgres");
		pfPassw = new PasswordField();
		pfPassw.setText("redocean");

		lbAddr = new Label("Address: ");
		lbPort = new Label("Port: ");
		lbDb = new Label("Database: ");
		lbUser = new Label("Username: ");
		lbPassw = new Label("Password: ");
		lbProduct = new Label("Choose SQL server type: ");

		// tfAddr = new TextField();
		// tfPort = new TextField();
		// tfDb = new TextField();
		// tfUser = new TextField();
		// pfPassw = new PasswordField();

		cbProduct = new ComboBox<>();
		cbProduct.getItems().setAll(Database.values());
		cbProduct.setValue(Database.POSTGRES);

		btnConnect = new Button("Connect");
	}

	private void addComponents() {

		add(lbAddr, 1, 0);
		add(lbPort, 1, 1);
		add(lbDb, 1, 2);
		add(lbUser, 1, 3);
		add(lbPassw, 1, 4);

		add(tfAddr, 2, 0);
		add(tfPort, 2, 1);
		add(tfDb, 2, 2);
		add(tfUser, 2, 3);
		add(pfPassw, 2, 4);

		add(lbProduct, 3, 0);
		add(cbProduct, 3, 1);

		add(btnConnect, 3, 5);
		btnConnect.setOnAction(e -> {
			setUpConnectButton();
		});
	}

	private void setUpConnectButton() {
		try {
			Database dbT = cbProduct.getValue();
			String addr = tfAddr.getText();
			String port = tfPort.getText();
			String dbName = tfDb.getText();
			String username = tfUser.getText();
			String passwd = pfPassw.getText();
			if (StringUtils.isNullOrEmpty(addr) || StringUtils.isNullOrEmpty(port) || StringUtils.isNullOrEmpty(dbName)
					|| StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(passwd)) {
				throw new NullPointerException("One of parameters is empty!");
			}
			MainWindowFunctions.connectToDb(dbT, addr, port, dbName, username, passwd);
			app.moveToConversionWindow();
		} catch (Exception exc) {
			failedToConnect(exc);
			exc.printStackTrace();
		}
	}

	public static Alert failedToConnect(Exception e) {
		Alert a = new Alert(AlertType.ERROR);
		a.setTitle("Connection error");
		a.setHeaderText("Failed to connect to database");
		a.setContentText("Check your input values\n");
		a.showAndWait();

		return a;
	}

}
