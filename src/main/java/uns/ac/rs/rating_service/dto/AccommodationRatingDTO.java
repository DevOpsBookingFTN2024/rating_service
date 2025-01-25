package uns.ac.rs.rating_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationRatingDTO {
    private UUID id;

    private String guest;

    private UUID idAccommodation;

    private Integer rating;

    private LocalDateTime dateTime;
}
