# LogiSmart — Rediseño del deck de defensa TPO

## Brief (respuestas del usuario)
- Doble uso: legible proyectado + interactivo.
- Estética: producto moderno SaaS, limpio, UN acento potente.
- Mantener interactividad: tarjetas expandibles + comparador.
- Caso de uso del envío → DIAGRAMA DE SECUENCIA interactivo (estilo UML), se completa paso a paso.
- Diagramas a mano (HTML/CSS), no Mermaid.
- Reorganizar para mejor narrativa. 15–20 min.

## Sistema visual
Base: deck_stage.js, 1920×1080.

### Tipografía
- Display/títulos: "Space Grotesk" (500/600/700) — moderno, técnico, geométrico.
- Cuerpo/UI: "IBM Plex Sans" (400/500/600) — limpio, ingenieril, legible.
- Código/mono: "IBM Plex Mono" (400/500) — para clases y paths (hay muchísimos).

### Color (claro, SaaS, 1 acento hero)
- --paper #F6F7F9 (fondo slide), --surface #FFFFFF (cards), --ink #15171C, --body #3A3F4A, --muted #6F7682, --line #E6E8EC
- ACENTO HERO: violeta-índigo eléctrico — oklch(0.53 0.245 276). Tints + strong para texto.
- Triada de categorías (dots/labels chicos, equal L≈0.64 C≈0.14, varía hue):
  - Creacional → naranja oklch(0.66 0.15 50)
  - Estructural → cian oklch(0.62 0.12 215)
  - Comportamiento → verde oklch(0.60 0.14 150)
- Sin gradientes decorativos. Hairlines, mucho aire, tipografía fuerte.

### Escala (px, 1920×1080) — piso 24px SIEMPRE
- display 104 / h1(divider) 76 / title 56 / h3 30 / lead 32 / body 25 / mono 24 / label 24(uppercase, tracking)
- pad-x 112 / pad-top 84 / pad-bottom 76 / gap 28

## Secuencia de títulos (noun-phrase corto, paralelo)
01. LogiSmart — portada
02. El sistema — qué es LogiSmart (dominio Envío + métricas)
03. El recorrido — 23 patrones en 7 hitos (roadmap/agenda)
04. ░ Creacionales (divider, Hitos 6–7)
05. Creacionales — cómo se crean los objetos (5 cards)
06. ░ Estructurales (divider, Hitos 8–9)
07. Estructurales — cómo se componen las clases (7 cards)
08. ░ Comportamiento (divider, Hitos 10–12)
09. Comportamiento I — Chain · Command · Interpreter
10. Comportamiento II — Iterator · Mediator · Memento · Observer
11. Comportamiento III — State · Strategy · Template Method · Visitor
12. El envío de punta a punta — DIAGRAMA DE SECUENCIA interactivo
13. Ciclo de vida del envío — diagrama de estados (State, a mano)
14. La red como árbol — diagrama Composite (a mano)
15. ¿Cuál es cuál? — comparador interactivo
16. Principios de diseño — SOLID · GRASP · decisiones
17. Cierre — LogiSmart completo

Lectura solo-títulos: presenta sistema → mapa → recorre las 3 familias → muestra cómo colaboran (secuencia) → dos estructuras clave → diferencias finas → fundamentos → cierre. Coherente.

## Componentes
- Section divider: número grande + palabra categoría + hitos + lista de patrones (parallel layout las 3).
- Pattern card: dot categoría + nombre (Space Grotesk) + idea 1 línea; click → expande detalle (clases + por qué) en panel, colapsa hermanos.
- Métricas: números grandes Space Grotesk.
- Roadmap: timeline horizontal de hitos 6→12 con chips de patrones.
- Secuencia: lifelines (header boxes) + líneas verticales dashed + mensajes grid revelados paso a paso + caption del patrón. Botones Anterior/Siguiente + dots.
- State diagram: nodos posicionados + flechas SVG etiquetadas. Happy path horizontal + ramas.
- Composite tree: árbol CSS root→regional→puntos, conectores.
- Comparador: tabs + paneles (Strategy/State, Adapter/Decorator/Proxy, FactoryMethod/AbstractFactory).

## Archivos
- LogiSmart Defensa.html (slides estáticas, editable)
- deck.css (sistema + componentes)
- deck.js (expand cards, comparador, stepper secuencia)
- deck-stage.js (starter)
