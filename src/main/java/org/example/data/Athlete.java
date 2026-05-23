package org.example.data;

import org.example.data.enums.Country;
import org.example.data.enums.Sex;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Athlete {
    private final UUID id;
    private String name;
    private Country country;
    private Sex sex;
    private LocalDate birthDate;

    public Athlete(String name, Country country, Sex sex, LocalDate birthDate) {
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

    public void setName(String name) throws IllegalArgumentException{
        if(name == null)
            throw new IllegalArgumentException("Name cannot be null!");
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) throws IllegalArgumentException{
        if(country == null)
            throw new IllegalArgumentException("Country cannot be null!");
        this.country = country;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) throws IllegalArgumentException{
        if(sex == null)
            throw new IllegalArgumentException("Sex cannot be null!");
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) throws IllegalArgumentException{
        if(birthDate == null)
            throw new IllegalArgumentException("BirthDate cannot be null!");
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Athlete athlete = (Athlete) o;
        return Objects.equals(id, athlete.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Athlete{" + "id=" + id + ", name='" + name + '\'' + ", country=" + country + ", sex=" + sex + ", birthDate=" + birthDate + '}';
    }
}
