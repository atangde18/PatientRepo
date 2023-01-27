package com.cerner.PatientApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cerner.PatientApp.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
