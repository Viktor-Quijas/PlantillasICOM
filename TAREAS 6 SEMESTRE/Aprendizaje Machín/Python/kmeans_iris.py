"""
K-means sobre el dataset Iris con 6 pares de atributos.

Por cada par:
  - Se anima el algoritmo k-means iteración por iteración (tiempo real).
  - Al converger se muestran el coeficiente de silueta y la inercia.
  - El programa espera un input (Enter) para pasar al siguiente par.
Al final se muestra una gráfica de codo (una por par) con el número de
centroides sugerido.

Uso:
    python kmeans_iris.py [ruta_al_csv]

Requisitos: pandas, numpy, matplotlib, scikit-learn
"""

import sys

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn.metrics import silhouette_score

# --------------------------------------------------------------------------
# Configuración
# --------------------------------------------------------------------------
CSV_PATH = sys.argv[1] if len(sys.argv) > 1 else "iris.csv"
K_ANIMACION = 3          # nº de centroides para la animación (Iris tiene 3 especies)
K_MAX_CODO = 10          # máximo k para la gráfica de codo
PAUSA = 0.6              # segundos entre iteraciones de la animación
MAX_ITER = 100
SEMILLA = 42

# 6 pares distintos (incluye inversos: X,Y y luego Y,X)
PARES = [
    ("petal.length", "petal.width"),
    ("petal.width", "petal.length"),     # inverso del anterior
    ("sepal.length", "sepal.width"),
    ("sepal.width", "sepal.length"),     # inverso del anterior
    ("sepal.length", "petal.length"),
    ("sepal.width", "petal.width"),
]

COLORES = ["tab:blue", "tab:orange", "tab:green", "tab:red", "tab:purple",
           "tab:brown", "tab:pink", "tab:gray", "tab:olive", "tab:cyan"]


# --------------------------------------------------------------------------
# K-means implementado paso a paso (para poder animarlo)
# --------------------------------------------------------------------------
def inicializar_kmeanspp(X, k, rng):
    """Inicialización k-means++ para centroides iniciales más estables."""
    centros = [X[rng.integers(len(X))]]
    for _ in range(1, k):
        d2 = np.min([np.sum((X - c) ** 2, axis=1) for c in centros], axis=0)
        if d2.sum() == 0:                       # todos los puntos coinciden
            centros.append(X[rng.integers(len(X))])
            continue
        probs = d2 / d2.sum()
        centros.append(X[rng.choice(len(X), p=probs)])
    return np.array(centros, dtype=float)


def asignar(X, centros):
    d2 = ((X[:, None, :] - centros[None, :, :]) ** 2).sum(axis=2)
    return d2.argmin(axis=1)


def actualizar(X, etiquetas, centros, rng):
    nuevos = centros.copy()
    for j in range(len(centros)):
        miembros = X[etiquetas == j]
        if len(miembros) > 0:
            nuevos[j] = miembros.mean(axis=0)
        else:                                    # cluster vacío: reubicar
            nuevos[j] = X[rng.integers(len(X))]
    return nuevos


def calcular_inercia(X, etiquetas, centros):
    return float(((X - centros[etiquetas]) ** 2).sum())


def kmeans_pasos(X, k, rng, max_iter=MAX_ITER):
    """Generador: produce (iteración, etiquetas, centros, inercia) en cada paso."""
    centros = inicializar_kmeanspp(X, k, rng)
    etiquetas = asignar(X, centros)
    yield 0, etiquetas, centros, calcular_inercia(X, etiquetas, centros)

    for it in range(1, max_iter + 1):
        nuevos = actualizar(X, etiquetas, centros, rng)
        nuevas_et = asignar(X, nuevos)
        convergio = np.allclose(nuevos, centros) and np.array_equal(nuevas_et, etiquetas)
        centros, etiquetas = nuevos, nuevas_et
        yield it, etiquetas, centros, calcular_inercia(X, etiquetas, centros)
        if convergio:
            return


def kmeans_final(X, k, n_init=10, seed=SEMILLA):
    """Mejor de n_init ejecuciones (menor inercia); se usa para el codo."""
    mejor = None
    for s in range(n_init):
        rng = np.random.default_rng(seed + s)
        for _, et, c, ine in kmeans_pasos(X, k, rng):
            pass
        if mejor is None or ine < mejor[0]:
            mejor = (ine, et, c)
    return mejor


