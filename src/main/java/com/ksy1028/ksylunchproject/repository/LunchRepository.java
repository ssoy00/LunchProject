package com.ksy1028.ksylunchproject.repository;

import com.ksy1028.ksylunchproject.domain.Lunch;
import com.ksy1028.ksylunchproject.repository.search.LunchSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LunchRepository extends JpaRepository<Lunch, Long>, LunchSearch {
}
