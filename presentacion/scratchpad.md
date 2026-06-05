# LogiSmart — Deck de defensa TPO (versión canónica)

## Estructura del deck
Archivo principal: `LogiSmart Arquitectura.html` (17 slides, 1920×1080)
Narrativa: negocio sobre arquitectura en 5 capas — cada capa arranca por la
necesidad de negocio y recién después aparece la decisión de diseño que la cumple.

## Números vigentes
- 27 patrones GoF + persistencia (23 GoF acumulados al Hito 12 + 4 de acceso a datos)
- 229 clases · 148 tests en verde · Hito 13

## Secuencia de slides (17 en total)
01. Portada (oscura) — chips: 5 capas · 35 patrones · 200 clases · H13
02. El cliente y su problema — antes/después (planillas → SaaS multitenant)
03. Qué le prometimos — CU-01 / CU-03 / CU-07 + actores
04. Atributos de calidad — 5 atributos como brújula de diseño
05. La arquitectura en 5 capas — hero: cada atributo vive en una capa
06. Del problema al diseño — sustantivos→clases, verbos→CUs, adjetivos→patrones
07. Presentación → Aislamiento de datos (Controller, Rol/Polymorphism)
08. [DECISIÓN B] 25 booleanos → Rol enum — GRASP Information Expert, refactoring con 148 tests
09. Aplicación → Alta disponibilidad (Facade, Chain of Responsibility)
10. Dominio · Envío el corazón — diagrama radial interactivo con 6 patrones
11. Dominio → Tiempo real (State, Observer, Memento)
12. Persistencia → Eficiencia de respuesta (Repository+DataMapper, Proxy+Lazy, UoW)
13. [DECISIÓN A] H7 sembró H13 cosechó — Builder reutilizado en persistencia, RUP iterativo
14. Infraestructura → Interoperabilidad + Elasticidad (Adapter, Abstract Factory, Flyweight)
15. [DECISIÓN E] Abstract Factory como decisión de negocio — AR vs BR, coherencia regional
16. Flujo de un envío — diagrama de secuencia interactivo (generado por JS)
17. [DECISIÓN C] Por qué cada cosa está donde está — 5 decisiones de ubicación por capa
18. [DECISIÓN D] Lo que decidimos no hacer — 4 sacrificios deliberados
19. Cierre (oscura) — pediste → cumplimos

## Narrativa por capa
- **Presentación**: aislamiento de datos multitenant → Controller + Polymorphism
- **Aplicación**: alta disponibilidad → Facade + Chain of Responsibility
- **Dominio**: trazabilidad en tiempo real → State + Observer + Memento
- **Persistencia**: eficiencia de respuesta → Repository + Data Mapper + Proxy + UoW
- **Infraestructura**: interoperabilidad + elasticidad → Adapter + Abstract Factory + Flyweight

## Interacciones (deck.js)
1. Tarjetas expandibles (`.pcard`): click abre `.pcard-body`, colapsa hermanas
2. Comparador (`.cmp-tab` / `.cmp-panel`): tabs con `.active`
3. Diagrama de secuencia (#seq-root): generado por JS, 8 lifelines, 10 mensajes,
   botones Atrás/Siguiente/Reset + dots de progreso

## Sistema visual (deck.css)
- Tipografía: Space Grotesk (títulos) / IBM Plex Sans (cuerpo) / IBM Plex Mono (mono)
- Acento: violeta-índigo eléctrico oklch(0.53 0.245 276)
- Triada categorías: naranja (creacional) / cian (estructural) / verde (comportamiento)
- Escala 1920×1080, piso 24px

## Reglas de CSS a respetar (bugs ya corregidos, no reintroducir)
- NO usar `transition: <tiempo>` pelado (= `transition: all`) en elementos dentro
  de un slide — captura el visibility hidden→visible de deck-stage y los congela.
  Usar siempre listas explícitas de propiedades.
- Animaciones del cover (.anim/.anim-2/.anim-3): base con `opacity:1`, animación
  solo por `transform` (keyframe riseT), para que el título no quede invisible.

## Archivos
- `LogiSmart Arquitectura.html` — markup de las 17 slides (deck canónico)
- `deck.css` — sistema visual + todos los componentes nuevos (business-led reframe)
- `deck.js` — 3 interacciones (cards, comparador, secuencia) — sin cambios
- `deck-stage.js` — web component contenedor (escala, navegación, print) — sin cambios
