package com.projectWork.gestioneRistoranti.repository;

import com.projectWork.gestioneRistoranti.model.UtenteRuolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRuoloRepository extends JpaRepository<UtenteRuolo, Long>{

}
