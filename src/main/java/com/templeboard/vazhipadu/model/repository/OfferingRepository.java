package com.templeboard.vazhipadu.model.repository;

import com.templeboard.vazhipadu.model.entity.Offering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface OfferingRepository extends JpaRepository<Offering, Long> {
@Query("SELECT o FROM Offering o WHERE o.id = :id")
    Optional<Object> findByIdForUpdate(Long offeringId);
}
