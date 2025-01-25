package uns.ac.rs.rating_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accommodation_ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "guest")
    private String guest;

    @Column(name = "id_accommodation")
    private UUID idAccommodation;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "date_time_column")
    private LocalDateTime dateTime;

    public AccommodationRating(String guest,
                               UUID idAccommodation,
                               Integer rating) {
        this.guest = guest;
        this.idAccommodation = idAccommodation;
        this.rating = rating;
    }
}
