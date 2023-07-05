package com.ati.seidmsautistic.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ati.seidmsautistic.dtos.SolicitationEditDto;
import com.ati.seidmsautistic.exceptions.AppError;
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

  @Operation(summary = "Altera o status da solicitação para REPPROVED (indeferido), ou SUSPENDED(suspenso).")
  @PatchMapping("/giveback")
  public ResponseEntity<Object> giveBackSolicitation(@RequestBody SolicitationEditDto solicitationEditDto) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(solicitationService.giveBackSolicitation(solicitationEditDto));
    } catch (AppError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @Operation(summary = "Realiza download de um documento pelo id do documento")
  // Fazer download
  @GetMapping("/download")
  public ResponseEntity<byte[]> downloadFile(@RequestParam UUID documentId) throws IOException {
    try {
      return documentService.downloadDocument(documentId);
    } catch (AppError e) {
      return ResponseEntity.notFound().build();
    }
  }

}
