package com.Chitti.AiVoiceMail.service.userRegistration.impl;

import com.Chitti.AiVoiceMail.entities.UserDetails;
import com.Chitti.AiVoiceMail.repository.UserDetailsRepository;
import com.Chitti.AiVoiceMail.requests.UserRegistrationData;
import com.Chitti.AiVoiceMail.service.userRegistration.UserRegistrationService;
import com.Chitti.AiVoiceMail.utilities.Utilities;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserRegistrationServiceImpl  implements UserRegistrationService {

 private final UserDetailsRepository userDetailsRepository;

    public UserRegistrationServiceImpl(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public void addUser(UserRegistrationData userRegistrationData) {

        UserDetails user= Utilities.mapUserRegistrationDataToUserDetails(userRegistrationData);

        userDetailsRepository.save(user);

    }
}
