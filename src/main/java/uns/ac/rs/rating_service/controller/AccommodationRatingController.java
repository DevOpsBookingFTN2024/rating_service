package uns.ac.rs.rating_service.controller;

import jakarta.validation.Valid;
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
        MessageResponse messageResponse = accommodationRatingService
                .rateAccommodation(idAccommodation, createAccommodationRatingRequest, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/all/{idAccommodation}")
    public ResponseEntity<?> getAllAccommodationRatingsByIdAccommodation(@PathVariable UUID idAccommodation) {
        List<AccommodationRatingDTO> accommodationRatings = accommodationRatingService
                .getAllAccommodationRatingsByIdAccommodation(idAccommodation);
        return ResponseEntity.ok(accommodationRatings);
    }

    @GetMapping("/{accommodationRatingId}")
    public ResponseEntity<?> getAccommodationRatingById(@PathVariable UUID accommodationRatingId) {
        AccommodationRatingDTO accommodationRating = accommodationRatingService
                .getAccommodationRatingById(accommodationRatingId);
        return ResponseEntity.ok(accommodationRating);
    }

    @GetMapping("/guest/{accommodationRatingId}")
    public ResponseEntity<?> getAccommodationRatingByGuest(@PathVariable UUID accommodationRatingId,
                                                           @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        AccommodationRatingDTO accommodationRating = accommodationRatingService
                .getAccommodationRatingByGuest(jwtToken, accommodationRatingId);
        return ResponseEntity.ok(accommodationRating);
    }

    @PutMapping("/update/{accommodationRatingId}")
    public ResponseEntity<?> updateAccommodationRating(@PathVariable UUID accommodationRatingId,
                             @Valid @RequestBody UpdateAccommodationRatingRequest updateAccommodationRatingRequest,
                             @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        MessageResponse messageResponse = accommodationRatingService
                .updateAccommodationRating(accommodationRatingId, updateAccommodationRatingRequest, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/average/{idAccommodation}")
    public ResponseEntity<?> getAccommodationAverageRating(@PathVariable UUID idAccommodation) {
        Double averageRating = accommodationRatingService.getAccommodationAverageRating(idAccommodation);
        return ResponseEntity.ok(averageRating);
    }

    @DeleteMapping("/delete/{accommodationRatingId}")
    public ResponseEntity<?> deleteAccommodationRating(@PathVariable UUID accommodationRatingId,
                                                       @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        MessageResponse messageResponse = accommodationRatingService
                .deleteAccommodationRating(accommodationRatingId, jwtToken);
        return ResponseEntity.ok(messageResponse);
    }
}
