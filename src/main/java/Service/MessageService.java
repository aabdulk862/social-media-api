package Service;
import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message writeMessage(Message message) {
        // Check if message text is not blank, is not over 255 characters, and posted_by refers to an existing user
        if (message.getMessage_text() != null && !message.getMessage_text().isEmpty() &&
            message.getMessage_text().length() <= 255 && messageDAO.doesUserExist(message.getPosted_by())) {
            // Create the account
            return messageDAO.createMessage(message);
        }
        return null; 
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id){
        return messageDAO.deleteMessageById(id);
    }

    
}
