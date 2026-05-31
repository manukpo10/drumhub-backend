# Generates V15__seed_varied_grooves.sql with guaranteed-correct pattern arrays.
# Invariants enforced at build time:
#   - every active piece must belong to the groove's kit (else it would synth-fallback)
#   - each pattern array length == stepsPerBar * bars
# Writes the file directly as UTF-8 (Flyway reads migrations as UTF-8).
# Run: python scripts/gen_v15_seed.py

import json
import io

OUT_PATH = 'src/main/resources/db/migration/V15__seed_varied_grooves.sql'
_buf = io.StringIO()
_real_print = print


def print(*args, **kwargs):  # noqa: A001 — capture output into the UTF-8 buffer
    kwargs['file'] = _buf
    _real_print(*args, **kwargs)

# stepsPerBar per time signature (mirrors frontend DH.TIME_SIGS)
STEPS_PER_BAR = {'2/4': 8, '4/4': 16, '3/4': 12, '5/4': 20, '6/8': 12, '7/8': 14}

# Pieces each kit actually has samples for (mirrors audio.js KITS).
# china has no samples in any kit, so it is never used here.
KIT_PIECES = {
    'pearl': {'crash', 'splash', 'ride', 'ride_bell', 'hihat', 'hihat_open',
              'snare', 'tom1', 'tom2', 'tom3', 'kick'},
    'tr909': {'kick', 'snare', 'hihat', 'hihat_open', 'ride', 'crash', 'tom1', 'tom2', 'clap'},
    'lm2':   {'kick', 'snare', 'hihat', 'hihat_open', 'ride', 'crash', 'tom1', 'tom2', 'tom3',
              'clap', 'conga', 'cowbell', 'cabasa', 'tamb', 'stick'},
    'cr78':  {'kick', 'snare', 'hihat', 'hihat_open', 'stick'},
}

# Full ordered key list stored in the pattern JSON (standard 12 + extras), matching V14.
ALL_KEYS = ['china', 'crash', 'splash', 'ride', 'ride_bell', 'hihat', 'hihat_open',
            'snare', 'tom1', 'tom2', 'tom3', 'kick', 'clap', 'stick',
            'cowbell', 'cabasa', 'tamb', 'conga']


def build_pattern(kit, time_sig, bars, hits):
    """hits: {piece: [step indices]}. Returns ordered JSON string of full pattern."""
    total = STEPS_PER_BAR[time_sig] * bars
    allowed = KIT_PIECES[kit]
    pat = {}
    used_keys = set()
    for piece, steps in hits.items():
        if steps and piece not in allowed:
            raise ValueError(f"[{kit}] piece '{piece}' not in kit ({sorted(allowed)})")
        used_keys.add(piece)
        arr = [0] * total
        for s in steps:
            if s < 0 or s >= total:
                raise ValueError(f"step {s} out of range 0..{total-1} for {time_sig} x{bars}")
            arr[s] = 1
        pat[piece] = arr
    # Emit every key that this kit has pieces for (zero-filled if unused), plus standard rows.
    keys = [k for k in ALL_KEYS if (k in allowed or k in ('china', 'crash', 'splash', 'ride',
            'ride_bell', 'hihat', 'hihat_open', 'snare', 'tom1', 'tom2', 'tom3', 'kick'))]
    out = {}
    for k in keys:
        out[k] = pat.get(k, [0] * total)
    return json.dumps(out, separators=(',', ':'))


def esc(s):
    return s.replace("'", "''")


