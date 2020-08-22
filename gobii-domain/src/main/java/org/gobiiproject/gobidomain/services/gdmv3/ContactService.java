/**
 * ContactService.java
 * 
 * Interface for GDM V3 Contact service
 */

package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public interface ContactService {

    PagedResult<ContactDTO> getContacts(Integer page, Integer pageSize,
                                        Integer organizationId) throws Exception;

    static String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(auth -> auth.getName())
            .orElse(null);
    }
}