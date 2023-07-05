package com.ati.seidmsautistic.controllers;

import com.ati.seidmsautistic.dtos.SolicitationDto;
import com.ati.seidmsautistic.entities.Document;
import com.ati.seidmsautistic.entities.Solicitation;
import com.ati.seidmsautistic.exceptions.AppError;
import com.ati.seidmsautistic.services.DocumentService;
import com.ati.seidmsautistic.services.SolicitationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Usuário", description = "Endpoints relacionados as telas de usuário")
@RestController
@RequestMapping("/solicitacao")
@RequiredArgsConstructor
public class UserController {

  private final SolicitationService solicitationService;
  private final DocumentService documentService;

  @Operation(summary = "Envia uma nova solicitação de CIA(Carteira de Identificação do Autista)")

  @PostMapping("/cia")
  public ResponseEntity<Object> postNewPassSolicitation(@ModelAttribute @Validated SolicitationDto solicitationDto,
      @RequestParam(name = "complementar", required = false) List<MultipartFile> complementary,
      @RequestParam("identificacao") MultipartFile identification,
      @RequestParam("foto") MultipartFile picture, @RequestParam("residencia") MultipartFile residency,
      @RequestParam("certificadoMedico") MultipartFile medicalCertificate) {
    Solicitation solicitation = new Solicitation();
    BeanUtils.copyProperties(solicitationDto, solicitation);

    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(solicitationService.saveNewSolicitation(solicitation, identification, picture, medicalCertificate,
              residency,
              complementary));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  @Operation(summary = "Retorna uma solicitação de CIA pelo cpf do usuário")
  @GetMapping
  public ResponseEntity<Object> getSolicitation(@RequestParam("cpf") String cpf) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(solicitationService.findSolicitationByUserCpf(cpf));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  @Operation(summary = "Retorna a lista de documentos associados a uma solicitação")
  @GetMapping("/documents")
  public ResponseEntity<Object> getDocumentBySolicitationId(@RequestParam("solicitationId") UUID solicitationId) {
    List<Document> documentList = documentService.findAllDocument(solicitationId);
    if (documentList == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documents not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(documentList);
  }

  @Operation(summary = "Caso a solicitação tenha recebido status 'SUSPENDED' este endpoint permite atualizar os documentos que receberam uma observação.")
  @PatchMapping("/update/resend")
  public ResponseEntity<Object> updateSolicitation(@ModelAttribute Solicitation solicitation,
      @RequestParam(name = "complementar", required = false) List<MultipartFile> complementary,
      @RequestParam(name = "identificacao", required = false) MultipartFile identification,
      @RequestParam(name = "foto", required = false) MultipartFile picture,
      @RequestParam(name = "residencia", required = false) MultipartFile residency,
      @RequestParam(name = "certificadoMedico", required = false) MultipartFile medicalCertificate) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(solicitationService.updateCiaSolicitation(solicitation,
          identification, picture, medicalCertificate,
          residency, complementary));
    } catch (AppError e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  @Operation(summary = "Realiza downloa de um documento pelo id do documento")
  // Fazer download
  @GetMapping("/download")
  public ResponseEntity<byte[]> downloadFile(@RequestParam UUID documentId) throws IOException {
    return documentService.downloadDocument(documentId);
  }
}
