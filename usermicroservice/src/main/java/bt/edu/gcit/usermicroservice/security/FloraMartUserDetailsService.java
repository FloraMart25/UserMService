package bt.edu.gcit.usermicroservice.security;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class FloraMartUserDetailsService implements UserDetailsService {
 @Autowired
 private final UserDAO userDAO;
 @Autowired
 public FloraMartUserDetailsService(UserDAO userDAO) {
 this.userDAO = userDAO;
 }
 @Override
 public UserDetails loadUserByUsername(String email) throws
UsernameNotFoundException {
 User user = userDAO.findByEmail(email);
 if (user == null) {
 throw new UsernameNotFoundException("User not found with email: " +
email);
 }
 //System.out.println("User: " + user.getEmail()); // print out the user email
 List<GrantedAuthority> authorities = user.getRoles().stream()
 .map(role -> {
 System.out.println("Role: " + role.getName()); // print out the role
// name
 return new SimpleGrantedAuthority(role.getName());
 })
 .collect(Collectors.toList());
 System.out.println("Authorities: " + authorities); // print out the list of
// authorities
 return new org.springframework.security.core.userdetails.User(user.getEmail(),
user.getPassword(), authorities);
 }
}
