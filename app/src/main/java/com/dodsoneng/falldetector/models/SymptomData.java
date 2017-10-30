package com.dodsoneng.falldetector.models;

/**
 * Created by sergio.eng on 10/18/17.
 */

public class SymptomData {
    String name;
    Boolean hasEntry;
    String unit;

    public String getName () {
        return name;
    }

    public void setName (String _name) {
        name = _name;
    }

    public Boolean getHasEntry () {
        return hasEntry;
    }

    public void setHasEntry (Boolean _hasEntry) {
        hasEntry = _hasEntry;
    }


    public String getUnit () {
        return unit;
    }

    public void setUnit (String _unit) {
        unit = _unit;
    }

}
