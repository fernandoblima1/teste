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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seid")
public class SeidController {

  private final SolicitationService solicitationService;
  private final DocumentService documentService;

  // Reprovar
  @PatchMapping("/repprove")
  public ResponseEntity<Object> updateSolicitationStatus(@RequestBody SolicitationEditDto solicitationEditDto) {
    return solicitationService.repproveSolicitation(solicitationEditDto);
  }

  // Fazer download
  @GetMapping("/download")
  public ResponseEntity<byte[]> downloadFile(@RequestParam UUID documentId) throws IOException {
    return documentService.downloadDocument(documentId);
  }

}
