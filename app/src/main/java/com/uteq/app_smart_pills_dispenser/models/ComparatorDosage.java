package com.uteq.app_smart_pills_dispenser.models;

import java.util.Comparator;

public class ComparatorDosage implements Comparator<Dosage>{

    @Override
    public int compare(Dosage dosage, Dosage t1) {
        return dosage.getDateTake().compareTo(t1.getDateTake());
    }
}
