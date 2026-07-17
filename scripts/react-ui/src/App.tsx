import {
  Activity,
  AlertTriangle,
  CheckCircle2,
  Clock3,
  Download,
  FileCode2,
  FileSpreadsheet,
  FlaskConical,
  KeyRound,
  Loader2,
  Play,
  RefreshCw,
  Search,
  Square,
  XCircle
} from "lucide-react";
import { FormEvent, useEffect, useMemo, useState } from "react";
import { API_BASE, ApiCallInfo, GeneratedTestInfo, RunEvent, RunInfo, RunMode, SampleKey, api } from "./lib/api";
import { STAGES, stageForPhase, statusTone } from "./lib/stages";

const DEFAULT_MODEL = "gpt-4o-mini-2024-07-18";
const DEFAULT_PROMPT = "rbl4-zero-shot";

function formatNumber(value: unknown) {
  if (value === undefined || value === null || value === "") return "-";
  const parsed = Number(value);
  if (Number.isNaN(parsed)) return String(value);
  return Number.isInteger(parsed) ? String(parsed) : parsed.toFixed(2);
}

function shortPath(path: string) {
  return path.replace(/\\/g, "/").split("/").slice(-2).join("/");
}

function statusIcon(status: string) {
  const tone = statusTone(status);
  if (tone === "success") return <CheckCircle2 size={16} />;
  if (tone === "danger") return <XCircle size={16} />;
  if (tone === "warning") return <AlertTriangle size={16} />;
  if (tone === "running") return <Loader2 className="spin" size={16} />;
  return <Clock3 size={16} />;
}

