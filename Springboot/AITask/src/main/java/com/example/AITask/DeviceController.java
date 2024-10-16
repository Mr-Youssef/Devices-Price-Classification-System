package com.example.AITask;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DeviceController {

    @Autowired
    private PricePredictionService pricePredictionService;

    private long idCounter = 100l;

    // The dataset, with a few records
    private Map<String, Device> db = new HashMap<>(){{
        put("1", new Device(1l, 842, 0, 2.2f, 0, 1, 0, 7, 0.6f, 188, 2, 2, 20, 756, 2549, 9, 7, 19, 0, 0, 1, 1));
        put("2", new Device(2l, 1021, 1, 0.5f, 1, 0, 1, 53, 0.7f, 136, 3, 6, 905, 1988, 2631, 17, 3, 7, 1, 1, 0, 2));
        put("3", new Device(3l, 1043, 1, 1.8f, 1, 14, 0, 5, 0.1f, 193, 3, 16, 226, 1412, 3476, 12, 7, 2, 0, 1, 0, 0));

    }};

    @GetMapping("/devices/")
    public Collection<Device> getDevices() {
        // returns all the devices
        // Note that this method cannot be POST
        return db.values();
    }

    @GetMapping("/devices/{id}")
    public Device getDeviceByID(@PathVariable String id) {
        // returns specific device given its id
        Device device = db.get(id);
        // return error 404 in case no device is found
        if (device == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return device;
    }

    @PostMapping("/devices")
    public Device addDevice(@RequestBody Device device) {
        // adds a device to the database
        device.setId(idCounter++);      // creates unique ID
        db.put(Long.toString(device.getId()), device);      // adds device to database
        return device;
    }

    @PostMapping("/predict/{deviceId}")
    @Transactional
    public int predictPrice(@PathVariable String deviceId) {
        // predicts a price range for the device
        Device device = db.get(deviceId);       // Gets the device
        // return error 404 in case no device is found
        if (device == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        int price = pricePredictionService.predictPrice(device);
        System.out.println("yaraaaaab: " + device);
        device.setPriceRange(price);
        return price;
    }

}
