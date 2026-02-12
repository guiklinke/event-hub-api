import axios from "axios";
import type {
  EventFormData,
  Evento,
  Ingresso,
  Participante,
  ParticipantFormData,
} from "../types";

export const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

export async function fetchEvents(): Promise<Evento[]> {
  const res = await api.get("/events?sort=dataEvento,asc");
  return res.data.conteudo ?? res.data.content ?? [];
}

export async function fetchEventById(id: number): Promise<Evento> {
  const res = await api.get(`/events/${id}`);
  return res.data;
}

export async function createEvent(data: EventFormData): Promise<void> {
  const dataFormatada =
    data.data.length === 16 ? `${data.data}:00-03:00` : data.data;
  await api.post("/events", { ...data, dataEvento: dataFormatada });
}

export async function updateEvent(
  id: number,
  data: EventFormData,
): Promise<void> {
  const dataFormatada =
    data.data.length === 16 ? `${data.data}:00-03:00` : data.data;
  await api.put(`/events/${id}`, { ...data, dataEvento: dataFormatada });
}

export async function deleteEvent(id: number): Promise<void> {
  await api.delete(`/events/${id}`);
}

export async function fetchParticipants(): Promise<Participante[]> {
  const res = await api.get("/participants");
  return res.data.conteudo ?? res.data.content ?? res.data ?? [];
}

export async function createParticipant(
  data: ParticipantFormData,
): Promise<Participante> {
  const res = await api.post("/participants", data);
  return res.data;
}

export async function fetchTicketsByParticipant(
  participantId: number,
): Promise<Ingresso[]> {
  const res = await api.get(`/tickets/participants/${participantId}`);
  return res.data.conteudo ?? res.data.content ?? [];
}

export async function buyTicket(
  eventoId: number,
  participanteId: number,
): Promise<void> {
  await api.post("/tickets", { eventoId, participanteId });
}
