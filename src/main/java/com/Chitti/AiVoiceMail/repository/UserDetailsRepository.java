package com.Chitti.AiVoiceMail.repository;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
