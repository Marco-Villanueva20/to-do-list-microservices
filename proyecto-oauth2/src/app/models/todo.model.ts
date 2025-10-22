import { User } from "./user.model";

export interface Todo {
  id?: number;                // opcional porque el backend lo genera
  title: string;
  description: string;
  completed: boolean;
  createdAt?: string;          // en JSON, LocalDateTime llega como string
  updatedAt?: string;
  user?: User;                 // referencia al usuario dueño del todo
}
