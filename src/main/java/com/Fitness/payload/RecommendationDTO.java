package com.Fitness.payload;

import com.Fitness.model.Activity;
import com.Fitness.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {

    private Long recommendationId;
    private String type;
    private String recommendation;
    private List<String> improvement;
    private List<String> safety;
    private List<String> suggestion;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Activity activity;
}
