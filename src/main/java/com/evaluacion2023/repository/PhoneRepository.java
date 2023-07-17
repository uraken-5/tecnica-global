package com.evaluacion2023.repository;

import com.evaluacion2023.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PhoneRepository extends JpaRepository<Phone, Long> {
    
}
