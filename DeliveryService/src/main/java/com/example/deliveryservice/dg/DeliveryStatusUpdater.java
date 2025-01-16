package com.example.deliveryservice.dg;

import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.enums.DeliveryStatus;
import com.example.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryStatusUpdater {
    private final DeliveryRepository deliveryRepository;

    @Transactional
    @Scheduled(fixedDelay = 10000)
    public void deliveryStatusUpdate() {
        log.info("----------- delivery status update ------------");

        List<Delivery> inDeliveryList = deliveryRepository.findAllByStatus(DeliveryStatus.IN_DELIVERY);
        inDeliveryList.forEach(delivery -> {
            delivery.changeStatus(DeliveryStatus.COMPLETED);
            deliveryRepository.save(delivery);
        });

        List<Delivery> requestedList = deliveryRepository.findAllByStatus(DeliveryStatus.REQUESTED);
        requestedList.forEach(delivery -> {
            delivery.changeStatus(DeliveryStatus.IN_DELIVERY);
            deliveryRepository.save(delivery);
        });
    }
}