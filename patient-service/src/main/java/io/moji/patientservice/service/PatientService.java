package io.moji.patientservice.service;

import io.moji.patientservice.entity.Patient;
import io.moji.patientservice.exception.EmailAlreadyExistsException;
import io.moji.patientservice.exception.PatientNotFoundException;
import io.moji.patientservice.grpc.BillingServiceGrpcClient;
import io.moji.patientservice.kafka.KafkaProducer;
import io.moji.patientservice.model.request.PatientRequest;
import io.moji.patientservice.model.response.PatientResponse;
import io.moji.patientservice.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PatientService {

  private final PatientRepository patientRepository;
  private final BillingServiceGrpcClient billingServiceClient;
  private final KafkaProducer kafkaProducer;

  public PatientService(PatientRepository patientRepository,
      BillingServiceGrpcClient billingServiceClient, KafkaProducer kafkaProducer) {
    this.patientRepository = patientRepository;
    this.billingServiceClient = billingServiceClient;
    this.kafkaProducer = kafkaProducer;
  }

  public List<PatientResponse> getPatients() {
    return patientRepository.findAll().stream()
        .map(PatientResponse::new)
        .collect(Collectors.toList());
  }

  public PatientResponse create(PatientRequest request) {
    if (patientRepository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyExistsException("Patient already exists with that email");
    }

    Patient patient = patientRepository.save(new Patient(request));
    billingServiceClient.createBillingAccount(patient.getId().toString(), patient.getName(),
        patient.getEmail());
    kafkaProducer.sendEvent(patient);

    return new PatientResponse(patient);
  }

  public PatientResponse update(UUID id, PatientRequest request) {
    Patient patient = patientRepository.findById(id).orElseThrow(
        () -> new PatientNotFoundException("Patient not found with ID: " + id));

    if (!Objects.equals(patient.getEmail(), request.getEmail()) && patientRepository.existsByEmail(
        request.getEmail())) {
      throw new EmailAlreadyExistsException("Patient already exists with that email");
    }

    patient.setName(request.getName());
    patient.setAddress(request.getAddress());
    patient.setEmail(request.getEmail());
    patient.setBirthDate(LocalDate.parse(request.getBirthDate()));

    return new PatientResponse(patientRepository.save(patient));
  }

  public void delete(UUID id) {
    patientRepository.deleteById(id);
  }
}
