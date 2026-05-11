package Amadeu.ScraperVagas.service;

import Amadeu.ScraperVagas.model.vaga;
import Amadeu.ScraperVagas.repository.vagaRepository;
import Amadeu.ScraperVagas.scrapper.vagasScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class vagaService {

    @Autowired
    private vagasScraper scraper;

    @Autowired
    private vagaRepository repository;

    @Autowired
    private VagaEnvioService envioService;

    public void coletarESalvar() throws Exception {
        List<vaga> vagas = scraper.buscarVagas();
        List<vaga> vagasNovas = new ArrayList<>();

        for (vaga v : vagas) {
            boolean jaExiste = repository.existsByLink(v.getLink());
            if (!jaExiste) {
                repository.save(v);
                vagasNovas.add(v);
            }
        }

        System.out.println("✅ " + vagasNovas.size() + " vagas novas salvas.");
        envioService.adicionarNaFila(vagasNovas);
    }

    public List<vaga> listarTodas() {
        return repository.findAll();
    }

}