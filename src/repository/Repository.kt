package xyz.anilkan.repository

interface Repository<D> {
    fun add(element: D): Int
    //fun getAll(): Sequence<D>
    //fun remove(indexer: Int): Int
    //fun replace(indexer: Int, element: D): Int
    fun getElement(indexer: Int): D
    //fun filter(predicate: SqlExpressionBuilder.() -> Op<Boolean>): Sequence<D>
}