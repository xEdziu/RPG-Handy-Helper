package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customEquipments;

import dev.goral.rpgmanager.security.CustomReturnables;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CpRedCustomEquipmentsService {
    private final CpRedCustomEquipmentsRepository cpRedCustomEquipmentsRepository;

    public Map<String, Object> getAllEquipments(){
        List<CpRedCustomEquipmentsDTO> allCustomEquipments = cpRedCustomEquipmentsRepository.findAll().stream()
                .map(CpRedCustomEquipments -> new CpRedCustomEquipmentsDTO(
                        CpRedCustomEquipments.getGameId().getId(),
                        CpRedCustomEquipments.getName(),
                        CpRedCustomEquipments.getPrice(),
                        CpRedCustomEquipments.getAvailability().toString(),
                        CpRedCustomEquipments.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe wyposażenie  zostały pobrane.");
        response.put("customEquipments", allCustomEquipments);
        return response;
    }

    public Map<String, Object> getEquipmentById(Long equipmentId){

    }

    public Map<String, Object> addEquipment(CpRedCustomEquipmentsRequest cpRedCustomEquipments){

    }

    public Map<String, Object> updateEquipment(Long equipmentId, CpRedCustomEquipmentsRequest cpRedCustomEquipments){

    }

    public Map<String, Object> getAllEquipmentsForAdmin(){

    }
}
