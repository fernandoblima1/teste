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
@Table(name = "CULTURE_FREEPASS_TB")
public class CultureFreePass {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cardNumber;
  private String name;
  private String rg;
  private String deficiency;
  private LocalDateTime dateOfBirth;
  private LocalDateTime validity;
}
