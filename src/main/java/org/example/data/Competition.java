package org.example.data;
import org.example.data.enums.Sex;
import org.example.data.exceptions.NegativeValueException;

public abstract class Competition {
    private int minAge;
    private Sex sex;

    public Competition(int minAge, Sex sex) {
        setMinAge(minAge);
        setSex(sex);
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) throws NegativeValueException {
        if(minAge < 0)
            throw new NegativeValueException("minAge value cannot be negative!");
        this.minAge = minAge;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) throws IllegalArgumentException{
        if(sex == null)
            throw new IllegalArgumentException("Sex cannot be null!");
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Competition{" + "minAge=" + minAge + ", sex=" + sex + '}';
    }
}
