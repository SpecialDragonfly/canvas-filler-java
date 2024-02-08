package orme.dominic.canvasfiller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import orme.dominic.canvasfiller.service.PixelService;

@Configuration
public class OtherConfig {
    @Bean
    public PixelService pixelService() {
        return new PixelService();
    }
}
