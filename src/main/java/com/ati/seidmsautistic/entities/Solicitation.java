package com.ati.seidmsautistic.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import com.ati.seidmsautistic.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "SOLICITATION_TB")
public class Solicitation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "solicitation_id")
  private UUID solicitationId;

  @Embedded
  private User user;

  // Details to business rules
  private Boolean Bpc;
  private Boolean minorOldAplicant;
  private Boolean statementOfResponsibility;

  // Details to observe
  private Status status;
  private String generalObservation;

  // Details to log
  private LocalDateTime createdAt;
  private LocalDateTime lastUpdatedAt;
  private LocalDateTime approvedAt;
  private LocalDateTime lastSuspendedAt;
  private LocalDateTime rejectedAt;
}
