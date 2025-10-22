import { Component, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { authCodeFlowConfig } from './auth.config';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NgIf],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('proyecto-oauth2');

  userName?: string;

  constructor(
    private oauthService: OAuthService,
    private router: Router) {
    this.configure();
  }

 private async configure() {
  // Forzar localStorage en la librería ANTES de loadDiscoveryDocumentAndTryLogin
  this.oauthService.setStorage(localStorage);

  // 1) Configurar la librería
  this.oauthService.configure(authCodeFlowConfig);

  await this.oauthService.loadDiscoveryDocumentAndTryLogin();

  console.log('DEBUG storage access_token:', localStorage.getItem('access_token'));
  
  this.oauthService.setupAutomaticSilentRefresh();//para activar el refresco pasado el 75% del tiempo


  // Leer claims si hay token válido
  if (this.oauthService.hasValidAccessToken()) {
    const claims = this.oauthService.getIdentityClaims() as any;
    this.userName = claims?.name || claims?.preferred_username || claims?.sub;
    console.log('🧍 Usuario autenticado:', this.userName);
  }
}

  login() { this.oauthService.initLoginFlow(); }
  logout() { this.oauthService.logOut(); }
  goToTasks(): void { this.router.navigate(['/tasks']); }
}
