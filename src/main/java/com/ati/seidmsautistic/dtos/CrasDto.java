package com.ati.seidmsautistic.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CrasDto {
  private String name;
  private String address;
  private Integer cep;
}
