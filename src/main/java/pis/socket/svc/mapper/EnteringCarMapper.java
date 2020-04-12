package pis.socket.svc.mapper;

import pis.socket.common.MariaDB;
import pis.socket.svc.dto.EnteringCarDto;

@MariaDB
public interface EnteringCarMapper {
    int evalDup(EnteringCarDto enteringCar);
    int updateCarImage(EnteringCarDto enteringCar);
    EnteringCarDto findCarImage(EnteringCarDto enteringCar);
}
