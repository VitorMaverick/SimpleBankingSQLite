package banking.repository;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class UtilDB {
    //String url = "jdbc:sqlite:C:\\Data\\HyperSkill\\card.s3db";
    public String url;

    public UtilDB() {
        this.url = "jdbc:sqlite:dbTest.s3db";
    }

    public UtilDB(String url) {
        this.url = "jdbc:sqlite:" + url;
    }

    public Connection getConn() throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = dataSource.getConnection();

        //System.out.println("Connectin is valied");
        return con;


    }
}
