package uns.ac.rs.rating_service.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.rating_service.dto.HostRatingDTO;
import uns.ac.rs.rating_service.dto.request.CreateHostRatingRequest;
import uns.ac.rs.rating_service.dto.request.UpdateHostRatingRequest;
import uns.ac.rs.rating_service.dto.response.MessageResponse;
import uns.ac.rs.rating_service.service.HostRatingService;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/ratings/host")
public class HostRatingController {
    @Autowired
    private HostRatingService hostRatingService;

    @PostMapping("/create/{host}")
    public ResponseEntity<?> rateHost(@PathVariable String host,
                                      @Valid @RequestBody CreateHostRatingRequest createHostRatingRequest,
                                      @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Received request to rate host {}", host);
        MessageResponse messageResponse = hostRatingService
                .rateHost(host, createHostRatingRequest, jwtToken);
        log.info("Successfully rated rate host {}", host);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/all/{host}")
    public ResponseEntity<?> getAllHostRatingsByHost(@PathVariable String host) {
        log.info("Fetching all ratings for host {}", host);
        List<HostRatingDTO> hostRatings = hostRatingService.getAllHostRatingsByHost(host);
        log.info("Fetched {} ratings for host {}", hostRatings.size(), host);
        return ResponseEntity.ok(hostRatings);
    }

    @GetMapping("/{hostRatingId}")
    public ResponseEntity<?> getHostRatingById(@PathVariable UUID hostRatingId) {
        log.info("Fetching rating with ID: {}", hostRatingId);
        HostRatingDTO hostRating = hostRatingService.getHostRatingById(hostRatingId);
        log.info("Successfully fetched rating with ID: {}", hostRatingId);
        return ResponseEntity.ok(hostRating);
    }

    @GetMapping("/guest/{host}")
    public ResponseEntity<?> getHostRatingByGuest(@PathVariable String host,
                                                  @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Fetching guest-specific rating for host {}", host);
        HostRatingDTO hostRating = hostRatingService.getHostRatingByGuest(jwtToken, host);
        log.info("Successfully fetched guest-specific rating for host {}", host);
        return ResponseEntity.ok(hostRating);
    }

    @PutMapping("/update/{hostRatingId}")
    public ResponseEntity<?> updateHostRating(@PathVariable UUID hostRatingId,
                                              @Valid @RequestBody UpdateHostRatingRequest updateHostRatingRequest,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Updating rating with ID: {}", hostRatingId);
        MessageResponse messageResponse = hostRatingService
                .updateHostRating(hostRatingId, updateHostRatingRequest, jwtToken);
        log.info("Successfully updated rating with ID: {}", hostRatingId);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/average/{host}")
    public ResponseEntity<?> getHostAverageRating(@PathVariable String host) {
        log.info("Calculating average rating for host {}", host);
        Double averageRating = hostRatingService.getHostAverageRating(host);
        log.info("Average rating for host {} is: {}", host, averageRating);
        return ResponseEntity.ok(averageRating);
    }

    @DeleteMapping("/delete/{hostRatingId}")
    public ResponseEntity<?> deleteHostRating(@PathVariable UUID hostRatingId,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Deleting rating with ID: {}", hostRatingId);
        MessageResponse messageResponse = hostRatingService.deleteHostRating(hostRatingId, jwtToken);
        log.info("Successfully deleted rating with ID: {}", hostRatingId);
        return ResponseEntity.ok(messageResponse);
    }
}
