package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customEquipments;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomEquipmentsService {
    private final CpRedCustomEquipmentsRepository cpRedCustomEquipmentsRepository;

    // Pobierz wszystkie przedmioty
    public List<CpRedCustomEquipmentsDTO> getAllEquipments(){

    }

    // Pobierz przedmiot po id
    public CpRedCustomEquipmentsDTO getEquipmentById(Long equipmentId){

    }

    // Dodaj przedmiot
    public Map<String, Object> addEquipment(CpRedCustomEquipments cpRedCustomEquipments){

    }

    // Modyfikować przedmiot
    public Map<String, Object> updateEquipment(Long equipmentId, CpRedCustomEquipments cpRedCustomEquipments){

    }

    // Pobierz wszystkie przedmioty dla admina
    public List<CpRedCustomEquipments> getAllEquipmentsForAdmin(){
        return cpRedCustomEquipmentsRepository.findAll();
    }


}
