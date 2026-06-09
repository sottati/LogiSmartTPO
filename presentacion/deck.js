/* ════════════════════════════════════════════════════════════════
   LogiSmart · Defensa TPO — Interacciones
   ════════════════════════════════════════════════════════════════ */

/* ── 1 · TARJETAS DE PATRÓN EXPANDIBLES ─────────────────────────── */
document.querySelectorAll('.pcard').forEach(card => {
  card.addEventListener('click', () => {
    const open = card.classList.contains('open');
    card.parentElement.querySelectorAll('.pcard').forEach(s => s.classList.remove('open'));
    if (!open) card.classList.add('open');
  });
});

/* ── 4 · HEART SATELLITES (slide 09 — satélite → panel detalle) ── */
(function heartSatellites(){
  const sats = document.querySelectorAll('.heart-sat[data-hpat]');
  if (!sats.length) return;
  const detail  = document.getElementById('heart-detail');
  if (!detail) return;
  const idle    = detail.querySelector('.hd-idle');
  const active  = detail.querySelector('.hd-active');
  const nameEl  = active.querySelector('.hd-name');
  const clsEl   = active.querySelector('.hd-cls');
  const descEl  = active.querySelector('.hd-desc');
  const preEl   = active.querySelector('.hd-pre');

  const data = {
    builder:   { name:'Builder',   cls:'Envio.EnvioBuilder (inner class)',
                 desc:'Construye el Envío con 12 atributos (9 opcionales) vía API fluida. Evita constructores con demasiados parámetros y objetos inconsistentes.',
                 code:'new Envio.EnvioBuilder("id", empresa)\n    .tipoEnvio(URGENTE).peso(12.5).build();' },
    prototype: { name:'Prototype', cls:'Envio implements Cloneable',
                 desc:'Clona un envío prototipo para generar lotes idénticos. Evita reconstruir con Builder cuando origen y destino se repiten.',
                 code:'Envio lote = (Envio) envioBase.clone();\n// deep copy del estado completo' },
    state:     { name:'State',     cls:'EstadoEnvio (interface) · 6 estados concretos',
                 desc:'Happy path: Confirmado → EnTránsito → EnReparto → Entregado.  Alternativos: Cancelado · Retenido.  Cada estado decide sus propias transiciones válidas — sin switch gigante.',
                 code:'// EstadoConfirmado.java\ncambiarEstado(new EstadoEnTransito(envio));' },
    strategy:  { name:'Strategy',  cls:'EstrategiaCalculoCosto · 5 estrategias',
                 desc:'La fórmula de tarifa se inyecta desde afuera. Cambiar de EstrategiaDistancia a EstrategiaUrgencia no toca Envio.',
                 code:'envio.setEstrategia(new EstrategiaUrgencia());\nenvio.calcularCosto(); // delega a la estrategia' },
    memento:   { name:'Memento',   cls:'MementoEnvio · HistorialEnvios',
                 desc:'Snapshot inmutable del envío (estado, costo, timestamp) sin exponer campos privados. Permite auditar y restaurar.',
                 code:'MementoEnvio snap = envio.guardarEstado();\nenvio.restaurarEstado(snap);' },
    observer:  { name:'Observer',  cls:'ObservadorEnvio · Dashboard · Auditoría · SMS',
                 desc:'Al cambiar de estado, Envio notifica automáticamente a todos sus suscriptores. Agregar un observador nuevo no toca Envio.',
                 code:'envio.notificarObservadores();\n// → Dashboard + SistemaAuditoria + NotificadorSMS' },
  };

  sats.forEach(sat => {
    sat.addEventListener('click', e => {
      e.stopPropagation();
      const isActive = sat.classList.contains('hs-active');
      sats.forEach(s => s.classList.remove('hs-active', 'hs-dim'));
      if (!isActive) {
        sat.classList.add('hs-active');
        sats.forEach(s => { if (s !== sat) s.classList.add('hs-dim'); });
        const p = data[sat.dataset.hpat];
        nameEl.textContent = p.name;
        clsEl.textContent  = p.cls;
        descEl.textContent = p.desc;
        preEl.textContent  = p.code;
        idle.style.display   = 'none';
        active.style.display = 'flex';
      } else {
        idle.style.display   = '';
        active.style.display = 'none';
      }
    });
  });
})();

