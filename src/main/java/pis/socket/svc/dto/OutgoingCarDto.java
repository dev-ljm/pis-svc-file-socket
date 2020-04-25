package pis.socket.svc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingCarDto {
    private String car_no;
    private String outcar_img_path;
    private LocalDateTime updt_date;
    private String updtr_id;
}
