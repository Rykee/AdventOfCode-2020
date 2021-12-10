package rykee.net.solver;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task07Solver {

    private final Map<String, BagData> bags = new HashMap<>();

    private final Pattern bagNamePattern = Pattern.compile("(([a-z]+\s){2})");
    private final Pattern countPattern = Pattern.compile("^([0-9]+)");

    public int getBagCountThatContainsShiny(List<String> lines) {
        setup(lines);
        BagData shinyGold = bags.get("shiny gold");
        AtomicInteger bagCount = new AtomicInteger(0);
        HashSet<BagData> visitedBags = new HashSet<>();
        visitedBags.add(shinyGold);
        countBagsThatContain(visitedBags, shinyGold, bagCount);
        return bagCount.get();
    }

    public int getShinyContainedCount(List<String> lines) {
        setup(lines);
        return sumBags(bags.get("shiny gold"));
    }

    private int sumBags(BagData bagData) {
        AtomicInteger sum = new AtomicInteger(0);
        bagData.containedBags.forEach((bag, amount) -> {
            sum.addAndGet(amount + amount * sumBags(bag));
        });
        return sum.get();
    }


    private void setup(List<String> lines) {
        bags.clear();
        lines.forEach(bagDesc -> {
            String[] bagParts = bagDesc.split("\sbags contain\s");
            BagData bagData = addOrGet(bagParts[0]);
            Arrays.stream(bagParts[1].split("\s*,\s*"))
                    .forEach(bagString -> {
                        String bagName = runPattern(bagString, bagNamePattern);
                        if (!bagName.equals("no other")) {
                            int amount = Integer.parseInt(runPattern(bagString, countPattern));
                            BagData containedBag = addOrGet(bagName);
                            setRelations(bagData, containedBag, amount);
                        }
                    });
        });
    }

    private String runPattern(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        return matcher.group(1).trim();
    }

    private void countBagsThatContain(Set<BagData> visitedBags, BagData currentBag, AtomicInteger counter) {
        currentBag.bagThatContain.forEach(bagData -> {
            if (!visitedBags.contains(bagData)) {
                visitedBags.add(bagData);
                counter.incrementAndGet();
                countBagsThatContain(visitedBags, bagData, counter);
            }
        });
    }

    private void setRelations(BagData bagData, BagData containedBag, int amount) {
        bagData.containedBags.put(containedBag, amount);
        containedBag.bagThatContain.add(bagData);
    }

    private BagData addOrGet(String bagName) {
        bags.putIfAbsent(bagName, new BagData(bagName));
        return bags.get(bagName);
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Setter
    private static class BagData {

        @EqualsAndHashCode.Include
        private final String bagName;

        private final Map<BagData, Integer> containedBags = new HashMap<>();
        private final List<BagData> bagThatContain = new ArrayList<>();

        private BagData(String bagName) {
            this.bagName = bagName;
        }
    }
}
