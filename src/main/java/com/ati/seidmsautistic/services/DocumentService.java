package com.ati.seidmsautistic.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ati.seidmsautistic.entities.Document;
import com.ati.seidmsautistic.entities.Solicitation;
import com.ati.seidmsautistic.enums.DocType;
import com.ati.seidmsautistic.repositories.DocumentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

  @Value("${root.dir}")
  private String rootDir;
  @Value("${file.dir}")
  private String fileDir;

  private final DocumentRepository documentRepository;

  public ResponseEntity<Object> findOneDocument(UUID solicitationId) {
    List<Optional<Document>> optionalDocumentList = documentRepository.findBySolicitationId(solicitationId);
    if (!optionalDocumentList.isEmpty()) {
      List<Document> documentList = new ArrayList<Document>();
      for (Optional<Document> optionalDocument : optionalDocumentList) {
        Document document = new Document();
        BeanUtils.copyProperties(optionalDocument.get(), document);
        documentList.add(document);
      }
      return ResponseEntity.status(HttpStatus.OK).body(documentList);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found.");
  }

  public void saveDocument(Solicitation solicitation, MultipartFile file, DocType docType, UUID uuid)
      throws IOException {
    if (file != null && !file.isEmpty()) {
      Document document = new Document();
      Path dirPath = Paths.get(this.rootDir, uuid.toString());
      Path filePath = dirPath.resolve(file.getOriginalFilename());
      try {
        Files.createDirectories(dirPath);
        file.transferTo(filePath.toFile());
      } catch (Exception e) {
        System.out.println(e);
      }
      document.setDiretory(filePath.toString());
      document.setSolicitation(solicitation);
      document.setDocType(docType);
      document.setName(file.getOriginalFilename());
      document.setSize(file.getSize());
      document.setObservation(null);
      documentRepository.save(document);
    }
  }
}
