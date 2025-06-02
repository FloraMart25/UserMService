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
import bt.edu.gcit.usermicroservice.service.ImageUploadService;


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
    private final ImageUploadService imageUploadService;

    @Autowired
    public FlowerRestController(FlowerService flowerService, UserService userService, ImageUploadService imageUploadService) {
        this.flowerService = flowerService;
        this.userService = userService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<?> addFlower(
            @RequestParam("name") String name,
            @RequestParam("quantity") int quantity,
            @RequestParam("details") String details,
            @RequestParam("price") int price,
            @RequestParam("image") MultipartFile image,
            @RequestParam("shopowner_id") Long shopownerId) {
        try {
            User shopOwner = userService.findById(shopownerId);
            if (shopOwner == null) {
                return ResponseEntity.badRequest().body("Shop owner not found");
            }

            Flower flower = new Flower();
            flower.setName(name);
            flower.setQuantity(quantity);
            flower.setDetails(details);
            flower.setPrice(price);
            flower.setPostedAt(LocalDateTime.now());
            flower.setShopOwner(shopOwner);

            // Upload the image FIRST and get URL
            String imageUrl = imageUploadService.uploadImage(image);
            if (imageUrl == null || imageUrl.isEmpty()) {
                return ResponseEntity.status(500).body("Failed to upload image");
            }
            flower.setImage(imageUrl);

            // Save flower with image URL
            Flower savedFlower = flowerService.save(flower);

            return ResponseEntity.ok(savedFlower);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateFlower(
            @PathVariable int id,
            @RequestParam("name") String name,
            @RequestParam("quantity") int quantity,
            @RequestParam("details") String details,
            @RequestParam("price") int price,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Flower flower = flowerService.findById(id);
            if (flower == null) {
                return ResponseEntity.notFound().build();
            }

            flower.setName(name);
            flower.setQuantity(quantity);
            flower.setDetails(details);
            flower.setPrice(price);

            // If image is provided, upload and update
            if (image != null && !image.isEmpty()) {
                String imageUrl = imageUploadService.uploadImage(image);
                if (imageUrl == null || imageUrl.isEmpty()) {
                    return ResponseEntity.status(500).body("Failed to upload image");
                }
                flower.setImage(imageUrl);
            }

            Flower updatedFlower = flowerService.updateFlower(flower.getFlower_id(), flower);
            return ResponseEntity.ok(updatedFlower);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFlowerById(@PathVariable int id) {
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

    @GetMapping("/all")
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

        if (flower.getImage() != null) {
            // Delete image from Cloudinary
            imageUploadService.deleteImage(flower.getImage());
        }

        flowerService.deleteById(id);
        return ResponseEntity.ok("Flower deleted successfully.");
    }
}
