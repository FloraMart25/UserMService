package bt.edu.gcit.usermicroservice.service;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageUploadService {
    private final Cloudinary cloudinary;

    @Autowired
    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not upload image", e);
        }
    }

    // ✅ NEW: Delete image from Cloudinary using image URL
    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId == null || publicId.isEmpty()) {
                throw new RuntimeException("Failed to extract public ID from image URL");
            }

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Optionally, check the result
            if (!"ok".equals(result.get("result"))) {
                throw new RuntimeException("Failed to delete image from Cloudinary");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not delete image", e);
        }
    }

    // ✅ Helper method to extract public ID from Cloudinary URL
    private String extractPublicIdFromUrl(String imageUrl) {
        // Example Cloudinary URL:
        // https://res.cloudinary.com/demo/image/upload/v1680000000/your_folder/image_name.jpg
        // You need to extract: your_folder/image_name (without extension)

        Pattern pattern = Pattern.compile(".*/upload/(?:v\\d+/)?(.*)\\.(jpg|png|jpeg|webp|gif)$");
        Matcher matcher = pattern.matcher(imageUrl);
        if (matcher.find()) {
            return matcher.group(1); // The public ID
        }
        return null;
    }
}
