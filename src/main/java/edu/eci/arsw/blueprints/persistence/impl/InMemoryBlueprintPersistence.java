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

/**
 *
 * @author hcadavid
 */
@Component("inMemory")
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final Map<Tuple<String, String>, Blueprint> blueprints = new HashMap<>();

    public InMemoryBlueprintPersistence() {
        // load stub data
        Point[] pts = new Point[] { new Point(140, 140), new Point(115, 115) };
        Blueprint bp = new Blueprint("_authorname_", "_bpname_ ", pts);
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        // New blueprints with same author - BP1
        Point[] pts2 = new Point[] { new Point(140, 140), new Point(115, 115) };
        Blueprint bp2 = new Blueprint("Jorge", "plano 1", pts2);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);
        // BP2
        Point[] pts3 = new Point[] { new Point(140, 140), new Point(115, 115) };
        Blueprint bp3 = new Blueprint("Jorge", "plano 2", pts3);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);


    }

    @Override
    public Blueprint saveBlueprint(Blueprint bp) throws BlueprintPersistenceException, BadRequestException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        }
        if(bp == null){
            throw new BadRequestException("The given blueprint can't be empty: " + bp);
        }
        if(bp.getAuthor() == null || bp.getName() == null){
            throw new BadRequestException("Author and Name can't be empty: " + bp);
        }
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        return blueprints.get(new Tuple<>(bp.getAuthor(), bp.getName()));
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException, BadRequestException {
        if(author == null || bprintname == null){
            throw new BadRequestException("Author and Name can't be empty: " + bprintname);
        }
        Blueprint bp = blueprints.get(new Tuple<>(author, bprintname));
        if (bp == null)
            throw new BlueprintNotFoundException("Not results found");
        return bp;
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException, BadRequestException {
        if(author == null || author == ""){
            throw new BadRequestException("Author can't be empty: " + author);
        }
        Set<Blueprint> bpps = new HashSet<>();
        for (Map.Entry<Tuple<String, String>, Blueprint> entry : blueprints.entrySet()) {
            if (entry.getKey().getElem1().equals(author)) {
                bpps.add(entry.getValue());
            }
        }
        if (bpps.isEmpty())
            throw new BlueprintNotFoundException("Not results found");
        return bpps;
    }

    @Override
    public Set<Blueprint> getAllBluePrints() throws BlueprintNotFoundException {
        if (blueprints.isEmpty())
            throw new BlueprintNotFoundException("Not results found");
        Set<Blueprint> blueprintSet = new HashSet<>(blueprints.values());
        return blueprintSet;
    }

    @Override
    public Blueprint updateBlueprint(String author, String bpname, Blueprint bp) throws BlueprintNotFoundException, BadRequestException {
        if(author == null || bpname == null){
            throw new BadRequestException("Author and Name can't be empty");
        }
        if(bp == null){
            throw new BadRequestException("Blueprint can't be empty");
        }
        Tuple<String, String> oldKey = new Tuple<>(author, bpname);
        Tuple<String, String> key = new Tuple<>(bp.getAuthor(), bp.getName());
        if (!blueprints.containsKey(oldKey)) {
            throw new BlueprintNotFoundException("Blueprint not found");
        }
        blueprints.remove(oldKey);
        blueprints.put(key, bp);
        return blueprints.get(key);
    }

    /*@Override
    public void deleteBlueprint(String author, String name) throws BlueprintPersistenceException {
        Tuple<String, String> key = new Tuple<>(author, name);
        if (!blueprints.containsKey(key)) {
            throw new BlueprintPersistenceException("Blueprint not found: " + author + " - " + name);
        }
        blueprints.remove(key);
    }*/

}
