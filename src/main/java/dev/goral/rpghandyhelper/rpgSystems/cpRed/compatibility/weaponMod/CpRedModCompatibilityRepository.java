package dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedModCompatibilityRepository extends JpaRepository<CpRedModCompatibility, Long> {
    List<CpRedModCompatibilityDTO> findAllByWeaponIdAndIsWeaponCustom(Long weaponId, boolean weaponCustom);

    List<CpRedModCompatibilityDTO> findAllByModIdAndIsModCustom(Long modId, boolean modCustom);

    boolean existsByWeaponIdAndModIdAndIsWeaponCustomAndIsModCustom(Long weaponId, Long modId, boolean weaponCustom, boolean modCustom);
}
