package Amadeu.ScraperVagas.service;

import org.springframework.stereotype.Service;

@Service
public class VagaEnriquecimentoService {

    public String gerarDescricao(String tituloVaga, String empresa, String localizacao) {
        return "📋 Acesse o link para ver os requisitos completos!\n" +
                "🏢 Empresa: " + empresa + "\n" +
                "📍 Local: " + localizacao + "\n" +
                "✨ Candidate-se agora e boa sorte!";
    }
}