package bt.edu.gcit.usermicroservice.dao;

import java.util.List;

import bt.edu.gcit.usermicroservice.entity.Flower;
// import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.entity.User;

public interface FlowerDAO {

        Flower save(Flower flower);
        List<Flower> findByShopOwnerId(Long shopOwnerId);
Flower findById(int theId);
Flower findById(long theId);
List<Flower> findAll();
  
} 

