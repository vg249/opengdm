package org.gobiiproject.gobidomain.services;

import java.util.List;

public interface V3ProjectService<T> {
    List<T> getProjects(Integer pageNum, Integer pageSize);
}