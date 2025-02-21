package com.projectWork.gestioneRistoranti.repository;

import com.projectWork.gestioneRistoranti.model.FotoPiatto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoPiattoRepository extends JpaRepository<FotoPiatto, Long>{

}
