import {KeycloakService} from 'keycloak-angular';
import {environmentSettings} from '../environments/environment';

export function initializer(keycloak: KeycloakService): () => Promise<any> {
    return (): Promise<any> => {
        return new Promise(async (resolve, reject) => {
            try {
                await keycloak.init({
                    config: {
                        url: environmentSettings.keycloak.issuer,
                        realm: environmentSettings.keycloak.realm,
                        clientId: environmentSettings.keycloak.clientId
                    },
                    loadUserProfileAtStartUp: false,
                    initOptions: {
                        onLoad: 'login-required',
                        checkLoginIframe: true
                    },
                    bearerExcludedUrls: []
                });
                resolve();
            } catch (error) {
                reject(error);
            }
        });
    };
}