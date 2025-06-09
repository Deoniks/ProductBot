package com.example.ProductBot.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotInit {

    private final TelegramLongPollingBot bot;

    public BotInit(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @PostConstruct
    public void start() throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
    }
}
