package org.example;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.TelegramRequest;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@BotController
@SpringBootApplication
public class Main implements TelegramMvcController {
    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @BotRequest(value = "/hello", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest hello(User user, Chat chat) {
        return new SendMessage(chat.id(), "Hello " + user.firstName());
    }

    @BotRequest("/start1")
    BaseRequest hello1(String text,
                       TelegramRequest telegramRequest,
                       TelegramBot telegramBot,
                       Update update,
                       Message message,
                       Chat chat,
                       User user
    ) {

        return new SendMessage(chat.id(), "I test the bot");
    }

    @BotRequest("/start2")
    BaseRequest hello2(String text,
                       TelegramRequest telegramRequest,
                       TelegramBot telegramBot,
                       Update update,
                       Message message,
                       Chat chat,
                       User user
    ) {
        telegramRequest.setCallback(new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
                System.out.println(1111);
                new SendMessage(chat.id(), "onResponse");
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {

            }
        });
        return new SendMessage(chat.id(), "I test the bot").replyMarkup(new ReplyKeyboardMarkup(
                new String[]{"first row button1", "first row button2"},
                new String[]{"second row button1", "second row button2"})
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true));       // optional);
    }
    @BotRequest("/start3")
    BaseRequest hello3(String text,
                       TelegramRequest telegramRequest,
                       TelegramBot telegramBot,
                       Update update,
                       Message message,
                       Chat chat,
                       User user
    ) {

        return new SendMessage(chat.id(), "I test the bot").replyMarkup(
                new InlineKeyboardMarkup(
                        new InlineKeyboardButton("url").url("www.google.com"),
                        new InlineKeyboardButton("callback_data").callbackData("callback_data"),
                        new InlineKeyboardButton("callback_data2").callbackData("callback_data2"),
//                        new InlineKeyboardButton("pay").pay(),
                        new InlineKeyboardButton("hello4").callbackData("hello4"),
                        new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query")));
    }
    @CallbackQueryRequest(value = "callback_data2")
    public BaseRequest hello32(User user, Chat chat) {
        return new SendMessage(chat.id(), "callback_data2");
    }
  @BotRequest(value = "callback_data", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest hello321(User user, Message message,Chat chat) {
      return new EditMessageText(chat.id(),message.messageId(), "callback_data").replyMarkup(
              new InlineKeyboardMarkup(
                      new InlineKeyboardButton("url").url("www.google.com"),
                      new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query")));
    }

    @BotRequest("/start4")
    BaseRequest hello4(String text,
                       TelegramRequest telegramRequest,
                       TelegramBot telegramBot,
                       Update update,
                       Message message,
                       Chat chat,
                       User user
    ) {

        return new SendMessage(chat.id(), "I test the bot").replyMarkup(
                new ReplyKeyboardMarkup(
                        new KeyboardButton("text"),
                        new KeyboardButton("text1").requestPoll(new KeyboardButtonPollType(Poll.Type.quiz)),
                        new KeyboardButton("text2"),
                        new KeyboardButton("contact").requestContact(true),
                        new KeyboardButton("location").requestLocation(true)));
    }

    @MessageRequest("/hello {name:[\\S]+}")
    public String helloWithName(@BotPathVariable("name") String userName) {
        // Return a string if you need to reply with a simple message
        return "Hello, " + userName;
    }

    @MessageRequest
    public void logMessages(TelegramRequest request, User user) {
        System.out.println(request);
    }

    @MessageRequest("/hell1")
    public BaseRequest helloWithCustomCallback1(TelegramRequest request, User user) {
        SetChatMenuButton setChatMenuButton = new SetChatMenuButton();

        request.setCallback(new Callback() {
            @Override
            public void onResponse(BaseRequest request, BaseResponse response) {
                // TODO

                setChatMenuButton.chatId((Long) request.getParameters().get("chat_id"));
                setChatMenuButton.menuButton(new MenuButton("asd"));
                setChatMenuButton.isMultipart();
                setChatMenuButton.toWebhookResponse();
                System.out.println(request.toString());
                System.out.println(response.toString());
            }

            //            @Override
//            public void onResponse(GetUpdates request, GetUpdatesResponse response) {
//                // TODO
//                List<Update> updates = response.updates();
//
//                setChatMenuButton.chatId((Long) request.getParameters().get("chat_id"));
//                setChatMenuButton.menuButton(new MenuButton("asd"));
//                setChatMenuButton.isMultipart();
//                setChatMenuButton.toWebhookResponse();
//                System.out.println(request.toString());
//                System.out.println(response.toString());
//            }
            @Override
            public void onFailure(BaseRequest request, IOException e) {
                // TODO
                System.out.println(request.toString());
            }
        });
        return setChatMenuButton;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}