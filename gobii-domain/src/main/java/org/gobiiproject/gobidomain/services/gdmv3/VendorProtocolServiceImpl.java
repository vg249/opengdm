package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ßVendorProtocolDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.VendorProtocol;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.VendorProtocolDao;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VendorProtocolServiceImpl implements VendorProtocolService {

    @Autowired
    private VendorProtocolDao vendorProtocolDao;
    
    @Override
    public PagedResult<VendorProtocolDTO>
    getVendorProtocols(Integer page, Integer pageSize) throws Exception {

        PagedResult<VendorProtocolDTO> pagedResult;
        
        try {

            Objects.requireNonNull(page);
            Objects.requireNonNull(pageSize);
            List<VendorProtocolDTO> vendorProtocolDTOS  = new ArrayList<>();

            List<VendorProtocol> vendorProtocols =
                    vendorProtocolDao.getVendorProtocols(page, pageSize);

            vendorProtocols.forEach(vendorProtocol -> {
                VendorProtocolDTO vendorProtocolDTO = new VendorProtocolDTO();
                ModelMapper.mapEntityToDto(vendorProtocol, vendorProtocolDTO);
                vendorProtocolDTOS.add(vendorProtocolDTO);
            });

            pagedResult = new PagedResult<>();
            pagedResult.setResult(vendorProtocolDTOS);
            pagedResult.setCurrentPageNum(page);
            pagedResult.setCurrentPageSize(vendorProtocolDTOS.size());
            return pagedResult;
        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            log.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

}