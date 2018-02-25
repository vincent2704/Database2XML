package mysqlDatabaseTest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class MySQLTDD {

	@Test
	void createConnectionTest() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=true", "root", "toor");
			System.out.println("Polaczono z baza");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
