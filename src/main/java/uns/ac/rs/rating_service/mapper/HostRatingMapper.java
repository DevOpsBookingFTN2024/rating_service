package uns.ac.rs.rating_service.mapper;

import uns.ac.rs.rating_service.dto.HostRatingDTO;
import uns.ac.rs.rating_service.model.HostRating;

public class HostRatingMapper {
    public static HostRatingDTO toHostRatingDTO(HostRating hostRating) {
        return HostRatingDTO.builder()
                .id(hostRating.getId())
                .guest(hostRating.getGuest())
                .host(hostRating.getHost())
                .rating(hostRating.getRating())
                .dateTime(hostRating.getDateTime())
                .build();
    }
}
