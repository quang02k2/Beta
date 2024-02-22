package com.example.BetaModel.respository;

import com.example.BetaModel.model.GeneralSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralSettingRepo extends JpaRepository<GeneralSetting, Long> {
}
