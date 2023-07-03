package com.ati.seidmsautistic.services;

import com.ati.seidmsautistic.entities.Document;
import com.ati.seidmsautistic.entities.Solicitation;
import com.ati.seidmsautistic.enums.DocType;
import com.ati.seidmsautistic.enums.Status;
import com.ati.seidmsautistic.repositories.DocumentRepository;
import com.ati.seidmsautistic.repositories.SolicitationRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentService {

  @Value("${root.dir}")
  private String rootDir;

  @Value("${file.dir}")
  private String fileDir;

  private final DocumentRepository documentRepository;
  private final SolicitationRepository solicitationRepository;

  public List<Document> findAllDocument(UUID solicitationId) {
    List<Optional<Document>> optionalDocumentList = documentRepository.findBySolicitationId(
        solicitationId);
    if (!optionalDocumentList.isEmpty()) {
      List<Document> documentList = new ArrayList<Document>();
      for (Optional<Document> optionalDocument : optionalDocumentList) {
        Document document = new Document();
        BeanUtils.copyProperties(optionalDocument.get(), document);
        documentList.add(document);
      }
      return documentList;
    }
    return null;
  }

  public void saveDocument(
      Solicitation solicitation,
      MultipartFile file,
      DocType docType,
      UUID solicitationId) throws IOException {
    if (file != null && !file.isEmpty()) {
      Document document = new Document();
      Path dirPath = Paths.get(this.rootDir, solicitationId.toString());
      Path filePath = dirPath.resolve(file.getOriginalFilename());

      Files.createDirectories(dirPath);
      file.transferTo(filePath.toFile());

      document.setDiretory(filePath.toString());
      document.setSolicitation(solicitation);
      document.setDocType(docType);
      document.setName(file.getOriginalFilename());
      document.setSize(file.getSize());
      document.setObservation(null);
      documentRepository.save(document);
    }
  }

  public void updateDocument(
      Solicitation solicitation,
      MultipartFile file,
      DocType docType) throws IllegalStateException, IOException {
    if (file != null && !file.isEmpty()) {
      Optional<Document> optionalDocument = documentRepository.findBySolicitationAndDocType(solicitation, docType);
      if (optionalDocument.isPresent()) {

        // Apaga o arquivo antigo salvo na pasta
        Path existingPath = Paths.get(optionalDocument.get().getDiretory());
        Files.delete(existingPath);

        // Cria novo caminho para salvar o novo arquivo
        Path dirPath = Paths.get(
            this.rootDir,
            solicitation.getSolicitationId().toString());
        // Files.createDirectories(dirPath);
        Path filePath = dirPath.resolve(file.getOriginalFilename());

        // Transfere o novo arquivo para o diretório desejado
        file.transferTo(filePath.toFile());

        // Seta as informações referentes a aquele arquivo atualizado
        Document document = optionalDocument.get();
        document.setName(file.getOriginalFilename());
        document.setSize(file.getSize());
        document.setDiretory(filePath.toString());

        // Salva o documento no repositório
        documentRepository.save(document);
      }
    }
  }

  public void updateMultipleDocuments(
      Solicitation solicitation,
      List<MultipartFile> files,
      DocType docType) throws IOException {
    if (files != null && !files.isEmpty()) {
      List<Optional<Document>> optionalDocumentList = documentRepository.findAllBySolicitationAndDocType(
          solicitation,
          docType);

      if (!optionalDocumentList.isEmpty()) {

        // Apaga todos os documentos antigos na pasta
        // Apaga também do banco de dados pois não há como saber quantos serão anexados,
        // logo a atualização não é viável
        for (Optional<Document> optionalDocument : optionalDocumentList) {
          Path existingPath = Paths.get(optionalDocument.get().getDiretory());
          Files.delete(existingPath);
          documentRepository.deleteById(optionalDocument.get().getId());
        }

        for (MultipartFile file : files) {
          Document document = new Document();
          // Cria novo caminho para salvar novo arquivo
          Path dirPath = Paths.get(
              this.rootDir,
              solicitation.getSolicitationId().toString());
          Path filePath = dirPath.resolve(file.getOriginalFilename());
          file.transferTo(filePath.toFile());
          document.setName(file.getOriginalFilename());
          document.setSize(file.getSize());
          document.setDocType(docType);
          document.setObservation(null);
          document.setSolicitation(solicitation);
          document.setDiretory(filePath.toString());
          documentRepository.save(document);
        }
      }
    }
  }

  public ResponseEntity<Object> updateStatus(
      UUID solicitationId,
      Status status) {
    Optional<Solicitation> optionalSolicitation = solicitationRepository.findById(
        solicitationId);
    if (optionalSolicitation.isPresent()) {
      Solicitation solicitation = optionalSolicitation.get();
      solicitation.setStatus(status);
      solicitationRepository.save(solicitation);
      return ResponseEntity.ok("Status da solicitação atualizado com sucesso!");
    }
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("Solicitação não encontrada.");
  }

  public ResponseEntity<byte[]> downloadDocument(UUID documentId) throws IOException {
    Optional<Document> optionalDocument = documentRepository.findById(documentId);
    if (optionalDocument.isPresent()) {
      Document document = optionalDocument.get();
      Path filePath = Paths.get(document.getDiretory());

      if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
        byte[] fileContent = Files.readAllBytes(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", document.getName());

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
      }
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

}
