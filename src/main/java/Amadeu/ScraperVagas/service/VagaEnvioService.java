package Amadeu.ScraperVagas.service;

import Amadeu.ScraperVagas.model.vaga;
import Amadeu.ScraperVagas.repository.vagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class VagaEnvioService {

    @Autowired
    private vagaRepository repository;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private VagaEnriquecimentoService enriquecimentoService;

    private final Queue<vaga> filaEnvio = new LinkedList<>();

    public void adicionarNaFila(List<vaga> vagas) {
        filaEnvio.addAll(vagas);
        System.out.println("📥 " + vagas.size() + " vagas adicionadas na fila. Total: " + filaEnvio.size());
    }

    public void enviarProxima() throws Exception {
        // só envia entre 8h e 23h
        LocalTime agora = LocalTime.now();
        if (agora.isBefore(LocalTime.of(8, 0)) || agora.isAfter(LocalTime.of(23, 0))) {
            System.out.println("⏰ Fora do horário de envio (8h-23h).");
            return;
        }

        if (filaEnvio.isEmpty()) {
            System.out.println("📭 Fila vazia, nada para enviar.");
            return;
        }

        vaga v = filaEnvio.poll();

        // gera descrição com IA
        String descricaoIA = enriquecimentoService.gerarDescricao(
                v.getTitulo(), v.getEmpresa(), v.getLocalizacao()
        );

        String mensagem = "💼 *" + v.getTitulo() + "*\n\n" +
                "🏢 *Empresa:* " + v.getEmpresa() + "\n" +
                "📍 *Local:* " + v.getLocalizacao() + "\n\n" +
                descricaoIA + "\n\n" +
                "🔗 " + v.getLink();

        telegramService.enviarMensagemFormatada(mensagem);
        System.out.println("✅ Vaga enviada: " + v.getTitulo());
    }
}