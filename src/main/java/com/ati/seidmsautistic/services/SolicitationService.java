package com.ati.seidmsautistic.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ati.seidmsautistic.dtos.SolicitationEditDto;
import com.ati.seidmsautistic.entities.Document;
import com.ati.seidmsautistic.entities.Solicitation;
import com.ati.seidmsautistic.enums.DocType;
import com.ati.seidmsautistic.enums.Status;
import com.ati.seidmsautistic.repositories.DocumentRepository;
import com.ati.seidmsautistic.repositories.SolicitationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitationService {

  private final SolicitationRepository solicitationRepository;
  private final DocumentService documentService;
  private final DocumentRepository documentRepository;

  @Transactional
  public ResponseEntity<Object> saveNewSolicitation(Solicitation solicitation, MultipartFile identification,
      MultipartFile picture,
      MultipartFile medicalCertificate, MultipartFile residency,
      List<MultipartFile> complementary) {
    if (solicitationRepository.existsByUserCpf(solicitation.getUser().getCpf())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("This solicitation already exists.");
    }
    try {
      solicitation.setStatus(Status.SENT);
      solicitation.setCreatedAt(LocalDateTime.now());
      Solicitation savedSolicitation = solicitationRepository.save(solicitation);
      documentService.saveDocument(savedSolicitation, identification, DocType.ID,
          savedSolicitation.getSolicitationId());
      documentService.saveDocument(savedSolicitation, picture, DocType.PICTURE, savedSolicitation.getSolicitationId());
      documentService.saveDocument(savedSolicitation, medicalCertificate, DocType.MEDICALCERTIFICATE,
          savedSolicitation.getSolicitationId());
      documentService.saveDocument(savedSolicitation, residency, DocType.RESIDENCY,
          savedSolicitation.getSolicitationId());

      for (MultipartFile complement : complementary) {
        documentService.saveDocument(savedSolicitation, complement, DocType.COMPLEMENTARY,
            savedSolicitation.getSolicitationId());
      }
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao ler o arquivo");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o formulário");
    }
    return ResponseEntity.ok("Formulário e documentos salvos com sucesso!");
  }

  public ResponseEntity<Object> findSolicitationByUserCpf(String cpf) {
    Optional<Solicitation> optionalSolicitation = solicitationRepository.findByUserCpf(cpf);
    if (optionalSolicitation.isPresent()) {
      Solicitation solicitation = new Solicitation();
      BeanUtils.copyProperties(optionalSolicitation.get(), solicitation);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(solicitation);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitation not found.");
  }

  @Transactional
  public ResponseEntity<Object> updateCiaSolicitation(Solicitation solicitation, MultipartFile identification,
      MultipartFile picture, MultipartFile medicalCertificate, MultipartFile residency,
      List<MultipartFile> complementary) {
    Optional<Solicitation> optionalSolicitation = solicitationRepository.findById(solicitation.getSolicitationId());
    if (optionalSolicitation.isPresent()) {
      Solicitation updatedSolicitation = optionalSolicitation.get();
      if (updatedSolicitation.getStatus() == Status.SUSPENDED
          || updatedSolicitation.getStatus() == Status.SUSPENDED) {
        try {
          updatedSolicitation.setStatus(Status.SENT);
          updatedSolicitation.setLastUpdatedAt(LocalDateTime.now());
          updatedSolicitation.setGeneralObservation("");
          solicitationRepository.save(updatedSolicitation);
          documentService.updateDocument(updatedSolicitation, identification, DocType.ID);
          documentService.updateDocument(updatedSolicitation, picture, DocType.PICTURE);
          documentService.updateDocument(updatedSolicitation, medicalCertificate,
              DocType.MEDICALCERTIFICATE);
          documentService.updateDocument(updatedSolicitation, residency, DocType.RESIDENCY);
          if (complementary != null && !complementary.isEmpty()) {
            documentService.updateMultipleDocuments(updatedSolicitation, complementary,
                DocType.COMPLEMENTARY);
          }
          return ResponseEntity.ok("Formulário e Documentos salvos com sucesso!");
        } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar documentos");
        }
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status da solicitação não permite atualização");
      }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitação não encontrada.");
  }

  public ResponseEntity<Object> repproveSolicitation(SolicitationEditDto solicitationEditDto) {
    Optional<Solicitation> optionalSolicitation = solicitationRepository
        .findById(solicitationEditDto.getSolicitationId());
    if (optionalSolicitation.isPresent()) {
      Solicitation solicitation = optionalSolicitation.get();
      solicitation.setGeneralObservation(solicitationEditDto.getObservationGeneral());
      solicitation.setStatus(Status.REJECTED);
      solicitation.setRejectedAt(LocalDateTime.now());
      List<Document> optionalDocumentList = documentService.findAllDocument(solicitation.getSolicitationId());
      if (!(optionalDocumentList == null)) {
        for (Document document : optionalDocumentList) {
          setObservation(document, DocType.ID, solicitationEditDto.getObservationIdentification());
          setObservation(document, DocType.MEDICALCERTIFICATE, solicitationEditDto.getObservationmedicalCertificate());
          setObservation(document, DocType.PICTURE, solicitationEditDto.getObservationPicture());
          setObservation(document, DocType.RESIDENCY, solicitationEditDto.getObservationResidency());
          setObservation(document, DocType.COMPLEMENTARY, solicitationEditDto.getObservationComplementary());
        }
        return ResponseEntity.status(HttpStatus.OK).body(solicitationRepository.save(optionalSolicitation.get()));
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documents not found");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitation not found");
  }

  public ResponseEntity<Object> suspendSolicitation(SolicitationEditDto solicitationEditDto) {
    Optional<Solicitation> optionalSolicitation = solicitationRepository
        .findById(solicitationEditDto.getSolicitationId());
    if (optionalSolicitation.isPresent()) {
      Solicitation solicitation = optionalSolicitation.get();
      solicitation.setGeneralObservation(solicitationEditDto.getObservationGeneral());
      solicitation.setStatus(Status.SUSPENDED);
      solicitation.setRejectedAt(LocalDateTime.now());
      List<Document> optionalDocumentList = documentService.findAllDocument(solicitation.getSolicitationId());
      if (!(optionalDocumentList == null)) {
        for (Document document : optionalDocumentList) {
          setObservation(document, DocType.ID, solicitationEditDto.getObservationIdentification());
          setObservation(document, DocType.MEDICALCERTIFICATE, solicitationEditDto.getObservationmedicalCertificate());
          setObservation(document, DocType.PICTURE, solicitationEditDto.getObservationPicture());
          setObservation(document, DocType.RESIDENCY, solicitationEditDto.getObservationResidency());
          setObservation(document, DocType.COMPLEMENTARY, solicitationEditDto.getObservationComplementary());
        }
        return ResponseEntity.status(HttpStatus.OK).body(solicitationRepository.save(optionalSolicitation.get()));
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documents not found");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitation not found");
  }

  private void setObservation(Document document, DocType docType, String observation) {
    if (document.getDocType() == docType) {
      document.setObservation(observation);
      documentRepository.save(document);
    }
  }

}
