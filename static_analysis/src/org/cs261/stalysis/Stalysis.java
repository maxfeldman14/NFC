package org.cs261.stalysis;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.StandardJavaFileManager;

/* Code examples borrowed from: 
 * http://today.java.net/pub/a/today/2008/04/10/source-code-analysis-using-java-6-compiler-apis.html
 */

class RecordVisitor extends SimpleTreeVisitor<Object, Void>
{
    public final Object visit(Tree node)
    {
        return visit(node, null);
    }
}

public class Stalysis
{
    public static void main(String[] args)
    {
        if (args.length != 1) {
            System.err.println("Wrong number of arguments: expected 1, got " + args.length);
            System.err.println("Usage: Stalysis input-file");
            System.exit(1);
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(args[0]);
        CompilationTask task = compiler.getTask(null, fileManager, null, null, null, files);

        JavacTask jcTask = (JavacTask) task;
        try {
            Iterable<? extends CompilationUnitTree> asts = jcTask.parse();
            for (CompilationUnitTree tree : asts) {
                System.out.println(tree);
                RecordVisitor visitor = new RecordVisitor();
                visitor.visit(tree);
            }
        } catch (java.io.IOException e) {
            System.err.println("Got some error");
            System.err.println(e);
            System.exit(2);
        }
    }
}
