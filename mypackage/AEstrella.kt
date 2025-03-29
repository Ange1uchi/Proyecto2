package mypackage

import java.util.*

// A* con entrada de DAG y pesos reales
class AEstrella {
    data class Nodo(val id: String, var g: Int = Int.MAX_VALUE, var h: Int = 0, var f: Int = Int.MAX_VALUE, var padre: Nodo? = null)

    fun heuristica(nodo: Nodo, objetivo: Nodo): Int {
        return 0 // heurística trivial por ahora
    }

    fun calcularRuta(grafo: Map<String, List<Pair<String, Int>>>, origenId: String, destinoId: String): List<String> {
        val nodos = grafo.keys.union(grafo.values.flatten().map { it.first }).associateWith { Nodo(it) }
        val origen = nodos[origenId]!!
        val destino = nodos[destinoId]!!

        val abiertos = PriorityQueue(compareBy<Nodo> { it.f })
        val cerrados = mutableSetOf<Nodo>()

        origen.g = 0
        origen.h = heuristica(origen, destino)
        origen.f = origen.g + origen.h
        abiertos.add(origen)

        while (abiertos.isNotEmpty()) {
            val actual = abiertos.poll()
            if (actual == destino) break
            cerrados.add(actual)

            for ((vecinoId, peso) in grafo[actual.id].orEmpty()) {
                val vecino = nodos[vecinoId]!!
                if (vecino in cerrados) continue
                val tentativeG = actual.g + peso
                if (tentativeG < vecino.g) {
                    vecino.padre = actual
                    vecino.g = tentativeG
                    vecino.h = heuristica(vecino, destino)
                    vecino.f = vecino.g + vecino.h
                    abiertos.add(vecino)
                }
            }
        }

        val ruta = mutableListOf<String>()
        var actual: Nodo? = destino
        while (actual != null) {
            ruta.add(actual.id)
            actual = actual.padre
        }
        return ruta.reversed()
    }
}