/* ── 5 · CODE PEEK (tarjeta → fragmento de código) ─────────────── */
document.querySelectorAll('.pgrid[data-preview]').forEach(grid => {
  const preview = document.getElementById(grid.dataset.preview);
  if (!preview) return;
  const idle     = preview.querySelector('.cp-idle');
  const snippets = preview.querySelectorAll('.cp-snippet');
  grid.addEventListener('click', () => {
    requestAnimationFrame(() => {
      const openCard = grid.querySelector('.pcard.open');
      if (!openCard) {
        idle.style.display = '';
        snippets.forEach(s => s.style.display = 'none');
      } else {
        idle.style.display = 'none';
        snippets.forEach(s => {
          s.style.display = s.dataset.snippet === openCard.dataset.snippet ? 'block' : 'none';
        });
      }
    });
  });
});

/* ── 2 · COMPARADOR (tabs) ──────────────────────────────────────── */
document.querySelectorAll('.cmp-tab').forEach(tab => {
  tab.addEventListener('click', () => {
    const scope = tab.closest('section');
    const id = tab.dataset.cmp;
    scope.querySelectorAll('.cmp-tab').forEach(t => t.classList.toggle('active', t === tab));
    scope.querySelectorAll('.cmp-panel').forEach(p => p.classList.toggle('active', p.dataset.panel === id));
  });
});

/* ── 3 · DIAGRAMAS DE SECUENCIA INTERACTIVOS ────────────────────── */
function buildSequence(rootId, actors, steps, initTxt) {
  const root = document.getElementById(rootId);
  if (!root) return;

  const N = actors.length;
  const M = steps.length;
  const center = i => (i + 0.5) / N * 100;

  const head = document.createElement('div');
  head.className = 'seq-actors';
  head.style.gridTemplateColumns = `repeat(${N}, 1fr)`;
  head.innerHTML = actors.map((a,i) =>
    `<div class="seq-actor" data-actor="${i}"><div class="role">${a.role}</div><div class="nm">${a.nm}</div></div>`
  ).join('');
  root.appendChild(head);

  const canvas = document.createElement('div');
  canvas.className = 'seq-canvas';
  const lines = document.createElement('div');
  lines.className = 'seq-lines';
  lines.style.gridTemplateColumns = `repeat(${N}, 1fr)`;
  lines.innerHTML = actors.map((_,i) => `<div class="ll" data-ll="${i}"></div>`).join('');
  canvas.appendChild(lines);

  const msgs = document.createElement('div');
  msgs.className = 'seq-msgs';
  msgs.style.justifyContent = 'space-evenly';
  steps.forEach((s,k) => {
    const lo = Math.min(s.from, s.to), hi = Math.max(s.from, s.to);
    const dir = s.to > s.from ? 'right' : 'left';
    const left = center(lo), width = center(hi) - center(lo);
    const m = document.createElement('div');
    m.className = 'seq-msg';
    m.dataset.step = k + 1;
    const chip = s.chip || s.pat;
    m.innerHTML =
      `<div class="seq-arrow ${dir}${s.ret ? ' ret' : ''}" style="left:${left}%;width:${width}%">
         <div class="line"></div><div class="head"></div>
         ${s.ret ? '' : `<div class="seq-chip">${chip}</div>`}
         <div class="seq-label${s.ret ? ' ret' : ''}">${s.label}</div>
       </div>`;
    msgs.appendChild(m);
  });
  canvas.appendChild(msgs);
  root.appendChild(canvas);

  const foot = document.createElement('div');
  foot.className = 'seq-foot';
  foot.innerHTML =
    `<div class="seq-caption">
       <div class="pat seq-pat">Diagrama de secuencia</div>
       <div class="txt seq-txt">${initTxt}</div>
     </div>
     <div class="seq-controls">
       <span class="seq-step-n seq-stepn">0 / ${M}</span>
       <div class="seq-dots">${steps.map((_,i)=>`<span class="d" data-d="${i+1}"></span>`).join('')}</div>
       <button class="seq-btn seq-prev">◀ Atrás</button>
       <button class="seq-btn primary seq-next">Siguiente paso ▶</button>
       <button class="seq-btn seq-reset" title="Reiniciar">↺</button>
     </div>`;
  root.appendChild(foot);

  let cur = 0;
  const patEl   = foot.querySelector('.seq-pat');
  const txtEl   = foot.querySelector('.seq-txt');
  const stepnEl = foot.querySelector('.seq-stepn');
  const prevBtn = foot.querySelector('.seq-prev');
  const nextBtn = foot.querySelector('.seq-next');
  const resetBtn = foot.querySelector('.seq-reset');

  function render() {
    msgs.querySelectorAll('.seq-msg').forEach(m => {
      m.classList.toggle('show', +m.dataset.step <= cur);
    });
    foot.querySelectorAll('.seq-dots .d').forEach(d => {
      const n = +d.dataset.d;
      d.classList.toggle('done', n <= cur);
      d.classList.toggle('cur', n === cur);
    });
    const live = cur > 0 ? [steps[cur-1].from, steps[cur-1].to] : [];
    head.querySelectorAll('.seq-actor').forEach(a => a.classList.toggle('live', live.includes(+a.dataset.actor)));
    lines.querySelectorAll('.ll').forEach(l => l.classList.toggle('live', live.includes(+l.dataset.ll)));

    if (cur === 0) {
      patEl.textContent = 'Diagrama de secuencia';
      txtEl.textContent = initTxt;
    } else {
      patEl.textContent = `Paso ${cur} · ${steps[cur-1].pat}`;
      txtEl.textContent = steps[cur-1].txt;
    }
    stepnEl.textContent = `${cur} / ${M}`;
    prevBtn.disabled = cur === 0;
    nextBtn.disabled = cur === M;
  }

  function next()  { if (cur < M) { cur++; render(); } }
  function prev()  { if (cur > 0) { cur--; render(); } }
  function reset() { cur = 0; render(); }

  nextBtn.addEventListener('click', next);
  prevBtn.addEventListener('click', prev);
  resetBtn.addEventListener('click', reset);
  canvas.addEventListener('click', next);

  render();
}

