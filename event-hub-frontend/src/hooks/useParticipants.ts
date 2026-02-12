import { useEffect, useMemo, useState } from "react";
import type { Ingresso, Participante, ParticipantFormData } from "../types";
import {
  createParticipant,
  fetchParticipants,
  fetchTicketsByParticipant,
} from "../api";

const EMPTY_FORM: ParticipantFormData = { nome: "", email: "" };

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

export function useParticipants(refreshKey: number) {
  const [participants, setParticipants] = useState<Participante[]>([]);
  const [selectedParticipantId, setSelectedParticipantId] = useState<
    number | null
  >(null);
  const [participantTickets, setParticipantTickets] = useState<Ingresso[]>([]);
  const [participantForm, setParticipantForm] =
    useState<ParticipantFormData>(EMPTY_FORM);

  const reloadParticipants = async () => {
    try {
      const data = await fetchParticipants();
      setParticipants(data);
    } catch {
      console.warn("API de participantes indisponível");
    }
  };

  const reloadTickets = async (id: number) => {
    try {
      const data = await fetchTicketsByParticipant(id);
      setParticipantTickets(data);
    } catch {
      setParticipantTickets([]);
    }
  };

  useEffect(() => {
    let ignore = false;

    const run = async () => {
      try {
        const data = await fetchParticipants();
        if (!ignore) setParticipants(data);
      } catch {
        if (!ignore) console.warn("API de participantes indisponível");
      }
    };

    void run();

    return () => {
      ignore = true;
    };
  }, [refreshKey]);

  useEffect(() => {
    let ignore = false;

    const run = async () => {
      if (!selectedParticipantId) {
        if (!ignore) setParticipantTickets([]);
        return;
      }

      try {
        const data = await fetchTicketsByParticipant(selectedParticipantId);
        if (!ignore) setParticipantTickets(data);
      } catch {
        if (!ignore) setParticipantTickets([]);
      }
    };

    void run();

    return () => {
      ignore = true;
    };
  }, [selectedParticipantId, refreshKey]);

  const handleCreateParticipant = async (e: React.SyntheticEvent) => {
    e.preventDefault();
    try {
      const created = await createParticipant(participantForm);
      alert(`Participante ${created.nome} cadastrado!`);
      setSelectedParticipantId(created.id);
      setParticipantForm(EMPTY_FORM);
      await reloadParticipants();
    } catch (error: unknown) {
      alert("Erro ao cadastrar: " + getErrorMessage(error));
    }
  };

  const activeParticipant = useMemo(
    () => participants.find((p) => p.id === selectedParticipantId) ?? null,
    [participants, selectedParticipantId],
  );

  return {
    participants,
    selectedParticipantId,
    setSelectedParticipantId,
    participantTickets,
    participantForm,
    setParticipantForm,
    handleCreateParticipant,
    activeParticipant,
    refreshParticipants: reloadParticipants,
    refreshTickets: reloadTickets,
  };
}
