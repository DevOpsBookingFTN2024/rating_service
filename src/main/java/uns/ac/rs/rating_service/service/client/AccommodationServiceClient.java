package uns.ac.rs.rating_service.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import uns.ac.rs.rating_service.dto.AccommodationDTO;
import java.util.UUID;

@Service
public class AccommodationServiceClient {
    private final WebClient webClient;

    @Autowired
    public AccommodationServiceClient(WebClient.Builder webClientBuilder,
                                      @Value("${accommodation.service.url}") String userServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(userServiceUrl).build();
    }

    public AccommodationDTO getAccommodationDetails(UUID accommodationId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/accommodations/{accommodationId}")
                            .build(accommodationId))
                    .retrieve()
                    .bodyToMono(AccommodationDTO.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to AccommodationService: ", e);
        }
    }
}
