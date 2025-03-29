package edu.eci.arsw.blueprints.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import edu.eci.arsw.blueprints.model.PointMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class STOMPMessagesHandler {
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    private final ConcurrentHashMap<String, List<PointMessage>> drawingPoints = new ConcurrentHashMap<>();

    public STOMPMessagesHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/newpoint.{drawingID}/{author}/{name}")
    //@SendTo("/topic/newpoint.{drawingID}/{author}/{name}")
    public void handlePointEvent(

        PointMessage pt,
        @DestinationVariable String author, 
        @DestinationVariable String name,
        @DestinationVariable String drawingID
        ) throws Exception {
        String drawingKey = drawingID + "/" + author + "/" + name;

        drawingPoints.putIfAbsent(drawingKey, new ArrayList<>());
        List<PointMessage> points = drawingPoints.get(drawingKey);
        synchronized (points) {
            points.add(pt);
        }
        String topic = "/topic/newpoint." + drawingID + "/" + author + "/" + name;
        System.out.println("Nuevo punto recibido para blueprint " + drawingID + "-" + author + " - " + name + ": " + pt);
        messagingTemplate.convertAndSend(topic, pt);
        if (points.size() > 3) {
            String polygonTopic = "/topic/newpolygon." + drawingID + "/" + author + "/" + name;
            System.out.println("Polígono generado para blueprint " + drawingID + "-" + author + " - " + name + ": " + points);
            messagingTemplate.convertAndSend(polygonTopic, points);
            synchronized (points) {
                drawingPoints.put(drawingKey, new ArrayList<>());
            }
        }
    }
}
