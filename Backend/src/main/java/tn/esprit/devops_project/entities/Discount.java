package tn.esprit.devops_project.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Discount {
    private float percentage; // e.g., 0.1 for 10%
}