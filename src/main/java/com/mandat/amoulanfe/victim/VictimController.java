package com.mandat.amoulanfe.victim;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/victims")
public class VictimController {

    VictimSpecifivationFactory victimSpecifivationFactory;

    @Autowired
    private VictimService victimService;

    @ApiOperation(value = "Ajout d'une victime")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    @ResponseStatus(CREATED)
    public Long save(@Valid @RequestBody VictimRequest victimRequest) {
        return victimService.save(victimRequest);
    }

    @ApiOperation(value = "Détail d'une victime")
    @GetMapping("{id}")
    @ResponseStatus(OK)
    public Victim getOne(@RequestParam(required = true) UUID id) {
        return victimService.getVictimById(id);
    }

    @ApiOperation(value = "Retourner la liste des victimes par page")
    @GetMapping("/all")
    @ResponseStatus(OK)
    public Page<Victim> findAll(@Valid VictimFilter filter, Pageable pageable) {
        return victimService.findAllVictims(VictimSpecifivationFactory.getVictimsByFilter(filter), pageable);
    }
}
