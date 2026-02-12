import { Calendar, MapPin, Users, Trash2, Ticket, Edit } from "lucide-react";
import type { Evento } from "../../types";

interface EventCardProps {
  event: Evento;
  selectedParticipantId: number | null;
  onBuyTicket: (eventId: number) => void;
  onEdit: (eventId: number) => void;
  onDelete: (eventId: number) => void;
}

export function EventCard({
  event,
  selectedParticipantId,
  onBuyTicket,
  onEdit,
  onDelete,
}: EventCardProps) {
  return (
    <div className="bg-white p-5 rounded-xl shadow-sm border border-slate-200 hover:border-blue-300 transition group">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h3 className="text-lg font-bold text-slate-800 group-hover:text-blue-700 transition">
            {event.nome}
          </h3>
          <div className="text-slate-500 text-sm flex flex-wrap gap-4 mt-2">
            <span className="flex items-center gap-1">
              <Calendar size={14} />
              {new Date(event.dataEvento).toLocaleString()}
            </span>
            <span className="flex items-center gap-1">
              <MapPin size={14} /> {event.local}
            </span>
            <span className="flex items-center gap-1">
              <Users size={14} /> {event.capacidade} vagas
            </span>
          </div>
        </div>

        <div className="flex gap-2 w-full sm:w-auto">
          <button
            onClick={() => onBuyTicket(event.id)}
            disabled={!selectedParticipantId}
            title={
              !selectedParticipantId
                ? "Selecione um participante acima para comprar"
                : ""
            }
            className={`flex-1 sm:flex-none px-4 py-2 rounded-lg font-bold text-sm flex items-center justify-center gap-2 transition ${
              selectedParticipantId
                ? "bg-blue-600 text-white hover:bg-blue-700 shadow-sm"
                : "bg-slate-100 text-slate-400 cursor-not-allowed"
            }`}
          >
            <Ticket size={16} /> Comprar
          </button>

          <div className="flex border-l pl-2 gap-1">
            <button
              onClick={() => onEdit(event.id)}
              className="p-2 text-slate-400 hover:text-orange-500 hover:bg-orange-50 rounded-lg transition"
            >
              <Edit size={18} />
            </button>
            <button
              onClick={() => onDelete(event.id)}
              className="p-2 text-slate-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition"
            >
              <Trash2 size={18} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
