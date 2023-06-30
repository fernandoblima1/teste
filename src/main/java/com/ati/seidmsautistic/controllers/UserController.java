package com.ati.seidmsautistic.controllers;

import com.ati.seidmsautistic.dtos.SolicitationDto;
import com.ati.seidmsautistic.entities.Solicitation;
import com.ati.seidmsautistic.services.DocumentService;
import com.ati.seidmsautistic.services.SolicitationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/solicitacao")
@RequiredArgsConstructor
public class UserController {

  private final SolicitationService solicitationService;
  private final DocumentService documentService;

  // Post sendNewSolicitation()
  @PostMapping("/cia")
  public ResponseEntity<Object> postNewPassSolicitation(@ModelAttribute @Validated SolicitationDto solicitationDto,
      @RequestParam("complementar") List<MultipartFile> complementary,
      @RequestParam("identificacao") MultipartFile identification,
      @RequestParam("foto") MultipartFile picture, @RequestParam("residencia") MultipartFile residency,
      @RequestParam("certificadoMedico") MultipartFile medicalCertificate) {
    Solicitation solicitation = new Solicitation();
    BeanUtils.copyProperties(solicitationDto, solicitation);

    return solicitationService.saveNewSolicitation(solicitation, identification, picture, medicalCertificate, residency,
        complementary);
  }

  @GetMapping
  public ResponseEntity<Object> getSolicitation(@RequestParam("cpf") String cpf) {
    return solicitationService.findSolicitationByUserCpf(cpf);
  }

  @GetMapping("/documents")
  public ResponseEntity<Object> getDocumentBySolicitationId(@RequestParam("solicitationId") UUID solicitationId) {
    return documentService.findOneDocument(solicitationId);
  }

  // // para testes somente
  // @PutMapping("/{id}/status")
  // public ResponseEntity<Object> updateSolicitationStatus(@PathVariable("id")
  // UUID solicitationId,
  // @RequestParam("status") Status status) {
  // return solicitationService.updateStatus(solicitationId, status);
  // }

  // Patch updatePartOfSolicitation()
  // @PatchMapping("/update/resend")
  // public ResponseEntity<Object> updateSolicitation(@ModelAttribute Solicitation
  // solicitation,
  // @RequestParam("complementar") List<MultipartFile> complementary,
  // @RequestParam("identificacao") MultipartFile identification,
  // @RequestParam("foto") MultipartFile picture, @RequestParam("residencia")
  // MultipartFile residency,
  // @RequestParam("renda") MultipartFile income,
  // @RequestParam("certificadoMedico") MultipartFile medicalCertificate) {
  // return solicitationService.updatePassSolicitation(solicitation,
  // identification, picture, medicalCertificate,
  // residency, income, complementary);
  // }

  // Post resortSolicitation()

  // Get validateCard()
}
