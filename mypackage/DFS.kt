//DFS
package mypackage
class Graph(private val vertices: Int) {
    private val adjacencyList = mutableListOf<MutableList<Int>>()

    init {
        for (i in 0 until vertices) {
            adjacencyList.add(mutableListOf())
        }
    }

    fun addEdge(u: Int, v: Int) {
        adjacencyList[u].add(v)
        adjacencyList[v].add(u)
    }

    fun dfs(start: Int, visited: BooleanArray, deepBranches: IntArray, parent: Int) {
        visited[start] = true
        print("$start ")

        var isLeaf = true
        for (v in adjacencyList[start]) {
            if (!visited[v]) {
                isLeaf = false
                dfs(v, visited, deepBranches, start)
            }
        }
        if (isLeaf && parent != -1) {
            deepBranches[0]++
        }
    }

    fun countDeepBranches(): Int {
        val visited = BooleanArray(vertices)
        val deepBranches = intArrayOf(0)
        dfs(0, visited, deepBranches, -1)
        return deepBranches[0]
    }
}