# ── Groove definitions ──────────────────────────────────────────────────────
# Each: slug, title, author_id, genre_slug, bpm, level, tags[], desc, kit, time_sig, bars, hits
GROOVES = [
    # ---- PEARL (full acoustic kit) ----
    dict(slug='rock-epico-4-compases', title='Rock Épico 4 Compases', author=200, genre='rock',
         bpm=128, level='Avanzado', kit='pearl', time_sig='4/4', bars=4,
         tags=['rock', '4 compases', 'fill', 'épico', 'estadio'],
         desc='Cuatro compases de rock de estadio. Tres compases de groove sólido y un cuarto que estalla en un fill de toms cerrando con crash.',
         hits={
             'crash':  [0, 56],
             'hihat':  [0,2,4,6,8,10,12,14, 16,18,20,22,24,26,28,30, 32,34,36,38,40,42,44,46],
             'snare':  [4,12, 20,28, 36,44, 52],
             'kick':   [0,8, 16,24, 32,40, 48],
             'tom1':   [58, 62],
             'tom2':   [59, 60],
             'tom3':   [61, 63],
         }),
    dict(slug='latin-songo-extendido', title='Latin Songo', author=203, genre='latin',
         bpm=104, level='Avanzado', kit='pearl', time_sig='4/4', bars=2,
         tags=['latin', 'songo', 'cuba', 'clave', 'síncopa'],
         desc='El songo cubano de Changuito llevado al kit. Clave de son en el aro, bombo sincopado y ride constante. Puro sabor caribeño.',
         hits={
             'crash':  [0],
             'ride':   [0,2,4,6,8,10,12,14, 16,18,20,22,24,26,28,30],
             'snare':  [3,6, 10, 19,22, 26],
             'kick':   [4,7, 12, 20,23, 28],
             'tom2':   [14, 30],
         }),
    dict(slug='jazz-swing-4-compases', title='Jazz Swing 4 Compases', author=202, genre='jazz',
         bpm=168, level='Avanzado', kit='pearl', time_sig='4/4', bars=4,
         tags=['jazz', 'swing', 'ride', '4 compases', 'comping'],
         desc='Cuatro compases de swing con el clásico patrón de ride spang-a-lang, hi-hat en 2 y 4, y comping libre de redoblante y bombo.',
         hits={
             'ride':      [0,4,6, 8,12,14, 16,20,22, 24,28,30, 32,36,38, 40,44,46, 48,52,54, 56,60,62],
             'hihat':     [4,12, 20,28, 36,44, 52,60],
             'snare':     [10, 26, 38, 54, 58],
             'kick':      [0, 16, 32, 48],
         }),
    dict(slug='metal-doble-bombo-5-4', title='Metal Doble Bombo 5/4', author=207, genre='metal',
         bpm=150, level='Avanzado', kit='pearl', time_sig='5/4', bars=2,
         tags=['metal', '5/4', 'doble bombo', 'odd time', 'progresivo'],
         desc='Dos compases de 5/4 con doble bombo en semicorcheas. Agrupación 3+2, crash al inicio de cada compás y china... digo, ride marcando.',
         hits={
             'crash':  [0, 20],
             'ride':   [0,2,4,6,8,10,12,14,16,18, 20,22,24,26,28,30,32,34,36,38],
             'snare':  [4,12, 16, 24,32, 36],
             'kick':   [0,1,2,3, 8,9, 12,13, 20,21,22,23, 28,29, 32,33],
             'tom1':   [18],
             'tom2':   [38],
         }),
    dict(slug='blues-shuffle-6-8-largo', title='Shuffle de Blues 6/8', author=205, genre='blues',
         bpm=76, level='Intermedio', kit='pearl', time_sig='6/8', bars=2,
         tags=['blues', '6/8', 'shuffle', 'slow', 'feeling'],
         desc='Dos compases de shuffle lento en 6/8. El ride con el balanceo de tresillos, el bombo en 1 y el redoblante en 4 sosteniendo el llanto del blues.',
         hits={
             'ride':   [0,2,4, 6,8,10, 12,14,16, 18,20,22],
             'hihat':  [],
             'snare':  [6, 18],
             'kick':   [0, 9, 12, 21],
             'crash':  [0],
         }),
    dict(slug='reggae-one-drop-extendido', title='One Drop Extendido', author=203, genre='reggae',
         bpm=74, level='Intermedio', kit='pearl', time_sig='4/4', bars=2,
         tags=['reggae', 'one drop', 'jamaica', 'rim', 'skank'],
         desc='El one drop clásico en dos compases. El bombo y el redoblante caen juntos en el 3, dejando el 1 vacío. Hi-hat en corcheas marcando el skank.',
         hits={
             'hihat':  [0,2,4,6,8,10,12,14, 16,18,20,22,24,26,28,30],
             'snare':  [8, 24],
             'kick':   [8, 24],
             'crash':  [0],
         }),
    dict(slug='vals-jazz-3-4-largo', title='Vals Jazz 3/4 Largo', author=202, genre='jazz',
         bpm=160, level='Avanzado', kit='pearl', time_sig='3/4', bars=4,
         tags=['jazz', 'vals', '3/4', 'swing', '4 compases'],
         desc='Cuatro compases de vals de jazz. El ride pinta el tres por cuatro con swing, el pie marca el 2 y el redoblante hace comping a lo largo de la frase.',
         hits={
             'ride':      [0,2,5, 6,8,11, 12,14,17, 18,20,23, 24,26,29, 30,32,35, 36,38,41, 42,44,47],
             'hihat':     [4, 10, 16, 22, 28, 34, 40, 46],
             'snare':     [7, 15, 26, 38, 44],
             'kick':      [0, 18, 36],
             'crash':     [0],
         }),
    dict(slug='prog-7-8-doble', title='Prog 7/8 Doble', author=207, genre='metal',
         bpm=155, level='Avanzado', kit='pearl', time_sig='7/8', bars=2,
         tags=['metal', '7/8', 'progresivo', 'odd time', 'técnica'],
         desc='Dos compases de 7/8 con agrupación 2+2+3. El bombo abre cada grupo, el redoblante cae en las junturas y el ride cierra cada compás.',
         hits={
             'crash':  [0, 14],
             'hihat':  [0,2,4,6,8,10,12, 14,16,18,20,22,24,26],
             'snare':  [2,6, 16,20],
             'kick':   [0,4,8, 14,18,22],
             'ride':   [12, 26],
         }),
    dict(slug='marcha-rock-2-4', title='Marcha Rock 2/4', author=200, genre='rock',
         bpm=120, level='Básico', kit='pearl', time_sig='2/4', bars=4,
         tags=['rock', '2/4', 'marcha', 'directo', 'punk'],
         desc='Cuatro compases de 2/4 directo y marcial. Bombo en 1, redoblante en 2, hi-hat en corcheas. Simple, contundente, perfecto para punk.',
         hits={
             'hihat':  [0,2,4,6, 8,10,12,14, 16,18,20,22, 24,26,28,30],
             'snare':  [4, 12, 20, 28],
             'kick':   [0, 8, 16, 24],
             'crash':  [0],
         }),

    # ---- TR-909 (electronic) ----
    dict(slug='techno-hipnotico-4-compases', title='Techno Hipnótico 4 Compases', author=206, genre='trap',
         bpm=132, level='Intermedio', kit='tr909', time_sig='4/4', bars=4,
         tags=['techno', 'tr909', '4 compases', 'hipnótico', 'electrónica'],
         desc='Cuatro compases de techno en bucle. Four-on-the-floor inquebrantable, hi-hats en contratiempo y un clap que entra en el segundo compás para sumar tensión.',
         hits={
             'crash':  [0],
             'hihat':  [2,6,10,14, 18,22,26,30, 34,38,42,46, 50,54,58,62],
             'hihat_open': [7, 23, 39, 55],
             'kick':   [0,4,8,12, 16,20,24,28, 32,36,40,44, 48,52,56,60],
             'clap':   [20,28, 36,44, 52,60],
         }),
    dict(slug='electro-funk-909', title='Electro Funk 909', author=206, genre='funk',
         bpm=110, level='Intermedio', kit='tr909', time_sig='4/4', bars=2,
         tags=['electro', 'funk', 'tr909', 'síncopa', '80s'],
         desc='El electro-funk de los 80s con el 909. Bombo sincopado estilo Egyptian Lover, clap en el backbeat y toms electrónicos cerrando la frase.',
         hits={
             'crash':  [0],
             'hihat':  [0,2,4,6,8,10,12,14, 16,18,20,22,24,26,28,30],
             'clap':   [4,12, 20,28],
             'kick':   [0,3,6, 10, 16,19, 24],
             'tom1':   [14],
             'tom2':   [30, 31],
         }),
    dict(slug='breakbeat-909', title='Breakbeat 909', author=206, genre='pop',
         bpm=140, level='Avanzado', kit='tr909', time_sig='4/4', bars=2,
         tags=['breakbeat', 'tr909', 'broken beat', 'rave', 'electrónica'],
         desc='Un breakbeat roto con el 909. El bombo y el clap se desplazan del grid esperado, las semicorcheas de hi-hat empujan el groove hacia adelante.',
         hits={
             'hihat':  [0,2,3,4,6,7,8,10,11,12,14,15, 16,18,19,20,22,23,24,26,27,28,30,31],
             'clap':   [4, 14, 20, 26],
             'kick':   [0,7, 11, 16,22, 25],
             'crash':  [0],
             'tom1':   [30],
         }),

    # ---- LM-2 (LinnDrum, rich) ----
    dict(slug='synthpop-linndrum-4-compases', title='Synthpop LinnDrum 4 Compases', author=209, genre='pop',
         bpm=116, level='Intermedio', kit='lm2', time_sig='4/4', bars=4,
         tags=['synthpop', 'linndrum', 'lm2', '80s', '4 compases'],
         desc='Cuatro compases de synthpop ochentoso. El LM-2 con su clap gigante, cowbell marcando el pulso y un tamb que aparece en el último compás.',
         hits={
             'crash':  [0],
             'hihat':  [0,2,4,6,8,10,12,14, 16,18,20,22,24,26,28,30, 32,34,36,38,40,42,44,46, 48,50,52,54,56,58,60,62],
             'clap':   [4,12, 20,28, 36,44, 52,60],
             'kick':   [0,10, 16,26, 32,42, 48,58],
             'cowbell':[0,8, 16,24, 32,40, 48,56],
             'tamb':   [48,52,56,60],
         }),
    dict(slug='new-jack-swing-lm2', title='New Jack Swing LM-2', author=209, genre='r-and-b',
         bpm=108, level='Avanzado', kit='lm2', time_sig='4/4', bars=2,
         tags=['new jack swing', 'lm2', 'r&b', 'swing', 'teddy riley'],
         desc='El new jack swing de Teddy Riley con el LM-2. Semicorcheas con swing, clap apilado al redoblante y congas llenando los huecos.',
         hits={
             'hihat':  [0,2,4,6,8,10,12,14, 16,18,20,22,24,26,28,30],
             'snare':  [4, 12, 20, 28],
             'clap':   [4, 12, 20, 28],
             'kick':   [0,3, 7, 10, 16,19, 23, 26],
             'conga':  [6,14, 22,30],
             'crash':  [0],
         }),
    dict(slug='funk-pesado-lm2', title='Funk Pesado LM-2', author=209, genre='funk',
         bpm=98, level='Avanzado', kit='lm2', time_sig='4/4', bars=2,
         tags=['funk', 'lm2', 'pesado', 'conga', 'cabasa'],
         desc='Funk denso con el LinnDrum. Hi-hats en semicorcheas, ghost notes de redoblante, congas y cabasa tejiendo una percusión hipnótica.',
         hits={
             'hihat':  [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15, 16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
             'snare':  [4, 12, 20, 28],
             'kick':   [0,6, 10, 16,22, 26],
             'conga':  [2, 9, 18, 25],
             'cabasa': [0,4,8,12, 16,20,24,28],
             'crash':  [0],
         }),

    # ---- CR-78 (sparse vintage) ----
    dict(slug='bossa-vintage-cr78-3-4', title='Bossa Vintage CR-78 3/4', author=208, genre='bossa',
         bpm=120, level='Básico', kit='cr78', time_sig='3/4', bars=2,
         tags=['bossa', 'cr78', '3/4', 'vintage', 'preset'],
         desc='Dos compases de bossa en 3/4 con el CR-78. El stick hace el "um-pa-pa" del vals, el hi-hat abierto respira y el bombo ancla cada compás.',
         hits={
             'hihat':      [0,2,4, 6,8,10, 12,14,16, 18,20,22],
             'hihat_open': [4, 10, 16, 22],
             'snare':      [4, 16],
             'kick':       [0, 8, 12, 20],
             'stick':      [2,6, 14,18],
         }),
    dict(slug='pop-suave-cr78', title='Pop Suave CR-78', author=208, genre='pop',
         bpm=104, level='Básico', kit='cr78', time_sig='4/4', bars=2,
         tags=['pop', 'cr78', 'suave', 'vintage', 'balada'],
         desc='El preset suave del CR-78 que sonaba en los órganos caseros de los 70s. Bombo y snare simples, hi-hat constante y stick adornando los contratiempos.',
         hits={
             'hihat':      [0,2,4,6,8,10,12,14, 16,18,20,22,24,26,28,30],
             'hihat_open': [14, 30],
             'snare':      [4, 12, 20, 28],
             'kick':       [0, 8, 16, 24],
             'stick':      [6, 22],
         }),
]

# Date stagger: spread across ~80 days, oldest first.
DAYS = [80, 75, 70, 64, 58, 52, 46, 40, 35, 30, 25, 20, 16, 12, 9, 6, 3]

print("-- V15: Seed varied grooves — 17 grooves across all 4 kits, longer patterns (2-4 bars),")
print("-- and every time signature (2/4, 4/4, 3/4, 5/4, 6/8, 7/8).")
print("-- Authors: 200 matias_drum, 202 carovillareal, 203 pablogroove, 205 juanmabaqueta,")
print("--          206 martinr909, 207 diegopercusion, 208 valentinasoul, 209 andrealm2")
print("-- Every active piece validated against its kit; arrays = stepsPerBar x bars.")
print()
print("INSERT INTO grooves (slug, title, author_id, genre_id, bpm, level, likes, plays, featured, tags, description, pattern, time_sig, bars, kit)")
print("VALUES")

rows = []
for g in GROOVES:
    pat = build_pattern(g['kit'], g['time_sig'], g['bars'], g['hits'])
    tags = json.dumps(g['tags'], ensure_ascii=False)
    row = (
        f"(\n"
        f"  '{g['slug']}',\n"
        f"  '{esc(g['title'])}',\n"
        f"  {g['author']},\n"
        f"  (SELECT id FROM genres WHERE slug = '{g['genre']}'),\n"
        f"  {g['bpm']}, '{g['level']}', 0, 0, FALSE,\n"
        f"  '{esc(tags)}',\n"
        f"  '{esc(g['desc'])}',\n"
        f"  '{pat}',\n"
        f"  '{g['time_sig']}', {g['bars']}, '{g['kit']}'\n"
        f")"
    )
    rows.append(row)

print(",\n".join(rows) + ";")
print()
print("-- Date stagger for a natural feed order")
for g, d in zip(GROOVES, DAYS):
    print(f"UPDATE grooves SET created_at = CURRENT_TIMESTAMP - INTERVAL '{d}' DAY, "
          f"updated_at = CURRENT_TIMESTAMP - INTERVAL '{d}' DAY WHERE slug = '{g['slug']}';")

with open(OUT_PATH, 'w', encoding='utf-8', newline='\n') as f:
    f.write(_buf.getvalue())
_real_print(f"Wrote {len(GROOVES)} grooves to {OUT_PATH} (UTF-8)")
