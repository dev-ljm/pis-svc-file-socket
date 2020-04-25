package pis.socket.svc.mapper;

import pis.socket.common.MariaDB;
import pis.socket.svc.dto.OutgoingCarDto;

@MariaDB
public interface OutgoingCarMapper {
    int updateCarImage(OutgoingCarDto outgoingCar);
}
