package pis.socket.svc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnteringCarDto {
    private long incar_seq;
    private String incar_date;
    private String station_id;
    private String oper_area_id;
    private String oper_zone_id;
    private String car_no;
    private String incar_img_path;

    private LocalDateTime regt_date;
    private String regtr_id;
    private LocalDateTime updt_date;
    private String updtr_id;

}
