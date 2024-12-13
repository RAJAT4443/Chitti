package com.Chitti.AiVoiceMail.repos.mysql;

import com.Chitti.AiVoiceMail.entities.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryRepo extends JpaRepository<Summary, String> {
}
