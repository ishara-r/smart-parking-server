
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlAccess {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public JSONArray readDataBase(String query) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://database-1.cu6qfjgwrjm5.us-west-2.rds.amazonaws.com:3306/demoprojectdb?"
                            + "user=admin&password=1qazxsw23");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery(query);
            return writeResultSet(resultSet);
//
//            // PreparedStatements can use variables and are more efficient
//            preparedStatement = connect
//                    .prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
//            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
//            // Parameters start with 1
//            preparedStatement.setString(1, "Test");
//            preparedStatement.setString(2, "TestEmail");
//            preparedStatement.setString(3, "TestWebpage");
//            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
//            preparedStatement.setString(5, "TestSummary");
//            preparedStatement.setString(6, "TestComment");
//            preparedStatement.executeUpdate();
//
//            preparedStatement = connect
//                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
//            resultSet = preparedStatement.executeQuery();
//            writeResultSet(resultSet);
//
//            // Remove again the insert comment
//            preparedStatement = connect
//                    .prepareStatement("delete from feedback.comments where myuser= ? ; ");
//            preparedStatement.setString(1, "Test");
//            preparedStatement.executeUpdate();
//
//            resultSet = statement
//                    .executeQuery("select * from feedback.comments");
//            writeMetaData(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public JSONObject updateVehicleIn(String plate) throws Exception {
        try {
            JSONArray reservations = readDataBase("SELECT * FROM demoprojectdb.reserved_slot order by res_id desc where vehi_num = `"+plate+"`;");
            if (reservations.length() > 0) {
                JSONObject reservation = reservations.getJSONObject(0);


                // This will load the MySQL driver, each DB has its own driver
                Class.forName("com.mysql.jdbc.Driver");
                // Setup the connection with the DB
                connect = DriverManager
                        .getConnection("jdbc:mysql://database-1.cu6qfjgwrjm5.us-west-2.rds.amazonaws.com:3306/demoprojectdb?"
                                + "user=admin&password=1qazxsw23");

//
                // PreparedStatements can use variables and are more efficient
                PreparedStatement preparedStatementUpdateReservedSlot = connect
                        .prepareStatement("UPDATE `demoprojectdb`.`reserved_slot` SET `status` = ? WHERE `vehi_num` = ?;");
                // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
                // Parameters start with 1
                preparedStatementUpdateReservedSlot.setString(1, "DONE");
                preparedStatementUpdateReservedSlot.setString(2, plate);
                preparedStatementUpdateReservedSlot.executeUpdate();

                // PreparedStatements can use variables and are more efficient
                PreparedStatement preparedStatementUpdateSlotsTable = connect
                        .prepareStatement("UPDATE `demoprojectdb`.`parking_slots` SET `slot_status` = ? WHERE `slot_id` = ?;");
                // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
                // Parameters start with 1
                preparedStatementUpdateSlotsTable.setString(1, "RE_DONE");
                preparedStatementUpdateSlotsTable.setString(2, reservation.getString("slotId"));
                preparedStatementUpdateSlotsTable.executeUpdate();

                return reservation;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public void blockParkingSlot(String slotId) throws Exception {
        try {

            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://database-1.cu6qfjgwrjm5.us-west-2.rds.amazonaws.com:3306/demoprojectdb?"
                            + "user=admin&password=1qazxsw23");

            // PreparedStatements can use variables and are more efficient
            PreparedStatement preparedStatementUpdateSlotsTable = connect
                    .prepareStatement("UPDATE `demoprojectdb`.`parking_slots` SET `slot_status` = ? WHERE `slot_id` = ?;");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatementUpdateSlotsTable.setString(1, "NOT-AVAILABLE");
            preparedStatementUpdateSlotsTable.setString(2, slotId);
            preparedStatementUpdateSlotsTable.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public void releaseParkingSlot(String slotId) throws Exception {
        try {

            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://database-1.cu6qfjgwrjm5.us-west-2.rds.amazonaws.com:3306/demoprojectdb?"
                            + "user=admin&password=1qazxsw23");
            // PreparedStatements can use variables and are more efficient
            PreparedStatement preparedStatementUpdateSlotsTable = connect
                    .prepareStatement("UPDATE `demoprojectdb`.`parking_slots` SET `slot_status` = ? WHERE `slot_id` = ?;");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatementUpdateSlotsTable.setString(1, "AVAILABLE");
            preparedStatementUpdateSlotsTable.setString(2, slotId);
            preparedStatementUpdateSlotsTable.executeUpdate();


            // PreparedStatements can use variables and are more efficient
            PreparedStatement deleteReservation = connect
                    .prepareStatement("DELETE FROM `demoprojectdb`.`reserved_slot` WHERE slotId = ? ;");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            deleteReservation.setString(1, slotId);
            deleteReservation.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public String updateVehicleOut(String plate) throws Exception {
        try {
            JSONArray reservations = readDataBase("SELECT * FROM demoprojectdb.reserved_slot order by res_id desc");
            if (reservations.length() > 0) {
                JSONObject reservation = reservations.getJSONObject(0);


                // This will load the MySQL driver, each DB has its own driver
                Class.forName("com.mysql.jdbc.Driver");
                // Setup the connection with the DB
                connect = DriverManager
                        .getConnection("jdbc:mysql://database-1.cu6qfjgwrjm5.us-west-2.rds.amazonaws.com:3306/demoprojectdb?"
                                + "user=admin&password=1qazxsw23");

//
                // PreparedStatements can use variables and are more efficient
                PreparedStatement preparedStatementUpdateReservedSlot = connect
                        .prepareStatement("DELETE FROM `demoprojectdb`.`reserved_slot` WHERE `vehi_num`= ? ;");
                // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
                // Parameters start with 1
                preparedStatementUpdateReservedSlot.setString(1, plate);
                preparedStatementUpdateReservedSlot.executeUpdate();

                // PreparedStatements can use variables and are more efficient
                PreparedStatement preparedStatementUpdateSlotsTable = connect
                        .prepareStatement("UPDATE `demoprojectdb`.`parking_slots` SET `slot_status` = ? WHERE `slot_id` = ?;");
                // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
                // Parameters start with 1
                preparedStatementUpdateSlotsTable.setString(1, "AVAILABLE");
                preparedStatementUpdateSlotsTable.setString(2, reservation.getString("slotId"));
                preparedStatementUpdateSlotsTable.executeUpdate();

                return "Reserved-parking-done";
            } else {
                return "Not-reserved";
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //  Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
        }
    }

    private JSONArray writeResultSet(ResultSet resultSet) throws SQLException {
        JSONArray ja = new JSONArray();
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            JSONObject jo = new JSONObject();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i) {
                if (resultSet.getMetaData().getColumnType(i) == 4) {
                    jo.put(resultSet.getMetaData().getColumnName(i), resultSet.getInt(resultSet.getMetaData().getColumnName(i)));
                }

                if (resultSet.getMetaData().getColumnType(i) == 12) {
                    jo.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(resultSet.getMetaData().getColumnName(i)));
                }
            }
            ja.put(jo);


//            String user = resultSet.getString("myuser");
//            String website = resultSet.getString("webpage");
//            String summary = resultSet.getString("summary");
//            Date date = resultSet.getDate("datum");
//            String comment = resultSet.getString("comments");
//            System.out.println("User: " + user);
//            System.out.println("Website: " + website);
//            System.out.println("summary: " + summary);
//            System.out.println("Date: " + date);
//            System.out.println("Comment: " + comment);
        }
        return ja;
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    public void reservationSeen(int reservation_id, String slotId) throws SQLException, ClassNotFoundException {

        try {

            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://database-1.cu6qfjgwrjm5.us-west-2.rds.amazonaws.com:3306/demoprojectdb?"
                            + "user=admin&password=1qazxsw23");

            //updateSlot
            PreparedStatement preparedStatementUpdateSlotsTable = connect
                    .prepareStatement("UPDATE `demoprojectdb`.`parking_slots` SET `slot_status` = ? WHERE `slot_id` = ?;");
            preparedStatementUpdateSlotsTable.setString(1, "RESERVED");
            preparedStatementUpdateSlotsTable.setString(2, slotId);
            preparedStatementUpdateSlotsTable.executeUpdate();

            // update reservation table
            PreparedStatement preparedStatementUpdateReservedSlot = connect
                    .prepareStatement("UPDATE `demoprojectdb`.`reserved_slot` SET `status` = ? WHERE `res_id` = ?;");
            preparedStatementUpdateReservedSlot.setString(1, "RESERVATION-SEEN");
            preparedStatementUpdateReservedSlot.setInt(2, reservation_id);
            preparedStatementUpdateReservedSlot.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
}
