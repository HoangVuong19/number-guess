package org.game.numberguess.repository;

import org.game.numberguess.dto.response.UserLeaderboard;
import org.game.numberguess.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u.username AS username, u.score AS score FROM User u ORDER BY u.score DESC")
    List<UserLeaderboard> findTop10Leaderboard();
}