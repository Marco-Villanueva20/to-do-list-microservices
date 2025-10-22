// service/todo.service.ts (VERSIÓN FINAL Y ROBUSTA)
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, from, Observable, switchMap, throwError } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';
import { ApiResponse } from '../models/api-response.model';
import { Todo } from '../models/todo.model';

@Injectable({
  providedIn: 'root'
})
export class TodoService {
  private baseUrl = 'http://localhost:8080/to-do-service/api/todo';

  constructor(private http: HttpClient, private oauthService: OAuthService) { }

 getAll(): Observable<ApiResponse<Todo[]>> {
  return this.http.get<ApiResponse<Todo[]>>(this.baseUrl);
}

}