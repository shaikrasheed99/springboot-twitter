package com.twitter.follows.model;

import com.twitter.users.model.IUser;
import com.twitter.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FollowRepositoryTest {
    @Autowired
    private FollowRepository followRepository;
    private User ironman;
    private User thor;
    private User captain;
    private User thanos;
    private ArrayList<Follow> follows;

    @BeforeEach
    void setUp() {
        ironman = new User(1, "Ironman");
        thor = new User(2, "Thor");
        captain = new User(3, "Captain");
        thanos = new User(4, "Thanos");
        FollowsCompositePrimaryKey captainIronmanKey = new FollowsCompositePrimaryKey(captain, ironman);
        FollowsCompositePrimaryKey thanosIronmanKey = new FollowsCompositePrimaryKey(thanos, ironman);
        FollowsCompositePrimaryKey thorIronmanKey = new FollowsCompositePrimaryKey(thor, ironman);
        FollowsCompositePrimaryKey thorCaptainKey = new FollowsCompositePrimaryKey(thor, captain);
        FollowsCompositePrimaryKey thorThanosKey = new FollowsCompositePrimaryKey(thor, thanos);
        Follow captainFollowsIronman = new Follow(captainIronmanKey);
        Follow thanosFollowsIronman = new Follow(thanosIronmanKey);
        Follow thorFollowsIronman = new Follow(thorIronmanKey);
        Follow thorFollowsCaptain = new Follow(thorCaptainKey);
        Follow thorFollowsThanos = new Follow(thorThanosKey);
        follows = new ArrayList<>();
        follows.add(captainFollowsIronman);
        follows.add(thanosFollowsIronman);
        follows.add(thorFollowsIronman);
        follows.add(thorFollowsCaptain);
        follows.add(thorFollowsThanos);
        followRepository.saveAll(follows);
    }

    @Test
    void shouldBeAbleToFollowAUser() {
        FollowsCompositePrimaryKey captainIronmanKey = follows.get(0).getFollowsCompositePrimaryKey();

        Follow result = followRepository.findById(captainIronmanKey).get();

        assertEquals(result.getFollowsCompositePrimaryKey().getFollower().getId(), captain.getId());
        assertEquals(result.getFollowsCompositePrimaryKey().getFollower().getName(), captain.getName());
    }

    @Test
    void shouldBeAbleToReturnFollowersOfAUser() {
        List<IUser> ironmanFollowers = followRepository.findFollowers(ironman.getId());

        assertEquals(3, ironmanFollowers.size());
    }

    @Test
    void shouldBeAbleToReturnFollowsOfAUser() {
        List<IUser> thorFollows = followRepository.findFollows(thor.getId());

        assertEquals(3, thorFollows.size());
    }

    @Test
    void shouldBeAbleToUnfollowAUser() {
        Follow captainFollowsIronman = follows.get(0);
        FollowsCompositePrimaryKey captainIronmanKey = captainFollowsIronman.getFollowsCompositePrimaryKey();

        followRepository.delete(captainFollowsIronman);

        Optional<Follow> resultFollow = followRepository.findById(captainIronmanKey);

        assertTrue(resultFollow.isEmpty());
    }
}
