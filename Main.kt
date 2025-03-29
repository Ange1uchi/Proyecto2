fun main() {
    val grafo = GrafoDirigido()

    // Asegúrate de que el archivo esté en la misma carpeta o proporciona la ruta completa
    grafo.cargarDesdeArchivo("PERT_entrada.txt")

    println("\n PERT cargado:")
    grafo.imprimirGrafo()

    println("\n Análisis de Camino Crítico:")
    grafo.caminoCritico()
}
