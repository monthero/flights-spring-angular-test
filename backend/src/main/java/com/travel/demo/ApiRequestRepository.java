package com.travel.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRequestRepository extends CrudRepository<ApiRequest, String> {

}
