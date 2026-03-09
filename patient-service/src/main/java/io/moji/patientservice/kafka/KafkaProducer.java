package io.moji.patientservice.kafka;

import io.moji.patientservice.entity.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaProducer {

  private static final String PATIENT_TOPIC_NAME = "patient";
  private static final String PATIENT_CREATED_EVENT = "PATIENT_CREATED";

  private final KafkaTemplate<String, byte[]> kafkaTemplate;

  public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendEvent(Patient patient) {
    PatientEvent event = PatientEvent.newBuilder()
        .setPatientId(patient.getId().toString())
        .setName(patient.getName())
        .setEmail(patient.getEmail())
        .setEventType(PATIENT_CREATED_EVENT)
        .build();

    try {
      kafkaTemplate.send(PATIENT_TOPIC_NAME, event.toByteArray());
    } catch (Exception e) {
      log.error("Error sending event: {}", PATIENT_CREATED_EVENT, e);
    }
  }
}
