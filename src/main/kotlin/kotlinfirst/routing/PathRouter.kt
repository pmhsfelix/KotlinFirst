package kotlinfirst.routing

import java.util
import java.util.*

/**
 * Created by pedro on 16/08/15.
 */

fun String.tryGetVariableName() =
        if (this.startsWith('{') && this.endsWith('}')) this.substring(1, this.length()-1) else null

fun List<T>.tail<T>() = this.subList(1, this.size());

public class InvalidRoute : Exception("Invalid route") {}

public class PathRouter<T>(){

    val root = RouteNode<T>();

    public fun addPath(path:String, target: T) {
        root.create(path.split('/'), target);
    }

    data class Result<T>(val target:T, val prms:Map<String,String>)

    public fun find(path:String):Result<T>?{
        val prms = HashMap<String, String>();
        var target = root.find(path.split('/'), prms);
        return if(target != null){
            Result(target, prms)
        }else{
            null
        }
    }

    public fun print(){
        root.print(0);
    }

    class RouteNode<T>(){
        val consts = util.HashMap<String, RouteNode<T>>()
        var variable : Pair<String,RouteNode<T>>? = null
        var target : T = null

        constructor(t:T) : this(){
            target = t
        }

        fun addConstNode(name:String) : RouteNode<T> {
            val node = RouteNode<T>();
            consts.put(name, node);
            return node;
        }

        public fun find(segs:List<String>, vars : MutableMap<String,String>) : T{
            if(segs.isEmpty()){
                return target;
            }else{
                var segment = segs.component1();
                var node = consts[segment];
                if(node != null){
                    return node.find(segs.tail(), vars);
                }else{
                    val v = variable;
                    if(v != null && v.first.equals(segment) != null){
                        vars.put(v.first, segment);
                        return v.second.find(segs.tail(), vars);
                    }else{
                        return null;
                    }
                }
            }
        }

        public fun create(segs:List<String>, t:T){
            if(segs.isEmpty()){
                target = t;
                return;
            }
            val segment = segs.component1();
            val varName = segment.tryGetVariableName();
            val varRef = variable;
            val node = if(varName != null){
                if(varRef != null){
                    if(!varRef.first.equals(varName)){
                        throw InvalidRoute();
                    }
                    varRef.second
                }else{
                    var n = RouteNode<T>()
                    variable = Pair(varName,n)
                    n
                }
            }else{
                consts[segment] ?: addConstNode(segment)
            }
            node.create(segs.tail(), t);
        }

        public fun print(il : Int){
            println(il, "target = ${target}");
            println(il, "var = ${variable?.first},${variable?.second}");
            val il2 = il+2;
            for(c in consts){
                println(il2, c.getKey());
                c.getValue().print(il2+2);
            }
        }

        fun println(il: Int, s: String){
            for(i in 1..il){
                print(" ")
            }
            print(s)
            print("\n")
        }
    }
}