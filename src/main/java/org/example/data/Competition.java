package org.example.data;
import java.util.HashSet;
import java.util.Set;

public abstract class Competition {
    private int minAge;
    private Sex sex;
    private Set<Athlete> participants;

    public Competition(int minAge, Sex sex) {
        setMinAge(minAge);
        setSex(sex);
        participants = new HashSet<>();
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) throws NegativeValueException{
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

    public Set<Athlete> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Athlete> participants) throws IllegalArgumentException{
        if(participants == null)
            throw new IllegalArgumentException("Participants cannot be null!");
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Competition{" + "minAge=" + minAge + ", sex=" + sex + ", participants=" + participants + '}';
    }
}
