package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CpRedCharactersService {

    private final CpRedCharactersRepository cpRedCharactersRepository;

    @Autowired
    public CpRedCharactersService(CpRedCharactersRepository cpRedCharactersRepository) {
        this.cpRedCharactersRepository = cpRedCharactersRepository;
    }


}
