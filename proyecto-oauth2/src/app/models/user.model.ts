import { Role } from "./role.model";

export interface User {
  id?: number;
  name: string;
  email: string;
  password?: string; // opcional, no debería venir en respuestas
  enabled?: number;
  roles?: Role[];
}