package tn.esprit.devops_project.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OperatorRatingStatistics {
    private double averageRating;
    private long count1to2;
    private long count2to3;
    private long count3to4;
    private long count4to5;
}
