package pis.socket.svc.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarImageDto {

    private String baseDir;
    private String baseUploadDir;
    private String baseDownloadDir;
    private String imageType;

}
