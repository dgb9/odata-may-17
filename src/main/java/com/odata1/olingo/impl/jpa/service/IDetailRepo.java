package com.odata1.olingo.impl.jpa.service;

import com.odata1.olingo.impl.jpa.data.DetailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface IDetailRepo extends JpaRepository<DetailData, String> {
}
