package com.clip.batch.actuator.feign;

import com.clip.batch.actuator.feign.dto.DiscordMSGRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "discordWebhookCall", url = "${discord.webhook-url}")
public interface DiscordFeign {
    @PostMapping(consumes = "application/json")
    void sendMessage(@RequestBody DiscordMSGRequestDto request);
}
