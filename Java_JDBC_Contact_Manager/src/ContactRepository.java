/**
 * DataBase bilan ishlaydigan class larimiz REPOSITORY deyiladi,
 * ya'ni u DB ga ma'lumot qo'shadi, o'chiradi, edit qiladi va hokozo...
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    public boolean saveContact(Contact contact){
        Connection connection = DataBaseUtil.getConnection();

        String sql = "insert into contact(name, surname, phone) values(?,?,?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getSurname());
            ps.setString(3, contact.getPhone());
            ps.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkPhoneNumber(String phone){
        Connection connection = null;
        try {
            connection = DataBaseUtil.getConnection();
            String sql = "select count(*) from contact where phone =?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()){
                int count = resultSet.getInt(1);
                System.out.println("count is " + count);
                if (count == 1){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public List<Contact> getAllContacts(){
        Connection connection = null;
        List<Contact> contacts = new ArrayList<>();
        try {
            connection = DataBaseUtil.getConnection();
            String sql = "select * from contact";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Contact contact = new Contact();
                contact.setName(resultSet.getString("name"));
                contact.setSurname(resultSet.getString("surname"));
                contact.setPhone(resultSet.getString("phone"));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return contacts;
    }

    public int deleteContact(String phone){
        Connection connection = null;
        try {
            connection = DataBaseUtil.getConnection();
            String sql = "delete from contact where phone =?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phone);
            int effectedRow = ps.executeUpdate();
            return effectedRow;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return 0;
    }
}
