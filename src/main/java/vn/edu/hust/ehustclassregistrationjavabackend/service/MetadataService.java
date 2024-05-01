package vn.edu.hust.ehustclassregistrationjavabackend.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.MetadataRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetadataService {
    private final MetadataRepository metadataRepository;
    public void saveAll(List<Metadata> metadatas){
        metadataRepository.saveAll(metadatas);
    }
    public void save(String name,Object value){
        metadataRepository.save(new Metadata(name,value.toString()));
    }

    public List<Metadata> getAllMetadata(){
        return metadataRepository.findAll();
    }
    public <T> T getMetadata(String name,Class<T> classofT){
        Metadata metadata = metadataRepository.findById(name).orElse(null);
        if(metadata==null){
            return null;
        }
        return new Gson().fromJson(metadata.getValue(),classofT);
    }

//    public String getMetadata(String name){
//        Metadata metadata = metadataRepository.findById(name).orElse(null);
//        if(metadata==null){
//            return null;
//        }
//        return metadata.getValue();
//    }

}
