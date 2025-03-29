package edu.eci.arsw.blueprints.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import edu.eci.arsw.blueprints.model.PointMessage;

@Controller
public class STOMPMessagesHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public STOMPMessagesHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/newpoint.{drawingID}/{author}/{name}")
    @SendTo("/topic/newpoint.{drawingID}/{author}/{name}")
    public PointMessage handlePointEvent(

        PointMessage pt,
        @DestinationVariable String author, 
        @DestinationVariable String name,
        @DestinationVariable String drawingID
        ) throws Exception {

        String topic = "/topic/newpoint." + drawingID +"/" + author + "/" + name;
        System.out.println("Nuevo punto recibido para blueprint " + drawingID + "-" + author + " - " + name + ": " + pt);
        //messagingTemplate.convertAndSend(topic, pt);
        return pt;
    }
}
