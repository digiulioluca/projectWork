package com.projectWork.gestioneRistoranti.repository;

import com.projectWork.gestioneRistoranti.model.OrdineDettagli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineDettagliRepository extends JpaRepository<OrdineDettagli, Long>{

}
