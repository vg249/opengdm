package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.LoaderTemplate;

public interface LoaderTemplateDao {

    LoaderTemplate create(LoaderTemplate loaderTemplate) throws GobiiDaoException;
    LoaderTemplate getById(Integer templateId);
}
