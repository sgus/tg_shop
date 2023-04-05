package org.example.tg.controller;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.TelegramRequest;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.Resource;
import org.example.service.CategoryService;
import org.example.tg.dto.ContentInlineKeyboardDto;
import org.example.tg.service.InlineKeyboardService;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@BotController
public class ReplyKeyboardController implements TelegramMvcController {
    @Value("${telegram.bot.token}")
    private String token;
    @Resource
    private CategoryService categoryService;

    @Resource
    private InlineKeyboardService inlineKeyboardService;

    @Override
    public String getToken() {
        return token;
    }

    @BotRequest(value = {"*"}, type = {MessageType.MESSAGE})
    BaseRequest mainManu1(String text,
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
        return new SendMessage(chat.id(), "Главное меню").replyMarkup(new ReplyKeyboardMarkup(
                new String[]{"Товары(каталог)", "Товары(списком)"},
//                new String[]{"Купить"},
                new String[]{"Личный кабинет", "Пополнить баланс"},
                new String[]{"Помощь", "Чат", "Реферальная система"}
        )
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true));       // optional);
//        return hello321(1,user,message,chat,update);
    }

    @BotRequest(value = {"Главное меню"}, type = {MessageType.CALLBACK_QUERY})
    BaseRequest mainManu2(String text,
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
        return new SendMessage(chat.id(), "Главное меню").replyMarkup(new ReplyKeyboardMarkup(
                new String[]{"Товары(каталог)", "Товары(списком)"},
                new String[]{"Личный кабинет", "Пополнить баланс"},
                new String[]{"Помощь", "Чат", "Реферальная система"}
        )
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true));       // optional);
//        return hello321(1,user,message,chat,update);
    }

    //Товары(каталог)
    @BotRequest(value = "Товары(каталог)", type = {MessageType.MESSAGE})
    BaseRequest productsCatalogs(String text,
                                 TelegramRequest telegramRequest,
                                 TelegramBot telegramBot,
                                 Update update,
                                 Message message,
                                 Chat chat,
                                 User user
    ) {
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboardService.findAllCategories();
        return new SendMessage(chat.id(), "Выберите категорию:").replyMarkup(
                inlineKeyboardMarkup
        );
    }
    @BotRequest(value = "Товары(списком)", type = {MessageType.MESSAGE})
    BaseRequest productsCatalogList(String text,
                                 TelegramRequest telegramRequest,
                                 TelegramBot telegramBot,
                                 Update update,
                                 Message message,
                                 Chat chat,
                                 User user
    ) {
        ContentInlineKeyboardDto contentInlineKeyboardDto = inlineKeyboardService.findAllCategoriesAndProduct();
        return new SendMessage(chat.id(), contentInlineKeyboardDto.getText()).replyMarkup(
                contentInlineKeyboardDto.getInlineKeyboardMarkup()
        );
    }
    @BotRequest(value = "Личный кабинет", type = {MessageType.MESSAGE})
    BaseRequest userAccount(String text,
                                 TelegramRequest telegramRequest,
                                 TelegramBot telegramBot,
                                 Update update,
                                 Message message,
                                 Chat chat,
                                 User user
    ) {
        ContentInlineKeyboardDto contentInlineKeyboardDto = inlineKeyboardService.accountInfo(user);
        return new SendMessage(chat.id(), contentInlineKeyboardDto.getText()).replyMarkup(
                contentInlineKeyboardDto.getInlineKeyboardMarkup()
        );
    }
    @BotRequest(value = "Помощь", type = {MessageType.MESSAGE})
    BaseRequest help1(String text,
                                 Chat chat
    ) {
        return new SendMessage(chat.id(), "По вопросам пишите @george1u");
    }
    @BotRequest(value = "Чат", type = {MessageType.MESSAGE})
    BaseRequest chat1(String text,
                                 Chat chat
    ) {
        return new SendMessage(chat.id(), "Чата еще нет");
    }
    @BotRequest(value = "Реферальная система", type = {MessageType.MESSAGE})
    BaseRequest referLink(String text,
                                 Chat chat
    ) {
        return new SendMessage(chat.id(), "Недоступна");
    }
}

