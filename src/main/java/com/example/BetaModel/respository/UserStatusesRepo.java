package com.example.BetaModel.respository;

import com.example.BetaModel.model.UserStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusesRepo extends JpaRepository<UserStatuses, Long> {

}
