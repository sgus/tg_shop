package org.example.tg.dto;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class ContentInlineKeyboardDto {
    private String text;
    private InlineKeyboardMarkup inlineKeyboardMarkup;

    public ContentInlineKeyboardDto() {
    }

    public ContentInlineKeyboardDto(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        this.text = text;
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        return inlineKeyboardMarkup;
    }

    public void setInlineKeyboardMarkup(InlineKeyboardMarkup inlineKeyboardMarkup) {
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
    }
}
