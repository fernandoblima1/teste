package com.ati.seidmsautistic.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ati.seidmsautistic.entities.Solicitation;

@Repository
public interface SolicitationRepository extends JpaRepository<Solicitation, UUID> {

  @Query("SELECT s FROM Solicitation s WHERE s.user.cpf = :cpf")
  Optional<Solicitation> findByUserCpf(@Param("cpf") String cpf);

  @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Solicitation s WHERE s.user.cpf = :cpf")
  boolean existsByUserCpf(@Param("cpf") String cpf);

}
