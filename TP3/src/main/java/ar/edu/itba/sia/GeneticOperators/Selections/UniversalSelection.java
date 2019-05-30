package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class UniversalSelection implements Selection {
    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        double r = Math.random();
        double[] randoms = new double[quantity];
        initializeRandoms(randoms, quantity, r);
        List<Warrior> selectedWarriors = new ArrayList<>();
        int warriorIndex = 0;
        int randomIndex  = 0;
        double totalPerformance = getTotalPerformance(warriors);
        //maybe shuffle warrior collection? TODO
        int selectedQuantity = 0;
        double accumulatedPerformance = warriors.get(warriorIndex).getPerformance();

        while(selectedQuantity < quantity) {

            if(accumulatedPerformance / totalPerformance > randoms[randomIndex]) {
                selectedWarriors.add(warriors.get(warriorIndex));
                randomIndex++;
                selectedQuantity++;
            }
            else {
                warriorIndex++;
                accumulatedPerformance += warriors.get(warriorIndex).getPerformance();

            }
        }

        return selectedWarriors;
    }

    protected void initializeRandoms(double[] randoms, int quantity, double r) {
        for(int i = 0; i < quantity; i++) {
            randoms[i] = (r + i) / quantity; // j = i + 1 -> j - 1 = i
        }
    }

    static double getTotalPerformance(List<Warrior> warriors) {
        return warriors.stream()
                .map(Warrior::getPerformance)
                .reduce(Double::sum)
                .orElse(0D);
    }

}
