package com.travel.demo;


import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "api_request")
public class ApiRequest implements Serializable {

    @Id
    @Generated(GenerationTime.ALWAYS)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uri;
    private Integer status;
    private String method;
    private Long response_time;

    ApiRequest() { }

    ApiRequest(String uri, int status, String method, Long response_time) {
        // this.id = id;
        this.uri = uri;
        this.status = status;
        this.method = method;
        this.response_time = response_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getResponseTime() {
        return response_time;
    }

    public void setResponseTime(Long response_time) {
        this.response_time = response_time;
    }
}
