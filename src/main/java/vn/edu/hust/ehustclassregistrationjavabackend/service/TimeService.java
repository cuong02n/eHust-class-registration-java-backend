package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.MetadataRepository;

@Service
@RequiredArgsConstructor
public class TimeService {
    final MetadataRepository metadataRepository;

    public boolean isElitechOfficialRegisterClass(long timeInMillis) {
        return timeBetweenMetadata("open_official_elitech", "close_official_elitech", timeInMillis);
    }

    public boolean isStandardOfficialRegisterClass(long timeInMillis) {
        return timeBetweenMetadata("open_official_standard", "close_official_standard", timeInMillis);
    }

    public boolean isElitechUnofficialRegisterClass(long timeInMillis) {
        return timeBetweenMetadata("open_unofficial_elitech", "close_unofficial_elitech", timeInMillis);
    }

    public boolean isStandardUnofficialRegisterClass(long timeInMillis) {
        return timeBetweenMetadata("open_unofficial_standard", "close_unofficial_standard", timeInMillis);
    }

    public boolean isFreeRegister(long timeInMillis) {
        return timeBetweenMetadata("open_free_all", "close_free_all", timeInMillis);
    }

    private boolean timeBetweenMetadata(@Nonnull String metadataKeyStart, @Nonnull String metadataKeyEnd, long timeInMillis) {
        Metadata start = metadataRepository.findById(metadataKeyStart).orElse(null);
        if (start == null) return false;
        Metadata end = metadataRepository.findById(metadataKeyEnd).orElse(null);
        if (end == null) return false;

        return Long.parseLong(start.getValue()) <= timeInMillis && timeInMillis <= Long.parseLong(end.getValue());
    }
}
