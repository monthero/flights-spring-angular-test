package com.travel.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiRequestController {


    @Autowired
    private ApiRequestRepository apiRequestRepository;

    @GetMapping("/api-requests")
    @CrossOrigin(origins = "*")
    public Map<String, Object> getRequestList() {
        HashMap<String, Object> info = new HashMap<>();
        //return apiRequestRepository.findAll();

        int total_requests = 0;
        int total_4xx = 0;
        int total_5xx = 0;
        int total_successes = 0;
        double average_response_time = 0.0;
        double total_of_response_times = 0.0;
        double min_response_time = Double.MAX_VALUE;
        double max_response_time = Double.MIN_VALUE;

        List<ApiRequest> request_list = new ArrayList<>();

        for (ApiRequest req: this.apiRequestRepository.findAll()) {
            if (! req.getUri().contains("api-requests")) {
                request_list.add(req);
                total_requests++;
                if (req.getStatus() > 499) {
                    total_5xx++;
                } else if (req.getStatus() > 399 && req.getStatus() < 500) {
                    total_4xx++;
                } else {
                    total_successes++;
                }
                total_of_response_times += req.getResponseTime();

                if (req.getResponseTime() < min_response_time) {
                    min_response_time = req.getResponseTime();
                }

                if (req.getResponseTime() > max_response_time) {
                    max_response_time = req.getResponseTime();
                }
            }
        }

        average_response_time = total_of_response_times / total_requests;


        info.put("list", request_list);

        HashMap<String, Integer> requests = new HashMap<>();
        requests.put("total", total_requests);
        requests.put("total_success", total_successes);
        requests.put("total_4xx", total_4xx);
        requests.put("total_5xx", total_5xx);

        HashMap<String, Double> response_times = new HashMap<>();
        response_times.put("min", min_response_time);
        response_times.put("max", max_response_time);
        response_times.put("average", average_response_time);
        response_times.put("total", total_of_response_times);

        info.put("response_times", response_times);
        info.put("info", requests);
        return info;
    }
}
