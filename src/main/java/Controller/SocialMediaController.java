package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService  messageService;
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postNewAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::createNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getAllMessagesByIdHandler);
        app.delete("/messages/{message_id}", this::deleteAllMessagesByIdHandler);
        app.patch("/messages/{message_id}", this::updateAllMessagesByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postNewAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.register(account);
        if(addedAccount==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.login(account);
        if(addedAccount==null){
            context.status(401);
        }else{
            context.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void createNewMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.writeMessage(message);
        if(addedMessage==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(addedMessage));
        }
    }

    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
       context.json(messageService.getAllMessages());
    }

    private void getAllMessagesByIdHandler(Context context) throws JsonProcessingException {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            context.status(200).result(""); // Empty response body
        } else {
            context.status(200).json(message);
        }
    }

    private void deleteAllMessagesByIdHandler(Context context) throws JsonProcessingException {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessageById(messageId);
        if (message == null) {
            context.status(200).result(""); // Empty response body
        } else {
            context.status(200).json(message);
        }
    }

    private void updateAllMessagesByIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.valueOf(context.pathParam("message_id"));
        String text = mapper.readTree(context.body()).get("message_text").asText();
        Message message = messageService.updateMessageById(text,messageId);
        if (message == null) {
            context.status(400); // Empty response body
        } else {
            context.status(200).json(message);
        }
    }

    private void getAllMessagesByAccountIdHandler(Context context) throws JsonProcessingException {
        int id = Integer.valueOf(context.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesById(id);
        if (messages.isEmpty()) {
            context.status(200).json(new ArrayList<>()); // Return an empty array if no messages are found
        } else {
            context.json(messages);
        }
    }

}