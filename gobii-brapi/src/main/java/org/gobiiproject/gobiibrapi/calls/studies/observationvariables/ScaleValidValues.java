package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

/**
 * Created by Phil on 12/18/2016.
 */
public class ScaleValidValues {

    private String min;

    private String max;

    private String[] categories;

    public String getMin ()
    {
        return min;
    }

    public void setMin (String min)
    {
        this.min = min;
    }

    public String getMax ()
    {
        return max;
    }

    public void setMax (String max)
    {
        this.max = max;
    }

    public String[] getCategories ()
    {
        return categories;
    }

    public void setCategories (String[] categories)
    {
        this.categories = categories;
    }

}
