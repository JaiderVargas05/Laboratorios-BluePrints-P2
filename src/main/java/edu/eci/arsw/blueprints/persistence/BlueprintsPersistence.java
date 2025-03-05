package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;

import java.util.Set;

public interface BlueprintsPersistence {

    /**
     *
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name
     *                                       already exists,
     *                                       or any other low-level persistence
     *                                       error occurs.
     */
    public Blueprint saveBlueprint(Blueprint bp) throws BlueprintPersistenceException, BadRequestException;

    /**
     *
     * @param author     blueprint's author
     * @param bprintname blueprint's author
     * @return the blueprint of the given name and author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException, BadRequestException;

    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException, BadRequestException;

    public Set<Blueprint> getAllBluePrints() throws BlueprintNotFoundException;

    public Blueprint updateBlueprint(String author, String bpname, Blueprint bp) throws BlueprintNotFoundException, BadRequestException;

    /*public void deleteBlueprint(String author, String name) throws BlueprintPersistenceException;*/
}