package uns.ac.rs.rating_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uns.ac.rs.rating_service.dto.AccommodationRatingDTO;
import uns.ac.rs.rating_service.dto.UserDTO;
import uns.ac.rs.rating_service.dto.request.CreateAccommodationRatingRequest;
import uns.ac.rs.rating_service.dto.request.UpdateAccommodationRatingRequest;
import uns.ac.rs.rating_service.dto.response.MessageResponse;
import uns.ac.rs.rating_service.mapper.AccommodationRatingMapper;
import uns.ac.rs.rating_service.model.AccommodationRating;
import uns.ac.rs.rating_service.repository.AccommodationRatingRepository;
import uns.ac.rs.rating_service.service.client.ReservationServiceClient;
import uns.ac.rs.rating_service.service.client.UserServiceClient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccommodationRatingService {
    private final AccommodationRatingRepository accommodationRatingRepository;

    private final UserServiceClient userServiceClient;

    private final ReservationServiceClient reservationServiceClient;


    public AccommodationRatingService(AccommodationRatingRepository accommodationRatingRepository,
                                      UserServiceClient userServiceClient,
                                      ReservationServiceClient reservationServiceClient) {
        this.accommodationRatingRepository = accommodationRatingRepository;
        this.userServiceClient = userServiceClient;
        this.reservationServiceClient = reservationServiceClient;
    }

    public MessageResponse rateAccommodation(UUID idAccommodation,
                                             CreateAccommodationRatingRequest createAccommodationRatingRequest,
                                             String jwtToken) {
        Integer rating = createAccommodationRatingRequest.getRating();
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        if (accommodationRatingRepository.existsByGuestAndIdAccommodation(userDetails.getUsername(), idAccommodation)) {
            throw new IllegalArgumentException("You have already rated this accommodation.");
        }

        if (reservationServiceClient.isGuestHasSuccessfullyPassedReservationAccommodation(idAccommodation, jwtToken)) {
            AccommodationRating newAccommodationRating = new AccommodationRating(
                    userDetails.getUsername(),
                    idAccommodation,
                    createAccommodationRatingRequest.getRating()
            );

            newAccommodationRating.setDateTime(LocalDateTime.now());

            accommodationRatingRepository.save(newAccommodationRating);

            return new MessageResponse("Accommodation rated successfully.");
        } else {
            throw new SecurityException("You cannot rate this accommodation.");
        }
    }

    public List<AccommodationRatingDTO> getAllAccommodationRatingsByIdAccommodation(UUID idAccommodation) {
        return accommodationRatingRepository.findByIdAccommodation(idAccommodation)
                .stream()
                .map(AccommodationRatingMapper::toAccommodationRatingDTO)
                .collect(Collectors.toList());
    }

    public AccommodationRatingDTO getAccommodationRatingById(UUID accommodationRatingId) {
        AccommodationRating accommodationRating = accommodationRatingRepository.findById(accommodationRatingId)
                .orElseThrow(() -> new NoSuchElementException("Accommodation rating not found with id: "
                        + accommodationRatingId));

        return AccommodationRatingMapper.toAccommodationRatingDTO(accommodationRating);
    }

    public MessageResponse updateAccommodationRating(UUID accommodationRatingId,
                                                     UpdateAccommodationRatingRequest updateAccommodationRatingRequest,
                                                     String jwtToken) {
        Integer rating = updateAccommodationRatingRequest.getRating();
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        AccommodationRating accommodationRating = accommodationRatingRepository.findById(accommodationRatingId)
                .orElseThrow(() -> new NoSuchElementException("Accommodation rating not found with id: "
                        + accommodationRatingId));

        if (!Objects.equals(accommodationRating.getGuest(), userDetails.getUsername())) {
            throw new SecurityException("User did not create this accommodation rating.");
        }

        accommodationRating.setRating(updateAccommodationRatingRequest.getRating());
        accommodationRating.setDateTime(LocalDateTime.now());

        accommodationRatingRepository.save(accommodationRating);

        return new MessageResponse("Accommodation rating updated successfully.");
    }

    public Double getAccommodationAverageRating(UUID idAccommodation) {
        Double averageRating = accommodationRatingRepository.findAverageRatingByIdAccommodation(idAccommodation);
        if (averageRating == null) {
            return 0.0;
        }

        return BigDecimal.valueOf(averageRating)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public MessageResponse deleteAccommodationRating(UUID accommodationRatingId, String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        AccommodationRating accommodationRating = accommodationRatingRepository.findById(accommodationRatingId)
                .orElseThrow(() -> new NoSuchElementException("Accommodation rating not found with id: "
                        + accommodationRatingId));

        if (!Objects.equals(accommodationRating.getGuest(), userDetails.getUsername())) {
            throw new SecurityException("User did not create this accommodation rating.");
        }

        accommodationRatingRepository.delete(accommodationRating);

        return new MessageResponse("Accommodation rating deleted successfully.");
    }
}
