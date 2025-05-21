// AuthServiceImpl.java
package bt.edu.gcit.usermicroservice.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import
org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import bt.edu.gcit.usermicroservice.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Service
public class AuthServiceImpl implements AuthService {
 // @Autowired
 // private AuthenticationManager authenticationManager;
 // @Autowired
 // private UserDetailsService userDetailsService;
 private final AuthenticationManager authenticationManager;
 private final UserDetailsService userDetailsService;
 private final PasswordEncoder passwordEncoder;
 @Autowired
 public AuthServiceImpl(@Lazy AuthenticationManager authenticationManager,@Lazy
UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
 this.authenticationManager = authenticationManager;
 this.userDetailsService = userDetailsService;
 this.passwordEncoder = passwordEncoder;
 }
 @Override
//  public UserDetails login(String email, String password) {
//     // System.out.println("show: " + authenticationManager.authenticate(new
//     System.out.println("authenticating");
// //    UsernamePasswordAuthenticationToken(email, password)));
// try {
//     UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//     // Print out the stored password (encoded) and the raw password from the request
//     String storedPassword = ((org.springframework.security.core.userdetails.User) userDetails).getPassword();
//     System.out.println("Stored Password (Encoded): " + storedPassword);
//     System.out.println("Password sent in the request: " + password);
//     // System.out.println(passwordEncoder.encode("123abc"));

//     // Compare passwords
//     if (passwordEncoder.matches(password, storedPassword)) {
//         // Passwords match, authenticate user
//         System.out.println("Password matches and authentication successful!");
//         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        
//         return userDetails;
//     } else {
//         System.out.println("Password does not match stored value");
//         throw new RuntimeException("Invalid email or password");
//     }
// } catch (Exception e) {
//     System.out.println("Authentication failed: " + e.getMessage());
//     throw new RuntimeException("Invalid email or password");
// }
//    }
// }
public UserDetails login(String email, String password) {
    try {
        System.out.println("Authenticating...");

        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        System.out.println("Authentication successful!");
        return (UserDetails) auth.getPrincipal();
    } catch (Exception e) {
        System.out.println("Authentication failed: " + e.getMessage());
        throw new RuntimeException("Invalid email or password");
    }
}
}