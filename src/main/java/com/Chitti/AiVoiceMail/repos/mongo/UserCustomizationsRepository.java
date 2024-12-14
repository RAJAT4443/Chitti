package com.Chitti.AiVoiceMail.repos.mongo;

import com.Chitti.AiVoiceMail.models.UserCustomizations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCustomizationsRepository extends MongoRepository<UserCustomizations, String> {
    UserCustomizations findUserCustomizationsByUserId(String userId);
}
