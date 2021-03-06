package me.bemind.glitch

import android.graphics.Point
import android.graphics.Region

/**
 * Created by angelomoroni on 21/07/17.
 */

abstract class  GShape{
    val SCALE_SLOP: Float = 0.005f
    abstract var vertices : List<Point>
    abstract fun contains(tap: Point): Boolean
}

class GRect : GShape{


    var w :Int = 0
    var h :Int = 0
    var BASE_W : Int = 0
    var BASE_H : Int = 0
    val center :Point = Point(0,0)
    var angle : Int = 0
    var xScaleFactor: Float = 1f
    var yScaleFactor: Float = 1f

    override var vertices : List<Point> = arrayListOf()

    init {
        vertices = generateVertices()
    }

    constructor(w: Int,h: Int, center :Point = Point(0,0)){
        this.w = w
        this.h = h
        this.center.copy(center)

        this.BASE_H = h
        this.BASE_W = w

        vertices = generateVertices()
    }

    constructor(w: Int,h: Int,imageW:Float,imageH:Float):this(w,h, Point((imageW/2).toInt(),(imageH/2).toInt()))

    override fun contains(tap: Point): Boolean {
       return false
    }

    fun move(deltaX:Int,deltaY:Int) : List<Point>{
        center.x += deltaX
        center.y += deltaY

        vertices = moveVertices(vertices,deltaX,deltaY)

        return vertices
    }

    fun rotate(angle:Int) : List<Point>{
        this.angle = angle
       // vertices = rotateVertices(vertices)
        return vertices
    }



    private fun rotateVertices(vl :List<Point>) : List<Point> {
        val vertices = vl.copy()
        for(p in vertices){
            val rotatedP = rotate(p)
            p.copy(rotatedP)
        }

        return vertices
    }

    private fun moveVertices(vl :List<Point>,deltaX:Int,deltaY:Int) : List<Point>{
        val vertices = vl.copy()
        for(p in vertices){
            p.x += deltaX
            p.y += deltaY
        }

        return vertices
    }



    fun scale(xScaledFactor: Float, yScaledFactor:Float) :List<Point> {
        if(Math.abs(this.xScaleFactor - Math.abs(xScaledFactor)) > SCALE_SLOP || Math.abs(this.yScaleFactor - Math.abs(yScaledFactor)) > SCALE_SLOP) {
            this.yScaleFactor = yScaledFactor
            this.xScaleFactor = xScaledFactor
            w = (BASE_W * xScaleFactor).toInt()
            h = (BASE_H * yScaleFactor).toInt()
            vertices = generateVertices()
        }

        return vertices


    }

    private fun rotate(p: Point) : Point{
        val x:Int = p.x
        val y:Int = p.y
        val radians = (Math.PI/180) * angle
        val cos = Math.cos(radians)
        val sin = Math.sin(radians)
        val nx = (cos * (x - center.x)) + (sin * (y - center.y)) + center.x
        val ny = (cos * (y - center.y)) - (sin * (x - center.x)) + center.y

        return Point(nx.toInt(),ny.toInt())
    }

    private fun generateVertices() : List<Point>{
        val vv : MutableList<Point> = arrayListOf()
        val topLeft = Point(center.x - w/2,center.y - h/2)
        vv.add(topLeft)
        vv.add(Point(topLeft.x + w,topLeft.y))
        vv.add(Point(topLeft.x + w,topLeft.y+h))
        vv.add(Point(topLeft.x,topLeft.y+h))
        return rotateVertices(vv)
    }


}

private fun <E> List<E>.copy(): List<E> {
    val copy = this.toList()
    return copy
}