export default function App() {
  const [runs, setRuns] = useState<RunInfo[]>([]);
  const [selectedRunId, setSelectedRunId] = useState<string>("");
  const [runDetail, setRunDetail] = useState<RunInfo | null>(null);
  const [summary, setSummary] = useState<Record<string, unknown>[]>([]);
  const [diagnostics, setDiagnostics] = useState<Record<string, unknown>[]>([]);
  const [generatedTests, setGeneratedTests] = useState<GeneratedTestInfo[]>([]);
  const [apiCalls, setApiCalls] = useState<ApiCallInfo[]>([]);
  const [metrics, setMetrics] = useState<Record<string, unknown>[]>([]);
  const [events, setEvents] = useState<RunEvent[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [logFilter, setLogFilter] = useState("");
  const [form, setForm] = useState({
    sample_key: "part1" as SampleKey,
    custom_sample_csv: "",
    run_mode: "dry_run" as RunMode,
    model: DEFAULT_MODEL,
    prompt: DEFAULT_PROMPT,
    resume: false,
    clear_agone_output: true
  });

  async function refreshRuns() {
    const data = await api.listRuns();
    setRuns(data);
    if (!selectedRunId && data[0]) setSelectedRunId(data[0].run_id);
  }

  async function refreshRun(runId: string) {
    const payload = await api.getRun(runId);
    setRunDetail(payload.run);
    setSummary(payload.summary);
    setDiagnostics(payload.diagnostics);
    setGeneratedTests(payload.generated_tests);
    setApiCalls(payload.api_calls);
    setMetrics(payload.metrics_preview);
    setEvents(payload.recent_events);
  }

  useEffect(() => {
    refreshRuns().catch((exc) => setError(exc.message));
  }, []);

  useEffect(() => {
    if (!selectedRunId) return;
    refreshRun(selectedRunId).catch((exc) => setError(exc.message));
    const source = new EventSource(`${API_BASE}/api/runs/${selectedRunId}/events`);
    source.addEventListener("phase", (message) => {
      const event = JSON.parse((message as MessageEvent).data) as RunEvent;
      setEvents((current) => [...current.filter((item) => item.event_id !== event.event_id), event].slice(-300));
      refreshRun(selectedRunId).catch(() => undefined);
    });
    source.addEventListener("done", () => {
      source.close();
      refreshRuns().catch(() => undefined);
      refreshRun(selectedRunId).catch(() => undefined);
    });
    source.onerror = () => source.close();
    return () => source.close();
  }, [selectedRunId]);

  const selectedRun = runDetail || runs.find((run) => run.run_id === selectedRunId) || null;
  const filteredEvents = useMemo(() => {
    const query = logFilter.trim().toLowerCase();
    if (!query) return events;
    return events.filter((event) =>
      [event.phase, event.project, event.arm, event.focal_class, event.test_class, event.status, event.detail]
        .join(" ")
        .toLowerCase()
        .includes(query)
    );
  }, [events, logFilter]);

  const stageState = useMemo(() => {
    const result: Record<string, { status: string; count: number; detail: string }> = {};
    for (const stage of STAGES) result[stage.key] = { status: "idle", count: 0, detail: "" };
    for (const event of events) {
      const key = stageForPhase(event.phase);
      result[key] = { status: event.status || "running", count: result[key].count + 1, detail: event.detail };
    }
    return result;
  }, [events]);

  const armStats = useMemo(() => {
    const rows = metrics.reduce<Record<string, { total: number; compiled: number; mutation: number[] }>>((acc, row) => {
      const arm = String(row.arm || "unknown");
      acc[arm] ||= { total: 0, compiled: 0, mutation: [] };
      acc[arm].total += 1;
      if (Number(row.compilation) === 1) acc[arm].compiled += 1;
      const mutation = Number(row.mutation_score_strict_zero_fill);
      if (!Number.isNaN(mutation)) acc[arm].mutation.push(mutation);
      return acc;
    }, {});
    return Object.entries(rows).map(([arm, value]) => ({
      arm,
      total: value.total,
      compiled: value.compiled,
      mutation:
        value.mutation.length > 0
          ? value.mutation.reduce((sum, item) => sum + item, 0) / value.mutation.length
          : 0
    }));
  }, [metrics]);

  const apiStats = useMemo(() => {
    const ok = apiCalls.filter((call) => call.status === "OK").length;
    const totalTokens = apiCalls.reduce((sum, call) => {
      const tokens = Number(call.total_tokens);
      return sum + (Number.isNaN(tokens) ? 0 : tokens);
    }, 0);
    const duration = apiCalls.reduce((sum, call) => {
      const seconds = Number(call.duration_sec);
      return sum + (Number.isNaN(seconds) ? 0 : seconds);
    }, 0);
    return { ok, total: apiCalls.length, totalTokens, duration };
  }, [apiCalls]);

  async function submitRun(event: FormEvent) {
    event.preventDefault();
    setLoading(true);
    setError("");
    try {
      const created = await api.createRun({
        ...form,
        custom_sample_csv: form.sample_key === "custom" ? form.custom_sample_csv : undefined
      });
      setSelectedRunId(created.run_id);
      await refreshRuns();
    } catch (exc) {
      setError(exc instanceof Error ? exc.message : String(exc));
    } finally {
      setLoading(false);
    }
  }

  async function cancelSelectedRun() {
    if (!selectedRun) return;
    await api.cancelRun(selectedRun.run_id);
    await refreshRun(selectedRun.run_id);
    await refreshRuns();
  }

  return (
    <main className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-mark"><FlaskConical size={20} /></div>
          <div>
            <h1>RBL-4</h1>
            <p>Experiment Tool</p>
          </div>
        </div>

        <form className="launcher panel" onSubmit={submitRun}>
          <label>
            Sample
            <select value={form.sample_key} onChange={(event) => setForm({ ...form, sample_key: event.target.value as SampleKey })}>
              <option value="part1">Pilot Part 1</option>
              <option value="part2">Pilot Part 2</option>
              <option value="part3">Pilot Part 3</option>
              <option value="pilot_60">Pilot 60</option>
              <option value="pilot_24">Pilot 24 New</option>
              <option value="full_300">Full 300</option>
              <option value="custom">Custom CSV</option>
            </select>
          </label>
          {form.sample_key === "custom" && (
            <label>
              Custom CSV
              <input value={form.custom_sample_csv} onChange={(event) => setForm({ ...form, custom_sample_csv: event.target.value })} />
            </label>
          )}
          <label>
            Mode
            <select value={form.run_mode} onChange={(event) => setForm({ ...form, run_mode: event.target.value as RunMode })}>
              <option value="dry_run">Dry Run</option>
              <option value="report_only">Report Only</option>
              <option value="full_run">Full Run</option>
            </select>
          </label>
          <label>
            Model
            <input value={form.model} onChange={(event) => setForm({ ...form, model: event.target.value })} />
          </label>
          <label>
            Prompt
            <input value={form.prompt} onChange={(event) => setForm({ ...form, prompt: event.target.value })} />
          </label>
          <label className="check-row">
            <input type="checkbox" checked={form.resume} onChange={(event) => setForm({ ...form, resume: event.target.checked })} />
            Resume previous AgoneTest outputs
          </label>
          <button className="primary" disabled={loading}>
            {loading ? <Loader2 className="spin" size={16} /> : <Play size={16} />}
            Start Run
          </button>
        </form>

        <section className="run-list panel">
          <div className="panel-heading">
            <span>Runs</span>
            <button className="icon-button" onClick={() => refreshRuns()} aria-label="Refresh runs"><RefreshCw size={15} /></button>
          </div>
          <div className="run-items">
            {runs.map((run) => (
              <button key={run.run_id} className={`run-item ${run.run_id === selectedRunId ? "active" : ""}`} onClick={() => setSelectedRunId(run.run_id)}>
                <span className={`status-dot ${statusTone(run.status)}`} />
                <span>
                  <strong>{run.run_mode}</strong>
                  <small>{run.run_id}</small>
                </span>
              </button>
            ))}
            {runs.length === 0 && <p className="muted">No runs yet.</p>}
          </div>
        </section>
      </aside>

      <section className="workspace">
        <header className="topbar">
          <div>
            <p className="eyebrow">CLASSES2TEST / AgoneTest</p>
            <h2>GPT-4o-mini vs EvoSuite pipeline</h2>
          </div>
          <div className="actions">
            {selectedRun?.status === "running" && (
              <button className="secondary danger-action" onClick={cancelSelectedRun}><Square size={15} />Cancel</button>
            )}
            <button className="secondary" onClick={() => selectedRun && refreshRun(selectedRun.run_id)}><RefreshCw size={15} />Refresh</button>
          </div>
        </header>

        {error && <div className="notice danger"><AlertTriangle size={16} />{error}</div>}

        <section className="hero-grid">
          <div className="overview panel">
            <div className="run-title">
              <div>
                <p className="eyebrow">Selected Run</p>
                <h3>{selectedRun ? selectedRun.run_id : "No run selected"}</h3>
              </div>
              <span className={`pill ${statusTone(selectedRun?.status || "idle")}`}>
                {statusIcon(selectedRun?.status || "idle")}
                {selectedRun?.status || "idle"}
              </span>
            </div>
            <div className="stat-grid">
              <div><span>Source N</span><strong>{formatNumber(selectedRun?.source_sample_n)}</strong></div>
              <div><span>Buildable</span><strong>{formatNumber(selectedRun?.buildable_run_n)}</strong></div>
              <div><span>Skipped</span><strong>{formatNumber(selectedRun?.precheck_skipped_n)}</strong></div>
              <div><span>Baseline OK</span><strong>{formatNumber(selectedRun?.baseline_pass_n)}</strong></div>
              <div><span>Baseline Fail</span><strong>{formatNumber(selectedRun?.baseline_failed_n)}</strong></div>
              <div><span>Generation</span><strong>{formatNumber(selectedRun?.generation_run_n)}</strong></div>
              <div><span>Artifacts</span><strong>{selectedRun?.artifacts.length || 0}</strong></div>
            </div>
            <p className="path-line">{selectedRun?.sample_csv ? shortPath(selectedRun.sample_csv) : "Start a run to stage an experiment sample."}</p>
          </div>

          <div className="arm-panel panel">
            <div className="panel-heading"><span>Arms</span><Activity size={16} /></div>
            <div className="arm-list">
              {armStats.map((arm) => (
                <div className="arm-card" key={arm.arm}>
                  <div>
                    <strong>{arm.arm}</strong>
                    <small>{arm.compiled}/{arm.total} compiled</small>
                  </div>
                  <b>{arm.mutation.toFixed(1)}%</b>
                </div>
              ))}
              {armStats.length === 0 && <p className="muted">Metrics appear after dry-run/report generation.</p>}
            </div>
          </div>
        </section>

        <section className="timeline panel">
          <div className="panel-heading"><span>Realtime Pipeline</span><Clock3 size={16} /></div>
          <div className="stage-row">
            {STAGES.map((stage) => {
              const state = stageState[stage.key];
              const tone = statusTone(state.status);
              return (
                <div className={`stage ${tone}`} key={stage.key}>
                  <div className="stage-icon">{statusIcon(state.status)}</div>
                  <strong>{stage.label}</strong>
                  <span>{stage.detail}</span>
                  <small>{state.count} events</small>
                </div>
              );
            })}
          </div>
        </section>

        <section className="content-grid">
          <div className="panel table-panel">
            <div className="panel-heading"><span>Metrics Preview</span><Activity size={16} /></div>
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Arm</th>
                    <th>Project</th>
                    <th>Focal Class</th>
                    <th>Compile</th>
                    <th>Branch</th>
                    <th>Line</th>
                    <th>Mutation</th>
                    <th>Fail Stage</th>
                  </tr>
                </thead>
                <tbody>
                  {metrics.slice(0, 80).map((row, index) => (
                    <tr key={`${row.id_focal_class}-${row.arm}-${index}`}>
                      <td>{String(row.arm || "-")}</td>
                      <td>{String(row.project || "-")}</td>
                      <td>{String(row.focal_class || "-")}</td>
                      <td>{formatNumber(row.compilation)}</td>
                      <td>{formatNumber(row.branch_coverage_strict_zero_fill)}</td>
                      <td>{formatNumber(row.line_coverage_strict_zero_fill)}</td>
                      <td>{formatNumber(row.mutation_score_strict_zero_fill)}</td>
                      <td><span className="soft-tag">{String(row.fail_stage || "-")}</span></td>
                    </tr>
                  ))}
                  {metrics.length === 0 && (
                    <tr><td colSpan={8} className="empty-cell">No metrics loaded.</td></tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>

          <aside className="panel artifacts">
            <div className="panel-heading"><span>Artifacts</span><FileSpreadsheet size={16} /></div>
            <div className="artifact-list">
              {selectedRun?.artifacts.map((artifact) => (
                <a key={artifact.name} className="artifact" href={api.artifactUrl(selectedRun.run_id, artifact.name)}>
                  <Download size={15} />
                  <span>{artifact.name}</span>
                  <small>{Math.max(1, Math.round(artifact.size_bytes / 1024))} KB</small>
                </a>
              ))}
              {!selectedRun?.artifacts.length && <p className="muted">Artifacts appear as the runner writes them.</p>}
            </div>
          </aside>
        </section>

        {diagnostics.length > 0 && (
          <section className="panel diagnostics-panel">
            <div className="panel-heading"><span>Diagnostics</span><AlertTriangle size={16} /></div>
            <div className="diagnostics-list">
              {diagnostics.map((row, index) => (
                <article className="diagnostic-card" key={`${row.category}-${index}`}>
                  <div>
                    <span className={`status-dot ${String(row.severity) === "error" ? "danger" : "warning"}`} />
                    <strong>{String(row.category || "runtime_issue")}</strong>
                    <small>{[row.project, row.module, row.arm, row.phase].filter(Boolean).join(" / ")}</small>
                  </div>
                  <p>{String(row.explanation_vi || row.detail || "-")}</p>
                  <em>{String(row.suggested_action_vi || "Xem phase_log.csv để phân tích chi tiết.")}</em>
                </article>
              ))}
            </div>
          </section>
        )}

        {apiCalls.length > 0 && (
          <section className="panel api-panel">
            <div className="panel-heading"><span>GPT API Calls</span><KeyRound size={16} /></div>
            <div className="api-stats">
              <div><span>OK Calls</span><strong>{apiStats.ok}/{apiStats.total}</strong></div>
              <div><span>Total Tokens</span><strong>{formatNumber(apiStats.totalTokens)}</strong></div>
              <div><span>Duration</span><strong>{apiStats.duration.toFixed(1)}s</strong></div>
            </div>
            <div className="api-call-list">
              {apiCalls.slice(-6).reverse().map((call, index) => (
                <article className="api-call-card" key={`${call.timestamp_utc}-${index}`}>
                  <div>
                    <strong>{call.model_returned || call.model_requested}</strong>
                    <small>{call.timestamp_utc}</small>
                  </div>
                  <span className={`soft-tag ${call.status === "OK" ? "ok" : "bad"}`}>{call.status}</span>
                  <small>{formatNumber(call.total_tokens)} tokens / {formatNumber(call.duration_sec)}s</small>
                </article>
              ))}
            </div>
          </section>
        )}

        {generatedTests.length > 0 && (
          <section className="panel generated-tests-panel">
            <div className="panel-heading"><span>Generated Test Classes</span><FileCode2 size={16} /></div>
            <div className="generated-tests-list">
              {generatedTests.slice(0, 12).map((test) => (
                <article className="generated-test-card" key={`${test.project}-${test.file_name}-${test.stored_path}`}>
                  <div>
                    <strong>{test.file_name}</strong>
                    <small>{[test.project, test.arm].filter(Boolean).join(" / ")}</small>
                  </div>
                  <p>{shortPath(test.stored_path)}</p>
                  <small className="mono-path">{test.stored_path}</small>
                </article>
              ))}
            </div>
            <p className="path-line">
              Full list: generated_tests_manifest.csv. Packaged copy: generated_tests.zip.
            </p>
          </section>
        )}

        <section className="panel log-panel">
          <div className="panel-heading">
            <span>Live Log</span>
            <label className="search">
              <Search size={15} />
              <input value={logFilter} onChange={(event) => setLogFilter(event.target.value)} placeholder="Filter logs" />
            </label>
          </div>
          <div className="log-list">
            {filteredEvents.slice().reverse().map((event) => (
              <article className="log-row" key={event.event_id}>
                <span className={`status-dot ${statusTone(event.status)}`} />
                <div>
                  <strong>{event.phase}</strong>
                  <p>{event.detail || event.status}</p>
                  <small>{[event.project, event.arm, event.focal_class].filter(Boolean).join(" / ") || event.timestamp_utc}</small>
                </div>
                <b>{event.status}</b>
              </article>
            ))}
            {filteredEvents.length === 0 && <p className="muted">No log events match the filter.</p>}
          </div>
        </section>

        {summary.length > 0 && (
          <section className="panel summary-strip">
            {summary.map((row, index) => (
              <div className="summary-card" key={index}>
                <span>{String(row.arm || "summary")}</span>
                <strong>{formatNumber(row.compiled_success_rate)}</strong>
                <small>compiled success rate</small>
              </div>
            ))}
          </section>
        )}
      </section>
    </main>
  );
}
