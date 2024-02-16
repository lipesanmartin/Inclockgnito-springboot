package com.inclockgnito.interfaces;

public interface BonusForKilometer {

    Double bonusPerKilometer = 0.1;
    static Double calculateBonus(Double kilometers) {
        return kilometers * bonusPerKilometer;
    }
}
