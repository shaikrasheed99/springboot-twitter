package com.twitter.follows.model;

import com.twitter.users.model.IUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends CrudRepository<Follow, FollowsCompositePrimaryKey> {
    @Query(value = "SELECT users.id AS id, users.name AS name FROM users WHERE id IN (SELECT follower_id FROM follows WHERE follows_id = :id)", nativeQuery = true)
    List<IUser> findFollowers(@Param("id") int id);

    @Query(value = "SELECT users.id AS id, users.name AS name FROM users WHERE id IN (SELECT follows_id FROM follows WHERE follower_id = :id)", nativeQuery = true)
    List<IUser> findFollows(@Param("id") int id);
}
