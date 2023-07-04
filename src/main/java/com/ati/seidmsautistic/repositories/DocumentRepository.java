package com.ati.seidmsautistic.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ati.seidmsautistic.entities.Document;
import com.ati.seidmsautistic.entities.Solicitation;
import com.ati.seidmsautistic.enums.DocType;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

  @Query("SELECT d FROM Document d WHERE d.solicitation.id = :solicitationId")
  List<Optional<Document>> findBySolicitationId(@Param("solicitationId") UUID solicitationId);

  Optional<UUID> findIdByDocType(DocType docType);

  Optional<Document> findBySolicitationAndDocType(Solicitation solicitation, DocType docType);

  List<Optional<Document>> findAllBySolicitationAndDocType(Solicitation solicitation, DocType docType);

  void deleteAllBySolicitationAndDocType(Solicitation updatedSolicitation, DocType docType);

  @Query("SELECT d FROM Document d WHERE d.solicitation.id = :solicitationId AND d.docType = :docType")
  Optional<List<Document>> findBySolicitationIdAndDocType(@Param("solicitationId") UUID solicitationId,
      @Param("docType") DocType docType);

}
