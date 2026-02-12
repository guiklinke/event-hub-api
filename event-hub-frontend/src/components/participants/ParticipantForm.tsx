import { UserPlus } from "lucide-react";
import type { ParticipantFormData } from "../../types";

interface ParticipantFormProps {
  form: ParticipantFormData;
  onChange: (form: ParticipantFormData) => void;
  onSubmit: (e: React.SyntheticEvent) => void;
}

export function ParticipantForm({
  form,
  onChange,
  onSubmit,
}: ParticipantFormProps) {
  return (
    <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-200">
      <h2 className="text-lg font-bold mb-4 flex items-center gap-2 text-purple-700">
        <UserPlus size={18} /> Novo Participante
      </h2>

      <form onSubmit={onSubmit} className="flex flex-col gap-3">
        <input
          placeholder="Nome Completo"
          className="border p-2 rounded focus:ring-2 focus:ring-purple-500 outline-none"
          value={form.nome}
          onChange={(e) => onChange({ ...form, nome: e.target.value })}
          required
        />
        <input
          type="email"
          placeholder="Email"
          className="border p-2 rounded focus:ring-2 focus:ring-purple-500 outline-none"
          value={form.email}
          onChange={(e) => onChange({ ...form, email: e.target.value })}
          required
        />
        <button
          type="submit"
          className="bg-purple-600 text-white py-2 rounded-lg font-bold hover:bg-purple-700 shadow-sm transition"
        >
          Cadastrar
        </button>
      </form>
    </div>
  );
}
