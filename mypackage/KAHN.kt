package mypackage

import java.io.File
import java.util.ArrayDeque

class KAHN(private val rutaArchivo: String) {

    private val grafo: MutableMap<Int, MutableList<Int>> = mutableMapOf()

    init {
        cargarGrafoDesdeArchivo()
    }

    private fun cargarGrafoDesdeArchivo() {
        val lineas = File(rutaArchivo).readLines()
        for (linea in lineas.drop(1)) {
            val partes = linea.trim().split(" ")
            if (partes.size >= 2) {
                val desde = partes[0].toInt()
                val hasta = partes[1].toInt()
                grafo.getOrPut(desde) { mutableListOf() }.add(hasta)
                grafo.putIfAbsent(hasta, mutableListOf()) // Asegura todos los nodos
            }
        }
    }

    fun ejecutarOrdenTopologico(): List<Int> {
        val inDegree = mutableMapOf<Int, Int>()

        for ((nodo, sucesores) in grafo) {
            inDegree.putIfAbsent(nodo, 0)
            for (sucesor in sucesores) {
                inDegree[sucesor] = inDegree.getOrDefault(sucesor, 0) + 1
            }
        }

        val queue = ArrayDeque<Int>()
        for ((nodo, grado) in inDegree) {
            if (grado == 0) queue.add(nodo)
        }

        val resultado = mutableListOf<Int>()
        while (queue.isNotEmpty()) {
            val nodo = queue.removeFirst()
            resultado.add(nodo)

            for (sucesor in grafo[nodo].orEmpty()) {
                inDegree[sucesor] = inDegree.getOrDefault(sucesor, 0) - 1
                if (inDegree[sucesor] == 0) {
                    queue.add(sucesor)
                }
            }
        }

        if (resultado.size != grafo.size) {
            error("El grafo contiene ciclos. No se puede hacer orden topológico.")
        }

        return resultado
    }
}
