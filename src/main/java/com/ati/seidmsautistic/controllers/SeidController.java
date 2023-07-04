package com.ati.seidmsautistic.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ati.seidmsautistic.dtos.SolicitationEditDto;
import com.ati.seidmsautistic.services.DocumentService;
import com.ati.seidmsautistic.services.SolicitationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seid")
public class SeidController {

  private final SolicitationService solicitationService;
  private final DocumentService documentService;

  @Operation(summary = "Altera o status da solicitação para REPPROVED (indeferido), de forma que não há mais como o usuário editar, envia também as observações para explicar o motivo da reprovação.")
  // Reprovar
  @PatchMapping("/repprove")
  public ResponseEntity<Object> repproveSolicitation(@RequestBody SolicitationEditDto solicitationEditDto) {
    return solicitationService.repproveSolicitation(solicitationEditDto);
  }

  @Operation(summary = "Altera o status da solicitação para SUSPENDED (suspenso), de tal forma que o usuário poderá editar e reenviar a solicitação, envia no seu corpo as observação referentes aos documentos errados.")
  // Reprovar
  @PatchMapping("/suspend")
  public ResponseEntity<Object> suspendSolicitation(@RequestBody SolicitationEditDto solicitationEditDto) {
    return solicitationService.suspendSolicitation(solicitationEditDto);
  }

  @Operation(summary = "Realiza downloa de um documento pelo id do documento")
  // Fazer download
  @GetMapping("/download")
  public ResponseEntity<byte[]> downloadFile(@RequestParam UUID documentId) throws IOException {
    return documentService.downloadDocument(documentId);
  }

}
