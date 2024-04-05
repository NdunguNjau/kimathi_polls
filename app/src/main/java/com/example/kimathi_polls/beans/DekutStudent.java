package com.example.kimathi_polls.beans;

public class DekutStudent {
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String registrationNumber;
    private String course;
    private String dob;
    private String admissionYear;
    private int currentYear;
    private int currentSemester;
    private String certificate;
    private String profilePhoto;

    // Add a default constructor
    public DekutStudent() {
    }

    // Add a constructor that initializes all fields
    public DekutStudent(String email, String firstName, String middleName, String lastName, String registrationNumber, String course, String dob, String admissionYear, int currentYear, int currentSemester, String certificate, String profilePhoto) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.registrationNumber = registrationNumber;
        this.course = course;
        this.dob = dob;
        this.admissionYear = admissionYear;
        this.currentYear = currentYear;
        this.currentSemester = currentSemester;
        this.certificate = certificate;
        this.profilePhoto = profilePhoto;
    }

    // Add getters and setters for each field
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(String admissionYear) {
        this.admissionYear = admissionYear;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }


}