import { Ticket, User } from "lucide-react";
import type { Participante } from "../../types";

interface HeaderProps {
  participants: Participante[];
  selectedParticipantId: number | null;
  onSelectParticipant: (id: number | null) => void;
}

export function Header({
  participants,
  selectedParticipantId,
  onSelectParticipant,
}: HeaderProps) {
  return (
    <header className="mb-8 bg-white p-4 rounded-xl shadow-sm border border-slate-200 flex flex-col md:flex-row justify-between items-center gap-4">
      <div className="flex items-center gap-2">
        <div className="bg-blue-600 p-2 rounded-lg">
          <Ticket className="text-white" size={24} />
        </div>
        <div>
          <h1 className="text-xl font-bold text-slate-900">
            EventHub{" "}
            <span className="text-xs font-normal text-slate-500">
              | Painel de Vendas
            </span>
          </h1>
        </div>
      </div>

      <div className="flex items-center gap-3 bg-slate-50 px-4 py-2 rounded-lg border border-slate-200 w-full md:w-auto">
        <div
          className={`p-2 rounded-full transition-colors ${
            selectedParticipantId
              ? "bg-purple-100 text-purple-700"
              : "bg-slate-200 text-slate-400"
          }`}
        >
          <User size={20} />
        </div>

        <div className="flex-1">
          <label className="text-[10px] font-bold text-slate-400 uppercase block mb-0.5">
            Realizar vendas para:
          </label>
          <select
            value={selectedParticipantId ?? ""}
            onChange={(e) =>
              onSelectParticipant(Number(e.target.value) || null)
            }
            className="bg-transparent font-bold text-sm text-slate-800 outline-none w-full min-w-[220px] cursor-pointer"
          >
            <option value="">-- Selecione um Participante --</option>
            {participants.map((p) => (
              <option key={p.id} value={p.id}>
                {p.nome}
              </option>
            ))}
          </select>
        </div>
      </div>
    </header>
  );
}
