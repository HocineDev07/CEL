package com.hocinedev.cel;

public class UserObject {
    private String firstLastName;
    private String birthDate;
    private String kaser;
    private String phone;
    private String level;
    private String speciality;
    private String otherSkills;
    private boolean graduate;
    private String univGraduate;
    private String yearGraduate;
    private String email;
    private String password;
    private long signUpDate;


    public UserObject() {
    }

    public UserObject(String firstLastName, String birthDate, String kaser, String phone, String level, String speciality, String otherSkills,boolean graduate,String univGraduate,String yearGraduate, String email, String password, long signUpDate) {
        this.firstLastName = firstLastName;
        this.birthDate = birthDate;
        this.kaser = kaser;
        this.phone = phone;
        this.level = level;
        this.speciality = speciality;
        this.otherSkills = otherSkills;
        this.graduate = graduate;
        this.univGraduate = univGraduate;
        this.yearGraduate = yearGraduate;
        this.email = email;
        this.password = password;
        this.signUpDate = signUpDate;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getKaser() {
        return kaser;
    }

    public void setKaser(String kaser) {
        this.kaser = kaser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getOtherSkills() {
        return otherSkills;
    }

    public void setOtherSkills(String otherSkills) {
        this.otherSkills = otherSkills;
    }

    public boolean isGraduate() {
        return graduate;
    }

    public void setGraduate(boolean graduate) {
        this.graduate = graduate;
    }

    public String getUnivGraduate() {
        return univGraduate;
    }

    public void setUnivGraduate(String univGraduate) {
        this.univGraduate = univGraduate;
    }

    public String getYearGraduate() {
        return yearGraduate;
    }

    public void setYearGraduate(String yearGraduate) {
        this.yearGraduate = yearGraduate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(long signUpDate) {
        this.signUpDate = signUpDate;
    }
}
