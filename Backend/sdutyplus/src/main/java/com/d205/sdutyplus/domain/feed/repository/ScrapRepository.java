package com.d205.sdutyplus.domain.feed.repository;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.feed.entity.Scrap;
import com.d205.sdutyplus.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    public Optional<Scrap> findByUserAndFeed(User user, Feed feed);
}