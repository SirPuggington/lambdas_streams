import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HSQLDBCSVFileImport {
    private Connection connection;

    /* PLEASE DO NOT MODIFY
    public static void main(String... args) {
        HSQLDBCSVFileImport application = new HSQLDBCSVFileImport();
        application.init();
        application.importCSVFile(Configuration.instance.dataPath + "records.csv");
        application.shutdown();
    }*/

    public void startup() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String driverName = "jdbc:hsqldb:";
            String databaseURL = driverName + Configuration.INSTANCE.dataPath + "records.db";
            String username = "sa";
            String password = "";
            connection = DriverManager.getConnection(databaseURL, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void update(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);
            if (result == -1) {
                System.out.println("error executing " + sqlStatement);
            }
            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    public void dropTable() {
        System.out.println("--- dropTable");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE data");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder);

        update(sqlStringBuilder.toString());
    }

    public void createTable() {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("CREATE TABLE data ").append(" ( ");
        sqlStringBuilder.append("id BIGINT NOT NULL").append(",");
        sqlStringBuilder.append("entrance INTEGER NOT NULL").append(",");
        sqlStringBuilder.append("carSize VARCHAR(1) NOT NULL").append(",");
        sqlStringBuilder.append("floor INTEGER NOT NULL").append(",");
        sqlStringBuilder.append("parkingSpot INTEGER NOT NULL").append(",");
        sqlStringBuilder.append("durationInDays INTEGER NOT NULL").append(",");
        sqlStringBuilder.append("payment VARCHAR(6) NOT NULL").append(",");
        sqlStringBuilder.append("PRIMARY KEY (id)");
        sqlStringBuilder.append(" )");
        update(sqlStringBuilder.toString());
    }

    public void init() {
        startup();
        dropTable();
        createTable();
    }

    public String buildSQLStatement(long id, int entrance, String carSize, int floor, int parkingSpot, int durationInDays, String payment) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO data (id,entrance,carSize,floor,parkingSpot,durationInDays,payment) VALUES (");
        stringBuilder.append(id).append(",");
        stringBuilder.append(entrance).append(",");
        stringBuilder.append("'").append(carSize).append("'").append(",");
        stringBuilder.append(floor).append(",");
        stringBuilder.append(parkingSpot).append(",");
        stringBuilder.append(durationInDays).append(",");
        stringBuilder.append("'").append(payment).append("'");
        stringBuilder.append(")");
        //System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public void insert(long id, int entrance, String carSize, int floor, int parkingSpot, int durationInDays, String payment) {
        update(buildSQLStatement(id, entrance, carSize, floor, parkingSpot, durationInDays, payment));
    }

    public void importCSVFile(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split(";");
                insert(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), strings[2], Integer.parseInt(strings[3]),
                        Integer.parseInt(strings[4]), Integer.parseInt(strings[5]), strings[6]);
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public void shutdown() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }
}