package org.gobiiproject.gobiiapimodel.payload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 9/6/2016.
 */
public class Payload<T> {

    private List<T> data = new ArrayList<>();

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
