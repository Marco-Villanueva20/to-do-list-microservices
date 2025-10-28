import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

export const authGuard: CanActivateFn = (route, state) => {
  const oauthService = inject(OAuthService);
  const router = inject(Router);

  // 1. Verificar si el usuario tiene un token válido
  if (oauthService.hasValidAccessToken()) {
    return true; // Permitir el acceso a la ruta
  }

  // 2. Si no tiene token, iniciar el flujo de login y redirigir
  // Se recomienda iniciar el login y que el Servidor de Autorización se encargue de la redirección.
  oauthService.initLoginFlow(state.url); // Guarda la URL a la que querían ir
  
  // Puedes retornar false y dejar que initLoginFlow() haga la redirección,
  // o redirigir tú mismo si necesitas controlar la navegación inmediatamente.
  // En este caso, dejamos que initLoginFlow lo maneje.
  return false;
};
