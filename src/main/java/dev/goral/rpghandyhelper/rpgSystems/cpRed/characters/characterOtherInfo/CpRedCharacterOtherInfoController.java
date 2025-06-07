package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterOtherInfo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterOtherInfoController {
    private final CpRedCharacterOtherInfoService cpRedCharacterOtherInfoService;


    @GetMapping(path = "/rpgSystems/cpRed/character/OtherInfo/all")
    public Map<String, Object> getAllOtherInfo() {
        return cpRedCharacterOtherInfoService.getAllOtherInfo();
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/OtherInfo/{infoId}")
    public Map<String, Object> getOtherInfoById(@PathVariable("infoId") Long infoId) {
        return cpRedCharacterOtherInfoService.getOtherInfoById(infoId);
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/OtherInfo/character/{characterId}")
    public Map<String, Object> getOtherInfoByCharacterId(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterOtherInfoService.getOtherInfoByCharacterId(characterId);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/character/OtherInfo/all")
    public Map<String, Object> getAllOtherInfoForAdmin() {
        return cpRedCharacterOtherInfoService.getAllOtherInfoForAdmin();
    }

    @PostMapping(path = "/rpgSystems/cpRed/character/OtherInfo/add")
    public Map<String, Object> addOtherInfo(@RequestBody CpRedCharacterOtherInfoRequest cpRedCharacterOtherInfo) {
        return cpRedCharacterOtherInfoService.addOtherInfo(cpRedCharacterOtherInfo);
    }

    @DeleteMapping(path = "/rpgSystems/cpRed/character/OtherInfo/delete/{infoId}")
    public Map<String, Object> deleteOtherInfo(@PathVariable("infoId") Long infoId) {
        return cpRedCharacterOtherInfoService.deleteOtherInfo(infoId);
    }

    @PutMapping(path = "/rpgSystems/cpRed/character/OtherInfo/update/{infoId}")
    public Map<String, Object> updateOtherInfo(@PathVariable("infoId") Long infoId,
                                               @RequestBody CpRedCharacterOtherInfoRequest cpRedCharacterOtherInfo) {
        return cpRedCharacterOtherInfoService.updateOtherInfo(infoId, cpRedCharacterOtherInfo);
    }
}
