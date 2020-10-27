package com.mandat.amoulanfe.victim;

import com.mandat.amoulanfe.role.AccessLevel;
import com.mandat.amoulanfe.role.AccessRestriction;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/victims")
public class VictimController {

    VictimSpecifivationFactory victimSpecifivationFactory;

    @Autowired
    private VictimService victimService;

    @ApiOperation(value = "Ajout d'une victime")
    @PostMapping("/add")
    @ResponseStatus(CREATED)
    public Long save(@Valid @RequestBody VictimRequest victimRequest) {
        AccessLevel accessLevel = AccessRestriction.getCurrentUserAccessLevel();
        if (accessLevel == AccessLevel.ALL || accessLevel == AccessLevel.ASSIGNED) {
            return victimService.save(victimRequest);
        }

        //TODO: throw an exception
        return null;
    }

    @ApiOperation(value = "Détail d'une victime")
    @GetMapping("{id}")
    @ResponseStatus(OK)
    public Victim getOne(@PathVariable Long id) {
        return victimService.getVictimById(id);
    }

    @ApiOperation(value = "Suppression d'une victime")
    @DeleteMapping("delete/{id}")
    @ResponseStatus(OK)
    public void delete(@PathVariable Long id) {
        victimService.deleteVictimById(id);
    }

    @ApiOperation(value = "Suppression d'une liste de victimes")
    @DeleteMapping("delete")
    @ResponseStatus(OK)
    public void deleteManyVictims(@RequestParam List<Long> idList) {
        //TODO: To verify all requests are done
        for (Long id : idList) {
            delete(id);
        }
    }

    @ApiOperation(value = "Retourner la liste des victimes par page")
    @GetMapping("/all")
    @ResponseStatus(OK)
    public Page<Victim> findAll(@Valid VictimFilter filter, Pageable pageable) {
        return victimService.findAllVictims(VictimSpecifivationFactory.getVictimsByFilter(filter), pageable);
    }
}
