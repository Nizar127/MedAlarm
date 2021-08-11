package com.liyana.medalarm.model;

public class DoctorContact {

    String ImageDoc, nameDoctor, phoneDoctor, emailDoctor, doctorType;

    public DoctorContact(String imageDoc, String nameDoctor, String phoneDoctor, String emailDoctor, String doctorType) {
        this.ImageDoc = imageDoc;
        this.nameDoctor = nameDoctor;
        this.phoneDoctor = phoneDoctor;
        this.emailDoctor = emailDoctor;
        this.doctorType = doctorType;
    }

    public DoctorContact(){

    }


    public String getImageDoc() {
        return ImageDoc;
    }

    public void setImageDoc(String imageDoc) {
        ImageDoc = imageDoc;
    }

    public String getNameDoctor() {
        return nameDoctor;
    }

    public void setNameDoctor(String nameDoctor) {
        this.nameDoctor = nameDoctor;
    }

    public String getPhoneDoctor() {
        return phoneDoctor;
    }

    public void setPhoneDoctor(String phoneDoctor) {
        this.phoneDoctor = phoneDoctor;
    }

    public String getEmailDoctor() {
        return emailDoctor;
    }

    public void setEmailDoctor(String emailDoctor) {
        this.emailDoctor = emailDoctor;
    }

    public String getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }
}
