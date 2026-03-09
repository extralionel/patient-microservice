package io.moji.patientservice.repository;

import io.moji.patientservice.entity.Patient;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

  boolean existsByEmail(String email);
}