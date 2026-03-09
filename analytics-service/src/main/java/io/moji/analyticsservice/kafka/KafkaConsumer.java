package io.moji.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaConsumer {

  @KafkaListener(topics = "patient", groupId = "analytics-service")
  public void consumeEvent(byte[] payload) {
    try {
      PatientEvent event = PatientEvent.parseFrom(payload);
      log.info("Received patientEvent: patientId: {}, patientName: {}",
          event.getPatientId(),
          event.getName());
    } catch (InvalidProtocolBufferException e) {
      log.error("Error deserializing event {}", e.getMessage(), e);
    }
  }
}
