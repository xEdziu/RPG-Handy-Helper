package dev.goral.rpgmanager.rpgSystems.cpRed.manual.equipments;

import dev.goral.rpgmanager.security.CustomReturnables;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedEquipmentsService {
    private final CpRedEquipmentsRepository cpRedEquipmentsRepository;

    // Pobierz wszystkie przedmioty
    public Map<String, Object>  getAllEquipments() {
        List<CpRedEquipmentsDTO> cpRedEquipmentsList = cpRedEquipmentsRepository.findAll().stream().
                map(
                cpRedEquipments -> new CpRedEquipmentsDTO(
                        cpRedEquipments.getName(),
                        cpRedEquipments.getPrice(),
                        cpRedEquipments.getAvailability().toString(),
                        cpRedEquipments.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wyposażenie zostało pobrane.");
        response.put("equipments", cpRedEquipmentsList);
        return response;
    }

    // Pobierz przedmiot po id
    public Map<String, Object> getEquipmentById(Long equipmentId) {
        CpRedEquipmentsDTO cpRedEquipments = cpRedEquipmentsRepository.findById(equipmentId)
                .map(
                        equipments -> new CpRedEquipmentsDTO(
                                equipments.getName(),
                                equipments.getPrice(),
                                equipments.getAvailability().toString(),
                                equipments.getDescription()
                        )).orElseThrow(() -> new RuntimeException("Przedmiot o id " + equipmentId + "nie istnieje"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano przedmiot.");
        response.put("equipment", cpRedEquipments);
        return response;

    }
//
//    // Pobierz wszystkie przedmioty dla admina
//    public Map<String, Object> getAllEquipmentsForAdmin() {
//
//    }
//
//    // Dodaj przedmiot
//    public Map<String, Object> addEquipment(CpRedEquipments cpRedEquipments) {
//
//    }
//
//    // Modyfikuj przedmiot
//    public Map<String, Object> updateEquipment(Long equipmentId, CpRedEquipments cpRedEquipments) {
//
//    }
}
