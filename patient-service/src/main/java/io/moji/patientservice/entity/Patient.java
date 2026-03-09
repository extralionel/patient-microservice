package io.moji.patientservice.entity;

import io.moji.patientservice.model.request.PatientRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "patient")
public class Patient {

  @Id
  @Column(name = "id", unique = true)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "address")
  private String address;

  @Column(name = "date_of_birth", nullable = false)
  private LocalDate birthDate;

  @Column(name = "registered_date", nullable = false)
  private LocalDate createdAt;

  public Patient(PatientRequest request) {
    this.name = request.getName();
    this.email = request.getEmail();
    this.address = request.getAddress();
    this.birthDate = LocalDate.parse(request.getBirthDate());
    this.createdAt = LocalDate.parse(request.getRegisteredDate());
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    Patient patient = (Patient) o;
    return getId() != null && Objects.equals(getId(), patient.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }
}
