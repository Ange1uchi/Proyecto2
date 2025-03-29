package mypackage

import java.util.ArrayDeque

class BFS {
    fun bfs(graph: Map<Int, List<Int>>, start: Int): List<List<Int>> {
        val visited = mutableSetOf<Int>()
        val queue = ArrayDeque<Pair<Int, Int>>()
        val levels = mutableMapOf<Int, MutableList<Int>>()
        queue.add(Pair(start, 0)) // Añadir el nodo de inicio junto con el nivel 0

        while (queue.isNotEmpty()) {
            val (vertex, level) = queue.removeFirst()
            if (vertex !in visited) {
                visited.add(vertex)
                levels.getOrPut(level) { mutableListOf() }.add(vertex)

                graph[vertex]?.let { neighbors ->
                    neighbors.filterNot { it in visited }.forEach {
                        queue.add(Pair(it, level + 1))
                    }
                }
            }
        }
        return levels.toSortedMap().values.map { it.toList() }
    }
}