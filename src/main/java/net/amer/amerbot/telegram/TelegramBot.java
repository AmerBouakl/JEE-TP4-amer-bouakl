package net.amer.amerbot.telegram;

import jakarta.annotation.PostConstruct;
import net.amer.amerbot.agents.AIAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String telegramBotToken;
    private final AIAgent aiAgent;

    public TelegramBot(AIAgent aiAgent, @Value("${telegram.api.key}") String telegramBotToken) {
        super(telegramBotToken);
        this.aiAgent = aiAgent;
        this.telegramBotToken = telegramBotToken;
    }

    @PostConstruct
    public void registerTelegramBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update telegraRequest) {
        if (!telegraRequest.hasMessage())
            return;
        Long chatId = telegraRequest.getMessage().getChatId();
        try {
            String messageText = telegraRequest.getMessage().getText();
            sendTypingQuestion(chatId);
            String answer = aiAgent.askAgent(messageText);
            sendTextMessage(chatId, answer);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                sendTextMessage(chatId, "Error: " + e.getMessage());
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "AmerAiBot";
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    private void sendTextMessage(long chatId, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        execute(sendMessage);
    }

    private void sendTypingQuestion(long chatId) throws TelegramApiException {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(String.valueOf(chatId));
        sendChatAction.setAction(ActionType.TYPING);
        execute(sendChatAction);
    }
}
