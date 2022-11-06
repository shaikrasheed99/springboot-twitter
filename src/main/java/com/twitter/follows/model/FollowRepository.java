package com.twitter.follows.model;

import org.springframework.data.repository.CrudRepository;

public interface FollowRepository extends CrudRepository<Follow, FollowsCompositePrimaryKey> {
}