/* ·· Flujo integral — crearEnvío a través de las 5 capas ·· */
buildSequence('seq-root',
  [
    { nm: 'Cliente',      role: 'actor' },
    { nm: 'Facade',       role: 'ServicioLogística' },
    { nm: 'Builder',      role: 'EnvioBuilder' },
    { nm: 'Validadores',  role: 'CadenaValidadores' },
    { nm: 'Strategy',     role: 'EstrategiaCosto' },
    { nm: 'Proceso',      role: 'ProcesoNacional' },
    { nm: 'Envío',        role: 'State' },
    { nm: 'Observers',    role: 'suscriptores' },
  ],
  [
    { from:0, to:1, label:'crearEnvio()', pat:'Facade',
      txt:'El cliente llama un único método. La fachada orquesta todos los subsistemas internos sin exponerlos.' },
    { from:1, to:2, label:'new EnvioBuilder()…build()', pat:'Builder',
      txt:'La fachada arma el Envío paso a paso —tipo, peso, origen, destino— de forma legible y segura.' },
    { from:2, to:1, ret:true, label:'Envio', pat:'Builder',
      txt:'El Builder devuelve un Envío ya construido y consistente.' },
    { from:1, to:3, label:'validarEnvio(envio)', pat:'Chain of Responsibility', chip:'Chain',
      txt:'El envío pasa por 5 validadores en orden de costo. Si uno falla, corta la cadena (fail-fast).' },
    { from:3, to:1, ret:true, label:'true', pat:'Chain of Responsibility', chip:'Chain',
      txt:'Todos los validadores pasaron: el envío es válido y la cadena devuelve el control.' },
    { from:1, to:4, label:'calcular(envio)', pat:'Strategy',
      txt:'Se inyecta la estrategia adecuada (ej. Híbrida). El algoritmo de costo es intercambiable en runtime.' },
    { from:4, to:1, ret:true, label:'costo', pat:'Strategy',
      txt:'La estrategia devuelve el costo sin que la fachada conozca la fórmula interna.' },
    { from:1, to:5, label:'procesarEnvio(envio)', pat:'Template Method',
      txt:'El proceso ejecuta un esqueleto fijo y final: validar → calcular → cobrar → notificar.' },
    { from:5, to:6, label:'cambiarEstado(EstadoEnTransito)', pat:'State',
      txt:'Confirmado el pago, el Envío transiciona. Delega la lógica al objeto de estado actual.' },
    { from:6, to:7, label:'notificarObservadores()', pat:'Observer',
      txt:'cambiarEstado() llama internamente a notificarObservadores(). El dashboard, la auditoría y el SMS reciben el evento automáticamente.' },
  ],
  'Seguí el flujo de un envío a través de los patrones. Pulsá "Siguiente paso" para empezar.'
);

/* ·· CU-04 — asignarRuta: seis patrones en un método ·· */
