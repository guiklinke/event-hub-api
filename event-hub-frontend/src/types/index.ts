export interface Evento {
  id: number;
  nome: string;
  dataEvento: string;
  local: string;
  capacidade: number;
}

export interface Participante {
  id: number;
  nome: string;
  email: string;
}

export interface Ingresso {
  id: number;
  dataCompra: string;
  nomeEvento: string;
}

export interface EventFormData {
  nome: string;
  data: string;
  local: string;
  capacidade: number;
}

export interface ParticipantFormData {
  nome: string;
  email: string;
}
