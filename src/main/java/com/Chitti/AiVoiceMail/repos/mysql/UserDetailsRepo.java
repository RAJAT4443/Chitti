package com.Chitti.AiVoiceMail.repos.mysql;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetails, Long>{
    UserDetails findUserDetailsByPhone(String bParty);
}
