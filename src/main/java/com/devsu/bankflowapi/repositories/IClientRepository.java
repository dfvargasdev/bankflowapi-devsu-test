package com.devsu.bankflowapi.repositories;

import com.devsu.bankflowapi.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepository extends JpaRepository<Client, Long> {
}
