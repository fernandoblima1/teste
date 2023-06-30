package com.ati.seidmsautistic.dtos;

import com.ati.seidmsautistic.entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitationDto {
  @NotNull
  private User user;
  @NotNull
  private Boolean minorOldAplicant;
  @NotNull
  private Boolean statementOfResponsibility;
}
