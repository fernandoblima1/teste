package com.ati.seidmsautistic.entities;

import java.util.UUID;
import com.ati.seidmsautistic.enums.DocType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "DOCUMENT_TB")
public class Document {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String name;
  private Long size;
  @ManyToOne
  @JoinColumn(name = "solicitation_id")
  private Solicitation solicitation;
  private String observation;
  private String diretory;
  private DocType docType;
}
