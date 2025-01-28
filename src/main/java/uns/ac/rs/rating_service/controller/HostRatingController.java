package uns.ac.rs.rating_service.controller;

import jakarta.validation.Valid;
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
        MessageResponse messageResponse = hostRatingService
                .rateHost(host, createHostRatingRequest, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/all/{host}")
    public ResponseEntity<?> getAllHostRatingsByHost(@PathVariable String host) {
        List<HostRatingDTO> hostRatings = hostRatingService.getAllHostRatingsByHost(host);
        return ResponseEntity.ok(hostRatings);
    }

    @GetMapping("/{hostRatingId}")
    public ResponseEntity<?> getHostRatingById(@PathVariable UUID hostRatingId) {
        HostRatingDTO hostRating = hostRatingService.getHostRatingById(hostRatingId);
        return ResponseEntity.ok(hostRating);
    }

    @PutMapping("/update/{hostRatingId}")
    public ResponseEntity<?> updateHostRating(@PathVariable UUID hostRatingId,
                                              @Valid @RequestBody UpdateHostRatingRequest updateHostRatingRequest,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        MessageResponse messageResponse = hostRatingService
                .updateHostRating(hostRatingId, updateHostRatingRequest, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/average/{host}")
    public ResponseEntity<?> getHostAverageRating(@PathVariable String host) {
        Double averageRating = hostRatingService.getHostAverageRating(host);
        return ResponseEntity.ok(averageRating);
    }

    @DeleteMapping("/delete/{hostRatingId}")
    public ResponseEntity<?> deleteHostRating(@PathVariable UUID hostRatingId,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        MessageResponse messageResponse = hostRatingService.deleteHostRating(hostRatingId, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }
}
