package com.cerner.PatientApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cerner.PatientApp.model.Address;

/**
 * AddressRepository is an interface that extends the JpaRepository.
 * 
 * It provides basic CRUD operations for the Address entity.
 * 
 * @author Akash Tangde
 *
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
