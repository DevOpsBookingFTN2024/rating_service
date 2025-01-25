package uns.ac.rs.rating_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "host_ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "guest")
    private String guest;

    @Column(name = "host")
    private String host;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "date_time_column")
    private LocalDateTime dateTime;

    public HostRating(String guest,
                      String host,
                      Integer rating) {
        this.guest = guest;
        this.host = host;
        this.rating = rating;
    }
}
