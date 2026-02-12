import { RefreshCw } from "lucide-react";
import type { Participante } from "../../types";

interface ParticipantListProps {
  participants: Participante[];
  selectedParticipantId: number | null;
  onSelectParticipant: (id: number) => void;
  onRefresh: () => void;
}

export function ParticipantList({
  participants,
  selectedParticipantId,
  onSelectParticipant,
  onRefresh,
}: ParticipantListProps) {
  return (
    <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-200">
      <div className="flex justify-between items-center mb-4">
        <h3 className="font-bold text-slate-700">Todos os Participantes</h3>
        <button
          onClick={onRefresh}
          className="text-slate-400 hover:text-blue-500 transition"
        >
          <RefreshCw size={16} />
        </button>
      </div>

      <div className="max-h-80 overflow-y-auto space-y-2 pr-2">
        {participants.map((p) => {
          const isActive = selectedParticipantId === p.id;
          return (
            <div
              key={p.id}
              onClick={() => onSelectParticipant(p.id)}
              className={`flex justify-between items-center p-3 rounded-lg border transition cursor-pointer ${
                isActive
                  ? "bg-green-50 border-green-200"
                  : "bg-slate-50 border-slate-100 hover:bg-white hover:border-blue-200"
              }`}
            >
              <div className="flex items-center gap-3">
                <div
                  className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold ${
                    isActive
                      ? "bg-green-200 text-green-700"
                      : "bg-slate-200 text-slate-500"
                  }`}
                >
                  {p.nome.charAt(0).toUpperCase()}
                </div>
                <div>
                  <p
                    className={`font-bold text-sm ${
                      isActive ? "text-green-800" : "text-slate-700"
                    }`}
                  >
                    {p.nome}
                  </p>
                  <p className="text-xs text-slate-400">{p.email}</p>
                </div>
              </div>

              {isActive && (
                <span className="text-xs font-bold text-green-600 bg-green-100 px-2 py-1 rounded-full">
                  Ativo
                </span>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}
