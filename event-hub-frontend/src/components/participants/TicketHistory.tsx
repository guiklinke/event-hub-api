import { Ticket, Calendar } from "lucide-react";
import type { Ingresso, Participante } from "../../types";

interface TicketHistoryProps {
  activeParticipant: Participante | null;
  tickets: Ingresso[];
}

export function TicketHistory({
  activeParticipant,
  tickets,
}: TicketHistoryProps) {
  return (
    <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-200 h-fit">
      <h2 className="text-lg font-bold mb-6 flex items-center gap-2 text-slate-800">
        <Ticket size={20} className="text-purple-600" />
        Ingressos de{" "}
        <span className="text-purple-600 underline decoration-dashed">
          {activeParticipant?.nome ?? ""}
        </span>
      </h2>

      {!activeParticipant ? (
        <div className="text-center py-10 bg-slate-50 rounded-lg border border-dashed">
          <p className="text-slate-400 text-sm">
            Selecione um participante para ver o histórico.
          </p>
        </div>
      ) : (
        <div className="space-y-3">
          {tickets.map((ticket) => (
            <div
              key={ticket.id}
              className="border border-purple-100 p-4 rounded-lg bg-purple-50 flex justify-between items-center hover:shadow-sm transition"
            >
              <div>
                <p className="font-bold text-purple-900">
                  {ticket.nomeEvento ?? "Evento Removido"}
                </p>
                <p className="text-xs text-purple-600 mt-1 flex items-center gap-1">
                  <Calendar size={10} /> Comprado em:{" "}
                  {new Date(ticket.dataCompra).toLocaleDateString()}
                </p>
              </div>
              <div className="text-right">
                <span className="text-xs bg-white text-purple-400 px-2 py-1 rounded border border-purple-100 font-mono">
                  #{ticket.id}
                </span>
              </div>
            </div>
          ))}

          {tickets.length === 0 && (
            <p className="text-slate-500 text-center text-sm py-4">
              Nenhum ingresso comprado.
            </p>
          )}
        </div>
      )}
    </div>
  );
}
