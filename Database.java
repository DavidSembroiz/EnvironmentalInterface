import java.io.*;
import java.sql.*;
import java.util.*;

import org.postgresql.ds.PGPoolingDataSource;

public class Database {

	private String DB_USERNAME;
	private String DB_PASSWORD;
	private String DB;
    private String DB_NAME;
    private String DB_TABLE;
	private Connection c;
    private PreparedStatement pst;

    private Properties prop;
    private PGPoolingDataSource poolSource;

    private Utils uts;

	public Database() {
        loadProperties();
        loadPoolSource();
        createIdTable();
	}
    
    public void assignUtils(Utils uts) {
        this.uts = uts;
    }

    private void loadProperties() {
        prop = new Properties();
		try {
			InputStream is = new FileInputStream("database.properties");
			prop.load(is);
			DB_USERNAME = prop.getProperty("db_username");
			DB_PASSWORD = prop.getProperty("db_password");
			DB = prop.getProperty("db");
            DB_NAME = prop.getProperty("db_name");
            DB_TABLE = prop.getProperty("db_table");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void loadPoolSource() {
		poolSource = new PGPoolingDataSource();
		poolSource.setDataSourceName("DB Data Source");
		poolSource.setServerName(DB);
		poolSource.setDatabaseName(DB_NAME);
		poolSource.setUser(DB_USERNAME);
		poolSource.setPassword(DB_PASSWORD);
		poolSource.setMaxConnections(20);
	}
    
    private void createIdTable() {
		try {
			c = poolSource.getConnection();
			pst = c.prepareStatement("CREATE TABLE IF NOT EXISTS " + DB_TABLE + " ("
					  	  + "servioticy_id varchar(50) primary key,"
					  	  + "model varchar(30),"
					  	  + "location varchar(100),"
					  	  + "associations varchar(100),"
					  	  + "created timestamp default current_timestamp(2));");
			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(c);
		}
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
			pst = c.prepareStatement("INSERT INTO " + DB_TABLE + " VALUES (?, ?, ?, ?)");
			pst.setString(1, soID);
            pst.setString(2, model);
            pst.setString(3, location);
            pst.setString(4, associations);
			pst.executeUpdate();
            notifyNewSO(soID);
		} catch(SQLException e) {
            //uts.revertCreation();
			e.printStackTrace();
		} finally {
			closeConnection(c);
		}
	}
}
