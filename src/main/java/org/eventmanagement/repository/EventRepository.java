package org.eventmanagement.repository;

import org.eventmanagement.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EventRepository extends JpaRepository<Event, Long>,  PagingAndSortingRepository<Event, Long> {
}
