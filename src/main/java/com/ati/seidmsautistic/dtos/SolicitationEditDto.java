package com.ati.seidmsautistic.dtos;

import java.util.UUID;

import com.ati.seidmsautistic.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitationEditDto {
  private UUID solicitationId;
  private Status status;
  private String observationGeneral;
  private String observationComplementary;
  private String observationIdentification;
  private String observationPicture;
  private String observationResidency;
  private String observationmedicalCertificate;
}
