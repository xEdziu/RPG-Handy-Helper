package dev.goral.rpgmanager.rpgSystems.cpRed.manual.equipments;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedEquipmentsService {
    private final CpRedEquipmentsRepository cpRedEquipmentsRepository;

    // Pobierz wszystkie przedmioty
    public List<CpRedEquipmentsDTO> getAllEquipments() {
        List<CpRedEquipments> cpRedEquipmentsList = cpRedEquipmentsRepository.findAll();
        return cpRedEquipmentsList.stream().map(
                cpRedEquipments -> new CpRedEquipmentsDTO(
                        cpRedEquipments.getName(),
                        cpRedEquipments.getPrice(),
                        cpRedEquipments.getAvailability().toString(),
                        cpRedEquipments.getDescription()
                )
        ).toList();
    }
//
//    // Pobierz przedmiot po id
//    public CpRedEquipmentsDTO getEquipmentById(Long equipmentId) {
//
//    }
//
//    // Pobierz wszystkie przedmioty dla admina
//    public List<CpRedEquipments> getAllEquipmentsForAdmin() {
//        return cpRedEquipmentsRepository.findAll();
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
