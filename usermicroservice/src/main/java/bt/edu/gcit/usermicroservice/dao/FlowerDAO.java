package bt.edu.gcit.usermicroservice.dao;

import java.util.List;

import bt.edu.gcit.usermicroservice.entity.Flower;
// import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.entity.User;

public interface FlowerDAO {

        Flower save(Flower flower);

        List<Flower> findByShopOwnerId(Long shopOwnerId);

        Flower findByID(int theId);

        void deleteByID(int id);

        List<Flower> findAll();

        Flower updateFlower(Flower updatedFlower);

}
