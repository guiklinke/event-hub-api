import { EventForm } from "./EventForm";
import { EventCard } from "./EventCard";
import type { EventFormData, Evento } from "../../types";

interface EventsViewProps {
  events: Evento[];
  editingEventId: number | null;
  eventForm: EventFormData;
  selectedParticipantId: number | null;
  onFormChange: (form: EventFormData) => void;
  onSubmit: (e: React.SyntheticEvent) => void;
  onCancelEdit: () => void;
  onBuyTicket: (eventId: number) => void;
  onEdit: (eventId: number) => void;
  onDelete: (eventId: number) => void;
}

export function EventsView({
  events,
  editingEventId,
  eventForm,
  selectedParticipantId,
  onFormChange,
  onSubmit,
  onCancelEdit,
  onBuyTicket,
  onEdit,
  onDelete,
}: EventsViewProps) {
  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <div className="lg:col-span-1">
        <EventForm
          form={eventForm}
          editingEventId={editingEventId}
          onChange={onFormChange}
          onSubmit={onSubmit}
          onCancel={onCancelEdit}
        />
      </div>

      <div className="lg:col-span-2 space-y-4">
        {events.map((event) => (
          <EventCard
            key={event.id}
            event={event}
            selectedParticipantId={selectedParticipantId}
            onBuyTicket={onBuyTicket}
            onEdit={onEdit}
            onDelete={onDelete}
          />
        ))}

        {events.length === 0 && (
          <div className="text-center py-12 text-slate-400 bg-white rounded-xl border border-dashed">
            Nenhum evento criado ainda.
          </div>
        )}
      </div>
    </div>
  );
}
