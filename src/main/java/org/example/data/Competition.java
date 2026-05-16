package org.example.data;

import java.util.ArrayList;
import java.util.List;

public abstract class Competition {
    private int minAge;
    private Sex sex;
    private List<Athlete> participants;

    public Competition(int minAge, Sex sex) {
        setMinAge(minAge);
        setSex(sex);
        participants = new ArrayList<>();
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public List<Athlete> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Athlete> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Competition{" + "minAge=" + minAge + ", sex=" + sex + ", participants=" + participants + '}';
    }
}
