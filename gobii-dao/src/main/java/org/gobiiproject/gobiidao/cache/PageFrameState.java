package org.gobiiproject.gobiidao.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PageFrameState {

    public Date created = new Date();
    public List<PageState> pages = new ArrayList<>();

    public Date getCreated() {
        return created;
    }

    public List<PageState> getPages() {
        return pages;
    }
}
