package bt.edu.gcit.usermicroservice.rest;
import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import bt.edu.gcit.usermicroservice.service.AuthService;
import bt.edu.gcit.usermicroservice.service.JWTUtil;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
 @Autowired
 private AuthService authService;
 @Autowired
 private JWTUtil jwtUtil;
//  @PostMapping("/login")
//  public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
//  UserDetails userDetails = authService.login(user.getEmail(),
// user.getPassword());
//  String jwt = jwtUtil.generateToken(userDetails);
//  Map<String, Object> response = new HashMap<>();
//  response.put("jwt", jwt);
//  response.put("user", userDetails);
//  return ResponseEntity.ok(response);
//  }

@PostMapping("/login")
// @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
    System.out.println(" Login attempt: " + user.getEmail());
    Map<String, Object> response = new HashMap<>();
    try {
        UserDetails userDetails = authService.login(user.getEmail(), user.getPassword());
        String jwt = jwtUtil.generateToken(userDetails);

        response.put("jwt", jwt);
        response.put("user", userDetails);
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        // Print the actual error to the console
        System.out.println("Login error: " + e.getClass().getName() + " - " + e.getMessage());

        response.put("error", "Invalid email or password");
        return ResponseEntity.status(401).body(response); // return 401 Unauthorized
    }
    
}

}
