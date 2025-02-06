package org.shreya.notificationservicenew.repo;

import org.shreya.notificationservicenew.model.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, String> {
}
