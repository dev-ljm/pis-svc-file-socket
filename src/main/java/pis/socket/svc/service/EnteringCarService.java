package pis.socket.svc.service;

import pis.socket.svc.dto.EnteringCarDto;

public interface EnteringCarService {
    int evalDup(EnteringCarDto enteringCar);
    int updateCarImage(EnteringCarDto enteringCar);
    EnteringCarDto findCarImage(EnteringCarDto enteringCar);
}
