package com.ati.seidmsautistic.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ati.seidmsautistic.dtos.SolicitationEditDto;
import com.ati.seidmsautistic.entities.Document;
import com.ati.seidmsautistic.entities.Solicitation;
import com.ati.seidmsautistic.enums.DocType;
import com.ati.seidmsautistic.enums.Status;
import com.ati.seidmsautistic.exceptions.AppError;
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
  public Solicitation saveNewSolicitation(Solicitation solicitation, MultipartFile identification,
      MultipartFile picture,
      MultipartFile medicalCertificate, MultipartFile residency,
      List<MultipartFile> complementary) {
    if (solicitationRepository.existsByUserCpf(solicitation.getUser().getCpf())) {
      throw new AppError("This solicitation already exists.");
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
      return savedSolicitation;
    } catch (Exception e) {
      throw new AppError("Erro ao ler o arquivo");
    }
  }

  public Solicitation findSolicitationByUserCpf(String cpf) {
    Optional<Solicitation> optionalSolicitation = solicitationRepository.findByUserCpf(cpf);
    if (optionalSolicitation.isPresent()) {
      Solicitation solicitation = new Solicitation();
      BeanUtils.copyProperties(optionalSolicitation.get(), solicitation);
      return solicitation;
    }
    throw new AppError("Nenhuma solicitação encontrada para este CPF!");
  }

  @Transactional
  public Solicitation updateCiaSolicitation(Solicitation solicitation, MultipartFile identification,
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
          return updatedSolicitation;
        } catch (Exception e) {
          throw new AppError("Erro ao atualizar documentos");
        }
      } else {
        throw new AppError("Status da solicitação não permite atualização");
      }
    }
    throw new AppError("Solicitação não encontrada.");
  }

  public Solicitation giveBackSolicitation(SolicitationEditDto solicitationEditDto) {
    Optional<Solicitation> optionalSolicitation = solicitationRepository
        .findById(solicitationEditDto.getSolicitationId());
    if (optionalSolicitation.isPresent()) {
      Solicitation solicitation = optionalSolicitation.get();
      solicitation.setGeneralObservation(solicitationEditDto.getObservationGeneral());
      solicitation.setStatus(solicitationEditDto.getStatus());
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
        return solicitationRepository.save(optionalSolicitation.get());
      }
      throw new AppError("Erro ao editar soliciatação");
    }
    throw new AppError("Solicitação não encontrada");
  }

  private void setObservation(Document document, DocType docType, String observation) {
    if (document.getDocType() == docType) {
      document.setObservation(observation);
      documentRepository.save(document);
    }
  }

}
