package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.MetadataRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetadataService {
    private final MetadataRepository metadataRepository;
    public Metadata findById(String userId,String metadataId){
        return metadataRepository.findById(metadataId).orElse(new Metadata(metadataId,""));
    }
    public void saveAll(List<Metadata> metadatas){
        metadataRepository.saveAll(metadatas);
    }

}
