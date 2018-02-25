package converter;

public enum Database {
	POSTGRES("PostgreSQL"), MYSQL("MySQL"), MSSQL("Microsoft Server"), JAVADB("Java DB");

	private String name;

	Database(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
