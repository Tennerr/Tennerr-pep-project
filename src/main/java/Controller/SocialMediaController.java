package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    MessageService messageService;
    AccountService accountService;
    public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}",this::getMessageByIdHandler);
        app.post("/messages", this::postMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.get("/accounts/{account_id}/messages",this::getMessageByAccountIdHandler);
        return app;
    }

    private void postAccountHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        String passwordText = mapper.readTree(context.body()).get("password").asText();
        String usernameText = mapper.readTree(context.body()).get("username").asText();
        
        Account addedAccount = accountService.createAccount(account);
        if(addedAccount == null || passwordText.length() < 4 || usernameText.isBlank())
        {
            context.status(400);
        }
        else
        {
            context.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void postLoginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account login = mapper.readValue(context.body(), Account.class);
        
        Account account = accountService.verifyLogin(login.getUsername(),login.getPassword());
        if(account == null)
        {
            context.status(401);
        }
        else
        {
            context.json(mapper.writeValueAsString(account));
        }
    }

   private void postMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        String messageText = mapper.readTree(context.body()).get("message_text").asText();
        
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage == null || messageText.isBlank())
        {
            context.status(400);
        }
        else
        {
            context.json(mapper.writeValueAsString(addedMessage));
        }


   }

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages); 
    }

    private void getMessageByIdHandler(Context context){
        int m_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(m_id);
        if(message == null)
        {
            context.status(200);
        }
        else{
            context.json(message);
        }
        
    }

    private void deleteMessageHandler(Context context)
    {
        int m_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessageById(m_id);
        if(message == null)
        {
            context.status(200);
        }
        else{
            context.json(message);
        }
    }

    private void updateMessageHandler(Context context)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        String newMessageText = mapper.readTree(context.body()).get("message_text").asText();
        Message updatedMessage = messageService.updateMessageById(message_id,newMessageText);
        if(updatedMessage == null || newMessageText.length() > 255 || newMessageText.isBlank()){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    private void getMessageByAccountIdHandler(Context context){
        int a_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = accountService.getMessageByAccountId(a_id);
        if(messages == null)
        {
            context.status(200);
        }
        else{
            context.json(messages);
        }
        
    }
        
    
        
        


}