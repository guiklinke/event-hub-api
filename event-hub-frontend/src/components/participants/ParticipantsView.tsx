import type { Ingresso, Participante, ParticipantFormData } from "../../types";
import { ParticipantForm } from "./ParticipantForm";
import { ParticipantList } from "./ParticipantList";
import { TicketHistory } from "./TicketHistory";

interface ParticipantsViewProps {
  participants: Participante[];
  selectedParticipantId: number | null;
  participantTickets: Ingresso[];
  activeParticipant: Participante | null;
  participantForm: ParticipantFormData;
  onFormChange: (form: ParticipantFormData) => void;
  onCreateParticipant: (e: React.SyntheticEvent) => void;
  onSelectParticipant: (id: number) => void;
  onRefreshParticipants: () => void;
}

export function ParticipantsView({
  participants,
  selectedParticipantId,
  participantTickets,
  activeParticipant,
  participantForm,
  onFormChange,
  onCreateParticipant,
  onSelectParticipant,
  onRefreshParticipants,
}: ParticipantsViewProps) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
      <div className="space-y-6">
        <ParticipantForm
          form={participantForm}
          onChange={onFormChange}
          onSubmit={onCreateParticipant}
        />
        <ParticipantList
          participants={participants}
          selectedParticipantId={selectedParticipantId}
          onSelectParticipant={onSelectParticipant}
          onRefresh={onRefreshParticipants}
        />
      </div>

      <TicketHistory
        activeParticipant={activeParticipant}
        tickets={participantTickets}
      />
    </div>
  );
}
