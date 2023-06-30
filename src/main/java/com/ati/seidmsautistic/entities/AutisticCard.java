package com.ati.seidmsautistic.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTISTIC_TB")
public class AutisticCard {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long registrationNumber;
  private LocalDateTime shippingDate;
  private LocalDateTime validity;
  private String name;
  private String filiation;
  private String rg;
  private String naturalness;
  private LocalDateTime dateOfBirth;
  private String seidResponsible;
}
