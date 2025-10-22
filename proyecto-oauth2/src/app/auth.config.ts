import { AuthConfig } from "angular-oauth2-oidc";
export const authCodeFlowConfig: AuthConfig = {
    issuer: 'http://localhost:9000',
    redirectUri: window.location.origin + '/',
    clientId: 'angular-app',
    responseType: 'code',
    scope: 'openid profile read write',
    showDebugInformation: true,
    requireHttps: false,
    useSilentRefresh: true, // necesario 
    
};
