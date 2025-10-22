import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
    const oauthService = inject(OAuthService);
    
    // 🛑 ELIMINAMOS: oauthService.refreshToken(); 
    // La renovación se maneja de forma ASÍNCRONA en el TodoService.
    
    const accessToken = oauthService.getAccessToken();
    const issuer = oauthService.issuer;

    // 1. Si no hay token, o si es una URL del Servidor de Autorización (Issuer), NO adjuntar.
    if (!accessToken || (issuer && req.url.startsWith(issuer))) {
        return next(req);
    }

    // 2. Para peticiones protegidas (To-Do Service):
    console.log(`[Token Interceptor] Añadiendo Access Token para: ${req.url}`);

    // Corrección de TypeScript para el log
    const logToken = (accessToken?.length > 20 ? accessToken.substring(0, 20) : accessToken) ?? '[NO ACCESS TOKEN]';
    console.log(`[Token Interceptor] Token (primeros 20 chars): ${logToken}`);

    const clonedRequest = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + accessToken)
    });

    return next(clonedRequest);
};