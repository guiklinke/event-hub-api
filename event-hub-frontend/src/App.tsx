import { useState } from "react";
import { useParticipants } from "./hooks/useParticipants";
import { useEvents } from "./hooks/useEvents";
import { Header } from "./components/layout/Header";
import { TabNavigation } from "./components/layout/TabNavigation";
import { EventsView } from "./components/events/EventsView";
import { ParticipantsView } from "./components/participants/ParticipantsView";

type ViewType = "events" | "participants";

function App() {
  const [view, setView] = useState<ViewType>("events");
  const [refreshKey, setRefreshKey] = useState(0);

  const refresh = () => setRefreshKey((k) => k + 1);

  const {
    participants,
    selectedParticipantId,
    setSelectedParticipantId,
    participantTickets,
    participantForm,
    setParticipantForm,
    handleCreateParticipant,
    activeParticipant,
    refreshParticipants,
  } = useParticipants(refreshKey);

  const {
    events,
    editingEventId,
    eventForm,
    setEventForm,
    handleSaveEvent,
    handleEditClick,
    handleDeleteEvent,
    handleBuyTicket,
    cancelEditing,
  } = useEvents(refreshKey);

  const onBuyTicket = async (eventId: number) => {
    await handleBuyTicket(eventId, selectedParticipantId);
    refresh();
  };

  return (
    <div className="min-h-screen bg-slate-50 p-6 font-sans text-slate-800">
      <div className="max-w-6xl mx-auto">
        <Header
          participants={participants}
          selectedParticipantId={selectedParticipantId}
          onSelectParticipant={setSelectedParticipantId}
        />

        <TabNavigation activeView={view} onChangeView={setView} />

        {view === "events" && (
          <EventsView
            events={events}
            editingEventId={editingEventId}
            eventForm={eventForm}
            selectedParticipantId={selectedParticipantId}
            onFormChange={setEventForm}
            onSubmit={handleSaveEvent}
            onCancelEdit={cancelEditing}
            onBuyTicket={onBuyTicket}
            onEdit={handleEditClick}
            onDelete={handleDeleteEvent}
          />
        )}

        {view === "participants" && (
          <ParticipantsView
            participants={participants}
            selectedParticipantId={selectedParticipantId}
            participantTickets={participantTickets}
            activeParticipant={activeParticipant}
            participantForm={participantForm}
            onFormChange={setParticipantForm}
            onCreateParticipant={handleCreateParticipant}
            onSelectParticipant={setSelectedParticipantId}
            onRefreshParticipants={refreshParticipants}
          />
        )}
      </div>
    </div>
  );
}

export default App;
