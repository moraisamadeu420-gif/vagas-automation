package Amadeu.ScraperVagas.repository;

import Amadeu.ScraperVagas.model.vaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface vagaRepository extends JpaRepository<vaga, Long> {
    boolean existsByLink(String link);
}