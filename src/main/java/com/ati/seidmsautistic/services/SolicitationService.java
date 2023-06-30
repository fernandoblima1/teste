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
import com.ati.seidmsautistic.entities.Solicitation;
import com.ati.seidmsautistic.enums.DocType;
import com.ati.seidmsautistic.enums.Status;
import com.ati.seidmsautistic.repositories.SolicitationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitationService {

  private final SolicitationRepository solicitationRepository;
  private final DocumentService documentService;

  @Transactional
  public ResponseEntity<Object> saveNewSolicitation(Solicitation solicitation, MultipartFile identification,
      MultipartFile picture,
      MultipartFile medicalCertificate, MultipartFile residency,
      List<MultipartFile> complementary) {
    if (solicitationRepository.existsByUserCpf(solicitation.getUser().getCpf())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("This solicitation already exists.");
    }
    try {
      solicitation.setStatus(Status.SENT_TO_CRAS);
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

  // @Transactional
  // public ResponseEntity<Object> updatePassSolicitation(Solicitation
  // solicitation, MultipartFile identification,
  // MultipartFile picture,
  // MultipartFile medicalCertificate, MultipartFile residency,
  // List<MultipartFile> complementary) {
  // Optional<Solicitation> optionalSolicitation =
  // solicitationRepository.findById(solicitation.getSolicitationId());
  // if (optionalSolicitation.isPresent()) {
  // Solicitation updatedSolicitation = optionalSolicitation.get();
  // if (updatedSolicitation.getStatus() == Status.SUSPENDED_BY_CRAS
  // || updatedSolicitation.getStatus() == Status.SUSPENDED_BY_SEID) {
  // try {
  // updatedSolicitation.setStatus(Status.SENT_TO_CRAS);
  // updatedSolicitation.setLastUpdatedAt(LocalDateTime.now());
  // updatedSolicitation.setGeneralObservation("");
  // solicitationRepository.save(updatedSolicitation);
  // updateDocument(updatedSolicitation, identification, DocType.ID);
  // updateDocument(updatedSolicitation, picture, DocType.PICTURE);
  // updateDocument(updatedSolicitation, medicalCertificate,
  // DocType.MEDICALCERTIFICATE);
  // updateDocument(updatedSolicitation, residency, DocType.RESIDENCY);
  // if (complementary != null && !complementary.isEmpty()) {
  // documentRepository.deleteAllBySolicitationAndType(updatedSolicitation,
  // DocType.COMPLEMENTARY);
  // updateMultipleDocuments(updatedSolicitation, complementary,
  // DocType.COMPLEMENTARY);
  // }
  // return ResponseEntity.ok("Formulário e Documentos salvos com sucesso!");
  // } catch (Exception e) {
  // e.printStackTrace();
  // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar
  // documentos");
  // }
  // } else {
  // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status da
  // solicitação não permite atualização");
  // }
  // }
  // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitação não
  // encontrada.");
  // }

  // @Transactional
  // public ResponseEntity<Object> updateCiaSolicitation(Solicitation
  // solicitation, MultipartFile identification,
  // MultipartFile picture,
  // MultipartFile medicalCertificate, MultipartFile residency,
  // List<MultipartFile> complementary) {
  // Optional<Solicitation> optionalSolicitation =
  // solicitationRepository.findById(solicitation.getSolicitationId());
  // if (optionalSolicitation.isPresent()) {
  // Solicitation updatedSolicitation = optionalSolicitation.get();
  // if (updatedSolicitation.getStatus() == Status.SUSPENDED_BY_CRAS
  // || updatedSolicitation.getStatus() == Status.SUSPENDED_BY_SEID) {
  // try {
  // updatedSolicitation.setStatus(Status.SENT_TO_CRAS);
  // updatedSolicitation.setLastUpdatedAt(LocalDateTime.now());
  // updatedSolicitation.setGeneralObservation("");
  // solicitationRepository.save(updatedSolicitation);
  // updateDocument(updatedSolicitation, identification, DocType.ID);
  // updateDocument(updatedSolicitation, picture, DocType.PICTURE);
  // updateDocument(updatedSolicitatifileDiron, medicalCertificate,
  // DocType.MEDICALCERTIFICATE);
  // updateDocument(updatedSolicitation, residency, DocType.RESIDENCY);
  // if (complementary != null && !complementary.isEmpty()) {
  // documentRepository.deleteAllBySolicitationAndType(updatedSolicitation,
  // DocType.COMPLEMENTARY);
  // updateMultipleDocuments(updatedSolicitation, complementary,
  // DocType.COMPLEMENTARY);
  // }
  // return ResponseEntity.ok("Formulário e Documentos salvos com sucesso!");
  // } catch (Exception e) {
  // e.printStackTrace();
  // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar
  // documentos");
  // }
  // } else {
  // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status da
  // solicitação não permite atualização");
  // }
  // }
  // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitação não
  // encontrada.");
  // }

  // private void updateDocument(Solicitation solicitation, MultipartFile file,
  // DocType docType) throws IOException {
  // if (file != null && !file.isEmpty()) {
  // Optional<Document> optionalDocument =
  // documentRepository.findBySolicitationAndType(solicitation, docType);
  // if (optionalDocument.isPresent()) {
  // Document document = optionalDocument.get();
  // document.setName(file.getOriginalFilename());
  // document.setSize(file.getSize());
  // document.setBytesInBase64(Base64.getEncoder().encodeToString(file.getBytes()));
  // documentRepository.save(document);
  // } else {
  // // Criar um novo documento caso não exista
  // saveDocument(solicitation, file, docType);
  // }
  // }
  // }

  // private void updateMultipleDocuments(Solicitation solicitation,
  // List<MultipartFile> files, DocType docType)
  // throws IOException {
  // if (files != null && !files.isEmpty()) {
  // List<Document> existingDocuments =
  // documentRepository.findAllBySolicitationAndType(solicitation, docType);
  // for (MultipartFile file : files) {
  // Optional<Document> optionalDocument = getMatchingDocument(existingDocuments,
  // file.getOriginalFilename());
  // if (optionalDocument.isPresent()) {
  // Document document = optionalDocument.get();
  // document.setName(file.getOriginalFilename());
  // document.setSize(file.getSize());
  // document.setBytesInBase64(Base64.getEncoder().encodeToString(file.getBytes()));
  // documentRepository.save(document);
  // } else {
  // // Criar um novo documento caso não exista
  // saveDocument(solicitation, file, docType);
  // }
  // }
  // }
  // }

  // private Optional<Document> getMatchingDocument(List<Document> documents,
  // String filename) {
  // return documents.stream().filter(document ->
  // document.getName().equals(filename)).findFirst();
  // }

  // public ResponseEntity<Object> updateStatus(UUID solicitationId, Status
  // status) {
  // Optional<Solicitation> optionalSolicitation =
  // solicitationRepository.findById(solicitationId);
  // if (optionalSolicitation.isPresent()) {
  // Solicitation solicitation = optionalSolicitation.get();
  // solicitation.setStatus(status);
  // solicitationRepository.save(solicitation);
  // return ResponseEntity.ok("Status da solicitação atualizado com sucesso!");
  // }
  // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitação não
  // encontrada.");
  // }

}
