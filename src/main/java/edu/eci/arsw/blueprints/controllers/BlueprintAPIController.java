/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Response;
import edu.eci.arsw.blueprints.service.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping("/v1/blueprints")
public class BlueprintAPIController {

    private BlueprintsServices blueprintServices;

    @Autowired
    public BlueprintAPIController(BlueprintsServices blueprintServices) {
        this.blueprintServices = blueprintServices;
    }


    @PostMapping("/add")
    public ResponseEntity<Response> addNewBlueprint(@RequestBody Blueprint bpp) {

        Response response = blueprintServices.addNewBlueprint(bpp);
        if(response.code!=200){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, response.description);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping
    public ResponseEntity<?>getAllBlueprints() {

        Response response =  blueprintServices.getAllBlueprints();
        if(response.code!=200){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, response.description);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{author}")
    public ResponseEntity<Response> getByAuthor(@PathVariable String author) {
        Response response =  blueprintServices.getBlueprintsByAuthor(author);
        if(response.code!=200){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, response.description);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<Response> getByAuthorAndBP(@PathVariable String author, @PathVariable String bpname) {
        Response response =  blueprintServices.getBlueprint(author,bpname);
        if(response.code!=200){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, response.description);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

