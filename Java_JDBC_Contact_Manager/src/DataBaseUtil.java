import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseUtil {
    public static void createTable(){
        String sql = "create table if not exists contact( "
                + "id serial primary key, "
                + "name varchar(20) not null,"
                + "surname varchar(20) not null,"
                + "phone varchar(20) not null unique" +")";

        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver"); // <1>
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/java_jdbc_contact_manager", "postgres", "19940102"); // <2>
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
