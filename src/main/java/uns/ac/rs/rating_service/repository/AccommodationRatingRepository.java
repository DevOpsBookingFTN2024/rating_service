package uns.ac.rs.rating_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uns.ac.rs.rating_service.model.AccommodationRating;
import java.util.List;
import java.util.UUID;

@Repository
public interface AccommodationRatingRepository extends JpaRepository<AccommodationRating, UUID> {
    List<AccommodationRating> findByIdAccommodation(UUID idAccommodation);

    boolean existsByGuestAndIdAccommodation(String guest, UUID idAccommodation);

    @Query("SELECT AVG(ar.rating) FROM AccommodationRating ar WHERE ar.idAccommodation = :idAccommodation")
    Double findAverageRatingByIdAccommodation(@Param("idAccommodation") UUID idAccommodation);
}
