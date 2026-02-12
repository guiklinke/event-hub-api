type ViewType = "events" | "participants";

interface TabNavigationProps {
  activeView: ViewType;
  onChangeView: (view: ViewType) => void;
}

export function TabNavigation({
  activeView,
  onChangeView,
}: TabNavigationProps) {
  return (
    <div className="flex gap-1 mb-6 bg-white p-1 rounded-lg w-fit shadow-sm border">
      <button
        onClick={() => onChangeView("events")}
        className={`px-6 py-2 rounded-md font-medium text-sm transition ${
          activeView === "events"
            ? "bg-blue-50 text-blue-700 shadow-sm"
            : "text-slate-500 hover:bg-slate-50"
        }`}
      >
        Eventos
      </button>
      <button
        onClick={() => onChangeView("participants")}
        className={`px-6 py-2 rounded-md font-medium text-sm transition ${
          activeView === "participants"
            ? "bg-blue-50 text-blue-700 shadow-sm"
            : "text-slate-500 hover:bg-slate-50"
        }`}
      >
        Participantes
      </button>
    </div>
  );
}
