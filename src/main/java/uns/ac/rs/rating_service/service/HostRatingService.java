package uns.ac.rs.rating_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uns.ac.rs.rating_service.dto.HostRatingDTO;
import uns.ac.rs.rating_service.dto.UserDTO;
import uns.ac.rs.rating_service.dto.request.CreateHostRatingRequest;
import uns.ac.rs.rating_service.dto.request.UpdateHostRatingRequest;
import uns.ac.rs.rating_service.dto.response.MessageResponse;
import uns.ac.rs.rating_service.mapper.HostRatingMapper;
import uns.ac.rs.rating_service.model.HostRating;
import uns.ac.rs.rating_service.repository.HostRatingRepository;
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
public class HostRatingService {
    private final HostRatingRepository hostRatingRepository;

    private final UserServiceClient userServiceClient;

    private final ReservationServiceClient reservationServiceClient;

    public HostRatingService(HostRatingRepository hostRatingRepository,
                             UserServiceClient userServiceClient,
                             ReservationServiceClient reservationServiceClient) {
        this.hostRatingRepository = hostRatingRepository;
        this.userServiceClient = userServiceClient;
        this.reservationServiceClient = reservationServiceClient;
    }

    public MessageResponse rateHost(String host,
                                    CreateHostRatingRequest createHostRatingRequest,
                                    String jwtToken) {
        Integer rating = createHostRatingRequest.getRating();
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

        if (hostRatingRepository.existsByGuestAndHost(userDetails.getUsername(), host)) {
            throw new IllegalArgumentException("You have already rated this host.");
        }

        if (reservationServiceClient.isGuestHasSuccessfullyPassedReservationHost(host, jwtToken)) {
            HostRating newHostRating = new HostRating(
                    userDetails.getUsername(),
                    host,
                    createHostRatingRequest.getRating()
            );

            newHostRating.setDateTime(LocalDateTime.now());

            hostRatingRepository.save(newHostRating);

            return new MessageResponse("Host rated successfully.");
        } else {
            throw new SecurityException("You cannot rate this host.");
        }
    }

    public List<HostRatingDTO> getAllHostRatingsByHost(String host) {
        return hostRatingRepository.findByHost(host)
                .stream()
                .map(HostRatingMapper::toHostRatingDTO)
                .collect(Collectors.toList());
    }

    public HostRatingDTO getHostRatingById(UUID hostRatingId) {
        HostRating hostRating = hostRatingRepository.findById(hostRatingId)
                .orElseThrow(() -> new NoSuchElementException("Host rating not found with id: " + hostRatingId));

        return HostRatingMapper.toHostRatingDTO(hostRating);
    }

    public MessageResponse updateHostRating(UUID hostRatingId,
                                            UpdateHostRatingRequest updateHostRatingRequest,
                                            String jwtToken) {
        Integer rating = updateHostRatingRequest.getRating();
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

        HostRating hostRating = hostRatingRepository.findById(hostRatingId)
                .orElseThrow(() -> new NoSuchElementException("Host rating not found with id: " + hostRatingId));

        if (!Objects.equals(hostRating.getGuest(), userDetails.getUsername())) {
            throw new SecurityException("User did not create this host rating.");
        }

        hostRating.setRating(updateHostRatingRequest.getRating());
        hostRating.setDateTime(LocalDateTime.now());

        hostRatingRepository.save(hostRating);

        return new MessageResponse("Host rating updated successfully.");
    }

    public Double getHostAverageRating(String host) {
        Double averageRating = hostRatingRepository.findAverageRatingByHost(host);
        if (averageRating == null) {
            return 0.0;
        }

        return BigDecimal.valueOf(averageRating)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public MessageResponse deleteHostRating(UUID hostRatingId, String jwtToken) {
        UserDTO userDetails = userServiceClient.getUserDetails(jwtToken);
        if (userDetails == null) {
            throw new IllegalStateException("User details could not be retrieved.");
        }
        if (!userDetails.getRoles().contains("ROLE_GUEST")) {
            throw new SecurityException("User do not have permission for this action.");
        }

        HostRating hostRating = hostRatingRepository.findById(hostRatingId)
                .orElseThrow(() -> new NoSuchElementException("Host rating not found with id: " + hostRatingId));

        if (!Objects.equals(hostRating.getGuest(), userDetails.getUsername())) {
            throw new SecurityException("User did not create this host rating.");
        }

        hostRatingRepository.delete(hostRating);

        return new MessageResponse("Host rating deleted successfully.");
    }
}
