package helper;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class CompositionIdentifier {

    public String findBestMatch(String input, List<String> items) {
        String[] inputArr = cleanCandidate(input).split("\\s+");
        HashSet<String> inputAsWorldsSet = new HashSet<>();
        Collections.addAll(inputAsWorldsSet, inputArr);
        System.out.printf("Input: %s\n", input);
        Map<String, List<String>> bestMatch = null;
        for (var item : items) {
            Map<String, List<String>> bestMatchCandidateMap = new HashMap<>();
            String[] itemArr = cleanCandidate(item).split("\\s+");
            List<String> listOfCandidateItems = new ArrayList<>();
            for (var candidateItem : itemArr) {
                if (inputAsWorldsSet.contains(candidateItem)) {
                    listOfCandidateItems.add(candidateItem);
                }
            }
            if (!listOfCandidateItems.isEmpty()) {
                bestMatchCandidateMap.put(item, listOfCandidateItems);
            }
            if (bestMatch == null && !listOfCandidateItems.isEmpty()) {
                bestMatch = bestMatchCandidateMap;
            } else if (bestMatch != null) {
                String bestMatchKey = null;
                Set<String> keys = bestMatch.keySet();
                for (var key : keys) {
                    bestMatchKey = key;
                }
                if (listOfCandidateItems.size() > bestMatch.get(bestMatchKey).size()) {
                    bestMatch = bestMatchCandidateMap;
                } else if (listOfCandidateItems.size() == bestMatch.get(bestMatchKey).size()) {
                    var bestMatchCommonElements = bestMatch.get(bestMatchKey);
                    if (String.join(", ", listOfCandidateItems).length() > String.join(", ", bestMatchCommonElements).length()) {
                        bestMatch = bestMatchCandidateMap;
                    }
                }
            }
        }
        System.out.printf("BestMatch content: %s\n", bestMatch);

        Set<String> keys = bestMatch.keySet();
        String stemName = null;
        for (var key : keys) {
            stemName = key;
        }
        return stemName;
    }

    public String correctCompositionSpelling(String compositionName) {
        String newCompositionName = compositionName.toLowerCase();
        if (newCompositionName.contains(" микс") && !newCompositionName.contains("роза")) {
            newCompositionName = newCompositionName.replace("микс", "");
        }
        if (newCompositionName.contains("голландия") && !newCompositionName.contains("роза")) {
            newCompositionName = newCompositionName.replace("голландия", "");
            newCompositionName = newCompositionName.replace(" г ", " ");
        }
        if (newCompositionName.contains("*")) {
            newCompositionName = newCompositionName.replace("*", "х");
        } else if (newCompositionName.contains("роза ")) {
            if (newCompositionName.contains("роза ") && newCompositionName.contains(" 4 ")) {
                newCompositionName = newCompositionName.replace(" 4 ", " 40 ");
            } else if (newCompositionName.contains("роза ") && newCompositionName.contains(" 5 ")) {
                newCompositionName = newCompositionName.replace(" 5 ", " 50 ");
            } else if (newCompositionName.contains("роза ") && newCompositionName.contains(" 6 ")) {
                newCompositionName = newCompositionName.replace(" 6 ", " 60 ");
            } else if (newCompositionName.contains("роза ") && newCompositionName.contains(" 7 ")) {
                newCompositionName = newCompositionName.replace(" 7 ", " 70 ");
            }
            if (newCompositionName.contains("спрей")) {
                newCompositionName = newCompositionName.replace("спрей", "кустовая");
            }
        } else if (newCompositionName.contains("спрей")) {
            newCompositionName = newCompositionName.replace("спрей", "кустовая");
        } else if (newCompositionName.contains("матиола")) {
            newCompositionName = "маттиола";
        } else if (newCompositionName.contains("хеллеборус")) {
            newCompositionName = "хелеборус";
        } else if (newCompositionName.contains("солидага")) {
            newCompositionName = "солидаго";
        } else if (newCompositionName.contains("антиринум")) {
            newCompositionName = "антирринум";
        } else if (newCompositionName.contains("пуансетия")) {
            newCompositionName = newCompositionName.replace("пуансетия", "пуансеттия");
        } else if (newCompositionName.contains("гербера") && newCompositionName.contains("мини")) {
            newCompositionName = "гербера мини";
        } else if (newCompositionName.contains("гербера") && !newCompositionName.contains("мини")) {
            newCompositionName = "гербера";
        } else if (newCompositionName.contains("нобилис блю")) {
            newCompositionName = "нобилис";
        } else if (newCompositionName.contains("шишка сосновая")) {
            newCompositionName = "шишка сосновая";
        } else if (newCompositionName.contains("топпер")) {
            newCompositionName = "топпер";
        } else if (newCompositionName.contains(" нг")) {
            if (newCompositionName.contains("игрушка")) {
                newCompositionName = newCompositionName.replace(" нг", " новогодняя");
            } else {
                newCompositionName = newCompositionName.replace(" нг", " новогодний");
            }
        }
        return newCompositionName;
    }

    private String cleanInput(String input) {
        String s = input.replaceAll("\\(\\d+\\s*[^)]+\\)", "").trim();
        String s2 = s.replaceAll("\\(\\s*[^)]+\\)", "").trim();
        return s2.replaceAll("\\(\\)", "").toLowerCase().trim(); //remove ()
    }

    private String cleanCandidate(String candidate) {
        return candidate.replaceAll("[^\\p{L}\\p{N} ]", " ").toLowerCase().trim();
    }
}

