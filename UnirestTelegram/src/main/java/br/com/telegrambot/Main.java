package br.com.telegrambot;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mashape.unirest.http.exceptions.UnirestException;

public class Main {
	// aqui deve colocar o token gerado pelo BotFather no telegram
	private static String token = "SEU_TOKEN";

	public static void main(String[] args) {

		TelegramBot tb = new TelegramBot(token);
		try {
			tb.run();
		} catch (UnirestException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
