package com.Chitti.AiVoiceMail.service.db.mysql;

import com.Chitti.AiVoiceMail.entities.Summary;
import com.Chitti.AiVoiceMail.repos.mysql.SummaryRepo;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    private final SummaryRepo summaryRepository;

    public SummaryService(SummaryRepo summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    public Summary getSummaryById(Long id) {
        return summaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Summary not found for ID: " + id));
    }

    public Summary saveSummary(Summary summary) {
        try {
            return summaryRepository.save(summary);
        } catch (Exception e) {
            throw new RuntimeException("Error saving summary: " + e.getMessage(), e);
        }
    }

    public void deleteSummary(Long id) {
        if (!summaryRepository.existsById(id)) {
            throw new IllegalArgumentException("Summary not found for ID: " + id);
        }
        summaryRepository.deleteById(id);
    }
}
