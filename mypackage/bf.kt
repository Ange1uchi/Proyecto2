package mypackage

import java.io.File

class WeightedGraph(val vertices: Int) {
    val adjacencyList = Array(vertices) { mutableListOf<Edge>() }

    fun addEdge(source: Int, dest: Int, weight: Int) {
        adjacencyList[source].add(Edge(source, dest, weight))
    }
}

class bf{

    // Función para leer el grafo desde un archivo
    fun leerGrafoDesdeArchivo(filePath: String): WeightedGraph {
        val file = File(filePath)

        if (!file.exists()) {
            println("ERROR: El archivo '$filePath' no se encontró.")
            return WeightedGraph(0)
        }

        val lines = file.readLines()
        if (lines.isEmpty()) {
            println("ERROR: El archivo '$filePath' está vacío.")
            return WeightedGraph(0)
        }

        val numVertices = lines[0].trim().toIntOrNull() ?: return WeightedGraph(0)
        val graph = WeightedGraph(numVertices)

        for (i in 1 until lines.size) {
            val values = lines[i].trim().split(" ").mapNotNull { it.toIntOrNull() }
            if (values.size == 3) {
                val (v1, v2, peso) = values
                graph.addEdge(v1, v2, peso)  // Si es dirigido, solo se añade en una dirección
            } else {
                println("ADVERTENCIA: Línea inválida en el archivo: '${lines[i]}'. Se ignorará.")
            }
        }

        return graph
    }

    // Implementación del algoritmo de Bellman-Ford
    fun ejecutarBellmanFord(graph: WeightedGraph, source: Int) {
        val distances = IntArray(graph.vertices) { Int.MAX_VALUE }
        distances[source] = 0

        for (i in 1 until graph.vertices) {
            for (u in 0 until graph.vertices) {
                for (edge in graph.adjacencyList[u]) {
                    val v = edge.dest
                    val weight = edge.weight

                    if (distances[u] != Int.MAX_VALUE && distances[u] + weight < distances[v]) {
                        distances[v] = distances[u] + weight
                    }
                }
            }
        }

        // Verificación de ciclos de peso negativo
        for (u in 0 until graph.vertices) {
            for (edge in graph.adjacencyList[u]) {
                val v = edge.dest
                val weight = edge.weight

                if (distances[u] != Int.MAX_VALUE && distances[u] + weight < distances[v]) {
                    println("Se detectó un ciclo de peso negativo en el grafo.")
                    return
                }
            }
        }

        // ✅ Ajuste en la salida: Sumamos 1 a los nodos para que coincidan con la numeración del grafo
        println("Resultados del Algoritmo Bellman-Ford:")
        for (i in distances.indices) {
            println("Distancia más corta desde el nodo V${source + 1} hasta el nodo V${i + 1}: ${if (distances[i] == Int.MAX_VALUE) "INF" else distances[i]}")
        }
    }

    // Función para ejecutar Bellman-Ford leyendo desde un archivo
    fun ejecutarDesdeArchivo(filePath: String) {
        val graph = leerGrafoDesdeArchivo(filePath)

        if (graph.vertices > 0) {
            ejecutarBellmanFord(graph, 0)  // Ejecuta Bellman-Ford desde el nodo 0
        } else {
            println("No se pudo ejecutar Bellman-Ford debido a un error en el archivo de entrada.")
        }
    }
}
