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

    public List<Contact> searchContact(String query){
        Connection connection = null;
        List<Contact> contacts = new ArrayList<>();
        try{
            connection = DataBaseUtil.getConnection();
            String sql = "select * from contact where " +
                    "lower(name) like ? or lower(surname) like ? or lower(phone) like ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            String param = "%" + query + "%";
            ps.setString(1, param);
            ps.setString(2, param);
            ps.setString(3, param);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                Contact contact = new Contact();
                contact.setName(resultSet.getString("name"));
                contact.setSurname(resultSet.getString("surname"));
                contact.setPhone(resultSet.getString("phone"));
                contacts.add(contact);
            }
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

        return contacts;
    }

    public boolean editContact(Contact contact) {
        StringBuilder sql = new StringBuilder("UPDATE contact SET ");
        List<Object> parameters = new ArrayList<>();

        // Add fields to update conditionally
        if (contact.getName() != null && !contact.getName().isEmpty()) {
            sql.append("name = ?, ");
            parameters.add(contact.getName());
        }
        if (contact.getSurname() != null && !contact.getSurname().isEmpty()) {
            sql.append("surname = ?, ");
            parameters.add(contact.getSurname());
        }
        // Check if there are fields to update
        if (parameters.isEmpty()) {
            // No fields to update
            return false;
        }

        // Remove trailing comma and space, then add the WHERE clause
        sql.setLength(sql.length() - 2); // Remove the last comma and space
        sql.append(" WHERE phone = ?");
        parameters.add(contact.getPhone()); // Add the phone parameter for the WHERE clause

        Connection connection = null;
        try {
            connection = DataBaseUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql.toString());

            // Set the parameters for the query
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
