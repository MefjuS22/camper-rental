package com.camper.rental.entity.fleet;

import com.camper.rental.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "campers",
    indexes = {
        @Index(name = "idx_campers_registration_number", columnList = "registration_number"),
        @Index(name = "idx_campers_vin", columnList = "vin")
    }
)
public class Camper extends BaseEntity {

    @Column(name = "registration_number", nullable = false, unique = true, length = 50)
    private String registrationNumber;

    @Column(name = "vin", nullable = false, unique = true, length = 100)
    private String vin;

    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false, length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "camper_model_id", nullable = false)
    private CamperModel camperModel;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CamperModel getCamperModel() {
        return camperModel;
    }

    public void setCamperModel(CamperModel camperModel) {
        this.camperModel = camperModel;
    }
}
