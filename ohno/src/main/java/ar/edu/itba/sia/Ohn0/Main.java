package ar.edu.itba.sia.Ohn0;

import ar.edu.itba.sia.Ohn0.Heuristics.FillingBlanksHeuristic;
import ar.edu.itba.sia.gps.GPSEngine;
import ar.edu.itba.sia.gps.SearchStrategy;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Main {

    public static void main(String[] args) throws IOException {
        FileManager fm = new FileManager();
        Board board = fm.readStateFromFile(Paths.get("board6X6"));
//        System.out.println(board.isNumberCorrect(4, 0));

        List<ar.edu.itba.sia.gps.api.Rule> problemRules = generateRulesFilling(board.getSize());
        ProblemImpl OhN0 = new ProblemImpl(board, problemRules);
        FillingBlanksHeuristic heuristic = new FillingBlanksHeuristic();
        GPSEngine engine = new GPSEngine(OhN0, SearchStrategy.ASTAR, heuristic);
        engine.findSolution();
    }

    private static List<ar.edu.itba.sia.gps.api.Rule> generateRulesReparation(int size) {
        int i, j;
        List<ar.edu.itba.sia.gps.api.Rule> rules = new ArrayList<>();

        for(i = 0; i < size; i++) {
            for(j = 0; j < size; j++) {
                final int I = i, J = j;
                rules.add(new Ohn0Rule("blue:" + i +"," + j, state -> {
                    Board currentBoard = (Board) state;
                    Board newBoard;
                    newBoard = currentBoard.switchColor(I, J, Color.BLUE);
                    if(newBoard == null) {
                        return Optional.empty();
                    }
                    else {
                        return Optional.of(newBoard);
                    }
                }));

                rules.add(new Ohn0Rule("red:" + i + "," + j, state -> {
                    Board currentBoard = (Board) state;
                    Board newBoard;
                    newBoard = currentBoard.switchColor(I, J, Color.RED);
                    if(newBoard == null) {
                        return Optional.empty();
                    }
                    else {
                        return Optional.of(newBoard);
                    }
                }));
            }
        }

        return rules;
    }

    private static List<ar.edu.itba.sia.gps.api.Rule> generateRulesFilling(int size) {
        int i, j;
        List<ar.edu.itba.sia.gps.api.Rule> rules = new ArrayList<>();

        for(i = 0; i < size; i++) {
            for(j = 0; j < size; j++) {
                rules.add(generateRule(i, j, Color.BLUE));
                rules.add(generateRule(i, j, Color.RED));
            }
        }

        return rules;
    }

    /**
     * Generate an {@link Ohn0Rule} that fills the cell at (i, j) with the given color if the cell is not blank.
     *
     * @param i     X coordinate
     * @param j     Y coordinate
     * @param color Color to fill if not empty
     * @return The corresponding rule
     */
    private static Ohn0Rule generateRule(final int i, final int j, Color color) {
        return new Ohn0Rule(String.format("%s @ (%d,%d)", color.name(), i, j), state -> {
            Board currentBoard = (Board) state;
            if (!currentBoard.getCell(i, j).isBlank()) return Optional.empty();
            Board newBoard = currentBoard.switchColor(i, j, color);
            if (newBoard == null || !newBoard.isChangeValid(i,j)) {
                return Optional.empty();
            }
            else {
                return Optional.of(newBoard);
            }
        });
    }
}