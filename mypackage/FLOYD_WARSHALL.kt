//FLOYD-WARSHALL - 13/03/2025
fun floydWarshall(graph: Array<IntArray>): Array<IntArray> {
    val n = graph.size
    val dist = graph.clone() // Initialize distance matrix
    for (k in 0 until n) {
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (dist[i][k] != Int.MAX_VALUE && dist[k][j] != Int.MAX_VALUE &&
                    dist[i][k] + dist[k][j] < dist[i][j]) {
                    dist[i][j] = dist[i][k] + dist[k][j]
                }
            }
        }
    }
    return dist
}
fun main() {
    // Example graph with 4 nodes and 6 edges
    val graph = arrayOf(
        intArrayOf(0, 1, 4, Int.MAX_VALUE, 2),  
        intArrayOf(1, 0, 2, 5, 3),              
        intArrayOf(4, 2, 0, 1, 2),  
        intArrayOf(Int.MAX_VALUE, 5, 1, 0, 6),  
        intArrayOf(2, 3, 2, 6, 0)   
    )
    val shortestDistances = floydWarshall(graph) // Find shortest paths
    for (i in 0 until graph.size) {
        for (j in 0 until graph.size) {
            print("${shortestDistances[i][j]}\t")
        }
        println()
    }
}