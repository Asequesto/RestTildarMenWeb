package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TranslatorsJobsDto {

    private Long applicationId;
    private Long employerId;
    private String type;
    private RequestStatus status;
    private LocalDateTime appliedAt;
    private JobDescDto job;

}
