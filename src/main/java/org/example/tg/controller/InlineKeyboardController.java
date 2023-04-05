package org.example.tg.controller;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.TelegramRequest;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.Resource;
import org.example.service.ProductService;
import org.example.tg.dto.ContentInlineKeyboardDto;
import org.example.tg.service.InlineKeyboardService;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@BotController
public class InlineKeyboardController implements TelegramMvcController {
    @Resource
    private ProductService productService;
    @Resource
    private InlineKeyboardService inlineKeyboardService;

    @Value("${telegram.bot.token}")
    private String token;

    @Resource
    private ReplyKeyboardController replyKeyboardController;

    @Override
    public String getToken() {
        return token;
    }

    @CallbackQueryRequest(value = "productsShowList {categoryId:[\\S]+}")
    public BaseRequest showProductByCategoryId(@BotPathVariable("categoryId") Long categoryId, @BotPathVariable("categoryName") List<String> categoryName, User user, Chat chat, Update update) {
        ContentInlineKeyboardDto result = inlineKeyboardService.showProductsByCategoryId(categoryId);
        return new EditMessageText(chat.id(), update.callbackQuery().message().messageId(), result.getText()).replyMarkup(result.getInlineKeyboardMarkup());
    }

    //Товары(каталог)
    @CallbackQueryRequest(value = "catalogShowList")
    BaseRequest productsCatalogs(String text,
                                 TelegramRequest telegramRequest,
                                 TelegramBot telegramBot,
                                 Update update,
                                 Message message,
                                 Chat chat,
                                 User user
    ) {
        InlineKeyboardMarkup content = inlineKeyboardService.findAllCategories();
            return new EditMessageText(chat.id(), update.callbackQuery().message().messageId(), "Выберите категорию: ").replyMarkup(
                    content);
    }

    //Товары(списком)
    @CallbackQueryRequest(value = "productsCatalogList")
    BaseRequest productsCatalogList(String text,
                                    TelegramRequest telegramRequest,
                                    TelegramBot telegramBot,
                                    Update update,
                                    Message message,
                                    Chat chat,
                                    User user
    ) {
        ContentInlineKeyboardDto contentInlineKeyboardDto = inlineKeyboardService.findAllCategoriesAndProduct();
        return new EditMessageText(chat.id(), update.callbackQuery().message().messageId(), contentInlineKeyboardDto.getText()).replyMarkup(
                contentInlineKeyboardDto.getInlineKeyboardMarkup()
        );
    }
    @CallbackQueryRequest(value = "productShowListButton {categoryId:[\\S]+}")
    public BaseRequest showButtonsProductListByCategoryId(@BotPathVariable("categoryId") Long categoryId, User user, Chat chat, Update update) {
        ContentInlineKeyboardDto result = inlineKeyboardService.showButtonsProductListByCategoryId(categoryId);
        return new EditMessageText(chat.id(), update.callbackQuery().message().messageId(), result.getText()).replyMarkup(result.getInlineKeyboardMarkup());
    }
    @CallbackQueryRequest(value = "productBuyMenu {productId:[\\S]+}")
    public BaseRequest showProductBuyMenuByProductId(@BotPathVariable("productId") Long productId, User user, Chat chat, Update update) {
        ContentInlineKeyboardDto result = inlineKeyboardService.showProductBuyMenuByProductId(productId);
        return new EditMessageText(chat.id(), update.callbackQuery().message().messageId(), result.getText()).replyMarkup(result.getInlineKeyboardMarkup());
    }
    @CallbackQueryRequest(value = "buyProduct {productId:[\\S]+} {count:[\\S]+}")
    public BaseRequest buyProduct(@BotPathVariable("productId") Long productId, @BotPathVariable("count") Long count, User user, Chat chat, Update update) {
        ContentInlineKeyboardDto result = inlineKeyboardService.prePaymentButtons(productId,count,user);
        return new EditMessageText(chat.id(), update.callbackQuery().message().messageId(), result.getText()).replyMarkup(result.getInlineKeyboardMarkup());
    }
}
