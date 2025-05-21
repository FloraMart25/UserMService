package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import bt.edu.gcit.usermicroservice.entity.Role;
import java.util.Set;
import bt.edu.gcit.usermicroservice.service.ImageUploadService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private UserService userService;
    private ImageUploadService imageUploadService;

    @Autowired
    public UserRestController(UserService userService, ImageUploadService imageUploadService) {
        this.userService = userService;
        this.imageUploadService = imageUploadService;
    }
    // @PostMapping(consumes = "multipart/form-data")
    // public User save(
    // @RequestPart("Name") @Valid @NotNull String Name,
    // @RequestPart("Phone") @Valid @NotNull int Phone,
    // @RequestPart("email") @Valid @NotNull String email,
    // @RequestPart("password") @Valid @NotNull String password,
    // @RequestPart("photo") @Valid @NotNull MultipartFile photo,
    // // @RequestPart("License_no") @Valid String License_No,
    // // @RequestPart("certifications") @Valid String certifications,
    // @RequestPart("roles") @Valid @NotNull String rolesJson)

    @PostMapping(consumes = "multipart/form-data")
    public User save(
            @RequestParam("Name") String Name,
            @RequestParam(name = "Phone") int Phone,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "License_No", required = false) String License_No,
            @RequestParam("roles") String rolesJson)

    {
        try {
            // Create a new User object
            User user = new User();
            user.setName(Name);
            // user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            // user.setRoles(roles);
            user.setPhone(Phone);
            // user.setLicense(License_No);
            user.setPassword(password);
            user.setLicense_No(License_No);
            // Parse the roles JSON string into a Set<Role>
            ObjectMapper objectMapper = new ObjectMapper();
            Set<Role> roles = objectMapper.readValue(rolesJson, new TypeReference<Set<Role>>() {
            });
            user.setRoles(roles);

            // Check if the user has roles with ID 1 or 3
            boolean hasSuperAdminOrClient = roles.stream()
                    .anyMatch(role -> role.getId() == 1 || role.getId() == 3);

            // Set enabled based on roles
            user.setEnabled(hasSuperAdminOrClient);
            System.out.println("Uploading photo");

            // Save the user and get the ID
            User savedUser = userService.save(user);

            // Upload the user photo
            // System.out.println("Uploading photo" + savedUser.getId().intValue());
            // userService.uploadUserPhoto(savedUser.getId().intValue(),
            // photo);//

            // Upload the user photo
            // System.out.println("Uploading photo"+savedUser.getId().intValue());
            // userService.uploadUserPhoto(savedUser.getId().intValue(), photo);
            // Upload the user photo to Cloudinary
            String imageUrl = imageUploadService.uploadImage(photo);
            savedUser.setPhoto(imageUrl);
            // Update the user with the photo URL
            userService.updateUser(savedUser.getId().intValue(),
                    savedUser);

            // Return the saved user
            return savedUser;
        } catch (IOException e) {
            // Handle the exception
            throw new RuntimeException("Error while uploading photo",
                    e);
        }
    }

    // @PostMapping("/")
    // public User save(@RequestBody User user) {
    // return userService.save(user);
    // }

    // GET - Fetch All Users
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    // GET - Fetch User by ID
    // @GetMapping("/{id}")
    // public User findById(@PathVariable int theId) {
    // return userService.findById(theid);
    // }

    // DELETE - Remove User
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<User> updateUser(
    // @PathVariable Long id,
    // @RequestBody User userDetails) {

    // // Ensure user exists before updating
    // Optional<User> existingUser = Optional.of(userService.findById(id));
    // if (existingUser.isEmpty()) {
    // return ResponseEntity.notFound().build();
    // }

    // // Ensure ID consistency
    // userDetails.setId(id);
    // User updatedUser = userService.update(userDetails);

    // return ResponseEntity.ok(updatedUser);
    // }

    @GetMapping("/checkDuplicateEmail")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);

    }

    /**
     * Updates a user with the given ID using the provided User object.
     *
     * @param id          the ID of the user to be updated
     * @param updatedUser the User object containing the updated information
     * @return the updated User object
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    /**
     * Update the enabled status of a user with the specified id
     *
     * @param id      The ID of the user to update
     * @param enabled The new enabled status
     * @return OK if the update was successful
     */
    @PutMapping("/{id}/enabled")
    public ResponseEntity<?> updateUserEnabledStatus(
            @PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
        Boolean enabled = requestBody.get("enabled");
        userService.updateUserEnabledStatus(id, enabled);
        System.out.println("User enabled status updated successfully");
        return ResponseEntity.ok().build();
    }

    // user login

}