package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Flower;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.FlowerService;
import bt.edu.gcit.usermicroservice.service.ImageUploadService;
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
    private ImageUploadService imageUploadService;

    @Autowired
    public FlowerRestController(FlowerService flowerService, UserService userService, ImageUploadService imageUploadService) {
        this.flowerService = flowerService;
        this.userService = userService;
        this.imageUploadService = imageUploadService;
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
            @RequestParam("shopowner_id") Long shopownerId) throws IOException {

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

         // Upload the product photo to Cloudinary
        String imageUrl = imageUploadService.uploadImage(image);
        savedFlower.setImage(imageUrl);

        // Update the product with the photo URL
        flowerService.updateFlower(savedFlower.getFlower_id(), savedFlower);

        // Return the saved product
        return ResponseEntity.ok(savedFlower);
    }
    
    // ==========================
    // Other CRUD methods (PUT, GET)
    // ==========================
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Flower> updateFlower(
            @PathVariable int id,
            @RequestParam("name") String name,
            @RequestParam("quantity") int quantity,
            @RequestParam("details") String details,
            @RequestParam("price") int price,
            @RequestParam("image") MultipartFile image) {
        Flower flower = flowerService.findById(id);
        if (flower == null) {
            return ResponseEntity.notFound().build();
        }

        flower.setName(name);
        flower.setQuantity(quantity);
        flower.setDetails(details);
        flower.setPrice(price);

       // Upload the product photo to Cloudinary
        String imageUrl = imageUploadService.uploadImage(image);
        flower.setImage(imageUrl);

        // Update the product with the photo URL

        // Return the saved product

        Flower updatedFlower = flowerService.updateFlower(flower.getFlower_id(), flower);
        return ResponseEntity.ok(updatedFlower);
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFlower(@PathVariable int id) {
        Flower flower = flowerService.findById(id);
        if (flower == null) {
            return ResponseEntity.notFound().build();
        }

        // Delete the image from disk if it exists
        if (flower.getImage() != null) {
            String imagePath = "uploads/flowers/" + flower.getImage();
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }

        flowerService.deleteById(id);
        return ResponseEntity.ok("Flower deleted successfully.");
    }

}
