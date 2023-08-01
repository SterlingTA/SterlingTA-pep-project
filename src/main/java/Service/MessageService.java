package Service;

import java.util.List;

import DAO.AccountDao;
import DAO.MessageDao;
import Model.Message;

public class MessageService {

    private MessageDao messageDao;
    private AccountDao accountDao;

    public MessageService() {
        messageDao = new MessageDao();
        accountDao = new AccountDao();
    }

    public MessageService(MessageDao messageDao, AccountDao accountDao) {
        this.messageDao = messageDao;
        this.accountDao = accountDao;
    }

    public Message addMessage(Message message) {
        int message_textL = message.getMessage_text().length();

        if (message_textL > 0 && message_textL < 255 && accountDao.findUserById(message.getPosted_by()) != null)
            return messageDao.insertMessage(message);

        return null;
    }

    public List<Message> getAllMessages() {
        return messageDao.selectAllMessages();
    }

    public Message getMessage(int message_id) {
        return messageDao.selectMessage(message_id);
    }

    public Message removeMessage(int message_id) {
        Message message = messageDao.selectMessage(message_id);
        if (message != null) {
            messageDao.deleteMessage(message_id);
            return message;
        }
        return null;
    }

    public Message changeMessage(int message_id, String message_text) {
        if (message_text.length() > 0 && message_text.length() < 255) {
            Message message = messageDao.selectMessage(message_id);
            if (message != null) {
                messageDao.updateMessage(message_id, message_text);
                message.setMessage_text(message_text);
                return message;
            }
        }
        return null;
    }

    public List<Message> getUserMessages(int user_id) {
        return messageDao.selectUserMessages(user_id);
    }

}
