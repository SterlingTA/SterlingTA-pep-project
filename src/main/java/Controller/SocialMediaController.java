package Controller;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */

    AccountService as;
    MessageService ms;

    public SocialMediaController() {
        as = new AccountService();
        ms = new MessageService();
    }

    public SocialMediaController(AccountService as, MessageService ms) {
        this.as = as;
        this.ms = ms;
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::newAccount);
        app.post("/login", this::loginUser);
        app.post("/messages", this::postMessage);
        app.get("/messages", this::getMessages);
        app.get("/messages/{message_id}", this::getMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getUserMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    // context.json("sample text");
    // }

    private void newAccount(Context context) {
        Account account = as.addAccount(context.bodyAsClass(Account.class));
        if (account != null) {
            context.json(account);
            context.status(200);
        } else {
            context.status(400);
        }

    }

    private void loginUser(Context context) {
        Account account = as.loginAccount(context.bodyAsClass(Account.class));
        if (account != null) {
            context.json(account);
            context.status(200);
        } else {
            context.status(401);
        }
    }

    private void postMessage(Context context) {
        Message message = ms.addMessage(context.bodyAsClass(Message.class));
        if (message != null) {
            context.json(message);
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void getMessages(Context context) {
        context.json(ms.getAllMessages());
        context.status(200);
    }

    private void getMessage(Context context) {
        Message message = ms.getMessage(
                Integer.parseInt(
                        Objects.requireNonNull(context.pathParam("message_id"))));
        if (message != null)
            context.json(message);
        context.status(200);
    }

    private void deleteMessage(Context context) {
        Message message = ms.removeMessage(
                Integer.parseInt(
                        Objects.requireNonNull(context.pathParam("message_id"))));
        if (message != null)
            context.json(message);
        context.status(200);
    }

    private void updateMessage(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectNode on = new ObjectMapper().readValue(context.body(), ObjectNode.class);
        String msg = on.get("message_text").asText();
        Message message = ms.changeMessage(
                Integer.parseInt(
                        Objects.requireNonNull(context.pathParam("message_id"))),
                msg);
        if (message != null) {
            context.json(message);
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void getUserMessages(Context context) {
        context.json(ms.getUserMessages(Integer.parseInt(
                Objects.requireNonNull(context.pathParam("account_id")))));
        context.status(200);
    }

}