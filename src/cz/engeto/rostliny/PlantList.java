package cz.engeto.rostliny;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class PlantList {

    private List<Plant> plants = new ArrayList<>();

    // region úprva listu
    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public Plant getPlant(int index) {
        if (index < 0 || index >= plants.size()) {
            throw new IndexOutOfBoundsException("Index je mimo rozsah seznamu");
        }
        return plants.get(index);
    }

    public void removePlant(int index) {
        if (index < 0 || index >= plants.size()) {
            throw new IndexOutOfBoundsException("Index je mimo rozsah seznamu");
        }
        plants.remove(index);
    }
    //endregion

    // Získání kopie seznamu rostlin
    public List<Plant> getPlantListCopy() {
        return new ArrayList<>(plants);
    }

    // Získání velikosti seznamu
    public int size() {
        return plants.size();
    }

    // region Rostliny k zalévání
    public List<Plant> getPlantsToWater() {
        List<Plant> plantsToWater = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Plant plant : plants) {
            if (!plant.getNextWateringDate().isAfter(today)) {
                plantsToWater.add(plant);
            }
        }
        return plantsToWater;
    }
    // endregion

    // region Řazení
    public void sortByName() {
        plants.sort(Comparator.comparing(Plant::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public void sortByLastWatering() {
        plants.sort(Comparator.comparing(Plant::getLastWatering));
    }
    // endregion

    // region Načítání a Ukládaní
    public static final String DELIMITER = "\t"; //nastavení oddělovače záznamů
    /**uložení do souboru */
    public void saveToFile(String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Plant plant : plants) {
                // název \t poznámky \t frekvence \t posledníZálivka \t zasazeno
                writer.write(plant.getName() + DELIMITER +
                        plant.getNotes() + DELIMITER +
                        plant.getFrequencyOfWatering() + DELIMITER +
                        plant.getLastWatering().toString()+ DELIMITER +
                        plant.getPlanted().toString() + "\n");
            }
        }
    }

    /** načítání ze souboru */
    public static PlantList loadFromFile(String fileName) throws IOException, PlantException {
        PlantList loadedList = new PlantList();
        int lineNumber = 0;

        StringBuilder errorMessages = new StringBuilder();


        try (Scanner scanner = new Scanner(Files.newBufferedReader(Path.of(fileName)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNumber++;
                String[] parts = line.split(DELIMITER);


                try {
                if (parts.length != 5) {
                    throw new PlantException("Chyba na řádku " + lineNumber + ": Nesprávný počet dat (očekáváno 5, nalezeno " + parts.length + ")");
                }

                    // Parsování dat
                    String name = parts[0];
                    String notes = parts[1];
                    int frequency = Integer.parseInt(parts[2]);
                    LocalDate lastWatering = LocalDate.parse(parts[3]);
                    LocalDate planted = LocalDate.parse(parts[4]);

                    Plant plant = new Plant(name, notes, planted, lastWatering, frequency);
                    loadedList.addPlant(plant);

                } catch (DateTimeParseException e) {
                    errorMessages.append("Chyba na řádku ")
                            .append(lineNumber)
                            .append(": Neplatný formát data (")
                            .append(parts.length > 3 ? parts[3] : "?")
                            .append(" nebo ")
                            .append(parts.length > 4 ? parts[4] : "?")
                            .append(").\n");
                } catch (NumberFormatException e) {
                    errorMessages.append("Chyba na řádku ")
                            .append(lineNumber)
                            .append(": Frekvence zalévání není celé číslo.\n");
                } catch (PlantException e) {
                    errorMessages.append("Chyba na řádku ")
                            .append(lineNumber)
                            .append(": Chybná data pro rostlinu (")
                            .append(e.getMessage())
                            .append(").\n");
                }
            }
        } catch (FileNotFoundException e) {
            throw new PlantException("Soubor " + fileName + " nebyl nalezen. Načten prázdný seznam.");
        }

        if (!errorMessages.isEmpty()) {
            throw new PlantException(errorMessages.toString());
        }

        return loadedList;
    }
    // endregion

}
