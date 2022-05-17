package com.odata1.olingo.impl.jpa.service;

import com.odata1.olingo.impl.jpa.data.CrtData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ICrtRepo extends JpaRepository<CrtData, String> {
}
