package uns.ac.rs.rating_service.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.UUID;

@Service
public class ReservationServiceClient {
    private final WebClient webClient;

    @Autowired
    public ReservationServiceClient(WebClient.Builder webClientBuilder,
                             @Value("${reservation.service.url}") String reservationServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(reservationServiceUrl).build();
    }

    public boolean isGuestHasSuccessfullyPassedReservationHost(String host, String jwtToken) {
        try {
            return Boolean.TRUE.equals(webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/reservations/guest/has-successfully-passed-host/{host}")
                            .build(host))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to ReservationService: ", e);
        }
    }

    public boolean isGuestHasSuccessfullyPassedReservationAccommodation(UUID idAccommodation, String jwtToken) {
        try {
            return Boolean.TRUE.equals(webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/reservations/guest/has-successfully-passed-accommodation/{idAccommodation}")
                            .build(idAccommodation))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to ReservationService: ", e);
        }
    }
}
