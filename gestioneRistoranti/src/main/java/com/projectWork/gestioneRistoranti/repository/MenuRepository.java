package com.projectWork.gestioneRistoranti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.projectWork.gestioneRistoranti.model.Menu;
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>{

}
