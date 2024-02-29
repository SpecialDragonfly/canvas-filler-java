package orme.dominic.canvasfiller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import orme.dominic.canvasfiller.handlers.CanvasHandler;
import orme.dominic.canvasfiller.handlers.HeartbeatHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(canvasHandler(),"/canvas-filler").setAllowedOrigins("*");
        registry.addHandler(heartbeatHandler(), "/heartbeat").setAllowedOrigins("*");
    }

    @Bean
    public CanvasHandler canvasHandler() {
        return new CanvasHandler();
    }

    @Bean
    public WebSocketHandler heartbeatHandler() {
        return new HeartbeatHandler();
    }
}


