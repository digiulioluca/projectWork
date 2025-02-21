package com.projectWork.gestioneRistoranti.repository;

import com.projectWork.gestioneRistoranti.model.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long>{

}
