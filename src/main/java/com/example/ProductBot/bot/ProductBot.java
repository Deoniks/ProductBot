package com.example.ProductBot.bot;

import com.example.ProductBot.domain.Product;
import com.example.ProductBot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductBot extends TelegramLongPollingBot {

    private final ProductRepository productRepository;
    private final BotConfig config;


    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            if(text.equalsIgnoreCase("/list")){
                List<Product> products = productRepository.findAll();
                StringBuilder response = new StringBuilder("📦 Товары в наличии:\n");
                for (Product product : products){
                    response.append("- ").append(product.getName())
                            .append(" (Цена: ").append(product.getPrice()).append(")\n");
                }
                sendMessage(chatId, response.toString());
            }else if (text.startsWith("/add")){
                String rest = text.length() > 5 ? text.substring(5).trim() : "";

                if(rest.isEmpty()){
                    //sendMessage(chatId, "ℹ️ Использование: /add <название> <цена>");
                } else {

                    String[] parts = rest.split(" ");
                    if(parts.length == 2){
                        try {
                            String name = parts[0];
                            double price = Double.parseDouble(parts[1]);
                            productRepository.save(new Product(null, name, price));
                            sendMessage(chatId, "✅ Товар добавлен:" + name);
                        }catch (Exception e){
                            sendMessage(chatId, "⚠️ Ошибка: неверный формат цены.");
                        }
                    }else   {
                        sendMessage(chatId, "ℹ️ Использование: /add <название> <цена>");
                    }
                }
            }else if(text.startsWith("/remove")){
                String name = text.substring(8).trim();
                productRepository.deleteByName(name);
                sendMessage(chatId, "🗑️ Удалён товар: " + name);
            }else {
                sendMessage(chatId, "❓ Неизвестная команда. Используйте /list, /add, /remove");
            }
        }
    }

    private void sendMessage(String chatId, String text){
        try {
            execute(SendMessage.builder().chatId(chatId).text(text).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
