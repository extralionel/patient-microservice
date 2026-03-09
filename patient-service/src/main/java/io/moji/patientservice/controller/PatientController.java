package io.moji.patientservice.controller;

import io.moji.patientservice.model.request.PatientRequest;
import io.moji.patientservice.model.response.PatientResponse;
import io.moji.patientservice.model.validator.CreatePatientValidationGroup;
import io.moji.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@Tag(name = "Patient API",description = "CRUD Operations for handling patients.")
@RequestMapping("/patients")
public class PatientController {

  private final PatientService patientService;

  @Autowired
  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @GetMapping
  public ResponseEntity<List<PatientResponse>> getPatients() {
    return ResponseEntity.ok(patientService.getPatients());
  }

  @PostMapping
  public ResponseEntity<PatientResponse> create(
      @Validated({Default.class, CreatePatientValidationGroup.class})
      @RequestBody PatientRequest request
  ) {
    return ResponseEntity.ok(patientService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PatientResponse> updatePatient(
      @PathVariable UUID id,
      @Valid @RequestBody PatientRequest request
  ) {
    return ResponseEntity.ok(patientService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> updatePatient(@PathVariable UUID id) {
    patientService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
