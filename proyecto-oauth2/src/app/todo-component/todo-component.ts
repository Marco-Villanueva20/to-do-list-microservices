import { Component } from '@angular/core';
import { TodoService } from '../service/todo-service';
import { Todo } from '../models/todo.model';
import { DatePipe, NgFor, NgIf, NgClass } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-todo-component',
  imports: [NgFor, NgIf, NgClass, DatePipe],
  templateUrl: './todo-component.html',
  styleUrl: './todo-component.css'
})
export class TodoComponent {
 todos: Todo[] = [];
  mensaje: string = '';
  constructor(private todoService: TodoService, private router: Router) { }

  ngOnInit(): void {
    this.todoService.getAll().subscribe({
      next: (resp) => {
        this.todos = resp.data ?? []; // si data es null, usa []
        this.mensaje = resp.message;
      },
      error: (err) => {
        console.error('Error al cargar todos', err);
      }
    });

  }

  goToIndex(): void {
    this.router.navigate(['']);
  }
}
