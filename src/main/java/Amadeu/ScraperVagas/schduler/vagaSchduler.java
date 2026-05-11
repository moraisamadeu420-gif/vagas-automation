package Amadeu.ScraperVagas.schduler;

import Amadeu.ScraperVagas.service.VagaEnvioService;
import Amadeu.ScraperVagas.service.vagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class vagaSchduler {

    @Autowired
    private vagaService service;

    @Autowired
    private VagaEnvioService envioService;

    // coleta novas vagas a cada 6 horas
    @Scheduled(fixedRate = 21600000)
    public void coletarVagas() throws Exception {
        System.out.println("🔄 Iniciando coleta automática...");
        service.coletarESalvar();
    }

    // envia 1 vaga a cada 5 minutos
    @Scheduled(fixedRate = 180000)
    public void enviarVaga() throws Exception {
        envioService.enviarProxima();
    }
}