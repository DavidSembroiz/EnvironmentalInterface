import java.io.*;
import java.sql.*;
import java.util.*;

import org.postgresql.ds.PGPoolingDataSource;

public class Database {

	private String AWS_USERNAME;
	private String AWS_PASSWORD;
	private String AWS_DB;
    private String AWS_DB_NAME;
	private Connection c;
    private PreparedStatement pst;

    private Properties prop;
    private PGPoolingDataSource poolSource;

    private Utils uts;

	public Database() {
        loadProperties();
        loadPoolSource();
	}
    
    public void assignUtils(Utils uts) {
        this.uts = uts;
    }

    private void loadProperties() {
        prop = new Properties();
		try {
			InputStream is = new FileInputStream("database.properties");
			prop.load(is);
			AWS_USERNAME = prop.getProperty("aws_username");
			AWS_PASSWORD = prop.getProperty("aws_password");
			AWS_DB = prop.getProperty("aws_db");
            AWS_DB_NAME = prop.getProperty("aws_db_name");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void loadPoolSource() {
		poolSource = new PGPoolingDataSource();
		poolSource.setDataSourceName("AWS Data Source");
		poolSource.setServerName(AWS_DB);
		poolSource.setDatabaseName(AWS_DB_NAME);
		poolSource.setUser(AWS_USERNAME);
		poolSource.setPassword(AWS_PASSWORD);
		poolSource.setMaxConnections(20);
	}

    private void closeConnection(Connection c) {
		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

    private void notifyNewSO(String soID) {
        try {
            pst = c.prepareStatement("NOTIFY SO_CHANNEL, \'" + soID + "\';");
            pst.execute();
        } catch(SQLException e) {
            e.printStackTrace();        
        }
    }

    public void insertSO(String soID, String model, String location, String associations) {
		c = null;
		try {
            System.out.println("Inserting new SO into AWS RDS...");
			c = poolSource.getConnection();
			pst = c.prepareStatement("INSERT INTO ids VALUES (?, ?, ?, ?)");
			pst.setString(1, soID);
            pst.setString(2, model);
            pst.setString(3, location);
            pst.setString(4, associations);
			pst.executeUpdate();
            notifyNewSO(soID);
		} catch(SQLException e) {
            uts.revertCreation();
			e.printStackTrace();
		} finally {
			closeConnection(c);
		}
	}
}
