package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message addMessage(Message message){
        return messageDAO.insertMessage(message);

    }
    public Message getMessageById(int message_id){
        return messageDAO.getMessageByID(message_id);
    }

    public Message deleteMessageById(int message_id)
    {
        return messageDAO.deleteMessageById(message_id);
    }
    
    public Message updateMessageById(int message_id, String newText)
    {
        return messageDAO.updateMessageById(message_id, newText);
    }
    
    
}
