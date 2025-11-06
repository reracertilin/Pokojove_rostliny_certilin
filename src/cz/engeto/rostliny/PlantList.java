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

    private List<PlantAttributes> plants = new ArrayList<>();

    // region úprva listu
    public void addPlant(PlantAttributes plant) {
        plants.add(plant);
    }

    public PlantAttributes getPlant(int index) {
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
    public List<PlantAttributes> getPlantListCopy() {
        return new ArrayList<>(plants);
    }

    // Získání velikosti seznamu
    public int size() {
        return plants.size();
    }

    // region Rostliny k zalévání
    public List<PlantAttributes> getPlantsToWater() {
        List<PlantAttributes> plantsToWater = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (PlantAttributes plant : plants) {
            LocalDate lastWatering = plant.getLastWatering();
            int frequency = plant.getFrequencyOfWatering();
            LocalDate nextWateringDate = lastWatering.plusDays(frequency);
            if (nextWateringDate.isBefore(today) || nextWateringDate.isEqual(today)) {
                plantsToWater.add(plant);
            }
        }
        return plantsToWater;
    }
    // endregion

    // region Řazení
    public void sortByName() {
        plants.sort(Comparator.comparing(PlantAttributes::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public void sortByLastWatering() {
        plants.sort(Comparator.comparing(PlantAttributes::getLastWatering));
    }
    // endregion

    // region Načítání a Ukládaní

    /**uložení do souboru */
    public void saveToFile(String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (PlantAttributes plant : plants) {
                // název \t poznámky \t zasazeno \t posledníZálivka \t frekvence
                writer.write(plant.getName() + "\t" +
                        plant.getNotes() + "\t" +
                        plant.getFrequencyOfWatering() + "\t" +
                        plant.getPlanted().toString() + "\t" +
                        plant.getLastWatering().toString() + "\n");
            }
        }
    }

    /** načítání ze souboru */
    public static PlantList loadFromFile(String fileName) throws IOException, PlantException {
        PlantList loadedList = new PlantList();
        int lineNumber = 0;

        try (Scanner scanner = new Scanner(Files.newBufferedReader(Path.of(fileName)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNumber++;
                String[] parts = line.split("\t");

                if (parts.length != 5) {
                    System.err.println("Chyba na řádku " + lineNumber + " (Vadná data): Nesprávný počet dat (očekáváno 5, nalezeno " + parts.length + "). Řádek ignorován.");
                    continue;
                }

                try {
                    // Parsování dat
                    String name = parts[0];
                    String notes = parts[1];
                    int frequency = Integer.parseInt(parts[2]);
                    LocalDate lastWatering = LocalDate.parse(parts[3]);
                    LocalDate planted = LocalDate.parse(parts[4]);

                    PlantAttributes plant = new PlantAttributes(name, notes, planted, lastWatering, frequency);
                    loadedList.addPlant(plant);

                } catch (DateTimeParseException e) {
                    System.err.println("Chyba na řádku " + lineNumber + " (Chybné datum): Neplatný formát data (" + parts[2] + " nebo " + parts[3] + "). Řádek ignorován.");
                } catch (NumberFormatException e) {
                    System.err.println("Chyba na řádku " + lineNumber + " (Chybná frekvence): Frekvence zalévání není celé číslo. Řádek ignorován.");
                } catch (PlantException e) {
                    System.err.println("Chyba na řádku " + lineNumber + " (Chybná data): Chybná data pro rostlinu (" + e.getMessage() + "). Řádek ignorován.");
                }
            }
        } catch (FileNotFoundException e) {
            throw new PlantException("Soubor " + fileName + " nebyl nalezen. Načten prázdný seznam.");
        }
        return loadedList;
    }

    // endregion

}
