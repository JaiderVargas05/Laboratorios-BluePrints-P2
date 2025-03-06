/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BadRequestException;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author hcadavid
 */
@Component("inMemory")
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final ConcurrentHashMap<Tuple<String, String>, Blueprint> blueprints = new ConcurrentHashMap<>();

    public InMemoryBlueprintPersistence() {
        // Load stub data
        Point[] pts = new Point[] { new Point(140, 140), new Point(115, 115) };
        Blueprint bp = new Blueprint("_authorname_", "_bpname_", pts);
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);

        Point[] pts2 = new Point[] { new Point(140, 140), new Point(115, 115) };
        Blueprint bp2 = new Blueprint("Jorge", "plano 1", pts2);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);

        Point[] pts3 = new Point[] { new Point(140, 140), new Point(115, 115) };
        Blueprint bp3 = new Blueprint("Jorge", "plano 2", pts3);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);
    }

    @Override
    public Blueprint saveBlueprint(Blueprint bp) throws BlueprintPersistenceException, BadRequestException {
        if (bp == null) {
            throw new BadRequestException("The given blueprint can't be empty.");
        }
        if (bp.getAuthor() == null || bp.getName() == null) {
            throw new BadRequestException("Author and Name can't be empty.");
        }

        Tuple<String, String> key = new Tuple<>(bp.getAuthor(), bp.getName());
        Blueprint existing = blueprints.putIfAbsent(key, bp);
        if (existing != null) {
            throw new BlueprintPersistenceException("The given blueprint already exists.");
        }

        return bp;
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname)
            throws BlueprintNotFoundException, BadRequestException {
        if (author == null || bprintname == null) {
            throw new BadRequestException("Author and Name can't be empty.");
        }
        Blueprint bp = blueprints.get(new Tuple<>(author, bprintname));
        if (bp == null) {
            throw new BlueprintNotFoundException("No results found.");
        }
        return bp;
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException, BadRequestException {
        if (author == null || author.isEmpty()) {
            throw new BadRequestException("Author can't be empty.");
        }

        Set<Blueprint> bpps = new CopyOnWriteArraySet<>();
        for (Map.Entry<Tuple<String, String>, Blueprint> entry : blueprints.entrySet()) {
            if (entry.getKey().getElem1().equals(author)) {
                bpps.add(entry.getValue());
            }
        }

        if (bpps.isEmpty()) {
            throw new BlueprintNotFoundException("No results found.");
        }

        return bpps;
    }

    @Override
    public Set<Blueprint> getAllBluePrints() throws BlueprintNotFoundException {
        if (blueprints.isEmpty()) {
            throw new BlueprintNotFoundException("No results found.");
        }
        return new CopyOnWriteArraySet<>(blueprints.values());
    }

    @Override
    public Blueprint updateBlueprint(String author, String bpname, Blueprint bp)
            throws BlueprintNotFoundException, BadRequestException {
        if (author == null || bpname == null) {
            throw new BadRequestException("Author and Name can't be empty.");
        }
        if (bp == null) {
            throw new BadRequestException("Blueprint can't be empty.");
        }

        Tuple<String, String> key = new Tuple<>(author, bpname);

        Blueprint updatedBlueprint = blueprints.compute(key, (k, existingBp) -> {
            if (existingBp == null) {
                return null;
            }
            return bp; // Reemplaza el blueprint existente con el nuevo
        });
        if (updatedBlueprint== null){
            throw new BlueprintNotFoundException("No blueprint found.");
        }
        return updatedBlueprint;
    }

    @Override
    public void deleteBlueprint(String author, String name) throws BlueprintPersistenceException {
        Tuple<String, String> key = new Tuple<>(author, name);
        if (blueprints.remove(key) == null) {
            throw new BlueprintPersistenceException("Blueprint not found: " + author + " - " + name);
        }
    }
}
