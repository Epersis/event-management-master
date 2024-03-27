package org.eventmanagement.repository;

import java.util.Optional;

import org.eventmanagement.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserId(Long id);
    Optional<Wallet> findByUserEmail(String id);
}
