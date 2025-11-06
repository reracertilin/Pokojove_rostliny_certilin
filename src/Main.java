
import cz.engeto.rostliny.PlantAttributes;
import cz.engeto.rostliny.PlantList;
import cz.engeto.rostliny.PlantException;

import java.io.IOException;

void main(String[] args) throws IOException {
        // soubory pro testování
        final String RESOURCE_FOLDER = "resources/";
        final String INPUT_FILE = RESOURCE_FOLDER + "kvetiny.txt";
        final String OUTPUT_FILE = RESOURCE_FOLDER + "kvetiny-zmeny.txt";

        // vadné soubory
        PlantList.loadFromFile(RESOURCE_FOLDER + "kvetiny-spatne-datum.txt");
        PlantList.loadFromFile(RESOURCE_FOLDER + "kvetiny-spatne-frekvence.txt");
        PlantList plantList = new PlantList();

        try {
            // načtení seznamu květin ze souboru
            System.out.println("=============================================");
            System.out.println("NAČÍTÁNÍ ZE SOUBORU (" + INPUT_FILE + ")");
            System.out.println("=============================================");

            // výpis počtu načtených rostlin
            plantList = PlantList.loadFromFile(INPUT_FILE);
            System.out.println("Načteno rostlin: " + plantList.size());

            // výpis informací o zalévání
            System.out.println("\n=============================================");
            System.out.println("VÝPIS INFORMACÍ O ZÁLÉVÁNÍ");
            System.out.println("=============================================");
            for (PlantAttributes plant : plantList.getPlantListCopy()) {
                System.out.println(plant.getWateringInfo());
                System.out.println("---");
            }

            // přidání nové květiny do seznamu
            PlantAttributes orchidej = new PlantAttributes("Orchidej Phalaenopsis", 10);
            orchidej.setNotes("Zalévat jen po úplném vyschnutí.");
            plantList.addPlant(orchidej);
            System.out.println("\nPřidána nová rostlina: " + orchidej.getName());

            // přidání 10 tulipánů
            for (int i = 1; i <= 10; i++) {
                PlantAttributes tulipan = new PlantAttributes("Tulipán na prodej " + i);
                tulipan.setFrequencyOfWatering(14); // Nastav frekvenci na 14 dnů
                plantList.addPlant(tulipan);
            }
            System.out.println("Přidáno 10 tulipánů na prodej.");
            System.out.println("Celkový počet rostlin v seznamu: " + plantList.size());

            // odebrání 3 květiny v seznamu
            if (plantList.size() > 2) {
                String removedName = plantList.getPlant(2).getName();
                plantList.removePlant(2);
                System.out.println("Odebrána rostlina na indexu 2: " + removedName);
            }

            // ulož seznam květin do nového souboru
            plantList.saveToFile(OUTPUT_FILE);
            System.out.println("\n Seznam uložen do souboru: " + OUTPUT_FILE);

            // opětovné načtení vygenerovaného souboru
            PlantList newLoadedList = PlantList.loadFromFile(OUTPUT_FILE);
            System.out.println("\n Znovu načteno ze souboru " + OUTPUT_FILE + ". Načteno " + newLoadedList.size() + " rostlin.");

            // seřazení rostlin a výpis
            System.out.println("\n=============================================");
            System.out.println("SEŘAZENÍ DLE RŮZNÝCH KRITÉRIÍ");
            System.out.println("=============================================");

            // Řazení podle názvu
            newLoadedList.sortByName();
            System.out.println("--- Seřazeno podle názvu ---");
            newLoadedList.getPlantListCopy().forEach(plant ->
                    System.out.println("- " + plant.getName()));

            // Řazení podle poslední zálivky
            newLoadedList.sortByLastWatering();
            System.out.println("\n--- Seřazeno podle data poslední zálivky ---");
            newLoadedList.getPlantListCopy().forEach(plant ->
                    System.out.println("- " + plant.getName() + " (Zalito: " + plant.getLastWatering() + ")"));

        } catch (PlantException | IOException e) {
            System.err.println("\n FATÁLNÍ CHYBA V APLIKACI: " + e.getMessage());
        }
}
