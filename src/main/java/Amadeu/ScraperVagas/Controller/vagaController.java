package Amadeu.ScraperVagas.Controller;

import Amadeu.ScraperVagas.model.vaga;
import Amadeu.ScraperVagas.service.VagaEnvioService;
import Amadeu.ScraperVagas.service.vagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vagas")
public class vagaController {

    @Autowired
    private vagaService service;

    @Autowired
    private VagaEnvioService envioService;

    @GetMapping
    public List<vaga> listar() {
        return service.listarTodas();
    }

    @PostMapping("/coletar")
    public String coletar() throws Exception {
        service.coletarESalvar();
        return "Coleta concluída!";
    }

    @PostMapping("/enviar-teste")
    public String enviarTeste() throws Exception {
        List<vaga> todas = service.listarTodas();
        if (!todas.isEmpty()) {
            envioService.adicionarNaFila(todas.subList(0, Math.min(5, todas.size())));
            return "5 vagas adicionadas na fila!";
        }
        return "Sem vagas no banco.";
    }
}