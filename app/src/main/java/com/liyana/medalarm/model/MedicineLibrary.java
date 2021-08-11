package com.liyana.medalarm.model;

public class MedicineLibrary {

    String name, description, dose, medicineID;


    public MedicineLibrary(String name, String description, String dose, String medicineID) {
        this.name = name;
        this.description = description;
        this.dose = dose;
        this.medicineID = medicineID;
    }

    public MedicineLibrary(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }
}
