package org.gobiiproject.gobiimodel.dto.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 9/6/2016.
 */
public class Result<T> {

    private List<T> data = new ArrayList<>();

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
