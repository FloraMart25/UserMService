package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Flower;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.FlowerService;
import bt.edu.gcit.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.io.File;
import java.nio.file.Files;

@RestController

@RequestMapping("/api/flowers")

public class FlowerRestController {

    private final FlowerService flowerService;
    private final UserService userService;

    @Autowired
    public FlowerRestController(FlowerService flowerService, UserService userService) {
        this.flowerService = flowerService;
        this.userService = userService;
    }

    // ==========================
    // 1. POST Flower with image
    // ==========================
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<Flower> addFlower(
            @RequestParam("name") String name,
            @RequestParam("quantity") int quantity,
            @RequestParam("details") String details,
            @RequestParam("price") int price,
            @RequestParam("image") MultipartFile image,
            @RequestParam("shopowner_id") Long shopownerId
    ) throws IOException {

        User shopOwner = userService.findById(shopownerId);
        if (shopOwner == null) {
            return ResponseEntity.badRequest().build();
        }

        Flower flower = new Flower();
        flower.setName(name);
        flower.setQuantity(quantity);
        flower.setDetails(details);
        flower.setPrice(price);
        flower.setPostedAt(LocalDateTime.now());
        flower.setShopOwner(shopOwner);

        // Save the flower to the database without the image first
        Flower savedFlower = flowerService.save(flower);

        // Check if image exists and save it to disk
        if (image != null && !image.isEmpty()) {
            String imagePath = saveImageToDisk(image, savedFlower.getFlower_id());  // Use Long flowerId
            savedFlower.setImage(imagePath);  // Store the image file name/path
            flowerService.save(savedFlower);  // Update the flower with the image path
        }

        return ResponseEntity.ok(savedFlower);
    }

    private String saveImageToDisk(MultipartFile image, int flowerId) throws IOException {
        String uploadsDir = "uploads/flowers/";  // This is where the image will be stored
        File dir = new File(uploadsDir);
        if (!dir.exists()) dir.mkdirs();  // Create the directory if it doesn't exist

        // Generate a unique filename for the image
        String originalName = image.getOriginalFilename();
        String fileExtension = originalName.substring(originalName.lastIndexOf("."));
        String fileName = "flower_" + flowerId + "_" + System.currentTimeMillis() + fileExtension;

        // Create the file path where the image will be saved
        Path filePath = Paths.get(uploadsDir, fileName);

        // Write the image file to the disk
        Files.write(filePath, image.getBytes());

        // Return the filename (path relative to the root or directory where images are stored)
        return fileName;
    }

    // ==========================
    // Other CRUD methods (PUT, GET)
    // ==========================
    @PutMapping("/{id}")
    public ResponseEntity<Flower> updateFlower(
            @PathVariable int id,
            @RequestBody Flower updatedFlower
    ) {
        Flower updated = flowerService.updateFlower(id, updatedFlower);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flower> getFlowerById(@PathVariable int id) {
        Flower flower = flowerService.findById(id);
        if (flower == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(flower);
    }

    @GetMapping("/shopowner/{shopownerId}")
    public ResponseEntity<List<Flower>> getFlowersByShopOwner(@PathVariable Long shopownerId) {
        List<Flower> flowers = flowerService.findByShopOwnerId(shopownerId);
        return ResponseEntity.ok(flowers);
    }

    @GetMapping("/getAllflowers")
public ResponseEntity<List<Flower>> getAllFlowers() {
    List<Flower> flowers = flowerService.findAll();
    return ResponseEntity.ok(flowers);
}
}
