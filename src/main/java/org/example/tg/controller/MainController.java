package org.example.tg.controller;

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
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetChatMenuButton;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.example.PagingUtil;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@BotController
public class MainController implements TelegramMvcController {
    @Value("${telegram.bot.token}")
    private String token;
    List<String> items = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "БУ", "АГА");
    PagingUtil<String> pagingUtil = new PagingUtil<>(items);
    int pageSize = 3;
    int maxPages = (int) Math.ceil(Double.valueOf(items.size()) / pageSize);

    @Override
    public String getToken() {
        return token;
    }


@BotRequest(value = "",type = {MessageType.PRECHECKOUT_QUERY})
public void regUser(User user, Chat chat, Update update){
    System.out.println(user.id() + " " + chat.id() + " " );
}
    @CallbackQueryRequest(value = "callback_data2")
    public BaseRequest hello32(User user, Chat chat, Update update) {
        return new SendMessage(chat.id(), "callback_data2");
    }

    @CallbackQueryRequest(value = "CQ1 {pageNumber:[\\S]+}")
    public BaseRequest hello321(@BotPathVariable("pageNumber") Integer pageNumber, User user, Message message, Chat chat, Update update) {
        int prevPageValue = ((pageNumber > 1) ? (pageNumber - 1) : maxPages);
        int nextPageValue = ((pageNumber < maxPages) ? pageNumber + 1 : 1);

        List<String> page = pagingUtil.getPage(pageNumber, pageSize);
        String result = String.join("\n", page);

        return new EditMessageText(chat.id(), update.callbackQuery().message().messageId(), "" +
                result)
                .replyMarkup(
                        new InlineKeyboardMarkup(
                                new InlineKeyboardButton("←").callbackData("CQ1 " + prevPageValue),
                                new InlineKeyboardButton("Купить").url("www.google.com"),
                                new InlineKeyboardButton("→").callbackData("CQ1 " + (nextPageValue))
                        ).addRow(
                                new InlineKeyboardButton(pageNumber.toString() + "/" + maxPages).callbackData("CQ1 " + pageNumber)
                        )).parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);

    }

    @BotRequest("/start4 ")
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

}
