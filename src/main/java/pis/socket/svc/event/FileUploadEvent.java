package pis.socket.svc.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pis.socket.svc.dto.EnteringCarDto;
import pis.socket.svc.dto.Message;
import pis.socket.svc.service.EnteringCarService;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Component
public class FileUploadEvent implements MessageEvent {

    @Resource
    private EnteringCarService enteringCarService;

    @Override
    public Message fire(Message message) {
        log.debug("FileUploadEvent fire!!!! ");

        String car_no = message.getCarNo();
        String flag = message.getFlag();
        String imageType = message.getImageType();
        String linkDir = message.getLinkDir();
        String imageName = message.getImageName();
        String imageDateDir = message.getImageDateDir();

        StringBuilder linkName = new StringBuilder(linkDir);
        linkName.append(flag).append("/");
        linkName.append(imageDateDir).append("/");
        linkName.append(imageName).append(".").append(imageType);

        EnteringCarDto enteringCar = EnteringCarDto.builder()
                .car_no(car_no)
                .station_id("")
                .oper_area_id("")
                .oper_zone_id("")
                .updt_date(LocalDateTime.now())
                .updtr_id("S05")
                .incar_img_path(linkName.toString())
                .build();

        int evalDupCount = this.enteringCarService.evalDup(enteringCar);
        log.debug("evalDupCount: {}", evalDupCount);
        if (evalDupCount > 0) {

            int successfulCount = this.enteringCarService.updateCarImage(enteringCar);
            log.debug("successfulCount: {}", successfulCount);
        }

        return message;
    }
}
