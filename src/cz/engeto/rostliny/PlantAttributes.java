package cz.engeto.rostliny;
import java.time.LocalDate;

public class PlantAttributes {

    private String name;
    private String notes;
    private LocalDate planted;
    private LocalDate lastWatering;
    private int frequencyOfWatering; //ve dnech

    //region constructor
    public PlantAttributes(String name, String notes, LocalDate planted, LocalDate lastWatering, int frequencyOfWatering) {
        this.name = name;
        this.notes = notes == null ? "Bez poznámky" : notes;
        this.planted = planted;
        this.lastWatering = lastWatering;
        this.frequencyOfWatering = frequencyOfWatering;
    }
    public PlantAttributes(String name, int frequencyOfWatering) {
        this (name, "Bez poznámky", LocalDate.now(), LocalDate.now(), frequencyOfWatering);
    }
    public PlantAttributes(String name) {
        this (name, "Bez poznámky", LocalDate.now(), LocalDate.now(), 7);
    }
    //endregion

    //region Gettery a settery
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getPlanted() {
        return planted;
    }

    public void setPlanted(LocalDate planted) {
        this.planted = planted;
    }

    public int getFrequencyOfWatering() {
        return frequencyOfWatering;
    }

    public void setFrequencyOfWatering(int frequencyOfWatering) {
        this.frequencyOfWatering = frequencyOfWatering;
    }

    public LocalDate getLastWatering() {
        return lastWatering;
    }

    public void setLastWatering(LocalDate lastWatering) {
        this.lastWatering = lastWatering;
    }
    //endregion

    //region Metody
    public String getWateringInfo() {
        return
                "jméno rostliny: " + name + "\n" +
                "poznámka: " + notes + "\n" +
                "zasazená: " + planted + "\n" +
                "naposled zalitá: " + lastWatering + "\n" +
                "frekvence zalévání: " + frequencyOfWatering ;
    }

    public void doWateringNow() {
        this.lastWatering = LocalDate.now();
    }

    @Override
    public String toString() {
        return getWateringInfo();
    }
    //endregion
}
