package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobApplicationDto {

    private RequestStatus status;
    private LocalDateTime appliedAt;

}
