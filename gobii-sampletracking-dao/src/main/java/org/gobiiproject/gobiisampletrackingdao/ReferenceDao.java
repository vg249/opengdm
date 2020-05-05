package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Reference;

public interface ReferenceDao {

    Reference getReference(Integer id);

	List<Reference> getReferences(Integer page, Integer offset);
}