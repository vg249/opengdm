package org.gobiiproject.gobiiclient;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.dto.system.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.types.ServerCapabilityType;

public class Helper {
    public static boolean isBackEndSupported() {
        boolean backendSupoorted = false;
        try {
            RestUri confgSettingsUri = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(GobiiServiceRequestId.URL_CONFIGSETTINGS);
            GobiiEnvelopeRestResource<ConfigSettingsDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(confgSettingsUri);
            PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = gobiiEnvelopeRestResource
                    .get(ConfigSettingsDTO.class);

            if (resultEnvelope.getHeader().getStatus().isSucceeded()) {
                ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);
                backendSupoorted = configSettingsDTOResponse.getServerCapabilities().containsKey(ServerCapabilityType.GOBII_BACKEND)
                        && configSettingsDTOResponse.getServerCapabilities().containsKey(ServerCapabilityType.GOBII_BACKEND);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return backendSupoorted;
    }
}
