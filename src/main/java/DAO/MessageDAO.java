package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class MessageDAO {
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            // Select all from Message table and store it in a List
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        // Generate neccessary values for message body
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch ) VALUES (?,?,?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            // Create a message
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }

    public Message getMessageByID(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();
        try {
            
            String sql = "SELECT * FROM Message WHERE message_id = (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            // If message exists store in Message object
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;
        try {
            // Retrieve the message before deleting it
            String selectSql = "SELECT * FROM Message WHERE message_id = (?);";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, message_id);
            ResultSet rs = selectStatement.executeQuery();
    
            // If the message exists, store it in the deletedMessage object
            if (rs.next()) {
                deletedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
    
                // Delete the message
                String deleteSql = "DELETE FROM Message WHERE message_id = (?);";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, message_id);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // Return the deleted message, or null if it wasn't found
        return deletedMessage;
    }

    public Message updateMessageById(int message_id, String newText) {
        Connection connection = ConnectionUtil.getConnection();
        System.out.println(newText);
        try {
            String sql = "UPDATE message SET message_text=? WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,newText);
            preparedStatement.setInt(2,message_id);

            preparedStatement.executeUpdate();
            return getMessageByID(message_id);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    

    
    


}
