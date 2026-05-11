package Amadeu.ScraperVagas.service;

import Amadeu.ScraperVagas.model.vaga;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.chat.id}")
    private String chatId;

    public void enviarVagas(List<vaga> vagas) throws Exception {
        if (vagas.isEmpty()) {
            System.out.println("Nenhuma vaga nova para enviar.");
            return;
        }

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("🚀 VAGAS DE TI - CAMPINAS E REMOTO\n\n");

        int contador = 1;
        for (vaga v : vagas) {
            mensagem.append(contador).append(". ").append(v.getTitulo()).append("\n");
            mensagem.append("🏢 ").append(v.getEmpresa()).append("\n");
            mensagem.append("📍 ").append(v.getLocalizacao()).append("\n");
            mensagem.append("🔗 ").append(v.getLink()).append("\n\n");
            contador++;

            if (contador % 10 == 0) {
                enviarMensagem(mensagem.toString());
                mensagem = new StringBuilder();
                Thread.sleep(1000);
            }
        }

        if (mensagem.length() > 0) {
            enviarMensagem(mensagem.toString());
        }
    }

    private void enviarMensagem(String texto) throws Exception {
        String textoEncoded = URLEncoder.encode(texto, StandardCharsets.UTF_8);

        String urlCompleta = "https://api.telegram.org/bot" + token
                + "/sendMessage?chat_id=" + chatId
                + "&text=" + textoEncoded;

        System.out.println("URL base: https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + chatId);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompleta))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Telegram status: " + response.statusCode());
        if (response.statusCode() != 200) {
            System.out.println("Telegram erro: " + response.body());
        }
    }

    public void enviarMensagemFormatada(String texto) throws Exception {
        enviarMensagem(texto);
    }
}