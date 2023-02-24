package com.bezkoder.spring.login.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.bezkoder.spring.login.payload.response.PostResponse;
import com.bezkoder.spring.login.payload.response.UserResponse;
import com.bezkoder.spring.login.security.services.UserService;
import com.bezkoder.spring.login.security.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.login.models.ERole;
import com.bezkoder.spring.login.models.Role;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.request.LoginRequest;
import com.bezkoder.spring.login.payload.request.SignupRequest;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.RoleRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.security.jwt.JwtUtils;
import com.bezkoder.spring.login.security.services.UserDetailsImpl;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;
  @Autowired
  VideoService videoService;

  @Autowired
  UserService userService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/connexion")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

   /* return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(),
                                   roles));*/
    String rooool= "ROLE_USER";
    String roleee= "ROLE_ADMIN";
    if (roles.equals(rooool)){
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body("Bienvenue User");
    }
    else
      roles.equals(roleee);
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body("Bienvenue Admin");
  }

  @PostMapping("/inscription")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Erreur: ce nom d'utilisateur existe deja", true));
    }

    if (userRepository.existsByNumero(signUpRequest.getNumero())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Erreur: ce numero est deja utilisé !", true));
    }

    // Create new user's account
    User user = new User( signUpRequest.getNom(),
                          signUpRequest.getPrenom(),
                          signUpRequest.getUsername(),
                          signUpRequest.getNumero(),
                          signUpRequest.getPhoto(),
                          encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Erreur: Impossible de trouver le rôle!!"));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;

        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    user.setNom(signUpRequest.getNom());
    user.setPrenom(signUpRequest.getPrenom());
    user.setUsername(signUpRequest.getUsername());
    user.setPhoto("http://127.0.0.1/ikagaImg.jpg");
    user.setFollowerCount(0);
    user.setFollowingCount(0);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("Utilisateur ajouter avec succes", true));
  }

  @PostMapping("/deconnection")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("Vous vous êtes deconnecter!", true));
  }

  @PostMapping("/account/update/profile-photo")
  public ResponseEntity<?> updateProfilePhoto(@RequestParam("photo") MultipartFile photo) {
    User updatedUser = userService.updateProfilePhoto(photo);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }



  @PostMapping("/account/follow/{userId}")
  public ResponseEntity<?> followUser(@PathVariable("userId") Long userId) {
    userService.followUser(userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/account/unfollow/{userId}")
  public ResponseEntity<?> unfollowUser(@PathVariable("userId") Long userId) {
    userService.unfollowUser(userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/users/{userId}/following")
  public ResponseEntity<?> getUserFollowingUsers(@PathVariable("userId") Long userId,
                                                 @RequestParam("page") Integer page,
                                                 @RequestParam("size") Integer size) {
    page = page < 0 ? 0 : page-1;
    size = size <= 0 ? 5 : size;
    List<UserResponse> followingList = userService.getFollowingUsersPaginate(userId, page, size);
    return new ResponseEntity<>(followingList, HttpStatus.OK);
  }

  @GetMapping("/users/{userId}/follower")
  public ResponseEntity<?> getUserFollowerUsers(@PathVariable("userId") Long userId,
                                                @RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size) {
    page = page < 0 ? 0 : page-1;
    size = size <= 0 ? 5 : size;
    List<UserResponse> followingList = userService.getFollowerUsersPaginate(userId, page, size);
    return new ResponseEntity<>(followingList, HttpStatus.OK);
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
    User authUser = userService.getAuthenticatedUser();
    User targetUser = userService.getUserById(userId);
    UserResponse userResponse = UserResponse.builder()
            .user(targetUser)
            .followedByAuthUser(targetUser.getFollowerUsers().contains(authUser))
            .build();
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @GetMapping("/users/{userId}/posts")
  public ResponseEntity<?> getUserPosts(@PathVariable("userId") Long userId,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size) {
    page = page < 0 ? 0 : page-1;
    size = size <= 0 ? 5 : size;
    User targetUser = userService.getUserById(userId);
    List<PostResponse> userPosts = videoService.getPostsByUserPaginate(targetUser, page, size);
    return new ResponseEntity<>(userPosts, HttpStatus.OK);
  }

  @GetMapping("/users/search")
  public ResponseEntity<?> searchUser(@RequestParam("key") String key,
                                      @RequestParam("page") Integer page,
                                      @RequestParam("size") Integer size) {
    page = page < 0 ? 0 : page-1;
    size = size <= 0 ? 5 : size;
    List<UserResponse> userSearchResult = userService.getUserSearchResult(key, page, size);
    return new ResponseEntity<>(userSearchResult, HttpStatus.OK);
  }
}
