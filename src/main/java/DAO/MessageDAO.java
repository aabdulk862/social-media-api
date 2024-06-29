package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, message.getPosted_by());
            pstmt.setString(2, message.getMessage_text());
            pstmt.setLong(3, message.getTime_posted_epoch());

            pstmt.executeUpdate();
            ResultSet pkeyResultSet = pstmt.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                
                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }   
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }


    public boolean doesUserExist(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT account_id FROM account WHERE account_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if user exists, false otherwise
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    
}
