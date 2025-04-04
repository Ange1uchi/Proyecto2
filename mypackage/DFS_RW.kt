// ORDENAMIENTO TOPOLOGICO - DFS(MEJORADO)
typealias Graph<T> = Map<T, List<T>>

fun <T> Graph<T>.topologicalSort(): List<T> {
    val graph = this
    val explored = mutableSetOf<T>()
    val result = linkedSetOf<T>()

    fun explore(vertex: T) {
        explored += vertex
        for (successor in graph[vertex].orEmpty()) {
            if (successor !in explored) {
                explore(successor)
            } else if (successor !in result) {
                error("Graph contains a cycle, topological sort not possible!")
            }
        }
        result += vertex
    }

    for ((vertex, _) in graph) {
        if (vertex !in explored) {
            explore(vertex)
        }
    }

    return result.toList().reversed()
}

fun main() {
    // u → v means v depends on u
    val graph = mapOf(
        "A" to listOf("I", "F", "B"),
        "B" to listOf("C","W"),
        "C" to listOf("E", "K"),
        "F" to listOf("H", "G"),
    )

    val result = graph.topologicalSort()

    println(result)
    //require(result == listOf("A", "B", "C", "D", "E", "F", "G", "H", "I"))
}