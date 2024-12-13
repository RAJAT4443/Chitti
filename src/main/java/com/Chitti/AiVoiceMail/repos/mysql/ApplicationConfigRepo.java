package com.Chitti.AiVoiceMail.repos.mysql;

import com.Chitti.AiVoiceMail.entities.ApplicationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationConfigRepo extends JpaRepository<ApplicationConfig, Long>{
}
