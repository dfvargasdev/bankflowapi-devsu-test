package com.devsu.bankflowapi.repositories;

import com.devsu.bankflowapi.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
}
