package io.moji.patientservice.model.response;

import io.moji.patientservice.entity.Patient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatientResponse {
  private String id;
  private String name;
  private String email;
  private String address;
  private String dateOfBirth;

  public PatientResponse(Patient patient) {
    this.id = patient.getId().toString();
    this.name = patient.getName();
    this.email = patient.getEmail();
    this.address = patient.getAddress();
    this.dateOfBirth = patient.getBirthDate().toString();
  }


}
