package io.moji.patientservice.model.request;

import io.moji.patientservice.model.validator.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatientRequest {

  @NotBlank
  @Size(max = 100, message = "Name cannot exceed 100 characters")
  private String name;

  @NotBlank
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank
  @Size(max = 100, message = "Name cannot exceed 100 characters")
  private String address;

  @NotBlank(groups = CreatePatientValidationGroup.class)
  private String registeredDate;

  @NotBlank
  private String birthDate;
}
