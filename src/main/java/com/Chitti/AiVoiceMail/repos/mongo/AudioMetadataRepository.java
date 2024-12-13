package com.Chitti.AiVoiceMail.repos.mongo;


import com.Chitti.AiVoiceMail.models.AudioMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioMetadataRepository extends MongoRepository<AudioMetadata, String> {
}
