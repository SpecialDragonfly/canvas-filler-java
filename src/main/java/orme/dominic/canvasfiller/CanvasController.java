package orme.dominic.canvasfiller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import orme.dominic.canvasfiller.dto.Point;
import orme.dominic.canvasfiller.dto.StreamMessage;
import orme.dominic.canvasfiller.service.PixelService;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
public class CanvasController {
    @Autowired
    private PixelService pixelService;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @MessageMapping("/test")
    @SendTo("/outgoing/test")
    public String testIndex() { return "Greetings from Spring Boot WebSocket!"; }

    @MessageMapping("/pixels/{queueName}")
    @SendTo("/outgoing/greetings")
    public String getDataStream(StreamMessage message) throws Exception {
        System.out.println(message);
        return "Hello world";
//        String queueName = message.queueName();
//        String retry = message.retry();
//        boolean isFinished = this.pixelService.isFinished(queueName);
//        // We're just mopping up.
//        if (isFinished && this.pixelService.numberInQueue(queueName) == 0) {
//            // If there's nothing left in the queue, there's nothing else coming either. So we can end.
//            return "end";
//        }
//
//        ArrayList<Point> points = this.pixelService.getPoints(queueName, retry);
//
//        return points.stream().map(Point::toString).collect(Collectors.joining("|"));
    }

    @PostMapping("/start")
    public String start(@RequestParam String queueName, @RequestParam int width, @RequestParam int height, @RequestParam String type, @RequestParam int blur) {
        try {
            this.pixelService.createCanvas(queueName, width, height, type, blur);
            this.pixelService.start(queueName);
            return "Started";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/pixels/{queueName}")
    public String getDataPipe(@PathVariable String queueName, @RequestParam String retry) {

        boolean isFinished = this.pixelService.isFinished(queueName);
        // We're just mopping up.
        if (isFinished && this.pixelService.numberInQueue(queueName) == 0) {
            // If there's nothing left in the queue, there's nothing else coming either. So we can end.
            return "end";
        }

        ArrayList<Point> points = this.pixelService.getPoints(queueName, retry);

        return points.stream().map(Point::toString).collect(Collectors.joining("|"));
    }

    @GetMapping("/refresh/{queueName}")
    public String refreshPixel(@PathVariable String queueName, @RequestParam("row") int row, @RequestParam("column") int column) {
        return this.pixelService.getPoint(queueName, row, column);
    }

    @GetMapping("/pipes")
    public String getPipeSizes() {
        return this.pixelService.toString();
    }

}
