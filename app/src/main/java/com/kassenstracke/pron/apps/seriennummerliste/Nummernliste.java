package com.kassenstracke.pron.apps.seriennummerliste;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Pron on 01.02.2016.
 */
public class Nummernliste {
    public List<String> Liste;

    public void Nummernliste() {
        Liste = new ArrayList<String>();
        }

    public void AddElement(String element) {
        if(!element.isEmpty()) {
            Liste.add(element);
        }
    }
}
