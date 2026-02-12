import { useState, useEffect } from "react";
import type { EventFormData, Evento } from "../types";
import {
  buyTicket,
  createEvent,
  deleteEvent,
  fetchEventById,
  fetchEvents,
  updateEvent,
} from "../api";

const EMPTY_FORM: EventFormData = {
  nome: "",
  data: "",
  local: "",
  capacidade: 0,
};

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === "object" && value !== null;
}

function getErrorMessage(error: unknown): string {
  if (isRecord(error)) {
    const response = error["response"];
    if (isRecord(response)) {
      const data = response["data"];
      if (isRecord(data)) {
        const message = data["message"];
        if (typeof message === "string") return message;
      }
    }

    const message = error["message"];
    if (typeof message === "string") return message;
  }

  if (error instanceof Error) return error.message;
  return "Erro desconhecido";
}

export function useEvents(refreshKey: number) {
  const [events, setEvents] = useState<Evento[]>([]);
  const [editingEventId, setEditingEventId] = useState<number | null>(null);
  const [eventForm, setEventForm] = useState<EventFormData>(EMPTY_FORM);

  const reloadEvents = async () => {
    try {
      const data = await fetchEvents();
      setEvents(data);
    } catch (error) {
      console.error("Erro ao carregar eventos", error);
    }
  };

  useEffect(() => {
    let ignore = false;

    const run = async () => {
      try {
        const data = await fetchEvents();
        if (!ignore) setEvents(data);
      } catch (error) {
        if (!ignore) console.error("Erro ao carregar eventos", error);
      }
    };

    void run();

    return () => {
      ignore = true;
    };
  }, [refreshKey]);

  const handleSaveEvent = async (e: React.SyntheticEvent) => {
    e.preventDefault();
    try {
      if (editingEventId) {
        await updateEvent(editingEventId, eventForm);
        alert("Evento atualizado!");
      } else {
        await createEvent(eventForm);
        alert("Evento criado!");
      }

      setEventForm(EMPTY_FORM);
      setEditingEventId(null);
      await reloadEvents();
    } catch (error: unknown) {
      alert("Erro: " + getErrorMessage(error));
    }
  };

  const handleEditClick = async (id: number) => {
    try {
      const evento = await fetchEventById(id);
      setEventForm({
        nome: evento.nome,
        data: evento.dataEvento ? evento.dataEvento.substring(0, 16) : "",
        local: evento.local,
        capacidade: evento.capacidade,
      });
      setEditingEventId(id);
    } catch {
      alert("Erro ao buscar evento.");
    }
  };

  const handleDeleteEvent = async (id: number) => {
    if (!confirm("Excluir evento?")) return;
    try {
      await deleteEvent(id);
      await reloadEvents();
    } catch {
      alert("Erro ao deletar.");
    }
  };

  const handleBuyTicket = async (
    eventoId: number,
    participanteId: number | null,
  ) => {
    if (!participanteId) {
      alert("Selecione um participante no menu superior primeiro.");
      return;
    }
    try {
      await buyTicket(eventoId, participanteId);
      alert("Ingresso comprado com sucesso!");
    } catch (error: unknown) {
      alert("Falha: " + getErrorMessage(error));
    }
  };

  const cancelEditing = () => {
    setEditingEventId(null);
    setEventForm(EMPTY_FORM);
  };

  return {
    events,
    editingEventId,
    eventForm,
    setEventForm,
    handleSaveEvent,
    handleEditClick,
    handleDeleteEvent,
    handleBuyTicket,
    cancelEditing,
    reloadEvents,
  };
}
