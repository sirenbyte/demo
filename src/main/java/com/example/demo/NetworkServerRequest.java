package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetworkServerRequest {
    @JsonProperty("deviceQueueItem")
    private DeviceQueueItem deviceQueueItem;
}