# --------------------------------------------------------------------------
# Animación en tiempo real
# --------------------------------------------------------------------------
def animar_par(X, nombres, k, idx_par, total):
    rng = np.random.default_rng(SEMILLA)
    fig, ax = plt.subplots(figsize=(7, 5.5))
    fig.canvas.manager.set_window_title(f"K-means - par {idx_par}/{total}")

    ultimo = None
    for it, etiquetas, centros, inercia in kmeans_pasos(X, k, rng):
        ax.clear()
        for j in range(k):
            m = etiquetas == j
            ax.scatter(X[m, 0], X[m, 1], s=35, color=COLORES[j], alpha=0.75,
                       label=f"Cluster {j + 1}")
        ax.scatter(centros[:, 0], centros[:, 1], marker="X", s=260, c="black",
                   edgecolors="white", linewidths=1.5, label="Centroides", zorder=5)
        ax.set_xlabel(nombres[0])
        ax.set_ylabel(nombres[1])
        ax.set_title(f"Par {idx_par}/{total}: {nombres[0]} vs {nombres[1]}\n"
                     f"Iteración {it} | Inercia = {inercia:.4f}")
        ax.legend(loc="best", fontsize=8)
        ax.grid(alpha=0.3)
        fig.canvas.draw_idle()
        plt.pause(PAUSA)
        ultimo = (etiquetas, centros, inercia, it)

    etiquetas, centros, inercia, it = ultimo
    sil = silhouette_score(X, etiquetas) if len(np.unique(etiquetas)) > 1 else float("nan")
    ax.set_title(f"Par {idx_par}/{total}: {nombres[0]} vs {nombres[1]}\n"
                 f"CONVERGIÓ en {it} iteraciones | Inercia = {inercia:.4f} | "
                 f"Silueta = {sil:.4f}")
    fig.canvas.draw_idle()
    plt.pause(0.1)
    return fig, sil, inercia, it


# --------------------------------------------------------------------------
# Gráfica de codo
# --------------------------------------------------------------------------
def detectar_codo(ks, inercias):
    """Punto de mayor distancia a la recta que une el primer y último punto
    de la curva (inercia normalizada). Devuelve el k sugerido."""
    x = (np.array(ks) - ks[0]) / (ks[-1] - ks[0])
    y = (np.array(inercias) - min(inercias)) / (max(inercias) - min(inercias))
    # distancia de cada punto a la recta (0,1)-(1,0): |x + y - 1| / sqrt(2)
    dist = np.abs(x + y - 1) / np.sqrt(2)
    return ks[int(np.argmax(dist))]


def grafica_codo(datos_pares):
    ks = list(range(1, K_MAX_CODO + 1))
    fig, axes = plt.subplots(2, 3, figsize=(15, 8))
    fig.suptitle("Método del codo: inercia vs número de centroides (k)", fontsize=14)
    resumen = []

    for ax, (nombres, X) in zip(axes.ravel(), datos_pares):
        inercias = [kmeans_final(X, k)[0] for k in ks]
        k_sug = detectar_codo(ks, inercias)
        resumen.append((nombres, k_sug))

        ax.plot(ks, inercias, "o-", color="tab:blue")
        ax.axvline(k_sug, color="tab:red", linestyle="--", label=f"Codo: k = {k_sug}")
        ax.scatter([k_sug], [inercias[k_sug - 1]], s=150, color="tab:red", zorder=5)
        ax.set_title(f"{nombres[0]} vs {nombres[1]}")
        ax.set_xlabel("Número de centroides (k)")
        ax.set_ylabel("Inercia")
        ax.set_xticks(ks)
        ax.grid(alpha=0.3)
        ax.legend()

    fig.tight_layout(rect=[0, 0, 1, 0.95])
    return fig, resumen


# --------------------------------------------------------------------------
# Programa principal
# --------------------------------------------------------------------------
def main():
    df = pd.read_csv(CSV_PATH)
    plt.ion()

    resultados = []
    datos_pares = []
    total = len(PARES)

    for i, (a, b) in enumerate(PARES, start=1):
        X = df[[a, b]].to_numpy(dtype=float)
        datos_pares.append(((a, b), X))

        fig, sil, inercia, it = animar_par(X, (a, b), K_ANIMACION, i, total)
        resultados.append((a, b, sil, inercia, it))

        print(f"\n--- Par {i}/{total}: {a} vs {b} (k = {K_ANIMACION}) ---")
        print(f"Iteraciones hasta converger : {it}")
        print(f"Coeficiente de silueta      : {sil:.4f}")
        print(f"Inercia                     : {inercia:.4f}")

        if i < total:
            input("Presiona Enter para continuar con el siguiente par... ")
        else:
            input("Presiona Enter para generar la gráfica de codo... ")
        plt.close(fig)

    # Resumen en consola
    print("\n================ RESUMEN ================")
    print(f"{'Par':<32}{'Silueta':>10}{'Inercia':>12}{'Iter':>6}")
    for a, b, sil, ine, it in resultados:
        print(f"{a + ' vs ' + b:<32}{sil:>10.4f}{ine:>12.4f}{it:>6}")

    # Gráfica de codo
    fig_codo, resumen = grafica_codo(datos_pares)
    print("\n=========== CODO (k sugerido) ===========")
    for (a, b), k in resumen:
        print(f"{a} vs {b}: k = {k}")

    plt.ioff()
    plt.show()


if __name__ == "__main__":
    main()
