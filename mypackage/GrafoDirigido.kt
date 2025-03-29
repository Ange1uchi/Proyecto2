data class Arista(val destino: String, val peso: Int)

class GrafoDirigido {
    private val adyacencias = mutableMapOf<String, MutableList<Arista>>()

    fun agregarNodo(nodo: String) {
        if (!adyacencias.containsKey(nodo)) {
            adyacencias[nodo] = mutableListOf()
        }
    }

    fun agregarArista(origen: String, destino: String, peso: Int) {
        agregarNodo(origen)
        agregarNodo(destino)
        adyacencias[origen]?.add(Arista(destino, peso))
    }

    fun obtenerVecinos(nodo: String): List<Arista> {
        return adyacencias[nodo] ?: emptyList()
    }

    fun nodos(): Set<String> = adyacencias.keys

    fun imprimirGrafo() {
        for ((nodo, vecinos) in adyacencias) {
            for (arista in vecinos) {
                println("$nodo -> ${arista.destino} [${arista.peso}]")
            }
        }
    }

    fun cargarDesdeArchivo(ruta: String) {
        val archivo = java.io.File(ruta)
        archivo.forEachLine { linea ->
            val partes = linea.trim().split(" ")
            if (partes.size == 3) {
                val origen = partes[0]
                val destino = partes[1]
                val peso = partes[2].toIntOrNull() ?: 0
                agregarArista(origen, destino, peso)
            }
        }
    }

    fun ordenTopologico(): List<String> {
        val inGrado = mutableMapOf<String, Int>()
        val resultado = mutableListOf<String>()
        val cola = ArrayDeque<String>()

        for (nodo in nodos()) {
            inGrado[nodo] = 0
        }

        for ((_, vecinos) in adyacencias) {
            for (arista in vecinos) {
                inGrado[arista.destino] = inGrado[arista.destino]!! + 1
            }
        }

        for ((nodo, grado) in inGrado) {
            if (grado == 0) cola.add(nodo)
        }

        while (cola.isNotEmpty()) {
            val actual = cola.removeFirst()
            resultado.add(actual)

            for (arista in obtenerVecinos(actual)) {
                val destino = arista.destino
                inGrado[destino] = inGrado[destino]!! - 1
                if (inGrado[destino] == 0) cola.add(destino)
            }
        }

        if (resultado.size != nodos().size) {
            throw IllegalStateException("El grafo contiene ciclos")
        }

        return resultado
    }

    fun caminoCritico() {
        val orden = ordenTopologico()
        val earliest = mutableMapOf<String, Int>()
        val latest = mutableMapOf<String, Int>()

        for (nodo in orden) {
            earliest[nodo] = 0
        }

        for (nodo in orden) {
            for (arista in obtenerVecinos(nodo)) {
                val nuevoTiempo = earliest[nodo]!! + arista.peso
                earliest[arista.destino] = maxOf(earliest[arista.destino] ?: 0, nuevoTiempo)
            }
        }

        val tiempoTotal = earliest.values.max()
        for (nodo in orden) {
            latest[nodo] = tiempoTotal
        }

        for (nodo in orden.reversed()) {
            for (arista in obtenerVecinos(nodo)) {
                val nuevoTiempo = latest[arista.destino]!! - arista.peso
                latest[nodo] = minOf(latest[nodo]!!, nuevoTiempo)
            }
        }

        println(" Tiempos por nodo:")
        for (nodo in orden) {
            val e = earliest[nodo]!!
            val l = latest[nodo]!!
            val margen = l - e
            val critico = if (margen == 0) "CRÍTICO" else ""
            println("$nodo -> Early: $e, Late: $l, Margen: $margen $critico")
        }
        val hitos = listOf("Desarrollo", "Beta_Cerrada", "Beta_Abierta", "Lanzamiento_General", "Lanzamiento_Finalizado")

        println("\n Deadlines de Hitos:")
        for (hito in hitos) {
            val tiempo = earliest[hito]
            println("$hito debe completarse a más tardar en el día $tiempo")
        }
        

        println("\n Tiempo total del proyecto: $tiempoTotal días")

        // Imprimir ruta del camino crítico como secuencia
        val camino = mutableListOf<String>()

        fun dfsCritico(nodo: String): Boolean {
            if ((latest[nodo]!! - earliest[nodo]!!) != 0) return false
            camino.add(nodo)
                if (obtenerVecinos(nodo).isEmpty()) return true
                for (arista in obtenerVecinos(nodo)) {
                    if ((latest[arista.destino]!! - earliest[arista.destino]!!) == 0) {
                         if (dfsCritico(arista.destino)) return true
                    }
                }
                if (camino.isNotEmpty()) {
                    camino.removeAt(camino.size - 1)
                }
            return false
            }
        
            // Buscar nodo inicial crítico (in-degree 0 y margen 0)
            val inGrado = mutableMapOf<String, Int>().withDefault { 0 }
            for (vecinos in adyacencias.values) {
                for (arista in vecinos) {
                    inGrado[arista.destino] = inGrado.getValue(arista.destino) + 1
                }
            }
            val nodoInicio = orden.firstOrNull { inGrado.getOrDefault(it, 0) == 0 && (latest[it]!! - earliest[it]!! == 0) }
        
            if (nodoInicio != null && dfsCritico(nodoInicio)) {
                println("\n Camino Crítico Identificado:")
                println(camino.joinToString(" -> "))
            } else {
                println("\n No se encontró un camino crítico válido.")
            }
        
    }
}
