import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Query implements IQuery {
    private Connection connection;
    private BufferedWriter bufferedWriter;

    public static void main(String... args) {
        Query query = new Query();
        query.startup();

        query.executeSQL01();
        query.executeSQL02();
        query.executeSQL03();
        query.executeSQL04();
        query.executeSQL05();
        query.executeSQL06();
        query.executeSQL07();
        query.executeSQL08();
        query.executeSQL09();
        query.executeSQL10();
        query.executeSQL11();
        query.executeSQL12();

        query.shutdown();
    }

    public void startup() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String driverName = "jdbc:hsqldb:";
            String databaseURL = driverName + Configuration.INSTANCE.dataPath + "records.db";
            String username = "sa";
            String password = "";
            connection = DriverManager.getConnection(databaseURL, username, password);

            bufferedWriter = new BufferedWriter(new FileWriter(Configuration.INSTANCE.logPath + "query.log"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void writeLogfile(String message) {
        try {
            bufferedWriter.append(message).append("\n");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public String dump(ResultSet resultSet) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int maximumNumberColumns = resultSetMetaData.getColumnCount();
            int i;
            Object object;

            while (resultSet.next()) {
                for (i = 0; i < maximumNumberColumns; ++i) {
                    object = resultSet.getObject(i + 1);
                    stringBuilder.append(object.toString()).append(" ");
                }
                stringBuilder.append(" \n");
            }

            return stringBuilder.toString();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return "-1";
    }

    public synchronized void queryDump(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            writeLogfile(sqlStatement);
            writeLogfile(dump(resultSet));
            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    // count
    public void executeSQL01() {
        writeLogfile("--- query 01 (count)");
        String sqlStatement = "SELECT COUNT(*) FROM data";
        queryDump(sqlStatement);
    }

    // count, where
    public void executeSQL02() {
        writeLogfile("--- query 02 (count, where)");
        String sqlStatement = "SELECT COUNT(*) FROM data " +
                "WHERE entrance = 2 AND carSize = 'm' AND floor = 3 AND durationInDays >= 10";
        queryDump(sqlStatement);
    }

    // count, where, in
    public void executeSQL03() {
        writeLogfile("--- query 03 (count, where, in)");
        String sqlStatement = "SELECT COUNT(*) FROM data " +
                "WHERE entrance = 1 AND carSize IN ('l','m') AND floor = 2 AND durationInDays <= 3 AND payment = 'cash'";
        queryDump(sqlStatement);
    }

    // count, where, not in
    public void executeSQL04() {
        writeLogfile("--- query 04 (count, where, not in)");
        String sqlStatement = "SELECT COUNT(*) FROM data " +
                "WHERE entrance = 2 AND carSize = 'l' AND floor NOT IN (3,5) AND durationInDays <= 3 AND payment = 'credit'";
        queryDump(sqlStatement);
    }

    // sum, where, in
    public void executeSQL05() {
        writeLogfile("--- query 05 (sum, where, in)");
        String sqlStatement = "SELECT SUM(durationInDays) FROM data " +
                "WHERE floor = 1 AND carSize IN ('s','m') AND parkingSpot <= 5";
        queryDump(sqlStatement);
    }

    // avg, where, not in
    public void executeSQL06() {
        writeLogfile("--- query 06 (avg, where, not in)");
        String sqlStatement = "SELECT AVG(durationInDays) FROM data " +
                "WHERE carSize = 'l' AND floor NOT IN (1,3,5) AND parkingSpot <= 5";
        queryDump(sqlStatement);
    }

    // id, where, in, order by desc limit
    public void executeSQL07() {
        writeLogfile("--- query 07 (id, where, in, order by desc limit)");
        String sqlStatement = "SELECT id FROM data " +
                "WHERE entrance IN (1,3) AND carSize = 's' AND floor = 2 AND parkingSpot <= 3 AND durationInDays >= 2 AND durationInDays <= 5 AND payment = 'credit' " +
                "ORDER BY durationInDays DESC LIMIT 3";
        queryDump(sqlStatement);
    }

    // id, where, in, order by desc, order by asc
    public void executeSQL08() {
        writeLogfile("--- query 08 (id, where, in, order by desc, order by asc)");
        String sqlStatement = "SELECT id FROM data " +
                "WHERE entrance = 3 AND carSize IN ('s','l') AND floor = 4 AND parkingSpot >= 4 AND parkingSpot <= 5 AND durationInDays <= 5 AND payment = 'debit' " +
                "ORDER BY durationInDays DESC, carSize";
        queryDump(sqlStatement);
    }

    // count, group by
    public void executeSQL09() {
        writeLogfile("--- query 09 (count, group by)");
        String sqlStatement = "SELECT floor,COUNT(*) FROM data " +
                "GROUP BY floor";
        queryDump(sqlStatement);
    }

    // count, where, group by
    public void executeSQL10() {
        writeLogfile("--- query 10 (count, where, group by)");
        String sqlStatement = "SELECT payment,COUNT(*) FROM data " +
                "WHERE entrance = 1 AND carSize = 'l' AND parkingSpot >= 4 AND durationInDays <= 5 " +
                "GROUP BY payment";
        queryDump(sqlStatement);
    }

    // count, where, in, group by
    public void executeSQL11() {
        writeLogfile("--- query 11 (count, where, in, group by)");
        String sqlStatement = "SELECT carSize,COUNT(*) FROM data " +
                "WHERE parkingSpot >= 4 AND durationInDays <= 10 AND payment = 'credit' " +
                "GROUP BY carSize";
        queryDump(sqlStatement);
    }

    // count, where, not in, group by
    public void executeSQL12() {
        writeLogfile("--- query 12 (count, where, not in, group by)");
        String sqlStatement = "SELECT durationInDays,COUNT(*) FROM data " +
                "WHERE entrance = 3 AND carSize = 'm' AND parkingSpot NOT IN (2,4) AND durationInDays <= 5 AND payment = 'debit' " +
                "GROUP BY durationInDays";
        queryDump(sqlStatement);
    }

    // sum, where, not in, in, group by
    public void executeSQL13() {
        writeLogfile("--- query 13 (sum, where, not in, in, group by)");
        String sqlStatement = "SELECT parkingSpot,SUM(durationInDays) FROM data " +
                "WHERE entrance NOT IN (1,3) AND carSize IN ('s','m') AND parkingSpot IN (50,100,200,300) AND durationInDays >= 5 " +
                "GROUP BY parkingSpot";
        queryDump(sqlStatement);
    }

    // avg, where, in, in, group by
    public void executeSQL14() {
        writeLogfile("--- query 14 (avg, where, in, in, group by)");
        String sqlStatement = "SELECT carSize,AVG(durationInDays) FROM data " +
                "WHERE entrance IN (1,3) AND carSize IN ('s','m') AND parkingSpot >= 50 AND parkingSpot <= 100 " +
                "GROUP BY carSize";
        queryDump(sqlStatement);
    }

    public void shutdown() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}