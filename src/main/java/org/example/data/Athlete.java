package org.example.data;

import java.util.Date;
import java.util.UUID;

public class Athlete {
    private final UUID id;
    private String name;
    private Country country;
    private Sex sex;
    private Date birthDate;

    public Athlete(String name, Country country, Sex sex, Date birthDate) {
        this.id = UUID.randomUUID();
        setName(name);
        setCountry(country);
        setSex(sex);
        setBirthDate(birthDate);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Athlete{" + "id=" + id + ", name='" + name + '\'' + ", country=" + country + ", sex=" + sex + ", birthDate=" + birthDate + '}';
    }
}
