import { Edit, Plus } from "lucide-react";
import type { EventFormData } from "../../types";

interface EventFormProps {
  form: EventFormData;
  editingEventId: number | null;
  onChange: (form: EventFormData) => void;
  onSubmit: (e: React.SyntheticEvent) => void;
  onCancel: () => void;
}

export function EventForm({
  form,
  editingEventId,
  onChange,
  onSubmit,
  onCancel,
}: EventFormProps) {
  const isEditing = editingEventId !== null;

  return (
    <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-200 sticky top-6">
      <h2
        className={`text-lg font-bold mb-4 flex items-center gap-2 ${
          isEditing ? "text-orange-600" : "text-blue-700"
        }`}
      >
        {isEditing ? <Edit size={18} /> : <Plus size={18} />}
        {isEditing ? "Editar Evento" : "Criar Evento"}
      </h2>

      <form onSubmit={onSubmit} className="flex flex-col gap-4">
        <div>
          <label className="text-xs font-bold text-slate-400 uppercase">
            Nome do Evento
          </label>
          <input
            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
            value={form.nome}
            onChange={(e) => onChange({ ...form, nome: e.target.value })}
            required
          />
        </div>

        <div>
          <label className="text-xs font-bold text-slate-400 uppercase">
            Data
          </label>
          <input
            type="datetime-local"
            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
            value={form.data}
            onChange={(e) => onChange({ ...form, data: e.target.value })}
            required
          />
        </div>

        <div>
          <label className="text-xs font-bold text-slate-400 uppercase">
            Local
          </label>
          <input
            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
            value={form.local}
            onChange={(e) => onChange({ ...form, local: e.target.value })}
            required
          />
        </div>

        <div>
          <label className="text-xs font-bold text-slate-400 uppercase">
            Capacidade
          </label>
          <input
            type="number"
            className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none"
            value={form.capacidade}
            onChange={(e) =>
              onChange({ ...form, capacidade: Number(e.target.value) })
            }
            required
          />
        </div>

        <div className="flex gap-2 pt-2">
          <button
            type="submit"
            className={`flex-1 text-white py-2 rounded-lg font-bold shadow-sm transition ${
              isEditing
                ? "bg-orange-500 hover:bg-orange-600"
                : "bg-blue-600 hover:bg-blue-700"
            }`}
          >
            {isEditing ? "Salvar" : "Criar"}
          </button>

          {isEditing && (
            <button
              type="button"
              onClick={onCancel}
              className="bg-slate-200 px-4 rounded-lg text-slate-600 hover:bg-slate-300"
            >
              Cancelar
            </button>
          )}
        </div>
      </form>
    </div>
  );
}
