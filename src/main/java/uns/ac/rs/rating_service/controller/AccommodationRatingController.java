package uns.ac.rs.rating_service.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.rating_service.dto.AccommodationRatingDTO;
import uns.ac.rs.rating_service.dto.request.CreateAccommodationRatingRequest;
import uns.ac.rs.rating_service.dto.request.UpdateAccommodationRatingRequest;
import uns.ac.rs.rating_service.dto.response.MessageResponse;
import uns.ac.rs.rating_service.service.AccommodationRatingService;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/ratings/accommodation")
public class AccommodationRatingController {
    @Autowired
    private AccommodationRatingService accommodationRatingService;

    @PostMapping("/create/{idAccommodation}")
    public ResponseEntity<?> rateAccommodation(@PathVariable UUID idAccommodation,
                             @Valid @RequestBody CreateAccommodationRatingRequest createAccommodationRatingRequest,
                             @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Received request to rate accommodation with ID: {}", idAccommodation);
        MessageResponse messageResponse = accommodationRatingService
                .rateAccommodation(idAccommodation, createAccommodationRatingRequest, jwtToken);
        log.info("Successfully rated accommodation with ID: {}", idAccommodation);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/all/{idAccommodation}")
    public ResponseEntity<?> getAllAccommodationRatingsByIdAccommodation(@PathVariable UUID idAccommodation) {
        log.info("Fetching all ratings for accommodation ID: {}", idAccommodation);
        List<AccommodationRatingDTO> accommodationRatings = accommodationRatingService
                .getAllAccommodationRatingsByIdAccommodation(idAccommodation);
        log.info("Fetched {} ratings for accommodation ID: {}", accommodationRatings.size(), idAccommodation);
        return ResponseEntity.ok(accommodationRatings);
    }

    @GetMapping("/{accommodationRatingId}")
    public ResponseEntity<?> getAccommodationRatingById(@PathVariable UUID accommodationRatingId) {
        log.info("Fetching rating with ID: {}", accommodationRatingId);
        AccommodationRatingDTO accommodationRating = accommodationRatingService
                .getAccommodationRatingById(accommodationRatingId);
        log.info("Successfully fetched rating with ID: {}", accommodationRatingId);
        return ResponseEntity.ok(accommodationRating);
    }

    @GetMapping("/guest/{accommodationRatingId}")
    public ResponseEntity<?> getAccommodationRatingByGuest(@PathVariable UUID accommodationRatingId,
                                                           @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Fetching guest-specific rating for ID: {}", accommodationRatingId);
        AccommodationRatingDTO accommodationRating = accommodationRatingService
                .getAccommodationRatingByGuest(jwtToken, accommodationRatingId);
        log.info("Successfully fetched guest-specific rating for ID: {}", accommodationRatingId);
        return ResponseEntity.ok(accommodationRating);
    }

    @PutMapping("/update/{accommodationRatingId}")
    public ResponseEntity<?> updateAccommodationRating(@PathVariable UUID accommodationRatingId,
                             @Valid @RequestBody UpdateAccommodationRatingRequest updateAccommodationRatingRequest,
                             @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Updating rating with ID: {}", accommodationRatingId);
        MessageResponse messageResponse = accommodationRatingService
                .updateAccommodationRating(accommodationRatingId, updateAccommodationRatingRequest, jwtToken);
        log.info("Successfully updated rating with ID: {}", accommodationRatingId);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/average/{idAccommodation}")
    public ResponseEntity<?> getAccommodationAverageRating(@PathVariable UUID idAccommodation) {
        log.info("Calculating average rating for accommodation with ID: {}", idAccommodation);
        Double averageRating = accommodationRatingService.getAccommodationAverageRating(idAccommodation);
        log.info("Average rating for accommodation with ID {} is: {}", idAccommodation, averageRating);
        return ResponseEntity.ok(averageRating);
    }

    @DeleteMapping("/delete/{accommodationRatingId}")
    public ResponseEntity<?> deleteAccommodationRating(@PathVariable UUID accommodationRatingId,
                                                       @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        log.info("Deleting rating with ID: {}", accommodationRatingId);
        MessageResponse messageResponse = accommodationRatingService
                .deleteAccommodationRating(accommodationRatingId, jwtToken);
        log.info("Successfully deleted rating with ID: {}", accommodationRatingId);
        return ResponseEntity.ok(messageResponse);
    }
}
