import { Routes } from '@angular/router';

import { authGuard } from './service/auth-guard'; // Importa tu guard
import { App } from './app';
import { TodoComponent } from './todo-component/todo-component';

export const routes: Routes = [

    { path: '', component: App, title: 'Inicio' },

    // Ruta PRIVADA protegida por el Guard
    {
        path: 'tasks',
        component: TodoComponent, // Reemplaza con tu componente de tareas
        title: 'Mis Tareas',
        canActivate: [authGuard] // <--- ¡Esto es lo crucial!
    },

];
