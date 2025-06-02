package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.FlowerDAO;
import bt.edu.gcit.usermicroservice.entity.Flower;
import bt.edu.gcit.usermicroservice.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bt.edu.gcit.usermicroservice.exception.FlowerNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.util.StringUtils;
import java.nio.file.Path;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FlowerServiceImpl implements FlowerService {
    private final FlowerDAO FlowerDAO;
    private final String uploadDir = "src/main/resources/static/images";

    @Autowired
    @Lazy
    public FlowerServiceImpl(FlowerDAO FlowerDAO) {
        this.FlowerDAO = FlowerDAO;

    }

    @Override
    @Transactional
    public Flower save(Flower flower) {

        return FlowerDAO.save(flower);
    }

    @Override
    public List<Flower> findByShopOwnerId(Long shopOwnerId) {
        return FlowerDAO.findByShopOwnerId(shopOwnerId); // <- likely returns a single Flower
    }

    @Override
    public Flower findById(int theId) {
        return FlowerDAO.findById(theId);
    }

    @Override
    public Flower findById(long theId) {
        return FlowerDAO.findById(theId);
    }

    @Transactional
    @Override
    public Flower updateFlower(int id, Flower updatedFlower) {
        // First, find the user by ID
        Flower existingFlower = FlowerDAO.findById(id);

        // If the user doesn't exist, throw FlowerNotFoundException
        if (existingFlower == null) {
            throw new FlowerNotFoundException("flower not found with id: " + id);
        }
        existingFlower.setName(updatedFlower.getName());
        existingFlower.setQuantity(updatedFlower.getQuantity());
        existingFlower.setPrice(updatedFlower.getPrice());
        existingFlower.setDetails(updatedFlower.getDetails());
        existingFlower.setImage(updatedFlower.getImage());

        return FlowerDAO.save(existingFlower);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        Flower flower = FlowerDAO.findById(id);
        if (flower == null) {
            throw new FlowerNotFoundException("Flower not found with id: " + id);
        }
        FlowerDAO.deleteById(id);
    }

    @Transactional
    @Override
    public void uploadFlowerPhoto(int id, MultipartFile photo) throws IOException {
        Flower flower = findById(id);
        if (flower == null) {
            throw new FlowerNotFoundException("flower not found with id " + id);
        }
        if (photo.getSize() > 1024 * 1024) {
            throw new FileSizeException("File size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0,
                originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        // Append the timestamp to the filename
        String filename = filenameWithoutExtension + "_" + timestamp + "." +
                filenameExtension;

        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);
        flower.setImage(filename);
        FlowerDAO.save(flower);
    }

    public List<Flower> findAll() {
        return FlowerDAO.findAll();
    }

}
