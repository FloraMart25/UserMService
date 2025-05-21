package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Flower;
// import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.entity.User;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


public interface FlowerService {
    Flower save(Flower user);
    Flower findById(int theId);
    Flower findById(long theId);
    List<Flower> findByShopOwnerId(Long shopOwnerId);
 Flower updateFlower(int id, Flower updatedFlower);

    void uploadFlowerPhoto(int id, MultipartFile photo) throws IOException;
   List<Flower> findAll();
   
    
    
}