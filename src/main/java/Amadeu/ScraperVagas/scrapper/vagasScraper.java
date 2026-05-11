package Amadeu.ScraperVagas.scrapper;

import Amadeu.ScraperVagas.model.vaga;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class vagasScraper {

    @Value("${adzuna.app.id}")
    private String appId;

    @Value("${adzuna.app.key}")
    private String appKey;

    public List<vaga> buscarVagas() throws Exception {
        List<vaga> vagas = new ArrayList<>();

        String[] termos = {"desenvolvedor java", "estagio ti", "backend", "frontend"};

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        for (String termo : termos) {
            String url = "https://api.adzuna.com/v1/api/jobs/br/search/1"
                    + "?app_id=" + appId
                    + "&app_key=" + appKey
                    + "&what=" + termo.replace(" ", "%20")
                    + "&where=Campinas"
                    + "&results_per_page=20"
                    + "&content-type=application/json";

            System.out.println("Buscando: " + termo);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());

            if (response.statusCode() != 200) {
                System.out.println("Erro na API: " + response.body());
                continue;
            }

            JsonNode root = mapper.readTree(response.body());
            JsonNode results = root.path("results");

            System.out.println("Vagas encontradas para '" + termo + "': " + results.size());

            for (JsonNode job : results) {
                vaga v = new vaga();
                v.setTitulo(job.path("title").asText());
                v.setEmpresa(job.path("company").path("display_name").asText());
                v.setLocalizacao(job.path("location").path("display_name").asText());
                v.setLink(job.path("redirect_url").asText());
                v.setDataColeta(LocalDateTime.now());
                vagas.add(v);
            }

            Thread.sleep(1000);
        }

        // busca remoto também
        for (String termo : termos) {
            String url = "https://api.adzuna.com/v1/api/jobs/br/search/1"
                    + "?app_id=" + appId
                    + "&app_key=" + appKey
                    + "&what=" + termo.replace(" ", "%20")
                    + "&what_or=remoto%20remote"
                    + "&results_per_page=20"
                    + "&content-type=application/json";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) continue;

            JsonNode root = mapper.readTree(response.body());
            JsonNode results = root.path("results");

            System.out.println("Vagas remotas para '" + termo + "': " + results.size());

            for (JsonNode job : results) {
                vaga v = new vaga();
                v.setTitulo(job.path("title").asText());
                v.setEmpresa(job.path("company").path("display_name").asText());
                v.setLocalizacao(job.path("location").path("display_name").asText());
                v.setLink(job.path("redirect_url").asText());
                v.setDataColeta(LocalDateTime.now());
                vagas.add(v);
            }

            Thread.sleep(1000);
        }

        return vagas;
    }
}