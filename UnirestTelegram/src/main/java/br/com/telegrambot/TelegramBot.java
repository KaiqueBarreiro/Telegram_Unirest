package br.com.telegrambot;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public final class TelegramBot {

	private final String endpoint = "https://api.telegram.org/";
	private final String token;

	public TelegramBot(String token) {
		this.token = token;
	}

	// responsável por enviar a mensagem ao usuario correspondente
	public HttpResponse<JsonNode> sendMessage(Integer chatId, String text) throws UnirestException {
		return Unirest.post(this.endpoint + "bot" + this.token + "/sendMessage").field("chat_id", chatId)
				.field("text", text).asJson();
	}

	// responsável por atualizar o id de filas para evitar que replique varias
	// vezes o mesmo id
	public HttpResponse<JsonNode> getUpdates(Integer offset) throws UnirestException {
		return Unirest.post(this.endpoint + "bot" + this.token + "/getUpdates").field("offset", offset).asJson();
	}

	public void run() throws UnirestException {
		// controle das mensagens processadas
		int last_update_id = 0;
		HttpResponse<JsonNode> response;
		// pode ser trocado por um timertask, schedule..
		while (true) {
			response = getUpdates(last_update_id++);
			if (response.getStatus() == 200) {
				// variavel que recebe a mensagem enviada pelo usuario
				JSONArray responses = response.getBody().getObject().getJSONArray("result");
				if (responses.isNull(0)) {
					continue;
				} else {
					// atualiza o id para não repetir as mensagem recebidas
					last_update_id = responses.getJSONObject(responses.length() - 1).getInt("update_id") + 1;
				}
				for (int i = 0; i < responses.length(); i++) {

					JSONObject message = responses.getJSONObject(i).getJSONObject("message");
					// pega o id de quem enviou a mensagem
					int chat_id = message.getJSONObject("chat").getInt("id");
					// pega o nome do usuario do telegram
					String usuario = message.getJSONObject("chat").getString("first_name") + " "
							+ message.getJSONObject("chat").getString("last_name");
					// pega a mensagem digitada no telegram
					String texto = message.getString("text");
					String textoInvertido = "";

					for (int j = texto.length() - 1; j >= 0; j--) {
						textoInvertido += texto.charAt(j);
					}
					//envia a mensagem para o usuario com seu nome que esta no telegram e seu texto invertido
					sendMessage(chat_id, "Olá, " + usuario + ". Sua mensagem invertida fica assim: " + textoInvertido);
				}
			}
		}
	}
}