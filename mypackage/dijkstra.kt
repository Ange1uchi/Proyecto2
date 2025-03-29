package mypackage

import java.io.File
import java.util.*

class dijkstra {

    // Función para leer el grafo desde un archivo y convertirlo a la estructura adecuada
    fun leerGrafoDesdeArchivo(filePath: String): Triple<Int, Int, Array<Array<Pair<Int, Int>>>> {
        val file = File(filePath)

        if (!file.exists()) {
            println("ERROR: El archivo '$filePath' no se encontró.")
            return Triple(0, 0, arrayOf())
        }

        val lines = file.readLines()
        if (lines.isEmpty()) {
            println("ERROR: El archivo '$filePath' está vacío.")
            return Triple(0, 0, arrayOf())
        }

        val numVertices = lines[0].trim().toIntOrNull() ?: return Triple(0, 0, arrayOf())
        val graph = Array(numVertices) { mutableListOf<Pair<Int, Int>>() }

        for (i in 1 until lines.size) {
            val values = lines[i].trim().split(" ").mapNotNull { it.toIntOrNull() }
            if (values.size == 3) {
                val (v1, v2, peso) = values
                graph[v1].add(Pair(v2, peso))
                graph[v2].add(Pair(v1, peso))  // Si el grafo es dirigido, elimina esta línea.
            } else {
                println("ADVERTENCIA: Línea inválida en el archivo: '${lines[i]}'. Se ignorará.")
            }
        }

        return Triple(numVertices, numVertices - 1, graph.map { it.toTypedArray() }.toTypedArray())
    }

    // Implementación del algoritmo de Dijkstra
    fun ejecutarDijkstra(start: Int, end: Int, graph: Array<Array<Pair<Int, Int>>>) {
        val n = graph.size
        val dist = IntArray(n) { Int.MAX_VALUE }
        val prev = IntArray(n) { -1 }
        dist[start] = 0
        val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
        pq.offer(Pair(start, 0))

        while (!pq.isEmpty()) {
            val (u, d) = pq.poll()
            if (u == end) break
            if (d > dist[u]) continue
            for ((v, w) in graph[u]) {
                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w
                    prev[v] = u
                    pq.offer(Pair(v, dist[v]))
                }
            }
        }

        val path = mutableListOf<Int>()
        var current = end
        while (current != -1) {
            path.add(current)
            current = prev[current]
        }
        path.reverse()

        // Ajuste en la salida: Sumar 1 a cada nodo para que coincida con la numeración del grafo
        println("El camino más corto desde V${start + 1} a V${end + 1} usando Dijkstra es: ${path.joinToString(" -> ") { (it + 1).toString() }} con costo ${dist[end]}")
    }

    // Función para ejecutar Dijkstra leyendo desde un archivo
    fun ejecutarDesdeArchivo(filePath: String) {
        val (numVertices, end, graph) = leerGrafoDesdeArchivo(filePath)

        if (graph.isNotEmpty()) {
            ejecutarDijkstra(0, end, graph)  // Ejecuta Dijkstra con los datos del archivo
        } else {
            println("No se pudo ejecutar Dijkstra debido a un error en el archivo de entrada.")
        }
    }
}
