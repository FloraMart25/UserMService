package bt.edu.gcit.usermicroservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "flower")
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flower_id;

    @Column(name = "flower_name", length = 100, nullable = false)
    private String name;

    @Column(length = 8, nullable = false)
    private int price;
    @Column(length = 8, nullable = false)
    private int quantity;
    @Column(nullable = false)
    private String details;

    @Column
    private String image;

    @Column(name = "posted_at", nullable = false)
    private LocalDateTime postedAt;

    // Optional user-related fields (commented out or include if relevant to your app)
    // @Column(nullable = false, unique = true)
    // private String email;

    // private String password;

    // private int phone;

    // private String licenseNo;

    // private boolean enabled;

    // private String photo;

    // private String certificate;

    @ManyToOne(fetch = FetchType.EAGER)
    // @JsonBackReference
    @JoinColumn(name = "shopowner_id", nullable = false)
    private User shopOwner;
    
    // Constructors
    public Flower() {
        // Empty constructor
    }

    public Flower(String name, int price, String details,  LocalDateTime postedAt, User shopOwner) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.postedAt = postedAt;
        this.shopOwner = shopOwner;
    }
    
    // Getters and Setters
    public int getFlower_id() {
        return flower_id;
    }

    public void setFlower_id(int flower_id) {
        this.flower_id = flower_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public User getShopOwner() {
        return shopOwner;
    }
    
    public void setShopOwner(User shopOwner) {
        this.shopOwner = shopOwner;
    }
    
}
