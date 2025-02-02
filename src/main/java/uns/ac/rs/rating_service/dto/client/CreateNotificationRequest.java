package uns.ac.rs.rating_service.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateNotificationRequest {
    private String recipient;

    private String message;

    private String type;
}
