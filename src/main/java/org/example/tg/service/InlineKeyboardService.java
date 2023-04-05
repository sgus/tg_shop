package org.example.tg.service;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import jakarta.annotation.Resource;
import org.example.entity.Category;
import org.example.entity.Product;
import org.example.service.CategoryService;
import org.example.service.ProductService;
import org.example.tg.dto.CategoryProductDto;
import org.example.tg.dto.ContentInlineKeyboardDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InlineKeyboardService {
    @Resource
    private CategoryService categoryService;
    @Resource
    private ProductService productService;

    public ContentInlineKeyboardDto findAllCategoriesAndProduct() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Product> productList = productService.findProductsByCategoryContains();
        StringBuilder text = new StringBuilder();
        Map<Category, List<Product>> productByCategory = productList.stream()
                .collect(Collectors.groupingBy(Product::getCategory));
        productByCategory.forEach((category, products) -> {
            text.append("┌"+category.getName()+"\n");
            products.forEach(product -> text.append("├"+product.getName()  + " ┃ "  + product.getPrice()  + " ┃ "  + product.getQuantity()+  " шт. \n"));
            text.append("└─────────\n");
        });
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Купить").callbackData("catalogShowList"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Главное меню").callbackData("Главное меню"));

        return new ContentInlineKeyboardDto(text.toString(), inlineKeyboardMarkup);
    }

    public InlineKeyboardMarkup findAllCategories() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Category> categories = categoryService.findAll();
        for (int i = categories.size() - 1; i >= 0; i -= 2) {
            InlineKeyboardButton button1 = new InlineKeyboardButton(categories.get(i).getName())
                    .callbackData("productsShowList " + categories.get(i).getId());
            if (i - 1 > 0) {
                InlineKeyboardButton button2 = new InlineKeyboardButton(categories.get(i - 1).getName())
                        .callbackData("productsShowList " + categories.get(i - 1).getId());
                inlineKeyboardMarkup.addRow(button1, button2);
            } else {
                inlineKeyboardMarkup.addRow(button1);
            }
        }

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Товары(списком)").callbackData("productsCatalogList"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Главное меню").callbackData("Главное меню"));
        return inlineKeyboardMarkup;
    }
    public ContentInlineKeyboardDto showProductsByCategoryId(Long categoryId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Product> byCategoryId = productService.findByCategoryId(categoryId);
        InlineKeyboardButton buyButton1 = new InlineKeyboardButton("Купить").callbackData("productShowListButton " + categoryId);
        InlineKeyboardButton backButton2 = new InlineKeyboardButton("Назад(в категории)").callbackData("catalogShowList");
        inlineKeyboardMarkup
                .addRow(buyButton1)
                .addRow(backButton2);
        StringBuffer text = new StringBuffer();
        text.append("Товары в этой категории: \n");
        byCategoryId.stream().forEach(product -> text.append(product.getName() + "   " + product.getQuantity() + "шт.\n"));
        return new ContentInlineKeyboardDto(text.toString(), inlineKeyboardMarkup);
    }

    public ContentInlineKeyboardDto showButtonsProductListByCategoryId(Long categoryId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Product> byCategoryId = productService.findByCategoryId(categoryId);
        String categoryName = categoryService.findById(categoryId).getName();
        byCategoryId.stream().forEach(product -> inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(product.getName() + "   " + product.getQuantity() + "шт.").callbackData("productBuyMenu " + product.getId())));
        StringBuffer text = new StringBuffer();
        text.append(categoryName);
        InlineKeyboardButton backButton2 = new InlineKeyboardButton("Назад(в категории)").callbackData("catalogShowList");
        inlineKeyboardMarkup.addRow(backButton2);
        return new ContentInlineKeyboardDto(text.toString(), inlineKeyboardMarkup);
    }

    public ContentInlineKeyboardDto showProductBuyMenuByProductId(Long productId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        Product byId = productService.findById(productId).get();
        int minQuantity = Math.min(byId.getQuantity(), 10);
        InlineKeyboardButton[] row1 = new InlineKeyboardButton[(minQuantity <= 5) ? minQuantity : 5];
        InlineKeyboardButton[] row2 = new InlineKeyboardButton[(minQuantity > 5) ? minQuantity - 5 : 0];
        for (int i = 0; i < minQuantity; i++) {
            if ((i <= 4)) {
                row1[i] = new InlineKeyboardButton((i + 1) + " шт.").callbackData("buyProduct " + byId.getId() + " " + (i + 1));
            } else {
                row2[i - 5] = new InlineKeyboardButton((i + 1) + " шт.").callbackData("buyProduct " + byId.getId() + " " + (i + 1));
            }
        }
        inlineKeyboardMarkup.addRow(row1);
        if (byId.getQuantity() > 5)
            inlineKeyboardMarkup.addRow(row2);
        StringBuffer text = new StringBuffer();
        text.append("Товар: " + byId.getName() + "\n" +
                "Описание: " + byId.getDescription() + "\n" +
                "Осталось: " + byId.getQuantity() + "\n" +
                "Цена за шт.: " + byId.getPrice() + "р." + "\n" + "\n" +
                "Введите необходимое кол-во товара или, для быстрой покупки, нажмите одну из кнопок ниже.");
        InlineKeyboardButton backButton2 = new InlineKeyboardButton("Назад(в категории)").callbackData("catalogShowList");
        inlineKeyboardMarkup.addRow(backButton2);
        return new ContentInlineKeyboardDto(text.toString(), inlineKeyboardMarkup);
    }

    public ContentInlineKeyboardDto prePaymentButtons(Long productId, Long count, User user) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        Product byId = productService.findById(productId).get();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("С баланса (456.18 руб.)").callbackData("buyProduct "));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Киви, Карта").callbackData("buyProduct "));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("USDT").callbackData("buyProduct "));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("ЮMoney, Карта").callbackData("buyProduct "));
        StringBuffer text = new StringBuffer();
        text.append("Товар: " + byId.getName() + "\n" +
                "Описание: " + byId.getDescription() + "\n \n" +
                "К оплате: " + (byId.getPrice().multiply(new BigDecimal(count))) + "р." + "\n" + "\n" +
                "Выберите способ оплаты: ");
        InlineKeyboardButton backButton2 = new InlineKeyboardButton("Назад(в категории)").callbackData("catalogShowList");
        inlineKeyboardMarkup.addRow(backButton2);
        return new ContentInlineKeyboardDto(text.toString(), inlineKeyboardMarkup);
    }

    public ContentInlineKeyboardDto accountInfo(User user) {
        StringBuffer text = new StringBuffer();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Пополнить баланс").callbackData("buyProduct ")); //todo
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("История моих покупок \uD83D\uDCE6").callbackData("Главное меню"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Главное меню").callbackData("Главное меню"));
        text.append("➖➖➖➖➖➖➖➖➖➖➖\n" +
                "   ℹ️ Информация о вас:\n" +
                "   \uD83D\uDD11 Логин: @"+ user.username()+" \n" +
                "   \uD83D\uDCB3 ID: "+ user.id()+"\n" +
                "   \uD83E\uDEE1 Ваш статус: Обычный пользователь\n" +  //todo
                "Потратьте ещё 4033 руб, чтобы получить статус \uD83D\uDC8EPremium, который даст вам персональную скидку 10% на все товары!\n" +
                "   \uD83D\uDCB5 Покупок на сумму: 967 руб  \n" +
                "   \n" +
                "\n" +
                "\uD83C\uDFE6 Баланс: 456.18 руб.\n" +
                "➖➖➖➖➖➖➖➖➖➖➖");
        return new ContentInlineKeyboardDto(text.toString(), inlineKeyboardMarkup);

    }
}
