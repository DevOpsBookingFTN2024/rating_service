package uns.ac.rs.rating_service.mapper;

import uns.ac.rs.rating_service.dto.AccommodationRatingDTO;
import uns.ac.rs.rating_service.model.AccommodationRating;

public class AccommodationRatingMapper {
    public static AccommodationRatingDTO toAccommodationRatingDTO(AccommodationRating accommodationRating) {
        return AccommodationRatingDTO.builder()
                .id(accommodationRating.getId())
                .guest(accommodationRating.getGuest())
                .idAccommodation(accommodationRating.getIdAccommodation())
                .rating(accommodationRating.getRating())
                .dateTime(accommodationRating.getDateTime())
                .build();
    }
}
