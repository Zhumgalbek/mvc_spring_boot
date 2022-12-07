package com.example.peaksoft.repository;

import com.example.peaksoft.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query(value = "select c from Group c where c.company.id = :companyId")
    List<Group> getAllGroup(Long companyId);
}
