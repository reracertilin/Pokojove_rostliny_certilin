package cz.engeto.rostliny;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlantList {

    private List<PlantAttributes> plants = new ArrayList<>();

    // region úprva listu
    public void addPlant(PlantAttributes plant) {
        plants.add(plant);
    }

    public PlantAttributes getPlant(int index) {
        return plants.get(index);
    }

    public void removePlant(int index) {
        plants.remove(index);
    }
    //endregion

    // Získání kopie seznamu rostlin
    public List<PlantAttributes> getPlantListCopy() {
        return new ArrayList<>(plants);
    }
    // endregion

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
}
