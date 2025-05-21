package bt.edu.gcit.usermicroservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import java.util.HashSet;
import java.util.Set;
import bt.edu.gcit.usermicroservice.entity.Role;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 128, nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private boolean enabled;
    @Column(name = "name", length = 45, nullable = false)
    private String Name;
    @Column(length = 8, nullable = false)
    private int Phone;
    @Column(length = 10, nullable = true)
    private String License_No;
    @Column(length = 64, nullable = true)
    private String certificate;
    @Column(length = 64, nullable = false)
    private String password;
    @Column(length = 500)
    private String photo;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // Constructors
    public User() {
        // Empty constructor
    }

    public User(String email, String password, String Name, int Phone, String License_No) {
        this.email = email;
        this.password = password;
        this.Name = Name;
        this.Phone = Phone;
        this.License_No = License_No;

    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getPhone() {
        return Phone;
    }

    public void setPhone(int Phone) {
        this.Phone = Phone;
    }

    public String getLicense_No() {
        return License_No;
    }

    public void setLicense_No(String License_No) {
        this.License_No = License_No;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getcertificate() {
        return certificate;
    }

    public void setcertificate(String certificate) {
        this.certificate = certificate;
    }
}
