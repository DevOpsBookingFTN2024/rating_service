package uns.ac.rs.rating_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uns.ac.rs.rating_service.model.HostRating;
import java.util.List;
import java.util.UUID;

@Repository
public interface HostRatingRepository extends JpaRepository<HostRating, UUID> {
    List<HostRating> findByHost(String host);

    HostRating findByGuestAndHost(String guest, String host);

    boolean existsByGuestAndHost(String guest, String host);

    @Query("SELECT AVG(hr.rating) FROM HostRating hr WHERE hr.host = :host")
    Double findAverageRatingByHost(@Param("host") String host);
}
