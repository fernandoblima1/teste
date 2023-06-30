package com.ati.seidmsautistic.entities;

import java.time.LocalDateTime;
import com.ati.seidmsautistic.enums.Gender;
import com.ati.seidmsautistic.enums.UF;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private String name;
  private String cpf;
  private String rg;
  private String email;
  private String phone;
  private LocalDateTime dateOfBirth;
  private Gender gender;
  private String naturalness;
  private String nationality;
  private Integer cep;
  private String address;
  private String neighborhood;
  private Integer number;
  private String complement;
  private UF uf;
  private String city;
}
